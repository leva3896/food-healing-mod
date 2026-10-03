package com.leva.foodhealing.verification.fe6.mixin;
import com.leva.foodhealing.verification.fe6.FE6Verification;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
/** Fixture-only finite branch selection. The native branch and product hook still execute. */
@Mixin(targets="com.mega.uom.common.entity.StarEntity",remap=false)
public class StarRng {
 @Redirect(method="m_6532_",at=@At(value="INVOKE",target="Ljava/lang/Math;random()D"),require=1,expect=1,allow=1)
 private double rng(){FE6Verification.rngCalls++;return FE6Verification.rng;}
}
