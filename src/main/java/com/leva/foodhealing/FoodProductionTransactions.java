package com.leva.foodhealing;

import com.leva.foodhealing.capability.ShokugiProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class FoodProductionTransactions {
    private FoodProductionTransactions() {
    }

    public static CraftingResultPlan planPersonalCraftingResult(Player player, ItemStack result) {
        if (result.isEmpty() || result.getFoodProperties(player) == null || !isEnabled(player)) {
            return new CraftingResultPlan(result, ItemStack.EMPTY);
        }

        return planEligibleFoodResult(result, true);
    }

    public static CraftingResultPlan planEligibleFoodResult(ItemStack result, boolean eligible) {
        if (!eligible || result.isEmpty() || result.getFoodProperties(null) == null) {
            return new CraftingResultPlan(result, ItemStack.EMPTY);
        }

        long doubledCount = (long) result.getCount() * 2L;
        int displayedCount = (int) Math.min(doubledCount, result.getMaxStackSize());
        ItemStack displayed = result.copy();
        displayed.setCount(displayedCount);

        int overflowCount = (int) (doubledCount - displayedCount);
        if (overflowCount <= 0) {
            return new CraftingResultPlan(displayed, ItemStack.EMPTY);
        }

        ItemStack overflow = result.copy();
        overflow.setCount(overflowCount);
        return new CraftingResultPlan(displayed, overflow);
    }

    public static boolean shouldDoubleFoodResult(Player player, ItemStack result) {
        return !result.isEmpty() && result.getFoodProperties(player) != null && isEnabled(player);
    }

    public static boolean isFoodProductionEnabled(Player player) {
        return isEnabled(player);
    }

    public static void deliverBonusOrDrop(Player player, ItemStack bonus) {
        if (player.level().isClientSide() || bonus.isEmpty()) {
            return;
        }
        ItemStack remaining = bonus.copy();
        player.getInventory().add(remaining);
        if (!remaining.isEmpty()) {
            player.drop(remaining, false);
        }
    }

    private static boolean isEnabled(Player player) {
        return player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .map(data -> FoodHealingSkills.isEnabled(data, FoodHealingSkillIds.FOOD_PRODUCTION_MASTERY))
                .orElse(false);
    }

    public record CraftingResultPlan(ItemStack displayedResult, ItemStack overflow) {
    }
}
