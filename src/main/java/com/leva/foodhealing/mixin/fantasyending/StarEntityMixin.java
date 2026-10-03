package com.leva.foodhealing.mixin.fantasyending;

import com.leva.foodhealing.compat.fantasyending.FantasyEndingCompatibility;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "com.mega.uom.common.entity.StarEntity", remap = false)
public abstract class StarEntityMixin {
    @Redirect(method = "m_6532_(Lnet/minecraft/world/phys/HitResult;)V", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/world/entity/LivingEntity;m_7292_(Lnet/minecraft/world/effect/MobEffectInstance;)Z", ordinal = 0),
            require = 1, expect = 1, allow = 1)
    private boolean foodhealing$aoeSecondary(LivingEntity target, MobEffectInstance effect) {
        return !FantasyEndingCompatibility.blocksProjectile((Projectile)(Object)this, target) && target.addEffect(effect);
    }
}
