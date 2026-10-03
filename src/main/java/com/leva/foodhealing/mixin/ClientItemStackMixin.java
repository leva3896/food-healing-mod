package com.leva.foodhealing.mixin;

import com.leva.foodhealing.client.ClientSatisfactionState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ClientItemStackMixin {
    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
    private void foodhealing$applyPredictedSatisfaction(Level level, LivingEntity entity,
                                                        CallbackInfoReturnable<ItemStack> callback) {
        if (!level.isClientSide() || !(entity instanceof Player player)) {
            return;
        }

        ItemStack original = (ItemStack) (Object) this;
        if (ClientSatisfactionState.consume(original) != ClientSatisfactionState.Decision.PRESERVE) {
            return;
        }

        ItemStack consumedCopy = original.copy();
        original.getItem().finishUsingItem(consumedCopy, level, player);
        callback.setReturnValue(original);
    }
}
