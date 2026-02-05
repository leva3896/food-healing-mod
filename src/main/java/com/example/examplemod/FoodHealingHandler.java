package com.example.examplemod;

import com.mojang.logging.LogUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

/**
 * 食事イベントを監視し、満腹度回復に応じて体力を回復させるハンドラー
 */
public class FoodHealingHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        // プレイヤーかどうかチェック
        if (!(event.getEntity() instanceof Player player)) {
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
        int bonusThreshold = FoodHealingConfig.COMMON.bonusThreshold.get();
        int bonusDurationSeconds = FoodHealingConfig.COMMON.bonusDurationSeconds.get();
        int bonusDurationTicks = bonusDurationSeconds * 20;

        // 体力回復量を計算
        float healAmount = (float) (nutrition * healMultiplier);

        // 体力を回復（最大体力を超えない）
        player.heal(healAmount);

        LOGGER.info("[FoodHealing] Player {} ate food with nutrition {}. Healed {} HP.",
                player.getName().getString(), nutrition, healAmount);

        // ボーナス効果の閾値以上の場合、ボーナス効果を付与
        if (nutrition >= bonusThreshold) {
            // 耐性レベル4（内部的には0-indexed なのでレベル3を指定）
            player.addEffect(new MobEffectInstance(
                    MobEffects.DAMAGE_RESISTANCE,
                    bonusDurationTicks,
                    3, // Level 4 (0-indexed)
                    false, // ambient
                    true, // visible particles
                    true // show icon
            ));

            // 火炎耐性
            player.addEffect(new MobEffectInstance(
                    MobEffects.FIRE_RESISTANCE,
                    bonusDurationTicks,
                    0, // Level 1
                    false,
                    true,
                    true));

            LOGGER.info("[FoodHealing] Bonus effects applied! Resistance IV and Fire Resistance for {} seconds.",
                    bonusDurationSeconds);
        }
    }
}
