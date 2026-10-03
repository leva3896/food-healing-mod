package com.leva.foodhealing;

import com.leva.foodhealing.capability.ShokugiProvider;
import com.leva.foodhealing.network.PacketHandler;
import com.leva.foodhealing.network.PurchaseBaseStatPacket;
import com.leva.foodhealing.network.PurchaseSkillPacket;
import com.leva.foodhealing.network.ToggleSkillPacket;
import com.leva.foodhealing.network.ShokugiSyncPacket;
import com.mojang.authlib.GameProfile;
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
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.network.ICustomPacket;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkHooks;

import java.util.UUID;

@GameTestHolder(FoodHealingMod.MODID)
@PrefixGameTestTemplate(false)
public final class FoodHealingNetworkGameTests {
    private FoodHealingNetworkGameTests() {
    }

    @GameTest(template = "empty", timeoutTicks = 200)
    public static void decodedPurchaseReplayAndToggleRemainPlayerScoped(GameTestHelper helper) {
        var server = helper.getLevel().getServer();
        ServerPlayer a = new ServerPlayer(server, helper.getLevel(), new GameProfile(UUID.randomUUID(), "fh-net-a"));
        ServerPlayer b = new ServerPlayer(server, helper.getLevel(), new GameProfile(UUID.randomUUID(), "fh-net-b"));
        Connection connectionA = new Connection(PacketFlow.SERVERBOUND);
        Connection connectionB = new Connection(PacketFlow.SERVERBOUND);
        EmbeddedChannel channelA = new EmbeddedChannel(connectionA);
        EmbeddedChannel channelB = new EmbeddedChannel(connectionB);
        try {
            server.getPlayerList().placeNewPlayer(connectionA, a);
            server.getPlayerList().placeNewPlayer(connectionB, b);
            var data = a.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(IllegalStateException::new);
            var other = b.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(IllegalStateException::new);
            data.setUnspentSkillPoints(1000);
            other.setUnspentSkillPoints(777);
            CompoundTag otherBefore = other.serializeNBT();
            drain(channelA);
            drain(channelB);
            for (int i = 0; i < 100; i++) {
                dispatch(helper, connectionA, new PurchaseSkillPacket(FoodHealingSkillIds.GUTS, 0));
                dispatch(helper, connectionA, new PurchaseBaseStatPacket(FoodHealingBaseStatIds.BASE_DEFENSE, 0));
                dispatch(helper, connectionA, new ToggleSkillPacket(FoodHealingSkillIds.GUTS, true));
            }
            helper.assertTrue(data.getSkillLevel(FoodHealingSkillIds.GUTS) == 1
                    && data.getBaseStatPoints(FoodHealingBaseStatIds.BASE_DEFENSE) == 1,
                    "wire replay purchased more than once");
            helper.assertTrue(data.getUnspentSkillPoints() == 985 && data.getSpentSkillPoints() == 15,
                    "wire replay double-spent SP");
            helper.assertTrue(data.isSkillDisabled(FoodHealingSkillIds.GUTS), "toggle-state replay was not idempotent");
            CompoundTag beforeRejected = data.serializeNBT();
            for (int invalid : new int[]{-1, Integer.MIN_VALUE, Integer.MAX_VALUE, 5}) {
                dispatch(helper, connectionA, new PurchaseSkillPacket(FoodHealingSkillIds.GUTS, invalid));
            }
            for (long invalid : new long[]{-1, Long.MIN_VALUE, Long.MAX_VALUE}) {
                dispatch(helper, connectionA, new PurchaseBaseStatPacket(FoodHealingBaseStatIds.BASE_DEFENSE, invalid));
            }
            dispatch(helper, connectionA, new PurchaseSkillPacket("invalid:unknown", 0));
            dispatch(helper, connectionA, new PurchaseBaseStatPacket("invalid:unknown", 0));
            dispatch(helper, connectionA, new ToggleSkillPacket(FoodHealingSkillIds.TRUE_GUTS, true));
            helper.assertTrue(data.serializeNBT().equals(beforeRejected), "rejected packet partially changed data");
            dispatch(helper, connectionA, new ToggleSkillPacket(FoodHealingSkillIds.GUTS, false));
            helper.assertTrue(!data.isSkillDisabled(FoodHealingSkillIds.GUTS), "wire enable did not reach canonical state");
            helper.assertTrue(other.serializeNBT().equals(otherBefore), "sender A packet changed player B");
            helper.assertTrue(drain(channelA) >= 2 && drain(channelB) == 0, "packet sync escaped sender scope");
            data.setUnspentSkillPoints(Long.MAX_VALUE);
            CompoundTag snapshot = data.serializeNBT();
            ShokugiSyncPacket packet = new ShokugiSyncPacket(snapshot);
            snapshot.putLong("UnspentSkillPoints", 0);
            FriendlyByteBuf wire = new FriendlyByteBuf(Unpooled.buffer());
            FriendlyByteBuf copied = new FriendlyByteBuf(Unpooled.buffer());
            try {
                packet.encode(wire);
                new ShokugiSyncPacket(wire).encode(copied);
                helper.assertTrue(copied.readNbt().getLong("UnspentSkillPoints") == Long.MAX_VALUE,
                        "sync packet did not retain immutable long snapshot");
            } finally { wire.release(); copied.release(); }
        } finally {
            server.getPlayerList().remove(a);
            server.getPlayerList().remove(b);
            channelA.finishAndReleaseAll();
            channelB.finishAndReleaseAll();
        }
        helper.succeed();
    }

    private static void dispatch(GameTestHelper helper, Connection connection, Object message) {
        ICustomPacket<?> packet = (ICustomPacket<?>) PacketHandler.INSTANCE.toVanillaPacket(
                message, NetworkDirection.PLAY_TO_SERVER);
        try {
            helper.assertTrue(NetworkHooks.onCustomPayload(packet, connection), "Forge did not handle registered packet");
        } finally {
            packet.getInternalData().release();
        }
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
}
