package com.leva.foodhealing.network;

import com.leva.foodhealing.capability.FoodDiversityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FoodDiversitySyncPacket {
    private final CompoundTag tag;

    // Use NBT serialization since it precisely matches the internal map structures of the Capability instance.
    public FoodDiversitySyncPacket(CompoundTag tag) {
        this.tag = tag == null ? new CompoundTag() : tag.copy();
    }

    public FoodDiversitySyncPacket(FriendlyByteBuf buf) {
        CompoundTag decoded = buf.readNbt();
        this.tag = decoded == null ? new CompoundTag() : decoded;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(this.tag);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (!context.getDirection().getReceptionSide().isClient()) {
                return;
            }
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.getCapability(FoodDiversityProvider.FOOD_DIVERSITY).ifPresent(cap -> {
                    if (cap instanceof com.leva.foodhealing.capability.FoodDiversityData data) {
                        data.deserializeNBT(this.tag);
                    }
                });
            }
        });
        context.setPacketHandled(true);
    }
}
