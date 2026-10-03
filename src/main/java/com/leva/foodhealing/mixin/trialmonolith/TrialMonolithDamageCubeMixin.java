package com.leva.foodhealing.mixin.trialmonolith;

import com.leva.foodhealing.compat.TrialMonolithCompatibility;
import io.github.kosianodangoo.trialmonolith.common.helper.EntityHelper;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Do not cancel the target lambda: its numeric hurt and subsequent motion handling must still run. */
@Pseudo
@Mixin(targets = "io.github.kosianodangoo.trialmonolith.common.entity.DamageCubeEntity", remap = false)
public abstract class TrialMonolithDamageCubeMixin {
    @Redirect(method = "lambda$activate$0", at = @At(value = "INVOKE",
            target = "Lio/github/kosianodangoo/trialmonolith/common/helper/EntityHelper;addSoulDamage(Lnet/minecraft/world/entity/Entity;F)V"),
            require = 1, expect = 1, allow = 1)
    private void foodhealing$ordinarySoulAddition(Entity target, float amount) {
        if (!TrialMonolithCompatibility.blocksSoulAddition(target)) {
            EntityHelper.addSoulDamage(target, amount);
        }
    }

    @Redirect(method = "lambda$activate$0", at = @At(value = "INVOKE",
            target = "Lio/github/kosianodangoo/trialmonolith/common/helper/EntityHelper;addSoulDamageForce(Lnet/minecraft/world/entity/Entity;F)V"),
            require = 1, expect = 1, allow = 1)
    private void foodhealing$forcedSoulAddition(Entity target, float amount) {
        if (!TrialMonolithCompatibility.blocksSoulAddition(target)) {
            EntityHelper.addSoulDamageForce(target, amount);
        }
    }
}
