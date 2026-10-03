package com.leva.foodhealing.mixin.trialmonolith;

import com.leva.foodhealing.compat.TrialInvaderCompatibility;
import io.github.kosianodangoo.trialmonolith.common.entity.AbstractDelayedTraceableEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** The locked Huge Beam numeric exception. No amount threshold or shared damage-type filter. */
@Pseudo
@Mixin(targets = "io.github.kosianodangoo.trialmonolith.common.entity.HugeBeamEntity", remap = false)
public abstract class TrialInvaderHugeBeamMixin {
    @Redirect(method = "lambda$activate$0", at = @At(value = "INVOKE", remap = true, target =
            "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),
            require = 1, expect = 1, allow = 1)
    private boolean foodhealing$hugeBeamNumeric(Entity target, DamageSource source, float amount) {
        return !TrialInvaderCompatibility.blocksBeam((AbstractDelayedTraceableEntity) (Object) this, target)
                && target.hurt(source, amount);
    }
}
