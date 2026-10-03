package com.leva.foodhealing;

import com.leva.foodhealing.capability.CapabilityEvents;
import com.leva.foodhealing.capability.FoodDiversityData;
import com.leva.foodhealing.capability.FoodDiversityProvider;
import com.leva.foodhealing.capability.IShokugiData;
import com.leva.foodhealing.capability.ShokugiData;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.ReferenceCountUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundKeepAlivePacket;
import net.minecraft.network.protocol.game.ServerboundKeepAlivePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/** Test-only source set: never included in the distributable mod Jar. */
@Mod.EventBusSubscriber(modid = FoodHealingMod.MODID)
public final class RestartPersistenceVerification {
    private static final UUID FRESH = UUID.fromString("c07b039a-b123-4234-9980-2a860448cbed");
    private static final UUID LEGACY = UUID.fromString("b18d016c-76f8-4e86-bd28-21c7f7933c63");
    private static final UUID FOREIGN = UUID.fromString("236088fc-dd99-4b27-b8fc-2e4db27ef0bd");
    private static SoakRun soak;

    private RestartPersistenceVerification() {
    }

    @SubscribeEvent
    public static void verify(ServerStartedEvent event) throws Exception {
        String phase = System.getProperty("foodhealing.restart.phase");
        if (phase == null) return;
        MinecraftServer server = event.getServer();
        List<EmbeddedChannel> channels = new ArrayList<>();
        try {
            require(!FMLEnvironment.production && server.isDedicatedServer(), "requires a development dedicated server");
            Path root = Path.of(System.getProperty("foodhealing.restart.root")).toRealPath();
            require(root.equals(server.getServerDirectory().toPath().toRealPath()), "wrong verification directory");
            String taczVersion = System.getProperty("foodhealing.test.tacz");
            if (taczVersion != null) {
                require(ModList.get().isLoaded("tacz"), "requested TaCZ artifact was not loaded");
                String loadedVersion = ModList.get().getModContainerById("tacz").orElseThrow()
                        .getModInfo().getVersion().toString();
                require(taczVersion.equals(loadedVersion), "unexpected TaCZ version: " + loadedVersion);
                LogUtils.getLogger().info("FOODHEALING_TACZ_LOADED version={}", loadedVersion);
            }
            boolean write = phase.equals("write");
            require(write || phase.equals("read") || phase.equals("soak"), "invalid phase");
            Path marker = root.resolve("writer.properties");
            long pid = ProcessHandle.current().pid();
            if (write) {
                require(!Files.exists(marker), "refusing to overwrite a prior test run; choose a new run ID");
            } else {
                Properties writer = new Properties();
                try (var input = Files.newInputStream(marker)) { writer.load(input); }
                require(Long.parseLong(writer.getProperty("pid")) != pid, "read must run in a different JVM");
            }

            verifyPlayer(server, FRESH, "FHRestartV3", freshData(), write, channels);
            verifyPlayer(server, LEGACY, "FHRestartV2", legacyData(), write, channels);
            if (Boolean.getBoolean("foodhealing.test.legacyDisk")) {
                LegacyDiskVerification.verify(server, root, write, channels);
            }
            if (System.getProperty("foodhealing.test.realLegacyRoot") != null) {
                RealLegacyCapabilityVerification.verify(server, root, write, channels);
            }
            if (taczVersion != null) {
                // This owned fixture exists only in the explicitly enabled optional test source set.
                Class.forName("com.leva.foodhealing.TaczIntegrationVerification")
                        .getMethod("verify", MinecraftServer.class).invoke(null, server);
            }
            server.saveEverything(true, true, true);
            for (UUID id : List.of(FRESH, LEGACY)) {
                require(Files.size(server.getWorldPath(LevelResource.PLAYER_DATA_DIR).resolve(id + ".dat")) > 0,
                        "normal PlayerDataStorage did not save " + id);
            }
            if (write) {
                Properties writer = new Properties();
                writer.setProperty("pid", Long.toString(pid));
                try (var output = Files.newOutputStream(marker)) { writer.store(output, "Restart fixture writer"); }
            }
            if (phase.equals("soak")) {
                soak = new SoakRun(server, channels);
                LogUtils.getLogger().info("FOODHEALING_SOAK_STARTED pid={} targetTicks=6000 players=2", pid);
            } else {
                LogUtils.getLogger().info("FOODHEALING_RESTART_PASS phase={} pid={} players=2", phase, pid);
                recordSuccess();
            }
        } catch (Exception | AssertionError error) {
            LogUtils.getLogger().error("FOODHEALING_RESTART_FAILED phase=" + phase, error);
            throw error;
        } finally {
            if (soak == null) {
                server.halt(false);
                for (EmbeddedChannel channel : channels) channel.finishAndReleaseAll();
            }
        }
    }

    @SubscribeEvent
    public static void tickSoak(TickEvent.ServerTickEvent event) throws Exception {
        SoakRun run = soak;
        if (run == null || event.phase != TickEvent.Phase.END) return;
        try {
            List<UUID> ids = List.of(FRESH, LEGACY);
            for (int i = 0; i < ids.size(); i++) {
                ServerPlayer player = run.server.getPlayerList().getPlayer(ids.get(i));
                require(player != null, "soak player disconnected");
                EmbeddedChannel channel = run.channels.get(i);
                channel.runPendingTasks();
                Object message;
                while ((message = channel.readOutbound()) != null) {
                    // Keep the synthetic connection alive without bypassing the server's timeout checks.
                    if (message instanceof ClientboundKeepAlivePacket keepAlive) {
                        player.connection.handleKeepAlive(new ServerboundKeepAlivePacket(keepAlive.getId()));
                    }
                    ReferenceCountUtil.release(message);
                }
                if (run.ticks % 200 == 0) {
                    var data = player.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(IllegalStateException::new);
                    require(data.serializeNBT().equals((i == 0 ? freshData() : legacyData()).serializeNBT()),
                            "soak changed canonical progression");
                    var armor = player.getAttribute(Attributes.ARMOR);
                    require(armor.getModifier(FOREIGN) != null && armor.getModifier(FOREIGN).getAmount() == 7,
                            "soak changed foreign modifier");
                    require(armor.getModifiers().stream().filter(m -> m.getId().equals(
                            FoodHealingBaseStats.BASE_DEFENSE_UUID)).count() == (i == 0 ? 1 : 0),
                            "soak duplicated/lost owned armor");
                    require(player.getInventory().getItem(0).getCount() == 7, "soak changed inventory");
                }
            }
            if (++run.ticks % 200 == 0) run.server.getPlayerList().saveAll();
            if (run.ticks % 1200 == 0) {
                LogUtils.getLogger().info("FOODHEALING_SOAK_PROGRESS ticks={} elapsedMs={} heapUsedBytes={}",
                        run.ticks, (System.nanoTime() - run.started) / 1_000_000,
                        Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
            }
            if (run.ticks == 6000) {
                LogUtils.getLogger().info("FOODHEALING_SOAK_PASS ticks=6000 saves=30 players=2");
                recordSuccess();
                stopSoak(run);
            }
        } catch (Exception | AssertionError error) {
            LogUtils.getLogger().error("FOODHEALING_SOAK_FAILED ticks=" + run.ticks, error);
            stopSoak(run);
            throw error;
        }
    }

    private static void stopSoak(SoakRun run) {
        soak = null;
        run.server.halt(false);
        for (EmbeddedChannel channel : run.channels) channel.finishAndReleaseAll();
    }

    private static void recordSuccess() throws java.io.IOException {
        String token = System.getProperty("foodhealing.restart.token");
        require(UUID.fromString(token).toString().equals(token), "invalid verification token");
        Properties receipt = new Properties();
        receipt.setProperty("token", token);
        receipt.setProperty("phase", System.getProperty("foodhealing.restart.phase"));
        receipt.setProperty("status", "PASS");
        receipt.setProperty("pid", Long.toString(ProcessHandle.current().pid()));
        Path path = Path.of(System.getProperty("foodhealing.restart.root")).resolve("result-" + token + ".properties");
        try (var output = Files.newOutputStream(path, java.nio.file.StandardOpenOption.CREATE_NEW)) {
            receipt.store(output, "Completed isolated Food Healing verification");
        }
    }

    private static final class SoakRun {
        private final MinecraftServer server;
        private final List<EmbeddedChannel> channels;
        private final long started = System.nanoTime();
        private int ticks;

        private SoakRun(MinecraftServer server, List<EmbeddedChannel> channels) {
            this.server = server;
            this.channels = channels;
        }
    }

    private static void verifyPlayer(MinecraftServer server, UUID id, String name, ShokugiData expected,
                                     boolean write, List<EmbeddedChannel> channels) {
        Path dataFile = server.getWorldPath(LevelResource.PLAYER_DATA_DIR).resolve(id + ".dat");
        require(Files.exists(dataFile) != write, "unexpected playerdata presence for " + name);
        ServerPlayer player = new ServerPlayer(server, server.overworld(), new GameProfile(id, name));
        Connection connection = new Connection(PacketFlow.SERVERBOUND);
        EmbeddedChannel channel = new EmbeddedChannel(connection);
        channels.add(channel);
        server.getPlayerList().placeNewPlayer(connection, player);
        IShokugiData actual = player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .orElseThrow(() -> new IllegalStateException("missing Shokugi capability"));
        var diversity = player.getCapability(FoodDiversityProvider.FOOD_DIVERSITY)
                .orElseThrow(() -> new IllegalStateException("missing diversity capability"));
        FoodDiversityData expectedDiversity = diversityData();
        if (write) {
            actual.copyFrom(expected);
            diversity.copyFrom(expectedDiversity);
            player.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier(
                    FOREIGN, "Foreign restart fixture", 7, AttributeModifier.Operation.ADDITION));
            player.getInventory().setItem(0, new ItemStack(Items.DIAMOND, 7));
            ItemStack stew = new ItemStack(Items.MUSHROOM_STEW);
            stew.getOrCreateTag().putString("fixture", "persistent-container");
            player.getInventory().setItem(1, stew);
        }

        require(actual.serializeNBT().equals(expected.serializeNBT()), name + " canonical progression mismatch");
        require(diversity.getAllEatenFoods().equals(expectedDiversity.getAllEatenFoods())
                        && diversity.getEatenFoods().equals(expectedDiversity.getEatenFoods())
                        && diversity.getMaxHealthBonus() == 8, name + " diversity mismatch");
        require(player.getInventory().getItem(0).is(Items.DIAMOND)
                && player.getInventory().getItem(0).getCount() == 7, name + " inventory count mismatch");
        require(player.getInventory().getItem(1).is(Items.MUSHROOM_STEW)
                && "persistent-container".equals(player.getInventory().getItem(1).getOrCreateTag().getString("fixture")),
                name + " item NBT mismatch");

        for (int i = 0; i < 10; i++) {
            CapabilityEvents.rebuildOwnedModifiers(player);
            FoodDiversityHandler.applyHealthBonus(player, diversity.getMaxHealthBonus());
        }
        require(actual.serializeNBT().equals(expected.serializeNBT()), name + " rebuild changed canonical data");
        var armor = player.getAttribute(Attributes.ARMOR);
        require(armor.getModifier(FOREIGN) != null && armor.getModifier(FOREIGN).getAmount() == 7,
                name + " foreign modifier lost or overwritten");
        long expectedOwnedArmor = actual.getBaseStatPoints(FoodHealingBaseStatIds.BASE_DEFENSE) > 0 ? 1 : 0;
        require(armor.getModifiers().stream().filter(m -> m.getId().equals(FoodHealingBaseStats.BASE_DEFENSE_UUID)).count()
                == expectedOwnedArmor, name + " owned armor duplicate/missing");
        require(player.getAttribute(Attributes.MAX_HEALTH).getModifiers().stream()
                .filter(m -> m.getId().equals(FoodDiversityHandler.HEALTH_BONUS_UUID)).count() == 1,
                name + " diversity modifier duplicate/missing");
        drain(channel);
        CapabilityEvents.syncToClient(player);
        FoodDiversityHandler.syncToClient(player);
        require(drain(channel) >= 2, name + " capability sync missing");
    }

    private static ShokugiData freshData() {
        ShokugiData data = new ShokugiData();
        data.setLevel(1_234_567_890_123L);
        data.setEatCount(199);
        data.setUnspentSkillPoints(Long.MAX_VALUE - 1000);
        data.setSpentSkillPoints(142);
        for (String id : List.of(FoodHealingSkillIds.GUTS, FoodHealingSkillIds.HEROICS)) {
            data.setSkillLevel(id, 5);
            data.setSkillDisabled(id, true);
        }
        for (String id : List.of(FoodHealingSkillIds.TRUE_GUTS, FoodHealingSkillIds.TRUE_HEROICS)) {
            data.setSkillLevel(id, 1);
            data.setSkillDisabled(id, true);
        }
        int i = 0;
        for (String id : FoodHealingBaseStatIds.ALL.stream().sorted().toList()) {
            data.setBaseStatPoints(id, 1000L + i++);
        }
        return data;
    }

    private static ShokugiData legacyData() {
        CompoundTag raw = new CompoundTag();
        raw.putInt("ShokugiLevel", 1000);
        raw.putInt("EatCount", 199);
        raw.putString("UnknownLegacyMarker", "must survive raw backup");
        ListTag disabled = new ListTag();
        disabled.add(StringTag.valueOf("satisfaction"));
        raw.put("DisabledSkills", disabled);
        ShokugiData data = new ShokugiData();
        data.deserializeNBT(raw);
        require(!data.isLegacyMigrationPending() && data.getUnspentSkillPoints() == 1000
                && data.getSpentSkillPoints() == 0 && data.getAcquiredSkills().isEmpty()
                && data.getDisabledSkills().isEmpty(), "legacy fixture must respec exactly once");
        require(data.serializeNBT().getCompound("LegacyV2Backup").equals(raw), "legacy raw backup mismatch");
        return data;
    }

    private static FoodDiversityData diversityData() {
        FoodDiversityData data = new FoodDiversityData();
        data.addEatenFood("minecraft:apple");
        data.resetFoodCount();
        data.addEatenFood("minecraft:bread");
        data.addEatenFood("minecraft:carrot");
        data.addMaxHealthBonus(8);
        return data;
    }

    private static int drain(EmbeddedChannel channel) {
        channel.runPendingTasks();
        int count = 0;
        Object message;
        while ((message = channel.readOutbound()) != null) {
            count++;
            ReferenceCountUtil.release(message);
        }
        return count;
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
