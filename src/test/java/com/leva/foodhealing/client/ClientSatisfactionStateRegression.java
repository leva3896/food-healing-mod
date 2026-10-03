package com.leva.foodhealing.client;

import com.leva.foodhealing.network.SatisfactionDecisionPacket;
import io.netty.buffer.Unpooled;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class ClientSatisfactionStateRegression {
    private ClientSatisfactionStateRegression() {
    }

    public static void run() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        ItemStack taggedFood = new ItemStack(Items.DRIED_KELP, 3);
        taggedFood.getOrCreateTag().putString("foodhealing_test", "client_snapshot");

        SatisfactionDecisionPacket outgoing = new SatisfactionDecisionPacket(taggedFood, true);
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        outgoing.encode(buffer);
        SatisfactionDecisionPacket decoded = new SatisfactionDecisionPacket(buffer);
        require(decoded.preserve() && ItemStack.matches(decoded.expectedStack(), taggedFood),
                "Satisfaction decision packet did not preserve item, count, and NBT");

        ClientSatisfactionState.accept(decoded.expectedStack(), decoded.preserve());
        require(ClientSatisfactionState.consume(new ItemStack(Items.BREAD))
                        == ClientSatisfactionState.Decision.NONE,
                "a Satisfaction decision matched an unrelated food");
        require(ClientSatisfactionState.consume(taggedFood)
                        == ClientSatisfactionState.Decision.PRESERVE,
                "the matching client food did not receive the preserve decision");
        require(ClientSatisfactionState.consume(taggedFood)
                        == ClientSatisfactionState.Decision.NONE,
                "one Satisfaction decision was consumed more than once");

        ClientSatisfactionState.accept(taggedFood, false);
        require(ClientSatisfactionState.consume(taggedFood)
                        == ClientSatisfactionState.Decision.CONSUME,
                "a failed Satisfaction roll did not explicitly replace stale client state");

        ClientSatisfactionState.accept(taggedFood, true);
        ClientSatisfactionState.accept(ItemStack.EMPTY, false);
        require(ClientSatisfactionState.consume(taggedFood)
                        == ClientSatisfactionState.Decision.NONE,
                "a canceled food use left a stale client preserve decision");

        CompoundTag originalTag = taggedFood.getTag().copy();
        ItemStack packetSnapshot = outgoing.expectedStack();
        packetSnapshot.getOrCreateTag().putString("mutated", "yes");
        require(originalTag.equals(outgoing.expectedStack().getTag()),
                "packet accessor exposed its canonical ItemStack snapshot");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
