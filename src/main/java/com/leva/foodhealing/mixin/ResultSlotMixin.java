package com.leva.foodhealing.mixin;

import com.leva.foodhealing.FoodProductionTransactions;
import com.leva.foodhealing.extension.FoodProductionResultSlotExtension;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResultSlot.class)
public abstract class ResultSlotMixin implements FoodProductionResultSlotExtension {
    @Unique
    private ItemStack foodhealing$craftingOverflow = ItemStack.EMPTY;

    @Override
    public void foodhealing$setCraftingOverflow(ItemStack overflow) {
        foodhealing$craftingOverflow = overflow.copy();
    }

    @Inject(method = "onTake", at = @At("HEAD"))
    private void foodhealing$deliverCraftingOverflow(Player player, ItemStack taken, CallbackInfo callback) {
        ItemStack overflow = foodhealing$craftingOverflow;
        foodhealing$craftingOverflow = ItemStack.EMPTY;
        FoodProductionTransactions.deliverBonusOrDrop(player, overflow);
    }
}
