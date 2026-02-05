package com.example.examplemod;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;

/**
 * MODのコンフィグ設定
 * ファイル: config/foodhealing-common.toml
 */
public class FoodHealingConfig {
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final CommonConfig COMMON;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        COMMON = new CommonConfig(builder);
        COMMON_SPEC = builder.build();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(
                net.minecraftforge.fml.config.ModConfig.Type.COMMON,
                COMMON_SPEC,
                "foodhealing-common.toml");
    }

    public static class CommonConfig {
        // 最大体力の上限
        public final ForgeConfigSpec.DoubleValue maxHealthCap;

        // 体力回復倍率
        public final ForgeConfigSpec.DoubleValue healMultiplier;

        // ボーナス効果の閾値（満腹度）
        public final ForgeConfigSpec.IntValue bonusThreshold;

        // ボーナス効果の持続時間（秒）
        public final ForgeConfigSpec.IntValue bonusDurationSeconds;

        // 食べ物多様性：何種類で体力アップ
        public final ForgeConfigSpec.IntValue foodsRequiredForBonus;

        // 食べ物多様性：体力増加量
        public final ForgeConfigSpec.IntValue healthIncreasePerBonus;

        public CommonConfig(ForgeConfigSpec.Builder builder) {
            builder.comment("========================================")
                    .comment("Food Healing MOD Configuration")
                    .comment("食事回復MOD設定")
                    .comment("========================================")
                    .push("general");

            maxHealthCap = builder
                    .comment("")
                    .comment("[Max Health Cap / 最大体力の上限]")
                    .comment("EN: Maximum health limit that can be set in-game")
                    .comment("JP: ゲーム内で設定可能な最大HPの上限値")
                    .comment("Default: 1,000,000 | Range: 1,024 ~ 1,000,000,000,000")
                    .defineInRange("maxHealthCap", 1_000_000.0, 1024.0,
                            1_000_000_000_000.0);

            healMultiplier = builder
                    .comment("")
                    .comment("[Heal Multiplier / 体力回復倍率]")
                    .comment("EN: HP healed per 1 nutrition point when eating food")
                    .comment("JP: 食事時、満腹度1につき回復する体力量")
                    .comment("Default: 2.0 | Range: 0.1 ~ 100.0")
                    .defineInRange("healMultiplier", 2.0, 0.1, 100.0);

            bonusThreshold = builder
                    .comment("")
                    .comment("[Bonus Effect Threshold / ボーナス効果の閾値]")
                    .comment("EN: Minimum nutrition to trigger Resistance IV + Fire Resistance")
                    .comment("JP: 耐性IV＋火炎耐性が発動する最小満腹度回復量")
                    .comment("Default: 19 | Range: 1 ~ 100")
                    .defineInRange("bonusThreshold", 19, 1, 100);

            bonusDurationSeconds = builder
                    .comment("")
                    .comment("[Bonus Duration / ボーナス効果の持続時間]")
                    .comment("EN: Duration of bonus effects in seconds")
                    .comment("JP: ボーナス効果の持続時間（秒単位）")
                    .comment("Default: 1200 (20 min) | Range: 10 ~ 86400 (24 hours)")
                    .defineInRange("bonusDurationSeconds", 1200, 10, 86400);

            builder.pop();

            builder.comment("")
                    .comment("========================================")
                    .comment("Food Diversity Settings / 食べ物多様性設定")
                    .comment("========================================")
                    .push("diversity");

            foodsRequiredForBonus = builder
                    .comment("")
                    .comment("[Foods Required / 必要な食べ物の種類数]")
                    .comment("EN: Number of unique foods needed to gain max health bonus")
                    .comment("JP: 最大体力ボーナスを得るために必要な異なる食べ物の数")
                    .comment("Default: 5 | Range: 1 ~ 100")
                    .defineInRange("foodsRequired", 5, 1, 100);

            healthIncreasePerBonus = builder
                    .comment("")
                    .comment("[Health Increase / 体力増加量]")
                    .comment("EN: Max health increase when bonus is achieved")
                    .comment("JP: ボーナス達成時に増加する最大体力")
                    .comment("Default: 2 | Range: 1 ~ 1000")
                    .defineInRange("healthIncrease", 2, 1, 1000);

            builder.pop();
        }
    }
}
