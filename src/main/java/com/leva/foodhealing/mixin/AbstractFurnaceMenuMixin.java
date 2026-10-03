package com.leva.foodhealing.mixin;

import com.leva.foodhealing.FoodProductionTransactions;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractFurnaceMenu.class)
public abstract class AbstractFurnaceMenuMixin extends RecipeBookMenu<Container> {
    protected AbstractFurnaceMenuMixin(MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    @Redirect(
            method = "quickMoveStack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/AbstractFurnaceMenu;moveItemStackTo(Lnet/minecraft/world/item/ItemStack;IIZ)Z"))
    private boolean foodhealing$doubleShiftClickFoodResult(AbstractFurnaceMenu menu, ItemStack result,
                                                           int startIndex, int endIndex, boolean reverseDirection,
                                                           Player player, int slotIndex) {
        if (slotIndex != 2 || player.level().isClientSide()
                || !FoodProductionTransactions.shouldDoubleFoodResult(player, result)) {
            return moveItemStackTo(result, startIndex, endIndex, reverseDirection);
        }

        ItemStack template = result.copy();
        int before = result.getCount();
        boolean moved = moveItemStackTo(result, startIndex, endIndex, reverseDirection);
        int extracted = before - result.getCount();
        if (extracted <= 0) {
            return moved;
        }

        ItemStack bonus = template.copy();
        bonus.setCount(extracted);
        moveItemStackTo(bonus, startIndex, endIndex, reverseDirection);
        if (!bonus.isEmpty()) {
            player.drop(bonus, false);
        }
        return moved;
    }
}
