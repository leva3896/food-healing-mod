package com.leva.foodhealing.network;

import com.leva.foodhealing.client.ClientSatisfactionState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public final class SatisfactionDecisionPacket {
    private final ItemStack expectedStack;
    private final boolean preserve;

    public SatisfactionDecisionPacket(ItemStack expectedStack, boolean preserve) {
        this.expectedStack = expectedStack == null ? ItemStack.EMPTY : expectedStack.copy();
        this.preserve = preserve;
    }

    public SatisfactionDecisionPacket(FriendlyByteBuf buffer) {
        this(buffer.readItem(), buffer.readBoolean());
    }

    public static SatisfactionDecisionPacket clear() {
        return new SatisfactionDecisionPacket(ItemStack.EMPTY, false);
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeItem(expectedStack);
        buffer.writeBoolean(preserve);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide().isClient()) {
                ClientSatisfactionState.accept(expectedStack, preserve);
            }
        });
        context.setPacketHandled(true);
    }

    public ItemStack expectedStack() {
        return expectedStack.copy();
    }

    public boolean preserve() {
        return preserve;
    }
}
