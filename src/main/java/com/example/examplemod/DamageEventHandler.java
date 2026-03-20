package com.example.examplemod;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DamageEventHandler {

    /**
     * エンティティがダメージを受ける計算の初期段階（防御・耐性などの前）。
     * ここで攻撃者がプレイヤーであり、かつプレイヤーのHPが設定値（火事場力）以下である場合、ダメージを倍増させる。
     * TACZの銃弾もこのイベントを通過するため銃撃にも適用される。
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onLivingHurt(LivingHurtEvent event) {
        // ダメージ元がプレイヤーであるか（直接の近接攻撃や、飛び道具の撃ち手など）
        if (event.getSource().getEntity() instanceof Player player) {
            float threshold = FoodHealingConfig.COMMON.heroicsThreshold.get().floatValue();
            
            // プレイヤーの現在のHPが最大HPの指定割合(デフォルト40%)未満か判定
            if (player.getHealth() < player.getMaxHealth() * threshold) {
                float multiplier = FoodHealingConfig.COMMON.heroicsMultiplier.get().floatValue();
                
                // ダメージを倍増させる
                event.setAmount(event.getAmount() * multiplier);
            }
        }
    }

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

    /**
     * エンティティの毎ティック更新処理（開始直後）。
     * 一般的なMOBやプレイヤーへの強制的なHP0化をキャッチして復活させる。
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();

        // 根性エフェクトが付与されているか
        if (entity.hasEffect(FoodHealingMod.GUTS_EFFECT.get())) {

            // HPが0以下になっている場合、強制蘇生
            if (entity.getHealth() <= 0.0F) {
                // HPを強制的に1に復元
                entity.setHealth(1.0F);

                // すでに死亡判定に入っていた場合、その判定状態を解除する
                if (entity.deathTime > 0) {
                    entity.deathTime = 0;
                }
            }
        }
    }

    /**
     * プレイヤーのティック更新完了時（終了直前）。
     * MekanismDelightの反物質シチュー(Antimatter Stew)など、プレイヤーのHPを強制的に0にする処理が
     * ティックの中盤(completeUsingItem等)で行われた場合、次のティックのonLivingTick実行前に
     * サーバーからクライアントへHP0が送信されてしまい、死亡画面が開いてしまう問題への対策。
     * ここでティックの終わりにHP1へ上書きし、死亡状態がサーバーから漏れるのを防ぐ。
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        // ティックの最後(END)かつ、サーバー側でのみ実行する
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide()) {
            net.minecraft.world.entity.player.Player player = event.player;

            // 根性エフェクトが付与されているか
            if (player.hasEffect(FoodHealingMod.GUTS_EFFECT.get())) {

                // HPが0以下になっている場合、強制蘇生
                if (player.getHealth() <= 0.0F) {
                    // クライアントへ送信されるパケットに乗る前にHPを1に復元
                    player.setHealth(1.0F);

                    if (player.deathTime > 0) {
                        player.deathTime = 0;
                    }
                }
            }
        }
    }
}
