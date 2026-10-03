package com.leva.foodhealing.mixin.fantasyending;

import com.leva.foodhealing.compat.fantasyending.FantasyEndingCompatibility;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.WitherSkull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WitherSkull.class)
public abstract class UomOwnedWitherSkullMixin {
    @Redirect(method = "onHitEntity(Lnet/minecraft/world/phys/EntityHitResult;)V", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", ordinal = 0),
            require = 1, expect = 1, allow = 1)
    private boolean foodhealing$skullSecondary(LivingEntity target, MobEffectInstance effect, Entity cause) {
        return !FantasyEndingCompatibility.blocksProjectile((WitherSkull)(Object)this, target) && target.addEffect(effect, cause);
    }
}
