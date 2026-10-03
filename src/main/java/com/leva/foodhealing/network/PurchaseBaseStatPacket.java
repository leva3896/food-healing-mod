package com.leva.foodhealing.network;

import com.leva.foodhealing.FoodHealingBaseStats;
import com.leva.foodhealing.capability.CapabilityEvents;
import com.leva.foodhealing.capability.ShokugiProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PurchaseBaseStatPacket(String statId, long expectedPurchases) {
    public PurchaseBaseStatPacket(FriendlyByteBuf buffer) {
        this(buffer.readUtf(128), buffer.readVarLong());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(statId, 128);
        buffer.writeVarLong(expectedPurchases);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        ServerPlayer player = contextSupplier.get().getSender();
        if (player == null) {
            return;
        }
        player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(data -> {
            FoodHealingBaseStats.PurchaseResult result = FoodHealingBaseStats.tryPurchase(
                    data, statId, expectedPurchases);
            if (result == FoodHealingBaseStats.PurchaseResult.SUCCESS) {
                FoodHealingBaseStats.rebuildOwnedModifiers(player, data);
            }
            player.sendSystemMessage(PurchaseMessages.stat(result, statId).copy()
                    .withStyle(result == FoodHealingBaseStats.PurchaseResult.SUCCESS ? ChatFormatting.GREEN
                            : result == FoodHealingBaseStats.PurchaseResult.OPTIONAL_MOD_MISSING
                            ? ChatFormatting.YELLOW : ChatFormatting.RED));
            CapabilityEvents.syncToClient(player);
        });
    }
}
