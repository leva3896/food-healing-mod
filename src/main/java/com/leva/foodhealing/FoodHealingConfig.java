package com.leva.foodhealing;

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

        public static long nutritionThreshold() {
                return COMMON_SPEC.isLoaded() ? COMMON.shokugiNutritionLevelUpRequirement.get() : 2000L;
        }

        public static long legacyCountThreshold() {
                return COMMON_SPEC.isLoaded() ? COMMON.legacyFoodActionThreshold.get() : 200L;
        }

        public static void register() {
                try {
                        NutritionConfigMigration.prepare(net.minecraftforge.fml.loading.FMLPaths.CONFIGDIR.get()
                                .resolve("foodhealing-common.toml"));
                } catch (java.io.IOException | IllegalArgumentException | ArithmeticException error) {
                        throw new IllegalStateException("Unsafe Nutrition Config migration; do not load player data", error);
                }
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

                // 根性効果の閾値（満腹度）
                public final ForgeConfigSpec.IntValue gutsThreshold;

                // 根性効果の持続時間（秒）
                public final ForgeConfigSpec.IntValue gutsDurationSeconds;

                // 火事場力の体力の閾値（割合）
                public final ForgeConfigSpec.DoubleValue heroicsThreshold;

                // 火事場力のダメージ倍率
                public final ForgeConfigSpec.DoubleValue heroicsMultiplier;

                // 食義レベルアップに必要な食事回数
                public final ForgeConfigSpec.IntValue shokugiLevelUpRequirement;
                public final ForgeConfigSpec.LongValue shokugiNutritionLevelUpRequirement;
                public final ForgeConfigSpec.LongValue legacyFoodActionThreshold;
                public final ForgeConfigSpec.IntValue shokugiCountModelVersion;

                // 食べ物多様性：何種類で体力アップ
                public final ForgeConfigSpec.IntValue foodsRequiredForBonus;

                // 食べ物多様性：体力増加量
                public final ForgeConfigSpec.IntValue healthIncreasePerBonus;

                // HUD overlay offsets
                public final ForgeConfigSpec.IntValue overlayOffsetY;
                public final ForgeConfigSpec.IntValue overlayOffsetX;

                // HUD offsets for Heroics
                public final ForgeConfigSpec.IntValue heroicsTextOffsetY;
                public final ForgeConfigSpec.IntValue heroicsTextOffsetX;

                // HUD offsets for Shokugi
                public final ForgeConfigSpec.IntValue shokugiTextOffsetY;
                public final ForgeConfigSpec.IntValue shokugiTextOffsetX;

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
                                        .comment("[Legacy Bonus Effect Threshold / 旧ボーナス効果の閾値]")
                                        .comment("EN: Legacy v2 config retained for config-file compatibility. v3 does not grant free Resistance/Fire Resistance from nutrition.")
                                        .comment("JP: v2互換用に保持されています。v3では栄養値だけで耐性/火炎耐性を無料付与しません。")
                                        .comment("Default: 19 | Range: 1 ~ 100 | Currently unused in v3 core")
                                        .defineInRange("bonusThreshold", 19, 1, 100);

                        bonusDurationSeconds = builder
                                        .comment("")
                                        .comment("[Bonus Duration / ボーナス効果の持続時間]")
                                        .comment("EN: Duration of bonus effects in seconds")
                                        .comment("JP: ボーナス効果の持続時間（秒単位）")
                                        .comment("Default: 1200 (20 min) | Range: 10 ~ 86400 (24 hours)")
                                        .defineInRange("bonusDurationSeconds", 1200, 10, 86400);

                        gutsThreshold = builder
                                        .comment("")
                                        .comment("[Legacy Guts Effect Threshold / 旧根性効果の閾値]")
                                        .comment("EN: Legacy v2 config retained for config-file compatibility. v3 Root is skill-driven and uses Nutrition 18 rules.")
                                        .comment("JP: v2互換用に保持されています。v3の根性はスキル駆動でNutrition 18ルールを使います。")
                                        .comment("Default: 19 | Range: 1 ~ 100 | Currently unused in v3 core")
                                        .defineInRange("gutsThreshold", 19, 1, 100);

                        gutsDurationSeconds = builder
                                        .comment("")
                                        .comment("[Guts Duration / 根性効果の持続時間]")
                                        .comment("EN: Duration of Guts effect in seconds")
                                        .comment("JP: 根性（即死回避）効果の持続時間（秒単位）")
                                        .comment("Default: 5 | Range: 1 ~ 86400 (24 hours)")
                                        .defineInRange("gutsDurationSeconds", 5, 1, 86400);

                        heroicsThreshold = builder
                                        .comment("")
                                        .comment("[Heroics Threshold / 火事場力発動時の体力閾値]")
                                        .comment("EN: The health percentage threshold to activate the Heroics damage buff.")
                                        .comment("JP: 与ダメージが増加する火事場力が発動する体力の閾値(割合)。")
                                        .comment("Default: 0.4 (40%) | Range: 0.0 ~ 1.0")
                                        .defineInRange("heroicsThreshold", 0.4, 0.0, 1.0);

                        heroicsMultiplier = builder
                                        .comment("")
                                        .comment("[Heroics Multiplier / 火事場力のダメージ倍率]")
                                        .comment("EN: The damage multiplier applied when health is below heroicsThreshold.")
                                        .comment("JP: 体力が閾値を下回っている時に与えるダメージの倍率。")
                                        .comment("Default: 2.0 (Double Damage) | Range: 1.0 ~ 100.0")
                                        .defineInRange("heroicsMultiplier", 2.0, 1.0, 100.0);

                        shokugiNutritionLevelUpRequirement = builder
                                        .comment("Nutrition/Food-Level units per Shokugi level and SP; default 2000")
                                        .defineInRange("shokugiNutritionLevelUpRequirement", 2000L, 1L, Long.MAX_VALUE);
                        legacyFoodActionThreshold = builder
                                        .comment("Frozen effective legacy threshold for one-time player migration; do not edit")
                                        .defineInRange("legacyFoodActionThreshold", 200L, 1L, 1_000_000L);
                        shokugiCountModelVersion = builder
                                        .comment("Count Config migration version; independent of player schema")
                                        .defineInRange("shokugiCountModelVersion", 1, 1, 1);
                        shokugiLevelUpRequirement = builder
                                        .comment("")
                                        .comment("[DEPRECATED: legacy food-action threshold; one-time migration input only]")
                                        .comment("EN: Amount of food items you need to eat to gain 1 Shokugi Level")
                                        .comment("JP: 食義レベルが1上がるのに必要な食事回数(デフォルト200回)")
                                        .comment("Default: 200 | Range: 1 ~ 1,000,000")
                                        .defineInRange("shokugiLevelUpRequirement", 200, 1, 1_000_000);

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

                        builder.comment("")
                                        .comment("========================================")
                                        .comment("GUI Settings / GUI設定")
                                        .comment("========================================")
                                        .push("gui");

                        overlayOffsetY = builder
                                        .comment("")
                                        .comment("[Overlay Offset Y / Y軸(縦)の表示位置調整]")
                                        .comment("EN: Adjust the vertical position of the health bar. Negative goes up.")
                                        .comment("JP: HPバーの縦の表示位置を調整します。マイナスで上方向へ移動します。")
                                        .comment("Default: -10 | Range: -1000 ~ 1000")
                                        .defineInRange("overlayOffsetY", -10, -1000, 1000);

                        overlayOffsetX = builder
                                        .comment("")
                                        .comment("[Overlay Offset X / X軸(横)の表示位置調整]")
                                        .comment("EN: Adjust the horizontal position of the health bar. Negative goes left.")
                                        .comment("JP: HPバーの横の表示位置を調整します。マイナスで左方向へ移動します。")
                                        .comment("Default: 0 | Range: -1000 ~ 1000")
                                        .defineInRange("overlayOffsetX", 0, -1000, 1000);

                        heroicsTextOffsetY = builder
                                        .comment("")
                                        .comment("[Heroics UI Offset Y / 火事場UIのY軸(縦)補正]")
                                        .comment("Default: 30 | Range: -1000 ~ 1000")
                                        .defineInRange("heroicsTextOffsetY", 30, -1000, 1000);

                        heroicsTextOffsetX = builder
                                        .comment("")
                                        .comment("[Heroics UI Offset X / 火事場UIのX軸(横)補正]")
                                        .comment("Default: 10 | Range: -1000 ~ 1000")
                                        .defineInRange("heroicsTextOffsetX", 10, -1000, 1000);

                        shokugiTextOffsetY = builder
                                        .comment("")
                                        .comment("[Shokugi UI Offset Y / 食技UIのY軸(縦)補正]")
                                        .comment("Default: 42 | Range: -1000 ~ 1000")
                                        .defineInRange("shokugiTextOffsetY", 42, -1000, 1000);

                        shokugiTextOffsetX = builder
                                        .comment("")
                                        .comment("[Shokugi UI Offset X / 食技UIのX軸(横)補正]")
                                        .comment("Default: 10 | Range: -1000 ~ 1000")
                                        .defineInRange("shokugiTextOffsetX", 10, -1000, 1000);

                        builder.pop();
                }
        }
}
