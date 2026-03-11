package com.example.examplemod;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DamageEventHandler {

    /**
     * ダメージを受ける直前の処理。
     * ここでHPを計算し、致死ダメージの場合はHPが必ず1残るようにダメージを軽減・またはキャンセルする。
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        
        // 根性エフェクトが付与されているかチェック
        if (entity.hasEffect(FoodHealingMod.GUTS_EFFECT.get())) {
            float health = entity.getHealth();
            float damage = event.getAmount();

            // 防具等の軽減計算前の素のダメージ額ではあるが、
            // 「現在のHP - ダメージ <= 1.0F」となるような致命傷をチェック（厳しめに確実に残す）
            if (health - damage <= 1.0F) {
                if (health <= 1.0F) {
                    // すでにHPが1以下の場合は、全てのダメージを無効化する
                    event.setCanceled(true);
                } else {
                    // HPが1残るようにダメージ量を調整する
                    event.setAmount(health - 1.0F);
                }
            }
        }
    }

    /**
     * /kill コマンドや落下（奈落）、その他MODによる強制的な死亡イベントに対する最後の砦。
     * 死亡処理自体をキャンセルし、HPを強制的に1に設定する。
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();

        // 根性エフェクトが付与されているかチェック
        if (entity.hasEffect(FoodHealingMod.GUTS_EFFECT.get())) {
            // 死亡をキャンセル
            event.setCanceled(true);
            
            // HPを1.0に設定
            entity.setHealth(1.0F);
        }
    }
}
