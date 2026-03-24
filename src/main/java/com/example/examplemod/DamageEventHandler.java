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
import com.example.examplemod.capability.ShokugiProvider;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.tags.DamageTypeTags;

public class DamageEventHandler {

    // 追撃による無限ループを防ぐためのフラグ
    private static final ThreadLocal<Boolean> IS_PURSUIT = ThreadLocal.withInitial(() -> false);

    /**
     * エンティティがダメージを受ける計算の初期段階（防御・耐性などの前）。
     * ここで攻撃者がプレイヤーであり、かつプレイヤーのHPが設定値（火事場力）以下である場合、ダメージを倍増させる。
     * TACZの銃弾もこのイベントを通過するため銃撃にも適用される。
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onLivingHurt(LivingHurtEvent event) {
        // ダメージ元がプレイヤーであるか（直接の近接攻撃や、飛び道具の撃ち手など）
        if (event.getSource().getEntity() instanceof Player player) {
            player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
                int level = cap.getLevel();
                
                // 1. 食義レベルによる基礎ステータスダメージの上昇処理
                if (level > 0) {
                    float shokugiMultiplier = 1.0F + (level * 0.1F);
                    event.setAmount(event.getAmount() * shokugiMultiplier);
                }

                // 2. 火事場力によるダメージ倍増処理 (Lv10解放)
                if (level >= 10) {
                    float threshold = FoodHealingConfig.COMMON.heroicsThreshold.get().floatValue();
                    
                    // プレイヤーの現在のHPが最大HPの指定割合(閾値)以下か判定（v2.0.0で緩和: < から <= に変更）
                    if (player.getHealth() <= player.getMaxHealth() * threshold) {
                        float multiplier = FoodHealingConfig.COMMON.heroicsMultiplier.get().floatValue();
                        
                        // ダメージを倍増させる
                        event.setAmount(event.getAmount() * multiplier);
                    }
                }
            });
        }
    }

    /**
     * ダメージを受ける直前の処理。
     * ここでHPを計算し、致死ダメージの場合はHPが必ず1残るようにダメージを軽減・またはキャンセルする。
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();

        // 食義によるダメージ軽減処理（プレイヤーのみ）
        if (entity instanceof Player player) {
            player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
                int level = cap.getLevel();
                
                // Lv 4: 落下ダメージ無効
                if (level >= 4 && event.getSource().is(DamageTypes.FALL)) {
                    event.setCanceled(true);
                    return;
                }
                
                // Lv 6: 爆発ダメージ90%カット
                if (level >= 6 && event.getSource().is(DamageTypeTags.IS_EXPLOSION)) {
                    event.setAmount(event.getAmount() * 0.1F);
                }

                if (level > 0) {
                    // 最大99%カット（耐力エンチャント等とは別枠で計算される）
                    float reductionFactor = Math.min(0.99F, level * 0.01F);
                    float newAmount = event.getAmount() * (1.0F - reductionFactor);
                    event.setAmount(newAmount);
                }
            });
        }

        // 追撃スキル（Lv14-16）の処理（攻撃側がプレイヤーの場合）
        if (!IS_PURSUIT.get() && event.getSource().getEntity() instanceof Player player) {
            player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
                int level = cap.getLevel();
                if (level >= 14) {
                    int hits = level >= 16 ? 3 : (level >= 15 ? 2 : 1);
                    float pursuitDamage = event.getAmount();

                    IS_PURSUIT.set(true);
                    try {
                        for (int i = 0; i < hits; i++) {
                            if (entity.isAlive()) {
                                // 無敵時間を無視して追撃を叩き込む
                                entity.invulnerableTime = 0;
                                entity.hurt(event.getSource(), pursuitDamage);
                            }
                        }
                    } finally {
                        IS_PURSUIT.set(false);
                    }
                }
            });
        }

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
