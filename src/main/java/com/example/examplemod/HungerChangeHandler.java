package com.example.examplemod;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 満腹度の変化を監視し、増加時に体力を回復させるハンドラー
 * 食べ物以外の方法（コマンド、他MOD等）での満腹度回復にも対応
 */
public class HungerChangeHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    // プレイヤーごとの前回の満腹度を記録
    private static final Map<UUID, Integer> previousFoodLevels = new HashMap<>();

    // プレイヤーが通常のアイテムで食事をした時点のゲーム内時間（Tick）を記録
    public static final Map<UUID, Long> lastAteTimes = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        // サーバーサイドのみ、かつティックの開始時のみ処理
        if (event.phase != TickEvent.Phase.START) {
            return;
        }

        Player player = event.player;

        // クライアントサイドでは処理しない（サーバーで処理）
        if (player.level().isClientSide()) {
            return;
        }

        UUID playerId = player.getUUID();
        FoodData foodData = player.getFoodData();
        int currentFoodLevel = foodData.getFoodLevel();

        // 前回の満腹度を取得（初回は現在値を設定）
        Integer previousFoodLevel = previousFoodLevels.get(playerId);

        if (previousFoodLevel == null) {
            // 初回登録
            previousFoodLevels.put(playerId, currentFoodLevel);
            return;
        }

        // 満腹度が増加した場合
        if (currentFoodLevel > previousFoodLevel) {
            Long lastAteTime = lastAteTimes.get(playerId);
            long currentTime = player.level().getGameTime();

            // 過去2Tick以内に通常の食事イベント(FoodHealingHandler)で処理されていた場合
            // 重複回復を防ぐために無視する
            if (lastAteTime != null && (currentTime - lastAteTime) <= 2) {
                LOGGER.debug("[FoodHealing] Ignoring food level increase for {} (handled by normal eating).",
                        player.getName().getString());
                // フラグを消費
                lastAteTimes.remove(playerId);
            } else {
                int foodIncrease = currentFoodLevel - previousFoodLevel;
                double healMultiplier = FoodHealingConfig.COMMON.healMultiplier.get();
                float healAmount = (float) (foodIncrease * healMultiplier);

                // 体力を回復
                player.heal(healAmount);

                LOGGER.info("[FoodHealing] Player {} hunger increased by {}. Healed {} HP (non-food source).",
                        player.getName().getString(), foodIncrease, healAmount);

                // 根性効果の付与判定
                int gutsThreshold = FoodHealingConfig.COMMON.gutsThreshold.get();
                if (foodIncrease >= gutsThreshold) {
                    int gutsDurationSeconds = FoodHealingConfig.COMMON.gutsDurationSeconds.get();
                    int gutsDurationTicks = gutsDurationSeconds * 20;

                    player.addEffect(new MobEffectInstance(
                            FoodHealingMod.GUTS_EFFECT.get(),
                            gutsDurationTicks,
                            0, // Level 1
                            false,
                            true,
                            true));

                    LOGGER.info("[FoodHealing] Guts effect applied (from non-food source) for {} seconds.",
                            gutsDurationSeconds);
                }
            }
        }

        // 現在の満腹度を記録
        previousFoodLevels.put(playerId, currentFoodLevel);
    }

    /**
     * プレイヤーがログアウトした際に記録をクリア
     */
    public static void clearPlayerData(UUID playerId) {
        previousFoodLevels.remove(playerId);
        lastAteTimes.remove(playerId);
    }
}
