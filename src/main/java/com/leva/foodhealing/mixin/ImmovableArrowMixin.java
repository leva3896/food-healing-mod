package com.leva.foodhealing.mixin;

import com.leva.foodhealing.ImmovableMasteryController;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractArrow.class)
public abstract class ImmovableArrowMixin {
    @Redirect(method = "onHitEntity", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;push(DDD)V"))
    private void foodhealing$arrowKnockback(LivingEntity target, double x, double y, double z) {
        if (!ImmovableMasteryController.protects(target)) target.push(x, y, z);
    }
}
