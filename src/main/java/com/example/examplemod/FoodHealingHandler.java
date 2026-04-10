package com.example.examplemod;

import com.mojang.logging.LogUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;
import com.example.examplemod.capability.ShokugiProvider;
import com.example.examplemod.network.PacketHandler;
import com.example.examplemod.network.ShokugiSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

/**
 * 食事イベントを監視し、満腹度回復に応じて体力を回復させるハンドラー
 */
public class FoodHealingHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onItemUseStart(LivingEntityUseItemEvent.Start event) {
        if (!(event.getEntity() instanceof Player player)) return;
        
        ItemStack itemStack = event.getItem();
        if (itemStack.getItem().getFoodProperties(itemStack, player) != null) {
            player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
                int level = cap.getLevel();
                if (level >= 3 && !cap.isSkillDisabled("早食い")) {
                    event.setDuration(event.getDuration() / 2); // 食べる速度 2倍
                }
            });
        }
    }

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

        int gutsThreshold = FoodHealingConfig.COMMON.gutsThreshold.get();
        int gutsDurationSeconds = FoodHealingConfig.COMMON.gutsDurationSeconds.get();
        int gutsDurationTicks = gutsDurationSeconds * 20;

        // 体力回復量を計算
        float healAmount = (float) (nutrition * healMultiplier);

        // 体力を回復（最大体力を超えない）
        player.heal(healAmount);

        LOGGER.info("[FoodHealing] Player {} ate food with nutrition {}. Healed {} HP.",
                player.getName().getString(), nutrition, healAmount);

        // HungerChangeHandlerでの重複回復を防ぐため、食事した時間を記録
        HungerChangeHandler.lastAteTimes.put(player.getUUID(), player.level().getGameTime());

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

        // 根性効果の付与
        if (nutrition >= gutsThreshold) {
            player.addEffect(new MobEffectInstance(
                    FoodHealingMod.GUTS_EFFECT.get(),
                    gutsDurationTicks,
                    0, // Level 1
                    false, // ambient
                    true, // visible particles
                    true // show icon
            ));

            LOGGER.info("[FoodHealing] Guts effect applied! for {} seconds.",
                    gutsDurationSeconds);
        }

        // 食義（Shokugi）データの更新（サーバー側のみ）
        if (!player.level().isClientSide()) {
            player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
                int required = FoodHealingConfig.COMMON.shokugiLevelUpRequirement.get();
                cap.addEatCount(1);
                
                // オーバーフローを考慮しつつレベルアップ判定
                while (cap.getEatCount() >= required && cap.getLevel() < 1000) {
                    cap.addLevel(1);
                    cap.setEatCount(cap.getEatCount() - required);
                    notifyLevelUp((ServerPlayer) player, cap.getLevel());
                }
                
                // カンスト処理
                if (cap.getLevel() >= 1000) {
                    cap.setLevel(1000);
                    cap.setEatCount(0);
                }

                // 同期パケット送信
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                        new ShokugiSyncPacket(cap.getLevel(), cap.getEatCount(), cap.getDisabledSkills()));
            });
        }

        // 満足感スキルの処理（Lv11-13）アイテム消費を確率で無効化
        player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
            int level = cap.getLevel();
            if (!cap.isSkillDisabled("満足感")) {
                float saveChance = 0.0f;
                if (level >= 13) saveChance = 0.75f;
                else if (level >= 12) saveChance = 0.50f;
                else if (level >= 11) saveChance = 0.25f;

                if (saveChance > 0 && player.getRandom().nextFloat() < saveChance) {
                    // 返却用アイテム
                    ItemStack refund = itemStack.copyWithCount(1);
                    ItemStack result = event.getResultStack();
                    
                    if (result.isEmpty()) {
                        event.setResultStack(refund); // 最後の1個だった場合そのまま返却
                    } else if (ItemStack.isSameItemSameTags(result, itemStack)) {
                        result.grow(1); // スタックサイズが減っただけの場合は+1して戻す
                    } else {
                        event.setResultStack(refund); // シチューの器など別のアイテムに変わってしまった場合は強制的に元のシチューを返す
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        
        ItemStack crafted = event.getCrafting();
        if (crafted.isEmpty()) return;
        
        // 食べ物か判定
        if (crafted.getItem().getFoodProperties(crafted, player) != null) {
            player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
                // Lv 8: 食べ物クラフト2倍
                if (cap.getLevel() >= 8 && !cap.isSkillDisabled("豊穣")) {
                    ItemStack bonus = crafted.copy();
                    if (!player.getInventory().add(bonus)) {
                        player.drop(bonus, false);
                    }
                }
            });
        }
    }

    private static void notifyLevelUp(ServerPlayer player, int newLevel) {
        player.level().playSound(null, player.blockPosition(), net.minecraft.sounds.SoundEvents.PLAYER_LEVELUP, net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
        String skillName = getLearnedSkillName(newLevel);
        if (skillName != null) {
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§6食義レベルが " + newLevel + " にアップした！スキル『" + skillName + "』を覚えた！！"));
        } else {
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§6食義レベルが " + newLevel + " にアップした！"));
        }
    }

    private static String getLearnedSkillName(int level) {
        return switch (level) {
            case 1 -> "耐火の心得";
            case 2 -> "水月と暗視の心得";
            case 3 -> "早食いⅠ";
            case 4 -> "軽業の心得";
            case 5 -> "炎の加護";
            case 6 -> "爆破耐性の心得";
            case 7 -> "浄化の心得";
            case 8 -> "豊穣の心得";
            case 9 -> "屠殺の心得";
            case 10 -> "火事場力解放";
            case 11 -> "満足感Ⅰ";
            case 12 -> "満足感Ⅱ";
            case 13 -> "満足感Ⅲ";
            case 14 -> "採取の心得Ⅰ";
            case 15 -> "採取の心得Ⅱ";
            case 16 -> "採取の心得Ⅲ";
            case 17 -> "不壊の心得Ⅰ";
            case 18 -> "不壊の心得Ⅱ";
            case 19 -> "不壊の心得Ⅲ";
            case 20 -> "防具の極意";
            case 30 -> "飛翔の心得";
            case 100 -> "金剛の心得";
            case 991 -> "追撃の心得 極Ⅰ";
            case 992 -> "追撃の心得 極Ⅱ";
            case 993 -> "追撃の心得 極Ⅲ";
            case 994 -> "追撃の心得 極Ⅳ";
            case 995 -> "追撃の心得 極Ⅴ";
            case 996 -> "追撃の心得 極Ⅵ";
            case 997 -> "追撃の心得 極Ⅶ";
            case 998 -> "追撃の心得 極Ⅷ";
            case 999 -> "追撃の心得 極Ⅸ";
            default -> null;
        };
    }
}
