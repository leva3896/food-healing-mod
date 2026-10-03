package com.leva.foodhealing.mixin.trialmonolith;

import com.leva.foodhealing.compat.TrialMonolithCompatibility;
import io.github.kosianodangoo.trialmonolith.common.helper.EntityHelper;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Only the Invader's hostile victim-clear site, not protection on generated attack entities. */
@Pseudo
@Mixin(targets = "io.github.kosianodangoo.trialmonolith.common.entity.invadermonolith.InvaderMonolithEntity", remap = false)
public abstract class TrialInvaderProtectionMixin {
    @Redirect(method = "lambda$tick$2", at = @At(value = "INVOKE", target =
            "Lio/github/kosianodangoo/trialmonolith/common/helper/EntityHelper;setSoulProtected(Lnet/minecraft/world/entity/Entity;Z)V"),
            require = 1, expect = 1, allow = 1)
    private static void foodhealing$keepExistingProtection(Entity target, boolean value) {
        if (value || !TrialMonolithCompatibility.blocksSoulAddition(target)) {
            EntityHelper.setSoulProtected(target, value);
        }
    }
}
