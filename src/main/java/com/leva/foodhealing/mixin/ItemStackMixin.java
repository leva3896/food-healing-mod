package com.leva.foodhealing.mixin;

import com.leva.foodhealing.DurabilityTransactions;
import com.leva.foodhealing.SatisfactionTransactions;
import com.leva.foodhealing.capability.ShokugiProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
    private void foodhealing$applySatisfaction(Level level, LivingEntity entity,
                                               CallbackInfoReturnable<ItemStack> callback) {
        if (level.isClientSide() || !(entity instanceof ServerPlayer player)) {
            return;
        }

        ItemStack original = (ItemStack) (Object) this;
        if (original.getFoodProperties(player) == null) {
            return;
        }

        if (!SatisfactionTransactions.consumeDecision(player, original)) {
            return;
        }

        // Apply the food's canonical finish behavior to a copy. Effects and hunger are
        // produced once, while the real stack and its NBT/capabilities are never consumed.
        ItemStack consumedCopy = original.copy();
        original.getItem().finishUsingItem(consumedCopy, level, player);
        callback.setReturnValue(original);
    }

    // Both direct hurt and hurtAndBreak converge here, after Forge Item.damageItem
    // (for hurtAndBreak) and before vanilla's Unbreaking enchantment. No second hook.
    @ModifyVariable(
            method = "hurt(ILnet/minecraft/util/RandomSource;Lnet/minecraft/server/level/ServerPlayer;)Z",
            at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int foodhealing$applyEquipmentDurabilitySkills(int amount, int originalAmount,
                                                          RandomSource random, ServerPlayer player) {
        if (player == null || amount <= 0) {
            return amount;
        }
        ItemStack stack = (ItemStack) (Object) this;
        return player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .map(data -> DurabilityTransactions.adjustDamage(
                        data, stack, amount, random))
                .orElse(amount);
    }
}
