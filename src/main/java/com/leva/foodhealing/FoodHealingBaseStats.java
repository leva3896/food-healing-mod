package com.leva.foodhealing;

import com.leva.foodhealing.capability.IShokugiData;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;

import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public final class FoodHealingBaseStats {
    static final UUID BASE_DEFENSE_UUID = UUID.fromString("13ab10e9-952c-4fa2-a58e-62fe3af981b7");
    static final UUID BASE_MAX_HEALTH_UUID = UUID.fromString("25c88c1b-093d-4d45-bfeb-5d6f46b1c854");

    private static final Map<String, Long> COSTS = Map.of(
            FoodHealingBaseStatIds.BASE_DEFENSE, 5L,
            FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR, 10L,
            FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_LINEAR, 10L,
            FoodHealingBaseStatIds.BASE_MAX_HEALTH, 5L,
            FoodHealingBaseStatIds.RECOVERY_MULTIPLIER, 20L,
            FoodHealingBaseStatIds.BASE_OUTGOING_DAMAGE, 20L,
            FoodHealingBaseStatIds.TACZ_BASE_OUTGOING_DAMAGE, 2L);

    private FoodHealingBaseStats() {
    }

    public static PurchaseResult tryPurchase(IShokugiData data, String statId) {
        return tryPurchase(data, statId, purchaseCount(data, statId));
    }

    public static PurchaseResult tryPurchase(IShokugiData data, String statId, long expectedPurchases) {
        return tryPurchase(data, statId, expectedPurchases,
                modId -> ModList.get() != null && ModList.get().isLoaded(modId));
    }

    static PurchaseResult tryPurchase(IShokugiData data, String statId, long expectedPurchases,
                                      Predicate<String> modLoaded) {
        Long cost = COSTS.get(statId);
        if (cost == null) {
            return PurchaseResult.UNKNOWN_STAT;
        }
        if (!isAvailable(statId, modLoaded)) {
            return PurchaseResult.OPTIONAL_MOD_MISSING;
        }
        long purchases = purchaseCount(data, statId);
        if (purchases != expectedPurchases) {
            return PurchaseResult.STALE_REQUEST;
        }
        // A saturated generation cannot distinguish the next request from a replay.
        if (purchases == Long.MAX_VALUE) {
            return PurchaseResult.NUMERIC_LIMIT;
        }

        String targetStat = statId;
        long current = data.getBaseStatPoints(targetStat);
        if (statId.equals(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR) && current >= 99L) {
            targetStat = FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_TRANSCENDENCE;
            current = data.getBaseStatPoints(targetStat);
        } else if (statId.equals(FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_LINEAR) && current >= 99L) {
            targetStat = FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_TRANSCENDENCE;
            current = data.getBaseStatPoints(targetStat);
        }

        if (current == Long.MAX_VALUE) {
            return PurchaseResult.NUMERIC_LIMIT;
        }
        if (!data.trySpendSkillPoints(cost)) {
            return PurchaseResult.INSUFFICIENT_SP;
        }
        data.setBaseStatPoints(targetStat, current + 1L);
        return PurchaseResult.SUCCESS;
    }

    public static long cost(String statId) {
        return COSTS.getOrDefault(statId, 0L);
    }

    /** Shared read-only optional-mod boundary for the GUI and authoritative purchase. */
    public static boolean isAvailable(String statId) {
        return isAvailable(statId, modId -> ModList.get() != null && ModList.get().isLoaded(modId));
    }

    private static boolean isAvailable(String statId, Predicate<String> modLoaded) {
        if (FoodHealingBaseStatIds.TACZ_BASE_OUTGOING_DAMAGE.equals(statId)) {
            return modLoaded.test("tacz");
        }
        return !FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_LINEAR.equals(statId)
                || modLoaded.test("l2hostility") || modLoaded.test("autoleveling");
    }

    public static long purchaseCount(IShokugiData data, String statId) {
        if (statId.equals(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR)) {
            return saturatedSum(
                    data.getBaseStatPoints(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR),
                    data.getBaseStatPoints(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_TRANSCENDENCE));
        }
        if (statId.equals(FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_LINEAR)) {
            return saturatedSum(
                    data.getBaseStatPoints(FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_LINEAR),
                    data.getBaseStatPoints(FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_TRANSCENDENCE));
        }
        return data.getBaseStatPoints(statId);
    }

    public static double recoveryMultiplier(IShokugiData data) {
        return safeLinearMultiplier(data.getBaseStatPoints(FoodHealingBaseStatIds.RECOVERY_MULTIPLIER), 0.25D);
    }

    public static double outgoingDamageMultiplier(IShokugiData data) {
        if (data.isLegacyMigrationPending()) {
            return safeLinearMultiplier(data.getLegacyShokugiLevel(), 0.10D);
        }
        return safeLinearMultiplier(data.getBaseStatPoints(FoodHealingBaseStatIds.BASE_OUTGOING_DAMAGE), 0.10D);
    }

    public static double taczDamageMultiplier(IShokugiData data) {
        if (data.isLegacyMigrationPending()) {
            return safeLinearMultiplier(data.getLegacyShokugiLevel(), 0.01D);
        }
        return safeLinearMultiplier(data.getBaseStatPoints(FoodHealingBaseStatIds.TACZ_BASE_OUTGOING_DAMAGE), 0.01D);
    }

    public static double normalDamageRemaining(IShokugiData data) {
        if (data.isLegacyMigrationPending()) {
            return 1.0D - Math.min(0.99D, data.getLegacyShokugiLevel() * 0.01D);
        }
        return damageRemaining(
                data.getBaseStatPoints(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR),
                data.getBaseStatPoints(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_TRANSCENDENCE));
    }

    public static double highDifficultyDamageRemaining(IShokugiData data) {
        return damageRemaining(
                data.getBaseStatPoints(FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_LINEAR),
                data.getBaseStatPoints(FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_TRANSCENDENCE));
    }

    public static double damageRemaining(long linearPoints, long transcendencePoints) {
        long linear = Math.min(99L, Math.max(0L, linearPoints));
        if (linear < 99L) {
            return (100.0D - linear) / 100.0D;
        }
        long exponent = transcendencePoints >= Long.MAX_VALUE - 2L
                ? Long.MAX_VALUE
                : Math.max(0L, transcendencePoints) + 2L;
        if (exponent > 307L) {
            return Double.MIN_NORMAL;
        }
        return Math.max(Double.MIN_NORMAL, Math.pow(10.0D, -exponent));
    }

    public static float multiplyPositiveDamage(float amount, double multiplier) {
        if (Float.isNaN(amount) || amount <= 0.0F) {
            return 0.0F;
        }
        if (amount == Float.POSITIVE_INFINITY) {
            amount = Float.MAX_VALUE;
        }
        if (!Double.isFinite(multiplier) || multiplier < 0.0D) {
            return multiplier == Double.POSITIVE_INFINITY ? Float.MAX_VALUE : amount;
        }
        double result = amount * multiplier;
        if (!Double.isFinite(result) || result > Float.MAX_VALUE) {
            return Float.MAX_VALUE;
        }
        if (multiplier > 0.0D && result < Float.MIN_NORMAL) {
            return Float.MIN_NORMAL;
        }
        return (float) result;
    }

    public static void rebuildOwnedModifiers(Player player, IShokugiData data) {
        if (player.level().isClientSide()) {
            return;
        }

        var armor = player.getAttribute(Attributes.ARMOR);
        if (armor != null) {
            armor.removeModifier(BASE_DEFENSE_UUID);
            double amount = safeScaledValue(data.getBaseStatPoints(FoodHealingBaseStatIds.BASE_DEFENSE), 5.0D);
            if (amount > 0.0D) {
                armor.addPermanentModifier(new AttributeModifier(
                        BASE_DEFENSE_UUID, "Food Healing Base Defense", amount, AttributeModifier.Operation.ADDITION));
            }
        }

        var maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth != null) {
            maxHealth.removeModifier(BASE_MAX_HEALTH_UUID);
            double configuredCap = FoodHealingConfig.COMMON.maxHealthCap.get();
            double amount = Math.min(configuredCap,
                    safeScaledValue(data.getBaseStatPoints(FoodHealingBaseStatIds.BASE_MAX_HEALTH), 2.0D));
            if (amount > 0.0D) {
                maxHealth.addPermanentModifier(new AttributeModifier(
                        BASE_MAX_HEALTH_UUID, "Food Healing Base Max Health", amount,
                        AttributeModifier.Operation.ADDITION));
            }
        }
    }

    private static double safeLinearMultiplier(long points, double step) {
        double increment = safeScaledValue(points, step);
        return increment >= Double.MAX_VALUE - 1.0D ? Double.MAX_VALUE : 1.0D + increment;
    }

    private static double safeScaledValue(long points, double step) {
        if (points <= 0L || step <= 0.0D) {
            return 0.0D;
        }
        double result = points * step;
        return Double.isFinite(result) ? result : Double.MAX_VALUE;
    }

    private static long saturatedSum(long left, long right) {
        return left > Long.MAX_VALUE - right ? Long.MAX_VALUE : left + right;
    }

    public enum PurchaseResult {
        SUCCESS,
        UNKNOWN_STAT,
        INSUFFICIENT_SP,
        OPTIONAL_MOD_MISSING,
        NUMERIC_LIMIT,
        STALE_REQUEST
    }
}
