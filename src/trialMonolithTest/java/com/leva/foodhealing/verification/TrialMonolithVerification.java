package com.leva.foodhealing.verification;

import com.google.gson.GsonBuilder;
import com.leva.foodhealing.FoodHealingSkillIds;
import com.leva.foodhealing.PurificationMasteryController;
import com.leva.foodhealing.capability.IShokugiData;
import com.leva.foodhealing.capability.ShokugiData;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.leva.foodhealing.network.*;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import io.github.kosianodangoo.trialmonolith.TrialMonolithConfig;
import io.github.kosianodangoo.trialmonolith.common.entity.DamageCubeEntity;
import io.github.kosianodangoo.trialmonolith.common.helper.EntityHelper;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.ReferenceCountUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.*;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/** Real external attack integration; isolated reobfuscated test mod, never product content. */
@Mod("foodhealing_trial_verification")
public final class TrialMonolithVerification {
    private static final String P = FoodHealingSkillIds.PURIFICATION;
    private static final String M = FoodHealingSkillIds.PURIFICATION_MASTERY;
    private static final UUID FOREIGN = UUID.fromString("bf8a07bb-aedf-4539-9510-8498bb31c012");
    private final List<Map<String, Object>> results = new ArrayList<>();
    private final Map<UUID, List<Map<String, Object>>> hits = new HashMap<>();
    private final Map<UUID, List<String>> deaths = new HashMap<>();
    private final Map<UUID, List<Integer>> drops = new HashMap<>();
    private MinecraftServer server;
    private ServerLevel level;
    private Cow owner;
    private String running = "startup";

    public TrialMonolithVerification() { MinecraftForge.EVENT_BUS.register(this); }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void hurt(LivingHurtEvent e) {
        var list = hits.get(e.getEntity().getUUID());
        if (list != null) list.add(Map.of("amount", e.getAmount(), "source",
                e.getSource().typeHolder().unwrapKey().orElseThrow().location().toString(),
                "owner", e.getSource().getEntity() == null ? "null" : e.getSource().getEntity().getUUID().toString()));
    }
    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void death(LivingDeathEvent e) {
        deaths.computeIfAbsent(e.getEntity().getUUID(), k -> new ArrayList<>()).add(
                e.getSource().typeHolder().unwrapKey().orElseThrow().location() + " canceled=" + e.isCanceled());
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void drop(LivingDropsEvent e) {
        int diamonds = e.getDrops().stream().filter(d -> d.getItem().is(Items.DIAMOND)).mapToInt(d -> d.getItem().getCount()).sum();
        drops.computeIfAbsent(e.getEntity().getUUID(), k -> new ArrayList<>()).add(diamonds);
    }

    @SubscribeEvent
    public void run(ServerStartedEvent event) throws Exception {
        server = event.getServer(); level = server.overworld();
        Path root = server.getServerDirectory().toPath().toRealPath();
        require(root.equals(Path.of(System.getProperty("foodhealing.trial.verificationRoot")).toRealPath()), "wrong root");
        var receipt = new LinkedHashMap<String, Object>();
        float originalConfig = TrialMonolithConfig.damageCubeSoulDamage;
        receipt.put("pid", ProcessHandle.current().pid());
        receipt.put("originalDamageCubeSoulDamage", originalConfig);
        receipt.put("loadedMods", ModList.get().getMods().stream().map(m -> Map.of("id", m.getModId(), "version",
                m.getVersion().toString(), "file", m.getOwningFile().getFile().getFilePath().toString())).toList());
        try {
            require(server.isDedicatedServer(), "not dedicated");
            require(ModList.get().getModContainerById("the_trial_monolith").orElseThrow().getModInfo().getVersion().toString().equals("1.4.9"), "wrong target");
            level.getChunk(0, 0);
            owner = new Cow(EntityType.COW, level); owner.setNoAi(true); owner.setPos(0, 4, 0); level.addFreshEntity(owner);
            for (boolean forced : new boolean[]{false, true}) {
                for (boolean parentOff : new boolean[]{false, true}) for (boolean masteryOff : new boolean[]{false, true}) {
                    try (Fixture f = fresh()) {
                        f.data.setSkillDisabled(P, parentOff); f.data.setSkillDisabled(M, masteryOff);
                        attack(f, "toggle-" + forced + "-" + parentOff + "-" + masteryOff,
                                forced, .2F, .2F, !parentOff && !masteryOff, 1);
                    }
                }
                for (String missing : new String[]{P, M}) try (Fixture f = fresh()) {
                    f.data.setSkillLevel(missing, 0);
                    attack(f, "unowned-" + forced + "-" + missing, forced, 0F, .2F, false, 1);
                }
                for (String invalid : new String[]{"pending", "schema", "overlevel"}) try (Fixture f = fresh()) {
                    var tag = f.data.serializeNBT();
                    if (invalid.equals("pending")) tag.putBoolean("LegacyMigrationPending", true);
                    if (invalid.equals("schema")) tag.putInt("FoodHealingDataVersion", 99);
                    f.data.deserializeNBT(tag);
                    if (invalid.equals("overlevel")) f.data.setSkillLevel(M, 2);
                    attack(f, "invalid-" + forced + "-" + invalid, forced, 0F, .2F, false, 1);
                }
                for (float initial : new float[]{0F, .25F, .95F}) for (float addition : new float[]{1.1F, 10.1F}) {
                    try (Fixture f = fresh()) {
                        attack(f, "protected-boundary-" + forced + "-" + initial + "-" + addition,
                                forced, initial, addition, true, 1);
                    }
                }
                try (Fixture f = fresh()) { attack(f, "independent-two-attacks-" + forced, forced, 0F, .2F, true, 2); }
                try (Fixture f = fresh()) {
                    f.data.setSkillDisabled(M, true);
                    attack(f, "unprotected-two-attacks-" + forced, forced, 0F, .2F, false, 2);
                }
                try (Fixture f = fresh()) {
                    CompoundTag legacy = new CompoundTag(); legacy.putLong("ShokugiLevel", 4); legacy.putLong("EatCount", 1);
                    f.data.deserializeNBT(legacy); f.data.setSkillLevel(P, 1); f.data.setSkillLevel(M, 1);
                    require(!f.data.isLegacyMigrationPending(), "normal migration failed");
                    attack(f, "migrated-" + forced, forced, .1F, .2F, true, 1);
                }
                toggleCase(forced);
            }
            isolation();
            numericReduction();
            numericDeath();
            forcedThreshold(1.1F, false); forcedThreshold(10.1F, false);
            forcedThreshold(1.1F, true); forcedThreshold(10.1F, true);
            helperOutsideCube();
            persistence();
            receipt.put("status", "PASS");
            LogUtils.getLogger().info("FOODHEALING_TRIAL_INTEGRATION_PASS cases={}", results.size());
        } catch (Throwable e) {
            receipt.put("status", "FAIL"); receipt.put("failedCase", running); receipt.put("failure", e.toString());
            LogUtils.getLogger().error("FOODHEALING_TRIAL_INTEGRATION_FAIL case=" + running, e);
        } finally {
            TrialMonolithConfig.damageCubeSoulDamage = originalConfig;
            if (owner != null) owner.discard();
            receipt.put("results", results);
            server.saveEverything(true, true, true); receipt.put("saveRequested", true);
            Files.writeString(root.resolve("trial-result.json"), new GsonBuilder().setPrettyPrinting().create().toJson(receipt));
            server.halt(false);
        }
    }

    private void attack(Fixture f, String label, boolean forced, float initial, float delta, boolean protectedTarget, int count) {
        running = label;
        EntityHelper.setSoulDamageForce(f.player, initial);
        EntityHelper.setSoulProtected(f.player, forced);
        f.player.setHealth(100F); f.player.setDeltaMovement(1, 2, 3);
        CompoundTag canonical = f.data.serializeNBT();
        clear(f.player);
        var cube = cube(forced);
        require(EntityHelper.getEntities(level, net.minecraft.world.phys.AABB.ofSize(cube.getPosition(0), 4, 4, 4), e -> true)
                .contains(f.player), "fixture target outside actual native cube selection");
        TrialMonolithConfig.damageCubeSoulDamage = forced ? delta * 10F : delta;
        for (int i = 0; i < count; i++) { f.player.invulnerableTime = 0; cube.activate(); }
        float expected = protectedTarget ? initial : initial + delta * count;
        closeFloat(EntityHelper.getSoulDamage(f.player), expected, label + " Soul");
        require(EntityHelper.isSoulProtected(f.player) == forced, "foreign flag rewritten");
        require(!f.player.isDeadOrDying() && server.getPlayerList().getPlayer(f.id) == f.player, "unexpected death/respawn");
        require(deaths.get(f.id).isEmpty() && drops.get(f.id).isEmpty(), "forced death/drop leaked");
        require(canonical.equals(f.data.serializeNBT()), "attack changed canonical data");
        require(f.player.getPersistentData().getString("verification:unrelated").equals("retained"), "foreign NBT changed");
        require(f.player.getAttribute(Attributes.LUCK).getModifier(FOREIGN) != null, "foreign modifier lost");
        require(f.player.getDeltaMovement().lengthSqr() == 0, "post-hurt motion reset lost");
        numeric(f.player, count, 5F);
        if (protectedTarget && !forced && initial == 0F) closeFloat(f.player.getHealth(), 100F - count * 5F, "numeric actual HP");
        record(label, Map.of("branch", forced ? "force" : "ordinary", "highDimensional", forced,
                "externalSoulProtection", forced, "initialSoul", initial, "config", TrialMonolithConfig.damageCubeSoulDamage,
                "finalSoul", EntityHelper.getSoulDamage(f.player), "hp", f.player.getHealth(), "hits", List.copyOf(hits.get(f.id))));
    }

    private void toggleCase(boolean forced) {
        try (Fixture f = fresh()) {
            var before = f.data.serializeNBT();
            f.packet(true); f.assertSync();
            require(f.data.isSkillDisabled(P) && !f.data.isSkillDisabled(M), "packet changed mastery");
            attack(f, "packet-parent-off-next-attack-" + forced, forced, 0, .2F, false, 1);
            f.rejectRemovedCommand(); f.packet(false); f.assertSync();
            require(before.equals(f.data.serializeNBT()), "command changed SP/ownership/mastery setting");
            attack(f, "command-parent-on-next-attack-" + forced, forced, .2F, .2F, true, 1);
        }
    }

    private void isolation() {
        running = "same-cube-target-isolation";
        try (Fixture a = fresh(); Fixture b = fresh()) {
            b.data.setSkillDisabled(M, true);
            Cow mob = new Cow(EntityType.COW, level); mob.setNoAi(true); mob.setPos(1, 4, 0);
            mob.getAttribute(Attributes.MAX_HEALTH).setBaseValue(100); mob.setHealth(100); level.addFreshEntity(mob);
            clear(a.player); clear(b.player); clear(mob); clear(owner);
            TrialMonolithConfig.damageCubeSoulDamage = .2F; cube(false).activate();
            closeFloat(EntityHelper.getSoulDamage(a.player), 0, "protected receiver");
            closeFloat(EntityHelper.getSoulDamage(b.player), .2F, "other receiver");
            closeFloat(EntityHelper.getSoulDamage(mob), .2F, "mob receiver");
            numeric(a.player, 1, 5); numeric(b.player, 1, 5); numeric(mob, 1, 5);
            require(hits.get(owner.getUUID()).isEmpty() && EntityHelper.getSoulDamage(owner) == 0, "native owner predicate lost");
            record(running, Map.of("protectedSoul", 0, "otherSoul", .2, "mobSoul", .2, "ownerExcluded", true)); mob.discard();
        }
    }

    private void numericReduction() {
        running = "numeric-existing-reduction";
        try (Fixture f = fresh()) {
            f.data.setBaseStatPoints(com.leva.foodhealing.FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR, 50);
            var before = f.data.serializeNBT(); clear(f.player);
            TrialMonolithConfig.damageCubeSoulDamage = .03F; cube(false).activate();
            numeric(f.player, 1, 5F);
            closeFloat(f.player.getHealth(), 97.5F, "existing 50 percent reduction lost");
            closeFloat(EntityHelper.getSoulDamage(f.player), 0, "default config Soul blocked");
            require(before.equals(f.data.serializeNBT()), "numeric reduction changed canonical data");
            record(running, Map.of("config", .03F, "rawNumeric", hits.get(f.id), "hpBefore", 100, "hpAfter", f.player.getHealth(), "linearDR", 50));
        }
    }

    private void numericDeath() {
        running = "legitimate-numeric-death";
        try (Fixture f = fresh()) {
            f.player.setHealth(2); f.player.getInventory().setItem(0, new ItemStack(Items.DIAMOND, 7)); clear(f.player);
            TrialMonolithConfig.damageCubeSoulDamage = 10.1F; cube(false).activate();
            closeFloat(EntityHelper.getSoulDamage(f.player), 0, "numeric death Soul"); numeric(f.player, 1, 5);
            require(f.player.isDeadOrDying() && deaths.get(f.id).size() == 1
                    && deaths.get(f.id).get(0).equals("the_trial_monolith:cube_attack canceled=false"), "not a single ordinary death: " + deaths.get(f.id));
            require(drops.get(f.id).equals(List.of(7)), "numeric diamond drops != once: " + drops.get(f.id));
            record(running, Map.of("hits", hits.get(f.id), "death", deaths.get(f.id), "diamondDrops", drops.get(f.id)));
        }
    }

    private void forcedThreshold(float amount, boolean forced) {
        running = "unprotected-threshold-" + amount + "-" + forced;
        try (Fixture f = fresh()) {
            f.data.setSkillDisabled(M, true); EntityHelper.setSoulProtected(f.player, forced);
            f.player.getInventory().setItem(0, new ItemStack(Items.DIAMOND, 7)); clear(f.player);
            TrialMonolithConfig.damageCubeSoulDamage = forced ? amount * 10F : amount; cube(forced).activate();
            closeFloat(EntityHelper.getSoulDamage(f.player), amount, "unprotected threshold addition");
            require(!deaths.get(f.id).isEmpty() && deaths.get(f.id).get(0).startsWith("the_trial_monolith:soul_damage"), "native forced death missing");
            // Native onSoulDeath can notify again with an already empty inventory. Compare an
            // external helper control; require exactly one nonempty drop and no item duplication.
            require(drops.get(f.id).stream().mapToInt(Integer::intValue).sum() == 7
                    && drops.get(f.id).stream().filter(n -> n != 0).count() == 1, "native item duplication: " + drops.get(f.id));
            boolean replaced = server.getPlayerList().getPlayer(f.id) != f.player;
            require(replaced == (amount >= 10), "native forced respawn threshold differs");
            Map<String, Object> control;
            try (Fixture nativeControl = fresh()) {
                nativeControl.data.setSkillDisabled(M, true);
                EntityHelper.setSoulProtected(nativeControl.player, forced);
                nativeControl.player.getInventory().setItem(0, new ItemStack(Items.DIAMOND, 7)); clear(nativeControl.player);
                if (forced) EntityHelper.addSoulDamageForce(nativeControl.player, amount);
                else EntityHelper.addSoulDamage(nativeControl.player, amount);
                require(drops.get(nativeControl.id).equals(drops.get(f.id)) && deaths.get(nativeControl.id).equals(deaths.get(f.id))
                        && (server.getPlayerList().getPlayer(nativeControl.id) != nativeControl.player) == replaced,
                        "cube native effects differ from external helper control");
                control = Map.of("classification", "external helper control, not activate integration",
                        "diamondDrops", drops.get(nativeControl.id), "death", deaths.get(nativeControl.id), "playerReplaced", replaced);
            }
            record(running, Map.of("soul", EntityHelper.getSoulDamage(f.player), "death", deaths.get(f.id),
                    "diamondDrops", drops.get(f.id), "playerReplaced", replaced, "numericEvents", hits.get(f.id), "nativeControl", control));
        }
    }

    private void helperOutsideCube() {
        running = "direct-helper-negative-not-cube-integration";
        try (Fixture f = fresh()) {
            EntityHelper.addSoulDamage(f.player, .1F); closeFloat(EntityHelper.getSoulDamage(f.player), .1F, "global helper intercepted");
            EntityHelper.addSoulDamageForce(f.player, .1F); closeFloat(EntityHelper.getSoulDamage(f.player), .2F, "global force intercepted");
            record(running, Map.of("soul", EntityHelper.getSoulDamage(f.player), "classification", "direct helper negative"));
        }
    }

    private void persistence() {
        running = "ordinary-player-save-reload";
        UUID id; CompoundTag expected;
        try (Fixture f = fresh()) {
            id = f.id; f.packet(true); f.assertSync(); expected = f.data.serializeNBT();
            EntityHelper.setSoulDamageForce(f.player, .25F);
        }
        require(Files.isRegularFile(server.getServerDirectory().toPath().resolve("world/playerdata/" + id + ".dat")), "normal player save missing");
        try (Fixture loaded = new Fixture(id, false)) {
            require(expected.equals(loaded.data.serializeNBT()), "canonical save/load differs");
            closeFloat(EntityHelper.getSoulDamage(loaded.player), .25F, "external Soul save/load differs");
            require(!PurificationMasteryController.isEnabled(loaded.data) && !loaded.data.isSkillDisabled(M), "parent OFF setting persisted incorrectly");
            require(loaded.player.getPersistentData().getString("verification:unrelated").equals("retained")
                    && loaded.player.getAttribute(Attributes.LUCK).getModifier(FOREIGN) != null, "foreign state save/load differs");
            record(running, Map.of("uuid", id.toString(), "canonical", expected.toString(), "soul", .25F,
                    "classification", "normal PlayerList remove/placeNewPlayer same JVM"));
        }
    }

    private DamageCubeEntity cube(boolean forced) {
        var type = ForgeRegistries.ENTITY_TYPES.getValues().stream().filter(t -> {
            var key = ForgeRegistries.ENTITY_TYPES.getKey(t); return key != null && key.getNamespace().equals("the_trial_monolith");
        }).filter(t -> t.getDescriptionId().endsWith("damage_cube")).findFirst().orElseThrow();
        Entity entity = type.create(level);
        require(entity instanceof DamageCubeEntity, "wrong registered cube class: " + entity);
        DamageCubeEntity cube = (DamageCubeEntity) entity;
        // activate() samples getPosition(0): initialize previous as well as current coordinates.
        cube.moveTo(0, 4, 0, 0, 0); cube.setOwner(owner); cube.setHighDimensional(forced);
        return cube;
    }
    private void numeric(Entity entity, int count, float amount) {
        var observed = hits.get(entity.getUUID()); require(observed.size() == count, running + " numeric count " + observed);
        for (var hit : observed) {
            closeFloat(((Number) hit.get("amount")).floatValue(), amount, "numeric amount");
            require(hit.get("source").equals("the_trial_monolith:cube_attack") && hit.get("owner").equals(owner.getUUID().toString()), "numeric source/owner changed");
        }
    }
    private void clear(Entity entity) { hits.put(entity.getUUID(), new ArrayList<>()); deaths.put(entity.getUUID(), new ArrayList<>()); drops.put(entity.getUUID(), new ArrayList<>()); }
    private void record(String label, Map<String, Object> details) {
        results.add(Map.of("case", label, "status", "PASS", "observed", details));
        LogUtils.getLogger().info("FOODHEALING_TRIAL_CASE_PASS {} {}", label, details);
    }
    private Fixture fresh() { return new Fixture(UUID.randomUUID(), true); }
    private static void require(boolean ok, String message) { if (!ok) throw new AssertionError(message); }
    private static void closeFloat(float actual, float expected, String label) { require(Math.abs(actual - expected) < .0001F, label + ": " + actual + " != " + expected); }

    private final class Fixture implements AutoCloseable {
        final UUID id;
        final ServerPlayer player;
        final IShokugiData data;
        final Connection connection = new Connection(PacketFlow.SERVERBOUND);
        final EmbeddedChannel channel = new EmbeddedChannel(connection);
        Fixture(UUID id, boolean fresh) {
            this.id = id;
            player = new ServerPlayer(server, level, new GameProfile(id, "fh-ttm-" + id.toString().substring(0, 8)));
            server.getPlayerList().placeNewPlayer(connection, player);
            data = player.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(AssertionError::new);
            if (fresh) {
                data.deserializeNBT(new ShokugiData().serializeNBT()); data.setSkillLevel(P, 1); data.setSkillLevel(M, 1);
                data.setUnspentSkillPoints(321); data.setSpentSkillPoints(103);
                player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(100); player.setHealth(100);
                player.getPersistentData().putString("verification:unrelated", "retained");
                player.getAttribute(Attributes.LUCK).addPermanentModifier(new AttributeModifier(FOREIGN, "verification foreign", 3, AttributeModifier.Operation.ADDITION));
            }
            player.setPos(0, 4, 0); player.invulnerableTime = 0; clear(player); drain();
        }
        void packet(boolean disabled) {
            ICustomPacket<?> packet = (ICustomPacket<?>) PacketHandler.INSTANCE.toVanillaPacket(new ToggleSkillPacket(P, disabled), NetworkDirection.PLAY_TO_SERVER);
            try { require(NetworkHooks.onCustomPayload(packet, connection), "registered packet unhandled"); }
            finally { packet.getInternalData().release(); }
        }
        void rejectRemovedCommand() {
            CompoundTag before = data.serializeNBT(); drain();
            require(server.getCommands().performPrefixedCommand(player.createCommandSourceStack().withPermission(2).withSuppressedOutput(),
                    "foodhealing syokugi toggle \"" + P + "\"") == 0, "obsolete parent toggle command still registered");
            channel.runPendingTasks();
            require(before.equals(data.serializeNBT()) && channel.outboundMessages().isEmpty(),
                    "removed command changed canonical state or sent sync");
        }
        void assertSync() {
            ICustomPacket<?> expected = (ICustomPacket<?>) PacketHandler.INSTANCE.toVanillaPacket(new ShokugiSyncPacket(data.serializeNBT()), NetworkDirection.PLAY_TO_CLIENT);
            boolean found = false; channel.runPendingTasks(); Object message;
            try {
                while ((message = channel.readOutbound()) != null) {
                    if (message instanceof ICustomPacket<?> actual) found |= ByteBufUtil.equals(expected.getInternalData(), actual.getInternalData());
                    ReferenceCountUtil.release(message);
                }
            } finally { expected.getInternalData().release(); }
            require(found, "canonical sync missing");
        }
        void drain() { channel.runPendingTasks(); Object message; while ((message = channel.readOutbound()) != null) ReferenceCountUtil.release(message); }
        public void close() {
            ServerPlayer current = server.getPlayerList().getPlayer(id);
            if (current != null) server.getPlayerList().remove(current);
            channel.finishAndReleaseAll();
        }
    }
}
