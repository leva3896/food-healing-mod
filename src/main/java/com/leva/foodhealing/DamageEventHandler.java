package com.leva.foodhealing;

import com.leva.foodhealing.capability.IShokugiData;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.leva.foodhealing.compat.LegacyProjectileCompat;
import com.leva.foodhealing.compat.SuperbWarfareCompat;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class DamageEventHandler {
    private static final ThreadLocal<Boolean> IS_PURSUIT = ThreadLocal.withInitial(() -> false);

    private DamageEventHandler() {
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide() || IS_PURSUIT.get()) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof Player player)) {
            return;
        }

        player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(data -> {
            float input = event.getAmount();
            // Restore the audited old local order for the newly connected v3 gunfire route.
            // Pending legacy remains on its existing branch below, never both branches.
            if (!data.isLegacyMigrationPending() && SuperbWarfareCompat.isSupportedGunfire(event.getSource())) {
                input = FoodHealingBaseStats.multiplyPositiveDamage(
                        input, SuperbWarfareCompat.shokugiMultiplier(data));
            }
            float amount = FoodHealingBaseStats.multiplyPositiveDamage(
                    input, FoodHealingBaseStats.outgoingDamageMultiplier(data));

            var directEntity = event.getSource().getDirectEntity();
            if (LegacyProjectileCompat.isFromOptionalMod(directEntity, "tacz")) {
                amount = FoodHealingBaseStats.multiplyPositiveDamage(
                        amount, FoodHealingBaseStats.taczDamageMultiplier(data));
                int ammoSkillLevel = FoodHealingSkills.getEnabledLevel(
                        data, FoodHealingSkillIds.TACZ_AMMO_CONSERVATION);
                if (ammoSkillLevel > 0) {
                    amount = FoodHealingBaseStats.multiplyPositiveDamage(
                            amount, 1.0D + Math.min(10, ammoSkillLevel) * 0.05D);
                }
            }

            // Preserve the existing guarded pending-legacy fallback without widening it.
            if (data.isLegacyMigrationPending()
                    && LegacyProjectileCompat.isFromOptionalMod(directEntity, "superbwarfare")) {
                amount = FoodHealingBaseStats.multiplyPositiveDamage(
                        amount, legacySuperbWarfareMultiplier(data.getLegacyShokugiLevel()));
            }

            amount = FoodHealingBaseStats.multiplyPositiveDamage(
                    amount, HeroicsController.outgoingMultiplier(player, data));
            event.setAmount(amount);
        });
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) {
            return;
        }

        if (entity instanceof Player player) {
            player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(data -> {
                if (FoodHealingSkills.isEnabled(data, FoodHealingSkillIds.ACROBATICS)
                        && event.getSource().is(DamageTypes.FALL)) {
                    event.setCanceled(true);
                    return;
                }
                if (FoodHealingSkills.isEnabled(data, FoodHealingSkillIds.FIRE_RESISTANCE)
                        && event.getSource().is(DamageTypeTags.IS_FIRE)) {
                    event.setCanceled(true);
                    return;
                }

                float amount = event.getAmount();
                if (FoodHealingSkills.isEnabled(data, FoodHealingSkillIds.EXPLOSION_RESISTANCE)
                        && event.getSource().is(DamageTypeTags.IS_EXPLOSION)) {
                    amount = FoodHealingBaseStats.multiplyPositiveDamage(amount, 0.10D);
                }
                if (FoodHealingSkills.isEnabled(data, FoodHealingSkillIds.FLAME_BLESSING) && player.isOnFire()) {
                    amount = FoodHealingBaseStats.multiplyPositiveDamage(amount, 0.70D);
                }

                amount = FoodHealingBaseStats.multiplyPositiveDamage(
                        amount, FoodHealingBaseStats.normalDamageRemaining(data));
                amount = FoodHealingBaseStats.multiplyPositiveDamage(
                        amount, FoodHealingBaseStats.highDifficultyDamageRemaining(data));
                amount = FoodHealingBaseStats.multiplyPositiveDamage(
                        amount, HeroicsController.damageRemaining(player, data));
                amount = FoodHealingBaseStats.multiplyPositiveDamage(amount, kongoDamageRemaining(player, data, event));
                if (player instanceof ServerPlayer && FoodHealingSkills.isCanonicalSkillEnabled(
                        data, FoodHealingSkillIds.IMMOVABLE_MASTERY)) {
                    amount = FoodHealingBaseStats.multiplyPositiveDamage(amount, 0.50D);
                }
                event.setAmount(amount);
            });
        }

        if (!event.isCanceled() && !IS_PURSUIT.get() && event.getSource().getEntity() instanceof Player attacker) {
            attacker.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(data -> {
                int hits = FoodHealingSkills.getEnabledLevel(data, FoodHealingSkillIds.PURSUIT);
                if (hits > 0) {
                    applyPursuitHits(entity, event, Math.min(9, hits));
                }
            });
        }

        if (isRootProtectionActive(entity)) {
            float health = entity.getHealth();
            float damage = event.getAmount();
            if (health - damage <= 1.0F) {
                if (health <= 1.0F) {
                    event.setCanceled(true);
                } else {
                    event.setAmount(health - 1.0F);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDeath(LivingDeathEvent event) {
        if (isRootProtectionActive(event.getEntity())) {
            event.setCanceled(true);
            event.getEntity().setHealth(1.0F);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        restoreRootProtectedHealth(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide()) {
            restoreRootProtectedHealth(event.player);
        }
    }

    private static void applyPursuitHits(LivingEntity target, LivingDamageEvent originalEvent, int hits) {
        float pursuitDamage = originalEvent.getAmount();
        int originalInvulnerableTime = target.invulnerableTime;
        IS_PURSUIT.set(true);
        try {
            for (int i = 0; i < hits && target.isAlive(); i++) {
                target.invulnerableTime = 0;
                target.hurt(originalEvent.getSource(), pursuitDamage);
            }
        } finally {
            target.invulnerableTime = originalInvulnerableTime;
            IS_PURSUIT.set(false);
        }
    }

    private static double kongoDamageRemaining(Player player, IShokugiData data, LivingDamageEvent event) {
        if (!FoodHealingSkills.isEnabled(data, FoodHealingSkillIds.KONGO)
                || event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS)) {
            return 1.0D;
        }

        var resistance = player.getEffect(MobEffects.DAMAGE_RESISTANCE);
        if (resistance == null) {
            return 0.20D;
        }
        int amplifier = resistance.getAmplifier();
        if (amplifier >= 3) {
            return 1.0D;
        }

        double existingRemaining = 1.0D - (amplifier + 1) * 0.20D;
        return existingRemaining > 0.0D ? 0.20D / existingRemaining : 1.0D;
    }

    private static double legacySuperbWarfareMultiplier(long legacyLevel) {
        double multiplier = 1.0D + (double) legacyLevel;
        return Double.isFinite(multiplier) ? multiplier : Double.MAX_VALUE;
    }

    private static boolean isRootProtectionActive(LivingEntity entity) {
        return entity instanceof ServerPlayer player && RootController.isActive(player);
    }

    private static void restoreRootProtectedHealth(LivingEntity entity) {
        if (isRootProtectionActive(entity) && entity.getHealth() <= 0.0F) {
            entity.setHealth(1.0F);
            if (entity.deathTime > 0) {
                entity.deathTime = 0;
            }
        }
    }
}
