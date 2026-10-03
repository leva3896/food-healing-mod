package com.leva.foodhealing.mixin;

import com.leva.foodhealing.FoodProductionTransactions;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FurnaceResultSlot.class)
public abstract class FurnaceResultSlotMixin extends Slot {
    @Shadow
    @Final
    private Player player;

    protected FurnaceResultSlotMixin(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @ModifyVariable(method = "remove", at = @At("HEAD"), argsOnly = true)
    private int foodhealing$limitManualFoodExtraction(int requestedAmount) {
        ItemStack result = getItem();
        if (!FoodProductionTransactions.shouldDoubleFoodResult(player, result)) {
            return requestedAmount;
        }
        return Math.min(requestedAmount, Math.max(1, result.getMaxStackSize() / 2));
    }

    @Inject(method = "remove", at = @At("RETURN"), cancellable = true)
    private void foodhealing$doubleManualFoodExtraction(int requestedAmount,
                                                        CallbackInfoReturnable<ItemStack> callback) {
        ItemStack extracted = callback.getReturnValue();
        if (!FoodProductionTransactions.shouldDoubleFoodResult(player, extracted)) {
            return;
        }

        long doubledCount = (long) extracted.getCount() * 2L;
        if (doubledCount <= extracted.getMaxStackSize()) {
            ItemStack doubled = extracted.copy();
            doubled.setCount((int) doubledCount);
            callback.setReturnValue(doubled);
            return;
        }

        ItemStack bonus = extracted.copy();
        FoodProductionTransactions.deliverBonusOrDrop(player, bonus);
    }
}
