package com.leva.foodhealing;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import org.slf4j.Logger;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.leva.foodhealing.network.PacketHandler;
import com.leva.foodhealing.network.ShokugiSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

/**
 * 食事イベントを監視し、満腹度回復に応じて体力を回復させるハンドラー
 */
public class FoodHealingHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemUseStart(LivingEntityUseItemEvent.Start event) {
        if (!(event.getEntity() instanceof Player player)) return;
        
        ItemStack itemStack = event.getItem();
        if (itemStack.getItem().getFoodProperties(itemStack, player) != null) {
            FoodHealingTransactions.captureFoodUseStart(player);
            if (player instanceof ServerPlayer serverPlayer) {
                SatisfactionTransactions.beginFoodUse(serverPlayer, itemStack);
            }
            player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
                if (FoodHealingSkills.isEnabled(cap, FoodHealingSkillIds.FAST_EATING)) {
                    event.setDuration(Math.max(1, event.getDuration() / 2)); // 食べる速度 2倍
                }
            });
        }
    }

    @SubscribeEvent
    public static void onItemUseTick(LivingEntityUseItemEvent.Tick event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        ItemStack itemStack = event.getItem();
        if (event.getDuration() <= 1 && itemStack.getFoodProperties(player) != null) {
            FoodHealingTransactions.captureFoodLevelBeforeFinish(player);
        }
    }

    @SubscribeEvent
    public static void onItemUseStopped(LivingEntityUseItemEvent.Stop event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide()) {
            FoodHealingTransactions.clearFoodUse(player.getUUID());
            if (player instanceof ServerPlayer serverPlayer) {
                SatisfactionTransactions.cancelFoodUse(serverPlayer);
            }
        }
    }

    @SubscribeEvent
    public static void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        // プレイヤーかどうかチェック
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }

        ItemStack itemStack = event.getItem();

        // FoodPropertiesからnutrition（満腹度回復量）を取得
        // 新しいAPI: getFoodProperties(ItemStack, LivingEntity) を使用
        FoodProperties foodProperties = itemStack.getItem().getFoodProperties(itemStack, player);

        // 食べ物でない場合はnullが返される
        if (foodProperties == null) {
            return;
        }

        int nutrition = foodProperties.getNutrition();

        // コンフィグから値を取得
        double healMultiplier = FoodHealingConfig.COMMON.healMultiplier.get();

        // 体力回復量を計算
        double recoveryMultiplier = player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .map(FoodHealingBaseStats::recoveryMultiplier)
                .orElse(1.0D);
        float baseHeal = (float) Math.max(0.0D, nutrition * healMultiplier);
        float healAmount = FoodHealingBaseStats.multiplyPositiveDamage(baseHeal, recoveryMultiplier);

        // 体力を回復（最大体力を超えない）
        player.heal(healAmount);
        FoodHealingTransactions.recordFoodConsumed(player, nutrition);
        if (player instanceof ServerPlayer serverPlayer) {
            RootController.onFoodConsumed(serverPlayer, nutrition);
        }

        LOGGER.info("[FoodHealing] Player {} ate food with nutrition {}. Healed {} HP.",
                player.getName().getString(), nutrition, healAmount);

        ShokugiProgression.add(player, nutrition);
    }

    private static void notifyLevelUp(ServerPlayer player, long newLevel) {
        player.level().playSound(null, player.blockPosition(), net.minecraft.sounds.SoundEvents.PLAYER_LEVELUP, net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
        player.sendSystemMessage(net.minecraft.network.chat.Component.translatable("message.foodhealing.shokugi_levelup", newLevel));
    }
}
