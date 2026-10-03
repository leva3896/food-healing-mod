package com.leva.foodhealing.mixin;

import com.leva.foodhealing.FoodProductionTransactions;
import com.leva.foodhealing.extension.CampfireFoodProductionExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Arrays;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin implements CampfireFoodProductionExtension {
    @Unique
    private static final String FOODHEALING_ELIGIBLE_TAG = "FoodHealingFoodProductionEligible";

    @Shadow
    @Final
    private NonNullList<ItemStack> items;

    @Shadow
    @Final
    private int[] cookingProgress;

    @Shadow
    @Final
    private int[] cookingTime;

    @Unique
    private final boolean[] foodhealing$productionEligible = new boolean[4];

    @Unique
    private int foodhealing$pendingPlacementSlot = -1;

    @Unique
    private boolean foodhealing$pendingPlacementEligible;

    @Inject(method = "placeFood", at = @At("HEAD"))
    private void foodhealing$capturePlacementOwner(@Nullable Entity actor, ItemStack input, int cookingTicks,
                                                    CallbackInfoReturnable<Boolean> callback) {
        foodhealing$pendingPlacementSlot = -1;
        foodhealing$pendingPlacementEligible = false;
        for (int slot = 0; slot < items.size(); slot++) {
            if (items.get(slot).isEmpty()) {
                foodhealing$pendingPlacementSlot = slot;
                foodhealing$pendingPlacementEligible = actor instanceof Player player
                        && !player.level().isClientSide()
                        && FoodProductionTransactions.isFoodProductionEnabled(player);
                return;
            }
        }
    }

    @Inject(method = "placeFood", at = @At("RETURN"))
    private void foodhealing$commitPlacementOwner(@Nullable Entity actor, ItemStack input, int cookingTicks,
                                                   CallbackInfoReturnable<Boolean> callback) {
        if (callback.getReturnValue() && foodhealing$pendingPlacementSlot >= 0) {
            foodhealing$productionEligible[foodhealing$pendingPlacementSlot] =
                    foodhealing$pendingPlacementEligible;
        }
        foodhealing$pendingPlacementSlot = -1;
        foodhealing$pendingPlacementEligible = false;
    }

    @Override
    public FoodProductionTransactions.CraftingResultPlan foodhealing$planReadyCampfireResult(ItemStack result) {
        for (int slot = 0; slot < items.size(); slot++) {
            if (!items.get(slot).isEmpty() && cookingProgress[slot] >= cookingTime[slot]) {
                boolean eligible = foodhealing$productionEligible[slot];
                foodhealing$productionEligible[slot] = false;
                return FoodProductionTransactions.planEligibleFoodResult(result, eligible);
            }
        }
        return new FoodProductionTransactions.CraftingResultPlan(result, ItemStack.EMPTY);
    }

    @Redirect(
            method = "cookTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/Containers;dropItemStack(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V"))
    private static void foodhealing$dropCampfireResult(Level dropLevel, double x, double y, double z,
                                                        ItemStack result, Level level, BlockPos pos,
                                                        BlockState state, CampfireBlockEntity campfire) {
        FoodProductionTransactions.CraftingResultPlan plan =
                ((CampfireFoodProductionExtension) campfire).foodhealing$planReadyCampfireResult(result);
        Containers.dropItemStack(dropLevel, x, y, z, plan.displayedResult());
        if (!plan.overflow().isEmpty()) {
            Containers.dropItemStack(dropLevel, x, y, z, plan.overflow());
        }
    }

    @Inject(method = "load", at = @At("TAIL"))
    private void foodhealing$loadPlacementOwners(CompoundTag tag, CallbackInfo callback) {
        Arrays.fill(foodhealing$productionEligible, false);
        int[] stored = tag.getIntArray(FOODHEALING_ELIGIBLE_TAG);
        for (int slot = 0; slot < Math.min(stored.length, foodhealing$productionEligible.length); slot++) {
            foodhealing$productionEligible[slot] = stored[slot] != 0;
        }
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void foodhealing$savePlacementOwners(CompoundTag tag, CallbackInfo callback) {
        int[] stored = new int[foodhealing$productionEligible.length];
        for (int slot = 0; slot < stored.length; slot++) {
            stored[slot] = foodhealing$productionEligible[slot] ? 1 : 0;
        }
        tag.putIntArray(FOODHEALING_ELIGIBLE_TAG, stored);
    }

    @Inject(method = "clearContent", at = @At("TAIL"))
    private void foodhealing$clearPlacementOwners(CallbackInfo callback) {
        Arrays.fill(foodhealing$productionEligible, false);
    }
}
