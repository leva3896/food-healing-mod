package com.example.examplemod;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
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
            int foodIncrease = currentFoodLevel - previousFoodLevel;
            double healMultiplier = FoodHealingConfig.COMMON.healMultiplier.get();
            float healAmount = (float) (foodIncrease * healMultiplier);

            // 体力を回復
            player.heal(healAmount);

            LOGGER.info("[FoodHealing] Player {} hunger increased by {}. Healed {} HP (non-food source).",
                    player.getName().getString(), foodIncrease, healAmount);
        }

        // 現在の満腹度を記録
        previousFoodLevels.put(playerId, currentFoodLevel);
    }

    /**
     * プレイヤーがログアウトした際に記録をクリア
     */
    public static void clearPlayerData(UUID playerId) {
        previousFoodLevels.remove(playerId);
    }
}
