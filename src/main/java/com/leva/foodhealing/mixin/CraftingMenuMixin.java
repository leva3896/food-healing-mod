package com.leva.foodhealing.mixin;

import com.leva.foodhealing.FoodProductionTransactions;
import com.leva.foodhealing.extension.FoodProductionResultSlotExtension;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CraftingMenu.class)
public abstract class CraftingMenuMixin {
    @Redirect(
            method = "slotChangedCraftingGrid",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/crafting/CraftingRecipe;assemble(Lnet/minecraft/world/Container;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;"))
    private static ItemStack foodhealing$assembleFoodProductionResult(
            CraftingRecipe recipe, Container craftingInput, RegistryAccess registryAccess,
            AbstractContainerMenu menu, Level level, Player player,
            CraftingContainer craftingContainer, ResultContainer resultContainer) {
        ItemStack result = recipe.assemble(craftingContainer, registryAccess);
        FoodProductionTransactions.CraftingResultPlan plan =
                FoodProductionTransactions.planPersonalCraftingResult(player, result);
        if (menu.getSlot(0) instanceof FoodProductionResultSlotExtension extension) {
            extension.foodhealing$setCraftingOverflow(plan.overflow());
        }
        return plan.displayedResult();
    }
}
