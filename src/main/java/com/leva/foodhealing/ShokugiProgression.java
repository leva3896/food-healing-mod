package com.leva.foodhealing;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.leva.foodhealing.capability.CapabilityEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
public final class ShokugiProgression {
    private ShokugiProgression() { }
    public static void add(Player player,long units) {
        if (player.level().isClientSide() || units<=0) return;
        player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(data -> {
            long before=data.getLevel();
            if (!data.addNutritionUnits(units,FoodHealingConfig.nutritionThreshold())) return;
            if (data.getLevel()>before && player instanceof ServerPlayer server) {
                server.level().playSound(null,server.blockPosition(),net.minecraft.sounds.SoundEvents.PLAYER_LEVELUP,
                    net.minecraft.sounds.SoundSource.PLAYERS,1.0F,1.0F);
                server.sendSystemMessage(net.minecraft.network.chat.Component.translatable(
                    "message.foodhealing.shokugi_levelup",data.getLevel()));
            }
            CapabilityEvents.syncToClient(player);
        });
    }
}

