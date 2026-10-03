package com.leva.foodhealing.mixin.fantasyending;

import com.leva.foodhealing.compat.fantasyending.FantasyEndingCompatibility;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "com.mega.uom.common.entity.boss.uom.UomWither", remap = false)
public abstract class UomWitherMixin {
    @Redirect(method = "dreamShadowBeam(Lnet/minecraft/world/entity/Entity;)V", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/world/entity/LivingEntity;m_7292_(Lnet/minecraft/world/effect/MobEffectInstance;)Z"),
            require = 2, expect = 2, allow = 2)
    private boolean foodhealing$dreamSecondary(LivingEntity target, MobEffectInstance effect) {
        return !FantasyEndingCompatibility.blocksDirect((Entity)(Object)this, target) && target.addEffect(effect);
    }
    @Redirect(method = "finalSkillAttack()V", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/world/entity/LivingEntity;m_7292_(Lnet/minecraft/world/effect/MobEffectInstance;)Z", ordinal = 0),
            require = 1, expect = 1, allow = 1)
    private boolean foodhealing$finalSecondary(LivingEntity target, MobEffectInstance effect) {
        return !FantasyEndingCompatibility.blocksDirect((Entity)(Object)this, target) && target.addEffect(effect);
    }
    @Redirect(method = "m_6475_(Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/world/entity/player/Player;m_7292_(Lnet/minecraft/world/effect/MobEffectInstance;)Z", ordinal = 0),
            require = 1, expect = 1, allow = 1)
    private boolean foodhealing$counterSecondary(Player target, MobEffectInstance effect) {
        return !FantasyEndingCompatibility.blocksDirect((Entity)(Object)this, target) && target.addEffect(effect);
    }
}
