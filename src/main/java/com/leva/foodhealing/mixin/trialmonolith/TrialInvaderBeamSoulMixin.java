package com.leva.foodhealing.mixin.trialmonolith;

import com.leva.foodhealing.compat.TrialInvaderCompatibility;
import io.github.kosianodangoo.trialmonolith.common.entity.AbstractDelayedTraceableEntity;
import io.github.kosianodangoo.trialmonolith.common.helper.EntityHelper;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = {"io.github.kosianodangoo.trialmonolith.common.entity.SmallBeamEntity",
        "io.github.kosianodangoo.trialmonolith.common.entity.HugeBeamEntity"}, remap = false)
public abstract class TrialInvaderBeamSoulMixin {
    @Redirect(method = "lambda$activate$0", at = @At(value = "INVOKE", target =
            "Lio/github/kosianodangoo/trialmonolith/common/helper/EntityHelper;addSoulDamage(Lnet/minecraft/world/entity/Entity;F)V"),
            require = 1, expect = 1, allow = 1)
    private void foodhealing$ordinary(Entity target, float amount) {
        if (!TrialInvaderCompatibility.blocksBeam((AbstractDelayedTraceableEntity) (Object) this, target))
            EntityHelper.addSoulDamage(target, amount);
    }

    @Redirect(method = "lambda$activate$0", at = @At(value = "INVOKE", target =
            "Lio/github/kosianodangoo/trialmonolith/common/helper/EntityHelper;addSoulDamageForce(Lnet/minecraft/world/entity/Entity;F)V"),
            require = 1, expect = 1, allow = 1)
    private void foodhealing$forced(Entity target, float amount) {
        if (!TrialInvaderCompatibility.blocksBeam((AbstractDelayedTraceableEntity) (Object) this, target))
            EntityHelper.addSoulDamageForce(target, amount);
    }
}
