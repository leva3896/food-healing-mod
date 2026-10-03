package com.leva.foodhealing;

import com.leva.foodhealing.capability.IShokugiData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public final class HeroicsController {
    private static final double HEROICS_THRESHOLD = 0.40D;
    private static final double TRUE_HEROICS_THRESHOLD = 0.80D;
    static final UUID LEGACY_ARMOR_MODIFIER_UUID = UUID.fromString("17b60e24-0eca-4afb-858f-f0e830dd0aa8");
    static final UUID LEGACY_TOUGHNESS_MODIFIER_UUID = UUID.fromString("b9db21d8-8787-42df-a724-11a0d36bbd30");

    private HeroicsController() {
    }

    public static boolean isNormalActive(Player player, IShokugiData data) {
        if (!FoodHealingSkills.isEnabled(data, FoodHealingSkillIds.HEROICS)) {
            return false;
        }
        double threshold = data.getSkillLevel(FoodHealingSkillIds.HEROICS) > 0
                ? HEROICS_THRESHOLD
                : FoodHealingConfig.COMMON.heroicsThreshold.get();
        return isAtOrBelowHealthRatio(player, threshold);
    }

    public static boolean isTrueActive(Player player, IShokugiData data) {
        return FoodHealingSkills.isEnabled(data, FoodHealingSkillIds.TRUE_HEROICS)
                && isAtOrBelowHealthRatio(player, TRUE_HEROICS_THRESHOLD);
    }

    public static double outgoingMultiplier(Player player, IShokugiData data) {
        if (isTrueActive(player, data)) {
            return 20.0D;
        }
        if (!isNormalActive(player, data)) {
            return 1.0D;
        }

        int purchasedLevel = data.getSkillLevel(FoodHealingSkillIds.HEROICS);
        if (purchasedLevel <= 0) {
            return FoodHealingConfig.COMMON.heroicsMultiplier.get();
        }
        return switch (Math.min(5, purchasedLevel)) {
            case 1 -> 2.0D;
            case 2 -> 2.5D;
            case 3 -> 3.0D;
            case 4 -> 4.0D;
            default -> 5.0D;
        };
    }

    public static double damageRemaining(Player player, IShokugiData data) {
        if (isTrueActive(player, data)) {
            return 0.01D;
        }
        int level = data.getSkillLevel(FoodHealingSkillIds.HEROICS);
        if (level <= 0 || !isNormalActive(player, data)) {
            return 1.0D;
        }
        return 1.0D - Math.min(5, level) * 0.10D;
    }

    public static double armorMultiplier(Player player, IShokugiData data) {
        if (isTrueActive(player, data)) {
            return 64.0D;
        }
        int level = data.getSkillLevel(FoodHealingSkillIds.HEROICS);
        if (level <= 0 || !isNormalActive(player, data)) {
            return 1.0D;
        }
        return Math.scalb(1.0D, Math.min(5, level));
    }

    public static float effectiveArmor(Player player, IShokugiData data) {
        return effectiveAttributeValue(player.getAttribute(Attributes.ARMOR),
                LEGACY_ARMOR_MODIFIER_UUID, armorMultiplier(player, data));
    }

    public static float effectiveToughness(Player player, IShokugiData data) {
        return effectiveAttributeValue(player.getAttribute(Attributes.ARMOR_TOUGHNESS),
                LEGACY_TOUGHNESS_MODIFIER_UUID, armorMultiplier(player, data));
    }

    private static float effectiveAttributeValue(AttributeInstance attribute, UUID excludedModifier,
                                                 double multiplier) {
        if (attribute == null) {
            return 0.0F;
        }

        double baseWithAdditions = attribute.getBaseValue();
        for (AttributeModifier modifier : attribute.getModifiers(AttributeModifier.Operation.ADDITION)) {
            if (!modifier.getId().equals(excludedModifier)) {
                baseWithAdditions = safeAdd(baseWithAdditions, modifier.getAmount());
            }
        }

        double value = baseWithAdditions;
        for (AttributeModifier modifier : attribute.getModifiers(AttributeModifier.Operation.MULTIPLY_BASE)) {
            if (!modifier.getId().equals(excludedModifier)) {
                value = safeAdd(value, safeMultiplySigned(baseWithAdditions, modifier.getAmount()));
            }
        }
        for (AttributeModifier modifier : attribute.getModifiers(AttributeModifier.Operation.MULTIPLY_TOTAL)) {
            if (!modifier.getId().equals(excludedModifier)) {
                value = safeMultiplySigned(value, safeAdd(1.0D, modifier.getAmount()));
            }
        }

        return toFiniteFloat(safeMultiplySigned(Math.max(0.0D, value), multiplier));
    }

    private static double safeAdd(double left, double right) {
        double result = left + right;
        if (Double.isNaN(result)) {
            return 0.0D;
        }
        if (result == Double.POSITIVE_INFINITY) {
            return Double.MAX_VALUE;
        }
        if (result == Double.NEGATIVE_INFINITY) {
            return -Double.MAX_VALUE;
        }
        return result;
    }

    private static double safeMultiplySigned(double value, double multiplier) {
        double result = value * multiplier;
        if (Double.isNaN(result)) {
            return 0.0D;
        }
        if (result == Double.POSITIVE_INFINITY) {
            return Double.MAX_VALUE;
        }
        if (result == Double.NEGATIVE_INFINITY) {
            return -Double.MAX_VALUE;
        }
        return result;
    }

    private static float toFiniteFloat(double value) {
        if (value <= 0.0D || Double.isNaN(value)) {
            return 0.0F;
        }
        return (float) Math.min(value, Float.MAX_VALUE);
    }

    public static boolean isAtOrBelowHealthRatio(Player player, double ratio) {
        return player.getMaxHealth() > 0.0F
                && player.getHealth() <= (double) player.getMaxHealth() * ratio;
    }
}
