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

    // 体力回復倍率（満腹度1につき体力2回復）
    private static final float HEAL_MULTIPLIER = 2.0f;

    // ボーナス効果の閾値（満腹度19以上で発動）
    private static final int BONUS_THRESHOLD = 19;

    // ボーナス効果の持続時間（20分 = 24000 tick）
    private static final int BONUS_DURATION = 20 * 60 * 20; // 24000 ticks

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

        // 体力回復量を計算（満腹度 × 2）
        float healAmount = nutrition * HEAL_MULTIPLIER;

        // 体力を回復（最大体力を超えない）
        player.heal(healAmount);

        LOGGER.info("[FoodHealing] Player {} ate food with nutrition {}. Healed {} HP.",
                player.getName().getString(), nutrition, healAmount);

        // 満腹度30以上の場合、ボーナス効果を付与
        if (nutrition >= BONUS_THRESHOLD) {
            // 耐性レベル4（内部的には0-indexed なのでレベル3を指定）
            player.addEffect(new MobEffectInstance(
                    MobEffects.DAMAGE_RESISTANCE,
                    BONUS_DURATION,
                    3, // Level 4 (0-indexed)
                    false, // ambient
                    true, // visible particles
                    true // show icon
            ));

            // 火炎耐性
            player.addEffect(new MobEffectInstance(
                    MobEffects.FIRE_RESISTANCE,
                    BONUS_DURATION,
                    0, // Level 1
                    false,
                    true,
                    true));

            LOGGER.info("[FoodHealing] Bonus effects applied! Resistance IV and Fire Resistance for 20 minutes.");
        }
    }
}
