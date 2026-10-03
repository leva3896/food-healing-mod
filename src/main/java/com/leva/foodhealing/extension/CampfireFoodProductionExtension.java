package com.leva.foodhealing.extension;

import com.leva.foodhealing.FoodProductionTransactions;
import net.minecraft.world.item.ItemStack;

public interface CampfireFoodProductionExtension {
    FoodProductionTransactions.CraftingResultPlan foodhealing$planReadyCampfireResult(ItemStack result);
}
