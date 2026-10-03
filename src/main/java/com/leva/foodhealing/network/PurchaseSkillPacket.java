package com.leva.foodhealing.network;

import com.leva.foodhealing.FoodHealingSkills;
import com.leva.foodhealing.capability.CapabilityEvents;
import com.leva.foodhealing.capability.ShokugiProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PurchaseSkillPacket(String skillId, int expectedLevel) {
    public PurchaseSkillPacket(FriendlyByteBuf buffer) {
        this(buffer.readUtf(128), buffer.readVarInt());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(skillId, 128);
        buffer.writeVarInt(expectedLevel);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        ServerPlayer player = contextSupplier.get().getSender();
        if (player == null) {
            return;
        }
        player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(data -> {
            FoodHealingSkills.PurchaseResult result = FoodHealingSkills.tryPurchase(data, skillId, expectedLevel);
            if (result == FoodHealingSkills.PurchaseResult.SUCCESS && expectedLevel == 0
                    && com.leva.foodhealing.FoodHealingSkillIds.TACZ_AMMO_CONSERVATION.equals(
                            com.leva.foodhealing.FoodHealingSkillIds.normalize(skillId))) {
                com.leva.foodhealing.compat.TaczAmmoCompatibility.onEnabled(player);
            }
            sendPurchaseResult(player, result, skillId,
                    data.getSkillLevel(com.leva.foodhealing.FoodHealingSkillIds.normalize(skillId)));
            CapabilityEvents.syncToClient(player);
        });
    }

    private static void sendPurchaseResult(ServerPlayer player, FoodHealingSkills.PurchaseResult result,
                                           String skillId, int level) {
        player.sendSystemMessage(PurchaseMessages.skill(result, skillId, level)
                .withStyle(result == FoodHealingSkills.PurchaseResult.SUCCESS
                        ? ChatFormatting.GREEN : ChatFormatting.RED));
    }
}
