package com.leva.foodhealing;

import com.leva.foodhealing.capability.IShokugiData;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.leva.foodhealing.network.PacketHandler;
import com.leva.foodhealing.network.PurchaseSkillPacket;
import com.leva.foodhealing.network.ShokugiSyncPacket;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.ReferenceCountUtil;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.network.ICustomPacket;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkHooks;

import java.util.UUID;

import static com.leva.foodhealing.FoodHealingSkills.PurchaseResult.*;

/** Isolated fixtures; excluded by the existing distribution GameTests glob. */
@GameTestHolder(FoodHealingMod.MODID)
@PrefixGameTestTemplate(false)
public final class FoodHealingPrerequisiteGameTests {
    private FoodHealingPrerequisiteGameTests() { }

    @GameTest(template = "empty")
    public static void acquiredMasteryDoesNotBecomeUniversalNumericOrDeathProtection(GameTestHelper h) {
        try (Fixture f = new Fixture(h)) {
            f.data.setSkillLevel(FoodHealingSkillIds.PURIFICATION, 1);
            f.data.setSkillLevel(FoodHealingSkillIds.PURIFICATION_MASTERY, 1);
            f.player.setHealth(2F);
            var hit = new net.minecraftforge.event.entity.living.LivingDamageEvent(
                    f.player, f.player.damageSources().generic(), 100F);
            DamageEventHandler.onLivingDamage(hit);
            h.assertTrue(!hit.isCanceled() && hit.getAmount() == 100F, "mastery altered ordinary numeric defense");
            var death = new net.minecraftforge.event.entity.living.LivingDeathEvent(f.player, hit.getSource());
            DamageEventHandler.onLivingDeath(death);
            h.assertTrue(!death.isCanceled() && f.player.getHealth() == 2F, "mastery borrowed Root death/HP repair");
        }
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void parentToggleKeepsMasterySettingAndCanonicalSync(GameTestHelper h) {
        try (Fixture f = new Fixture(h)) {
            f.data.setSkillLevel(FoodHealingSkillIds.PURIFICATION, 1);
            f.data.setSkillLevel(FoodHealingSkillIds.PURIFICATION_MASTERY, 1);
            f.data.setUnspentSkillPoints(321);
            var original = f.data.serializeNBT();
            f.rejectRemovedCommand("toggle \"" + FoodHealingSkillIds.PURIFICATION + "\"");
            f.toggle(FoodHealingSkillIds.PURIFICATION, true);
            f.assertSync();
            h.assertTrue(!PurificationMasteryController.isEnabled(f.data)
                    && !f.data.isSkillDisabled(FoodHealingSkillIds.PURIFICATION_MASTERY), "parent OFF rewrote mastery");
            f.toggle(FoodHealingSkillIds.PURIFICATION, false);
            f.assertSync();
            h.assertTrue(PurificationMasteryController.isEnabled(f.data)
                    && f.data.serializeNBT().equals(original), "toggle changed ownership/SP or failed restoration");
        }
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void purificationAcquisitionUsesLockedCostAndOffParent(GameTestHelper helper) {
        acquisition(helper, FoodHealingSkillIds.PURIFICATION_MASTERY, FoodHealingSkillIds.PURIFICATION, 100);
    }

    @GameTest(template = "empty")
    public static void truthAcquisitionUsesLockedCostAndOffParent(GameTestHelper helper) {
        acquisition(helper, FoodHealingSkillIds.TRUTH_MASTERY, FoodHealingSkillIds.PURIFICATION_MASTERY, 500);
    }

    @GameTest(template = "empty")
    public static void truthToggleKeepsParentsAndCanonicalSync(GameTestHelper h) {
        try (Fixture f = new Fixture(h)) {
            f.data.setSkillLevel(FoodHealingSkillIds.PURIFICATION, 1);
            f.data.setSkillLevel(FoodHealingSkillIds.PURIFICATION_MASTERY, 1);
            f.data.setSkillLevel(FoodHealingSkillIds.TRUTH_MASTERY, 1);
            f.data.setUnspentSkillPoints(321);
            var before = f.data.serializeNBT();
            for (String id : new String[]{FoodHealingSkillIds.PURIFICATION,
                    FoodHealingSkillIds.PURIFICATION_MASTERY, FoodHealingSkillIds.TRUTH_MASTERY}) {
                f.rejectRemovedCommand("toggle \"" + id + "\"");
                f.toggle(id, true);
                f.assertSync();
                h.assertTrue(!TruthMasteryController.isEnabled(f.data), "OFF must stop new Truth applications");
                f.toggle(id, false);
                f.assertSync();
                h.assertTrue(TruthMasteryController.isEnabled(f.data)
                        && before.equals(f.data.serializeNBT()), "Truth toggle altered saved parents, SP or ownership");
            }
        }
        h.succeed();
    }

    private static void acquisition(GameTestHelper h, String id, String parent, long cost) {
        try (Fixture f = new Fixture(h)) {
            var definition = FoodHealingSkills.definitions().get(id);
            h.assertTrue(!definition.openPrerequisites() && definition.requirements().size() == 1
                    && definition.levelCosts()[0] == cost, "locked definition/cost differs");
            f.data.setUnspentSkillPoints(cost);
            h.assertTrue(!FoodHealingSkills.hasRequiredSkills(f.data, definition.requirements()), "missing parent accepted");
            f.reject(id, 0, PREREQUISITE_MISSING);
            f.data.setSkillLevel(parent, 1);
            f.data.setSkillDisabled(parent, true);
            h.assertTrue(FoodHealingSkills.hasRequiredSkills(f.data, definition.requirements()), "owned OFF parent rejected");
            f.data.setUnspentSkillPoints(cost - 1);
            f.reject(id, 0, INSUFFICIENT_SP);
            f.data.setUnspentSkillPoints(cost);
            f.purchase(id, cost);
            h.assertTrue(f.data.isSkillDisabled(parent), "purchase enabled parent");
        }
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void registeredPurificationPurchaseAndReplay(GameTestHelper helper) {
        rejectedPackets(helper, FoodHealingSkillIds.PURIFICATION_MASTERY, FoodHealingSkillIds.PURIFICATION);
    }

    @GameTest(template = "empty")
    public static void registeredTruthPurchaseAndReplay(GameTestHelper helper) {
        rejectedPackets(helper, FoodHealingSkillIds.TRUTH_MASTERY, FoodHealingSkillIds.PURIFICATION_MASTERY);
    }

    private static void rejectedPackets(GameTestHelper h, String id, String parent) {
        try (Fixture a = new Fixture(h); Fixture b = new Fixture(h)) {
            a.data.setUnspentSkillPoints(1000);
            a.data.setSpentSkillPoints(77);
            a.data.setSkillLevel(parent, 1);
            a.data.setSkillDisabled(parent, true);
            CompoundTag other = b.data.serializeNBT();
            a.purchase(id, FoodHealingSkills.definitions().get(id).levelCosts()[0]);
            h.assertTrue(a.data.isSkillDisabled(parent), "purchase changed OFF parent");
            CompoundTag before = a.data.serializeNBT();
            for (int level : new int[]{0, 0, -1, Integer.MIN_VALUE, Integer.MAX_VALUE, 1}) {
                a.dispatch(id, level);
                h.assertTrue(a.data.serializeNBT().equals(before), "duplicate/invalid request changed canonical state");
                a.assertSync();
            }
            a.dispatch("invalid:unknown", 0);
            a.assertSync();
            h.assertTrue(a.data.serializeNBT().equals(before) && b.data.serializeNBT().equals(other)
                    && b.drain() == 0, "request changed another player or invalid ID state");
        }
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void readyNodePurchaseWithOffParentSpendsAndSyncsExactlyOnce(GameTestHelper h) {
        try (Fixture a = new Fixture(h); Fixture b = new Fixture(h)) {
            a.data.setUnspentSkillPoints(100);
            a.data.setSkillLevel(FoodHealingSkillIds.HEROICS, 5);
            a.data.setSkillDisabled(FoodHealingSkillIds.HEROICS, true);
            CompoundTag other = b.data.serializeNBT();
            a.dispatch(FoodHealingSkillIds.TRUE_HEROICS, 0);
            h.assertTrue(a.data.getUnspentSkillPoints() == 0 && a.data.getSpentSkillPoints() == 100
                    && a.data.getSkillLevel(FoodHealingSkillIds.TRUE_HEROICS) == 1, "ready purchase did not spend once");
            a.assertSync();
            CompoundTag purchased = a.data.serializeNBT();
            for (int i = 0; i < 3; i++) a.dispatch(FoodHealingSkillIds.TRUE_HEROICS, 0);
            h.assertTrue(a.data.serializeNBT().equals(purchased), "replay changed purchased canonical state");
            a.assertSync();
            h.assertTrue(b.data.serializeNBT().equals(other) && b.drain() == 0, "successful purchase leaked to another player");
        }
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void managementCommandsDoNotGrantUnownedMasteries(GameTestHelper h) {
        try (Fixture f = new Fixture(h)) {
            f.command("setskillpoint 1000");
            h.assertTrue(f.data.getUnspentSkillPoints() == 1000, "existing authorized SP command failed");
            CompoundTag before = f.data.serializeNBT();
            for (String id : new String[]{FoodHealingSkillIds.PURIFICATION_MASTERY, FoodHealingSkillIds.TRUTH_MASTERY}) {
                f.rejectRemovedCommand("skill \"" + id + "\"");
                f.rejectRemovedCommand("toggle \"" + id + "\"");
                h.assertTrue(f.data.serializeNBT().equals(before), "detail/toggle granted an unowned mastery");
            }
        }
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void registeredFreshMasteryChainCosts603AndIsolatesOtherPlayer(GameTestHelper h) {
        try (Fixture a = new Fixture(h); Fixture b = new Fixture(h)) {
            a.data.setUnspentSkillPoints(603);
            a.data.setEatCount(123);
            a.data.setRootCooldownUntil(456);
            CompoundTag other = b.data.serializeNBT();
            a.purchase(FoodHealingSkillIds.PURIFICATION, 3);
            a.purchase(FoodHealingSkillIds.PURIFICATION_MASTERY, 100);
            a.purchase(FoodHealingSkillIds.TRUTH_MASTERY, 500);
            h.assertTrue(a.data.getUnspentSkillPoints() == 0 && a.data.getSpentSkillPoints() == 603
                    && a.data.getLevel() == 0 && TruthMasteryController.isEnabled(a.data), "fresh chain totals/conditions");
            h.assertTrue(other.equals(b.data.serializeNBT()) && b.drain() == 0, "other player received mutation/sync");
        }
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void registeredPurificationPrerequisiteAnd99Boundary(GameTestHelper h) {
        packetBoundaries(h, FoodHealingSkillIds.PURIFICATION_MASTERY, FoodHealingSkillIds.PURIFICATION, 100);
    }

    @GameTest(template = "empty")
    public static void registeredTruthPrerequisiteAnd499Boundary(GameTestHelper h) {
        packetBoundaries(h, FoodHealingSkillIds.TRUTH_MASTERY, FoodHealingSkillIds.PURIFICATION_MASTERY, 500);
    }

    private static void packetBoundaries(GameTestHelper h, String id, String parent, long cost) {
        try (Fixture f = new Fixture(h)) {
            f.data.setUnspentSkillPoints(cost);
            f.rejectPacket(id, 0, PREREQUISITE_MISSING);
            f.data.setSkillLevel(parent, 1);
            f.data.setSkillDisabled(parent, true);
            f.data.setUnspentSkillPoints(cost - 1);
            f.rejectPacket(id, 0, INSUFFICIENT_SP);
            f.data.setUnspentSkillPoints(cost);
            for (int level : new int[]{-1, Integer.MIN_VALUE, Integer.MAX_VALUE, 1}) f.rejectPacket(id, level, STALE_REQUEST);
            f.rejectPacket("invalid:unknown", 0, UNKNOWN_SKILL);
            f.purchase(id, cost);
            f.rejectPacket(id, 0, STALE_REQUEST);
            f.rejectPacket(id, 1, MAX_LEVEL);
        }
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void registeredPurificationRejectsInvalidCanonical(GameTestHelper h) {
        invalidCanonical(h, FoodHealingSkillIds.PURIFICATION_MASTERY, FoodHealingSkillIds.PURIFICATION);
    }

    @GameTest(template = "empty")
    public static void registeredTruthRejectsInvalidCanonical(GameTestHelper h) {
        invalidCanonical(h, FoodHealingSkillIds.TRUTH_MASTERY, FoodHealingSkillIds.PURIFICATION_MASTERY);
    }

    private static void invalidCanonical(GameTestHelper h, String id, String parent) {
        for (String bad : new String[]{"pending", "future", "parent2", "unknown", "malformed-parent"}) {
            try (Fixture f = new Fixture(h)) {
                f.data.setUnspentSkillPoints(1000);
                f.data.setSkillLevel(parent, 1);
                CompoundTag raw = f.data.serializeNBT();
                var reason = STALE_REQUEST;
                switch (bad) {
                    case "pending" -> { raw.putBoolean("LegacyMigrationPending", true); reason = LEGACY_MIGRATION_PENDING; }
                    case "future" -> { raw.putInt("FoodHealingDataVersion", 999); reason = LEGACY_MIGRATION_PENDING; }
                    case "parent2" -> raw.getList("AcquiredSkills", 10).getCompound(0).putInt("Level", 2);
                    case "unknown" -> raw.getList("AcquiredSkills", 10).getCompound(0).putString("Id", "invalid:unknown");
                    case "malformed-parent" -> { raw.getList("AcquiredSkills", 10).getCompound(0).putString("Level", "one"); reason = PREREQUISITE_MISSING; }
                }
                f.data.deserializeNBT(raw); // Normal load semantics, never repaired by the request.
                f.rejectPacket(id, 0, reason);
            }
        }
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void registeredMasteryPreservesExistingOwnersAndSavedToggles(GameTestHelper h) {
        try (Fixture f = new Fixture(h)) {
            f.data.setUnspentSkillPoints(77);
            f.data.setSpentSkillPoints(603);
            for (String id : new String[]{FoodHealingSkillIds.PURIFICATION, FoodHealingSkillIds.PURIFICATION_MASTERY,
                    FoodHealingSkillIds.TRUTH_MASTERY}) {
                f.data.setSkillLevel(id, 1);
                f.data.setSkillDisabled(id, true);
            }
            CompoundTag before = f.data.serializeNBT();
            f.rejectPacket(FoodHealingSkillIds.PURIFICATION_MASTERY, 1, MAX_LEVEL);
            f.rejectPacket(FoodHealingSkillIds.TRUTH_MASTERY, 1, MAX_LEVEL);
            f.data.deserializeNBT(before);
            h.assertTrue(before.equals(f.data.serializeNBT()), "saved owner changed/refunded");
        }
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void registeredMasteryLongAccountingBoundary(GameTestHelper h) {
        for (String id : new String[]{FoodHealingSkillIds.PURIFICATION_MASTERY, FoodHealingSkillIds.TRUTH_MASTERY}) {
            try (Fixture f = new Fixture(h)) {
                var definition = FoodHealingSkills.definitions().get(id);
                long cost = definition.levelCosts()[0];
                f.data.setSkillLevel(definition.requirements().get(0).skillId(), 1);
                f.data.setUnspentSkillPoints(Long.MAX_VALUE);
                f.data.setSpentSkillPoints(Long.MAX_VALUE - cost + 1);
                f.rejectPacket(id, 0, INSUFFICIENT_SP);
                f.data.setSpentSkillPoints(Long.MAX_VALUE - cost);
                f.purchase(id, cost);
                h.assertTrue(f.data.getSpentSkillPoints() == Long.MAX_VALUE, "long boundary exact debit");
            }
        }
        h.succeed();
    }

    private static final class Fixture implements AutoCloseable {
        final GameTestHelper h;
        final ServerPlayer player;
        final IShokugiData data;
        final Connection connection = new Connection(PacketFlow.SERVERBOUND);
        final EmbeddedChannel channel = new EmbeddedChannel(connection);

        Fixture(GameTestHelper h) {
            this.h = h;
            UUID id = UUID.randomUUID();
            player = new ServerPlayer(h.getLevel().getServer(), h.getLevel(),
                    new GameProfile(id, "fh-pr-" + id.toString().substring(0, 8)));
            h.getLevel().getServer().getPlayerList().placeNewPlayer(connection, player);
            data = player.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(IllegalStateException::new);
            drain();
        }

        void reject(String id, int expected, FoodHealingSkills.PurchaseResult result) {
            CompoundTag before = data.serializeNBT();
            h.assertTrue(FoodHealingSkills.purchaseStatus(data, id, expected) == result, "wrong preview reason");
            h.assertTrue(FoodHealingSkills.tryPurchase(data, id, expected) == result, "wrong server reason");
            h.assertTrue(data.serializeNBT().equals(before), "rejection spent SP or changed ownership");
        }

        void purchase(String id, long cost) {
            CompoundTag before = data.serializeNBT();
            h.assertTrue(FoodHealingSkills.purchaseStatus(data, id, 0) == SUCCESS, "preview is not ready");
            h.assertTrue(before.equals(data.serializeNBT()), "preview mutation");
            var expected = new com.leva.foodhealing.capability.ShokugiData();
            expected.deserializeNBT(before);
            expected.setUnspentSkillPoints(data.getUnspentSkillPoints() - cost);
            expected.setSpentSkillPoints(data.getSpentSkillPoints() + cost);
            expected.setSkillLevel(id, 1); // Expected snapshot only; never inject into the player.
            dispatch(id, 0);
            h.assertTrue(data.serializeNBT().equals(expected.serializeNBT()), "purchase canonical/debit mismatch: " + id);
            h.assertTrue(!data.isSkillDisabled(id), "new purchase default is not ON");
            assertSync(1);
        }

        void rejectPacket(String id, int level, FoodHealingSkills.PurchaseResult reason) {
            CompoundTag before = data.serializeNBT();
            h.assertTrue(FoodHealingSkills.purchaseStatus(data, id, level) == reason, "wrong preview refusal " + reason);
            h.assertTrue(before.equals(data.serializeNBT()), "preview mutated rejected state");
            dispatch(id, level);
            h.assertTrue(before.equals(data.serializeNBT()), "packet refusal changed state");
            assertSync(1);
        }

        void dispatch(String id, int expected) {
            ICustomPacket<?> packet = (ICustomPacket<?>) PacketHandler.INSTANCE.toVanillaPacket(
                    new PurchaseSkillPacket(id, expected), NetworkDirection.PLAY_TO_SERVER);
            try { h.assertTrue(NetworkHooks.onCustomPayload(packet, connection), "registered purchase packet was not handled"); }
            finally { packet.getInternalData().release(); }
        }

        void command(String suffix) {
            int result = h.getLevel().getServer().getCommands().performPrefixedCommand(
                    player.createCommandSourceStack().withPermission(2).withSuppressedOutput(), "foodhealing syokugi " + suffix);
            h.assertTrue(result == 1, "existing management command failed: " + suffix);
        }

        void rejectRemovedCommand(String suffix) {
            CompoundTag before = data.serializeNBT();
            drain();
            int result = h.getLevel().getServer().getCommands().performPrefixedCommand(
                    player.createCommandSourceStack().withPermission(2).withSuppressedOutput(), "foodhealing syokugi " + suffix);
            h.assertTrue(result == 0 && before.equals(data.serializeNBT()) && drain() == 0,
                    "obsolete command changed mastery/SP/toggle or sent sync: " + suffix);
        }

        void toggle(String id, boolean disabled) {
            ICustomPacket<?> packet = (ICustomPacket<?>) PacketHandler.INSTANCE.toVanillaPacket(
                    new com.leva.foodhealing.network.ToggleSkillPacket(id, disabled), NetworkDirection.PLAY_TO_SERVER);
            try { h.assertTrue(NetworkHooks.onCustomPayload(packet, connection), "registered GUI toggle packet was not handled"); }
            finally { packet.getInternalData().release(); }
        }

        void assertSync() { assertSync(-1); }

        void assertSync(int expectedCount) {
            ICustomPacket<?> expected = (ICustomPacket<?>) PacketHandler.INSTANCE.toVanillaPacket(
                    new ShokugiSyncPacket(data.serializeNBT()), NetworkDirection.PLAY_TO_CLIENT);
            boolean found = false;
            int matching = 0;
            channel.runPendingTasks();
            Object message;
            try {
                while ((message = channel.readOutbound()) != null) {
                    if (message instanceof ICustomPacket<?> actual) {
                        boolean equal = ByteBufUtil.equals(expected.getInternalData(), actual.getInternalData());
                        found |= equal;
                        if (equal) matching++;
                    }
                    ReferenceCountUtil.release(message);
                }
            } finally { expected.getInternalData().release(); }
            h.assertTrue(found, "sync payload does not match post-transaction canonical data");
            if (expectedCount >= 0) h.assertTrue(matching == expectedCount, "canonical sync count: " + matching);
        }

        int drain() {
            channel.runPendingTasks();
            int count = 0;
            Object message;
            while ((message = channel.readOutbound()) != null) { count++; ReferenceCountUtil.release(message); }
            return count;
        }

        @Override public void close() {
            h.getLevel().getServer().getPlayerList().remove(player);
            channel.finishAndReleaseAll();
        }
    }
}
