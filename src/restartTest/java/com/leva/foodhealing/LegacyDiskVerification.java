package com.leva.foodhealing;

import com.leva.foodhealing.capability.CapabilityEvents;
import com.leva.foodhealing.capability.FoodDiversityProvider;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

/** Synthetic old-format inputs only; never imports or modifies a user's world. */
final class LegacyDiskVerification {
    private static final UUID FOREIGN = UUID.fromString("317c98e9-5510-4284-9134-2f3972c8dc11");
    private static final String CAP = "foodhealing:shokugi_data";

    private LegacyDiskVerification() { }

    static void verify(MinecraftServer server, Path root, boolean write, List<EmbeddedChannel> channels) throws Exception {
        // These files are freshly generated fixtures, not a v2.2.5-world migration claim.
        for (int variant = 0; variant < 5; variant++) {
            UUID id = UUID.nameUUIDFromBytes(("foodhealing-legacy-disk-" + variant).getBytes(StandardCharsets.UTF_8));
            GameProfile profile = new GameProfile(id, "FHLegacyDisk" + variant);
            Path source = root.resolve("synthetic-legacy-source-" + variant + ".dat");
            Path dataFile = server.getWorldPath(LevelResource.PLAYER_DATA_DIR).resolve(id + ".dat");
            if (write) {
                require(!Files.exists(source) && !Files.exists(dataFile), "refusing to replace a fixture input");
                ServerPlayer template = new ServerPlayer(server, server.overworld(), profile);
                template.getInventory().setItem(0, new ItemStack(Items.DIAMOND, 7));
                ItemStack stew = new ItemStack(Items.MUSHROOM_STEW);
                stew.getOrCreateTag().putIntArray("unknown-item-data", new int[]{3, 1, 4});
                template.getInventory().setItem(1, stew);
                template.getEnderChestInventory().setItem(3, stew.copy());
                template.getPersistentData().putString("foreign-fixture-marker", "not Food Healing progression");
                template.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier(
                        FOREIGN, "Foreign legacy disk fixture", 7, AttributeModifier.Operation.ADDITION));
                template.getCapability(FoodDiversityProvider.FOOD_DIVERSITY).ifPresent(data -> {
                    data.addEatenFood("minecraft:apple");
                    data.resetFoodCount();
                    data.addEatenFood("minecraft:bread");
                    data.addMaxHealthBonus(8);
                });
                CompoundTag input = template.saveWithoutId(new CompoundTag());
                input.getCompound("ForgeCaps").put(CAP, rawProgress(variant));
                NbtIo.writeCompressed(input, source.toFile());
                Files.createDirectories(dataFile.getParent());
                NbtIo.writeCompressed(input, dataFile.toFile());
            }
            require(Files.isRegularFile(source) && Files.isRegularFile(dataFile), "missing fixture input/playerdata");
            byte[] sourceBytes = Files.readAllBytes(source);
            CompoundTag input = NbtIo.readCompressed(source.toFile());
            CompoundTag raw = input.getCompound("ForgeCaps").getCompound(CAP);
            require(raw.equals(rawProgress(variant)), "synthetic source changed between JVMs");
            ServerPlayer player = new ServerPlayer(server, server.overworld(), profile);
            Connection connection = new Connection(PacketFlow.SERVERBOUND);
            EmbeddedChannel channel = new EmbeddedChannel(connection);
            channels.add(channel);
            server.getPlayerList().placeNewPlayer(connection, player);
            var progress = player.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(IllegalStateException::new);
            require(progress.serializeNBT().getCompound("LegacyV2Backup").equals(raw), "raw backup lost at login");
            require(progress.getUnspentSkillPoints() == (variant == 0 ? 1000 : 0) && progress.getSpentSkillPoints() == 0
                    && progress.getAcquiredSkills().isEmpty() && progress.getBaseStats().isEmpty(),
                    "login respec SP/ownership mismatch");
            require(progress.getEatCount() == (variant < 2 ? 1990 : 199) && progress.getLevel() == (variant == 0 ? 1000 : 0),
                    "legacy level/count normalization changed");
            require(progress.isLegacyMigrationPending() == (variant >= 2), "invalid legacy safety gate changed");
            CompoundTag canonical = progress.serializeNBT();
            for (int i = 0; i < 20; i++) {
                CapabilityEvents.rebuildOwnedModifiers(player);
                FoodDiversityHandler.applyHealthBonus(player, 8);
            }
            require(canonical.equals(progress.serializeNBT()), "modifier rebuild mutated progression");
            var armor = player.getAttribute(Attributes.ARMOR);
            require(armor.getModifier(FOREIGN) != null && armor.getModifier(FOREIGN).getAmount() == 7,
                    "foreign modifier changed");
            require(armor.getModifier(FoodHealingBaseStats.BASE_DEFENSE_UUID) == null, "unexpected base stat modifier");
            require(player.getAttribute(Attributes.MAX_HEALTH).getModifiers().stream().filter(m ->
                    m.getId().equals(FoodDiversityHandler.HEALTH_BONUS_UUID)).count() == 1,
                    "diversity modifier duplicated or missing");
            server.getPlayerList().saveAll();
            CompoundTag saved = NbtIo.readCompressed(dataFile.toFile());
            require(saved.getCompound("ForgeCaps").getCompound(CAP).equals(canonical), "disk progression differs");
            for (String field : List.of("Inventory", "EnderItems", "ForgeData")) {
                require(input.get(field).equals(saved.get(field)), "unrelated player data changed: " + field);
            }
            String diversityKey = FoodDiversityHandler.FOOD_DIVERSITY_CAP.toString();
            require(input.getCompound("ForgeCaps").getCompound(diversityKey).getInt("MaxHealthBonus") == 8,
                    "fixture diversity input missing");
            require(input.getCompound("ForgeCaps").getCompound(diversityKey)
                    .equals(saved.getCompound("ForgeCaps").getCompound(diversityKey)),
                    "diversity capability changed");
            require(java.util.Arrays.equals(sourceBytes, Files.readAllBytes(source)), "fixture source was modified");
            LogUtils.getLogger().info("FOODHEALING_LEGACY_DISK_CASE_PASS variant={} phase={} uuid={} raw=true items=true foreign=true",
                    variant, write ? "write" : "read", id);
            server.getPlayerList().remove(player);
        }
        LogUtils.getLogger().info("FOODHEALING_LEGACY_DISK_PASS cases=5 scope=AUTOMATED_FIXTURE_TESTED");
    }

    private static CompoundTag rawProgress(int variant) {
        CompoundTag raw = new CompoundTag();
        if (variant == 0) raw.putInt("ShokugiLevel", 1000);
        if (variant == 1) raw.putInt("ShokugiLevel", 0);
        if (variant == 2) raw.putInt("ShokugiLevel", -1);
        if (variant == 3) raw.putString("ShokugiLevel", "malformed fixture");
        raw.putInt("EatCount", 199);
        CompoundTag unknown = new CompoundTag();
        unknown.putIntArray("unmapped-sublevel-fixture", new int[]{2, 3, 4});
        unknown.putString("note", "preserve without OPEN-05 mapping");
        raw.put("UnknownLegacyFields", unknown);
        return raw;
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
