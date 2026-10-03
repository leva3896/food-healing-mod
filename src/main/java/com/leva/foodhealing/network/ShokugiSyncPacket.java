package com.leva.foodhealing.network;

import com.leva.foodhealing.FoodHealingConfig;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.leva.foodhealing.client.ClientFoodHealingState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ShokugiSyncPacket {
    private static final String SYNCED_REQUIREMENT_KEY = "SyncedLevelUpRequirement";
    private final CompoundTag data;

    public ShokugiSyncPacket(CompoundTag data) {
        this.data = data.copy();
        this.data.putLong(SYNCED_REQUIREMENT_KEY, FoodHealingConfig.nutritionThreshold());
    }

    public ShokugiSyncPacket(FriendlyByteBuf buf) {
        CompoundTag decoded = buf.readNbt();
        this.data = decoded == null ? new CompoundTag() : decoded;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(data);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (!context.getDirection().getReceptionSide().isClient()) {
                return;
            }
            if (Minecraft.getInstance().player != null) {
                ClientFoodHealingState.setLevelUpRequirement(data.getLong(SYNCED_REQUIREMENT_KEY));
                Minecraft.getInstance().player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                        .ifPresent(cap -> cap.deserializeNBT(data));
            }
        });
        context.setPacketHandled(true);
    }
}
