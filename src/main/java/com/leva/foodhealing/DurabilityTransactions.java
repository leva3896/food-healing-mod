package com.leva.foodhealing;

import com.leva.foodhealing.capability.IShokugiData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

public final class DurabilityTransactions {
    private DurabilityTransactions() {
    }

    public static int adjustDamage(IShokugiData data, ItemStack stack, int amount, RandomSource random) {
        return adjustDamage(data, stack, amount, random::nextFloat);
    }

    public static int adjustDamage(IShokugiData data, ItemStack stack, int amount, float randomRoll) {
        return adjustDamage(data, stack, amount, () -> randomRoll);
    }

    private static int adjustDamage(IShokugiData data, ItemStack stack, int amount, RollSource rollSource) {
        if (amount <= 0 || !stack.isDamageableItem()) {
            return amount;
        }

        int adjusted = amount;
        if (FoodHealingSkills.isEnabled(data, FoodHealingSkillIds.ARMOR_MASTERY)) {
            adjusted = Math.min(adjusted, 1);
        }

        int unbreakingLevel = FoodHealingSkills.getEnabledLevel(data, FoodHealingSkillIds.UNBREAKING);
        if (unbreakingLevel <= 0) {
            return adjusted;
        }
        float saveChance = switch (Math.min(3, unbreakingLevel)) {
            case 1 -> 9.0F / 10.0F;
            case 2 -> 19.0F / 20.0F;
            default -> 29.0F / 30.0F;
        };
        float randomRoll = rollSource.nextFloat();
        return Float.isFinite(randomRoll) && randomRoll >= 0.0F && randomRoll < saveChance
                ? 0
                : adjusted;
    }

    @FunctionalInterface
    private interface RollSource {
        float nextFloat();
    }
}
