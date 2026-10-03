package com.leva.foodhealing;

import com.leva.foodhealing.capability.IShokugiData;
import com.leva.foodhealing.capability.ShokugiData;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.leva.foodhealing.network.PacketHandler;
import com.leva.foodhealing.network.ShokugiSyncPacket;
import com.leva.foodhealing.network.ToggleSkillPacket;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.ReferenceCountUtil;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.network.ICustomPacket;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkHooks;

import java.util.UUID;

/** Server-side fixtures only; excluded from the distribution by the existing GameTests glob. */
@GameTestHolder(FoodHealingMod.MODID)
@PrefixGameTestTemplate(false)
public final class FoodHealingTrueRootGameTests {
    private FoodHealingTrueRootGameTests() { }

    @GameTest(template = "empty")
    public static void completedReservationSurvivesOffWaitWithoutProtection(GameTestHelper helper) {
        try (Fixture f = new Fixture(helper)) {
            long now = f.now();
            f.complete(now);
            long end = f.data.getRootActiveUntil();
            f.packet(true);
            helper.assertTrue(f.data.getRootReservedNutrition() == 18, "OFF erased the completed reservation");
            RootController.advanceState(f.data, end - 1);
            helper.assertTrue(f.data.getRootActiveUntil() == end && RootController.isActive(f.player),
                    "OFF changed the original Root protection or deadline");
            RootController.recordNutrition(f.data, 18, end - 1);
            RootController.advanceState(f.data, end);
            RootController.advanceState(f.data, end + 1000);
            helper.assertTrue(f.data.getRootReservedNutrition() == 18,
                    "ordinary OFF waiting erased the completed reservation at expiry");
            helper.assertTrue(f.data.getRootActiveUntil() == 0 && !RootController.isActive(f.player),
                    "OFF reservation reactivated or protected after expiry");
            LivingDeathEvent death = new LivingDeathEvent(f.player, helper.getLevel().damageSources().genericKill());
            DamageEventHandler.onLivingDeath(death);
            helper.assertTrue(!death.isCanceled(), "retained OFF reservation canceled death");
            RootController.recordNutrition(f.data, 18, end + 1001);
            helper.assertTrue(f.data.getRootActiveUntil() == end + 1301
                            && f.data.getRootReservedNutrition() == 18,
                    "OFF retention prevented independent normal Root activation");
            // A separate empty-reservation fixture distinguishes 'no new reservation' from the cap.
            f.data.setRootReservedNutrition(0);
            RootController.recordNutrition(f.data, 18, end + 1002);
            helper.assertTrue(f.data.getRootReservedNutrition() == 0, "OFF food created a new True Root reservation");
        }
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void packetReenableConsumesOnceAndSynchronizes(GameTestHelper helper) {
        try (Fixture f = new Fixture(helper); Fixture other = new Fixture(helper)) {
            long now = f.now();
            f.completeAtExpiry();
            f.packet(true);
            f.tick();
            helper.assertTrue(f.data.getRootReservedNutrition() == 18, "OFF tick lost the ready reservation");
            f.assertSync();
            other.drain();
            CompoundTag otherBefore = other.data.serializeNBT();
            f.packet(false);
            f.assertActivation(now + 300);
            f.assertSync();
            CompoundTag activated = f.data.serializeNBT();
            for (int i = 0; i < 50; i++) { f.packet(false); f.tick(); }
            helper.assertTrue(f.data.serializeNBT().equals(activated), "duplicate ON/tick consumed or extended again");
            f.assertSync();
            helper.assertTrue(other.data.serializeNBT().equals(otherBefore) && other.drain() == 0,
                    "True Root activation or sync escaped the sender");
            for (int i = 0; i < 20; i++) { f.packet(true); f.packet(false); }
            helper.assertTrue(f.data.serializeNBT().equals(activated), "OFF/ON refilled an active window");
            RootController.advanceState(f.data, now + 300);
            f.packet(true);
            f.packet(false);
            helper.assertTrue(f.data.getRootActiveUntil() == 0 && f.data.getRootReservedNutrition() == 0,
                    "consumed reservation reactivated after another OFF/ON");
        }
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void removedCommandCannotReenableAndPacketPreservesProgression(GameTestHelper helper) {
        try (Fixture packet = new Fixture(helper); Fixture command = new Fixture(helper)) {
            long now = packet.now();
            packet.completeAtExpiry();
            command.completeAtExpiry();
            packet.packet(true);
            command.rejectRemovedCommand();
            command.packet(true);
            packet.tick();
            command.tick();
            command.assertSync();
            packet.packet(false);
            command.rejectRemovedCommand();
            command.packet(false);
            command.assertActivation(now + 300);
            helper.assertTrue(command.data.serializeNBT().equals(packet.data.serializeNBT()),
                    "rejected command interfered with GUI packet canonical state");
            command.assertSync();
            CompoundTag before = command.data.serializeNBT();
            for (int i = 0; i < 20; i++) { command.rejectRemovedCommand(); command.packet(true); command.packet(false); }
            helper.assertTrue(command.data.serializeNBT().equals(before), "rejected command or GUI toggles refilled the active window");
            helper.assertTrue(command.data.getLevel() == 77 && command.data.getEatCount() == 12
                            && command.data.getUnspentSkillPoints() == 43 && command.data.getSpentSkillPoints() == 25,
                    "Root toggle changed progression/SP");
        }
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void activeWindowTogglesPreserveDeadlineAndLegitimateChains(GameTestHelper helper) {
        try (Fixture f = new Fixture(helper)) {
            long now = f.now();
            f.complete(now);
            // Represent 200 ticks already elapsed in this fixture, so a reset is observable
            // even though the packet actions below execute within one server tick.
            f.data.setRootActiveUntil(now + 100);
            f.data.setRootCooldownUntil(now + 100);
            long end = f.data.getRootActiveUntil();
            for (int i = 0; i < 20; i++) { f.packet(true); f.packet(false); f.tick(); }
            helper.assertTrue(f.data.getRootActiveUntil() == end && f.data.getRootReservedNutrition() == 18,
                    "pre-expiry toggle consumed reservation or extended current window");
            RootController.advanceState(f.data, end);
            f.assertActivation(end + 300);
            RootController.recordNutrition(f.data, 9, end + 1);
            RootController.recordNutrition(f.data, 9, end + 2);
            RootController.recordNutrition(f.data, Integer.MAX_VALUE, end + 3);
            helper.assertTrue(f.data.getRootReservedNutrition() == 18, "new reservation exceeded one activation");
            RootController.advanceState(f.data, end + 300);
            f.assertActivation(end + 600);
            RootController.advanceState(f.data, end + 600);
            helper.assertTrue(f.data.getRootActiveUntil() == 0, "excess Nutrition stored multiple future activations");
        }
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void expiryTickAndEnableOrdersConsumeExactlyOnce(GameTestHelper helper) {
        for (int tickFirst = 0; tickFirst < 2; tickFirst++) {
            try (Fixture f = new Fixture(helper)) {
                long now = f.now();
                f.completeAtExpiry();
                f.packet(true);
                if (tickFirst == 1) f.tick();
                f.packet(false);
                f.tick();
                f.packet(false);
                f.tick();
                f.assertActivation(now + 300);
                f.assertSync();
            }
        }
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void enableRejectsForbiddenConditionsWithoutChangingOtherToggles(GameTestHelper helper) {
        for (int mode = 0; mode < 4; mode++) {
            try (Fixture f = new Fixture(helper)) {
                f.completeAtExpiry();
                f.data.setRootActiveUntil(0);
                f.data.setSkillDisabled(FoodHealingSkillIds.TRUE_GUTS, true);
                if (mode == 0) f.data.setRootReservedNutrition(0);
                if (mode == 1) f.data.setSkillLevel(FoodHealingSkillIds.TRUE_GUTS, 0);
                if (mode == 2) f.data.setSkillDisabled(FoodHealingSkillIds.GUTS, true);
                if (mode == 3) f.data.setSkillLevel(FoodHealingSkillIds.GUTS, 0);
                f.packet(false);
                helper.assertTrue(f.data.getRootActiveUntil() == 0 && !RootController.isActive(f.player),
                        "packet activated under forbidden condition " + mode);
                f.data.setSkillDisabled(FoodHealingSkillIds.TRUE_GUTS, true);
                f.rejectRemovedCommand();
                helper.assertTrue(f.data.getRootActiveUntil() == 0 && !RootController.isActive(f.player),
                        "command activated under forbidden condition " + mode);
                // No assertion fixes reservation disposition across parent OFF or other lifecycle boundaries.
            }
        }
        try (Fixture f = new Fixture(helper)) {
            f.completeAtExpiry();
            f.data.setRootActiveUntil(0);
            f.packet(false);
            helper.assertTrue(f.data.getRootReservedNutrition() == 18 && f.data.getRootActiveUntil() == 0,
                    "already-ON request was mistaken for an OFF-to-ON transition");
            f.data.setSkillDisabled(FoodHealingSkillIds.TRUE_GUTS, true);
            f.data.setSkillLevel(FoodHealingSkillIds.HEROICS, 1);
            f.dispatch(new ToggleSkillPacket(FoodHealingSkillIds.HEROICS, true));
            f.dispatch(new ToggleSkillPacket(FoodHealingSkillIds.HEROICS, false));
            helper.assertTrue(f.data.getRootReservedNutrition() == 18 && f.data.getRootActiveUntil() == 0,
                    "unrelated skill toggle activated True Root");
        }
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void normalRootDurationsCooldownAndFixedWindowRemainUnchanged(GameTestHelper helper) {
        int[] active = {40, 80, 120, 200, 300};
        int[] cooldown = {400, 300, 200, 100, 0};
        for (int level = 1; level <= 5; level++) {
            IShokugiData data = new ShokugiData();
            data.setSkillLevel(FoodHealingSkillIds.GUTS, level);
            RootController.recordNutrition(data, 9, 1000);
            RootController.recordNutrition(data, 1, 1299);
            helper.assertTrue(data.getRootAccumulationDeadline() == 1300, "Root window became sliding");
            RootController.recordNutrition(data, 8, 1300);
            long end = 1300 + active[level - 1];
            helper.assertTrue(data.getRootActiveUntil() == end
                            && data.getRootCooldownUntil() == end + cooldown[level - 1],
                    "Root duration/cooldown changed at level " + level);
            RootController.recordNutrition(data, 18, end - 1);
            helper.assertTrue(data.getRootReservedNutrition() == 0, "normal Root banked an active reservation");
            RootController.advanceState(data, end);
            if (cooldown[level - 1] > 0) {
                RootController.recordNutrition(data, 18, end + cooldown[level - 1] - 1);
                helper.assertTrue(data.getRootActiveUntil() == 0, "normal Root bypassed cooldown");
            }
            RootController.recordNutrition(data, 18, end + cooldown[level - 1]);
            helper.assertTrue(data.getRootActiveUntil() == end + cooldown[level - 1] + active[level - 1],
                    "normal Root did not activate when cooldown ended");
        }
        IShokugiData expired = new ShokugiData();
        expired.setSkillLevel(FoodHealingSkillIds.GUTS, 5);
        RootController.recordNutrition(expired, 9, 1000);
        RootController.recordNutrition(expired, 9, 1301);
        helper.assertTrue(expired.getRootActiveUntil() == 0 && expired.getRootAccumulatedNutrition() == 9
                        && expired.getRootAccumulationDeadline() == 1601,
                "expired normal accumulation was carried over");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void copyAndDecodeDoNotActAsPlayerEnableActions(GameTestHelper helper) {
        IShokugiData stored = new ShokugiData();
        stored.setSkillLevel(FoodHealingSkillIds.GUTS, 5);
        stored.setSkillLevel(FoodHealingSkillIds.TRUE_GUTS, 1);
        stored.setSkillDisabled(FoodHealingSkillIds.TRUE_GUTS, true);
        stored.setRootReservedNutrition(18);
        CompoundTag before = stored.serializeNBT();
        IShokugiData copy = new ShokugiData();
        copy.copyFrom(stored);
        FriendlyByteBuf wire = new FriendlyByteBuf(Unpooled.buffer());
        FriendlyByteBuf roundTrip = new FriendlyByteBuf(Unpooled.buffer());
        try {
            new ShokugiSyncPacket(before).encode(wire);
            new ShokugiSyncPacket(wire).encode(roundTrip);
            IShokugiData decoded = new ShokugiData();
            decoded.deserializeNBT(roundTrip.readNbt());
            helper.assertTrue(copy.serializeNBT().equals(before) && decoded.serializeNBT().equals(before),
                    "copy/deserialize acted like a toggle or changed canonical state");
        } finally {
            wire.release();
            roundTrip.release();
        }
        // This checks passive data operations, not death/restart reservation gameplay policy.
        helper.succeed();
    }

    private static final class Fixture implements AutoCloseable {
        final GameTestHelper helper;
        final ServerPlayer player;
        final IShokugiData data;
        final Connection connection = new Connection(PacketFlow.SERVERBOUND);
        final EmbeddedChannel channel = new EmbeddedChannel(connection);

        Fixture(GameTestHelper helper) {
            this.helper = helper;
            UUID id = UUID.randomUUID();
            player = new ServerPlayer(helper.getLevel().getServer(), helper.getLevel(),
                    new GameProfile(id, "fh-root-" + id.toString().substring(0, 8)));
            helper.getLevel().getServer().getPlayerList().placeNewPlayer(connection, player);
            data = player.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(IllegalStateException::new);
            drain();
        }

        long now() { return player.level().getGameTime(); }

        void complete(long start) {
            data.setLevel(77);
            data.setEatCount(12);
            data.setUnspentSkillPoints(43);
            data.setSpentSkillPoints(25);
            data.setSkillLevel(FoodHealingSkillIds.GUTS, 5);
            data.setSkillLevel(FoodHealingSkillIds.TRUE_GUTS, 1);
            RootController.recordNutrition(data, 18, start);
            RootController.recordNutrition(data, 18, start + 1);
            helper.assertTrue(data.getRootReservedNutrition() == 18, "fixture did not earn a completed reservation");
        }

        void packet(boolean disabled) { dispatch(new ToggleSkillPacket(FoodHealingSkillIds.TRUE_GUTS, disabled)); }

        void completeAtExpiry() {
            complete(now());
            // Move only this fixture's deadline to the server's current tick; never alter the world clock.
            helper.assertTrue(now() > 0, "expiry fixture requires a nonzero game time");
            data.setRootActiveUntil(now());
            data.setRootCooldownUntil(now());
        }

        void dispatch(Object message) {
            ICustomPacket<?> packet = (ICustomPacket<?>) PacketHandler.INSTANCE.toVanillaPacket(message, NetworkDirection.PLAY_TO_SERVER);
            try { helper.assertTrue(NetworkHooks.onCustomPayload(packet, connection), "registered packet was not handled"); }
            finally { packet.getInternalData().release(); }
        }

        void rejectRemovedCommand() {
            CompoundTag before = data.serializeNBT();
            drain();
            int result = helper.getLevel().getServer().getCommands().performPrefixedCommand(
                    player.createCommandSourceStack().withPermission(2).withSuppressedOutput(),
                    "foodhealing syokugi toggle \"" + FoodHealingSkillIds.TRUE_GUTS + "\"");
            helper.assertTrue(result == 0 && data.serializeNBT().equals(before),
                    "removed toggle command changed canonical data");
            helper.assertTrue(drain() == 0, "removed command sent a capability sync");
        }

        void tick() { RootController.onPlayerTick(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, player)); }

        void assertActivation(long end) {
            helper.assertTrue(data.getRootReservedNutrition() == 0 && data.getRootActiveUntil() == end
                            && data.getRootCooldownUntil() == end,
                    "reservation did not consume exactly once for the level-5 duration; expected=" + end
                            + " active=" + data.getRootActiveUntil() + " reserved=" + data.getRootReservedNutrition());
        }

        void assertSync() {
            ICustomPacket<?> expected = (ICustomPacket<?>) PacketHandler.INSTANCE.toVanillaPacket(
                    new ShokugiSyncPacket(data.serializeNBT()), NetworkDirection.PLAY_TO_CLIENT);
            boolean found = false;
            channel.runPendingTasks();
            Object message;
            try {
                while ((message = channel.readOutbound()) != null) {
                    if (message instanceof ICustomPacket<?> actual) {
                        found |= ByteBufUtil.equals(expected.getInternalData(), actual.getInternalData());
                    }
                    ReferenceCountUtil.release(message);
                }
            } finally { expected.getInternalData().release(); }
            helper.assertTrue(found, "outbound sync payload did not match post-operation canonical NBT");
        }

        int drain() {
            channel.runPendingTasks();
            int count = 0;
            Object message;
            while ((message = channel.readOutbound()) != null) { count++; ReferenceCountUtil.release(message); }
            return count;
        }

        @Override public void close() {
            helper.getLevel().getServer().getPlayerList().remove(player);
            channel.finishAndReleaseAll();
        }
    }
}
