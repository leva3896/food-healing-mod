package com.leva.foodhealing.verification.fe6.mixin;
import com.leva.foodhealing.verification.fe6.FE6Verification;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(targets="com.mega.uom.util.entity.EntityASMUtil",remap=false)
public class DeltaObserver {
 @Inject(method="addDelta(Lnet/minecraft/world/entity/LivingEntity;F)V",at=@At("HEAD"),require=1)
 private static void delta(LivingEntity target,float a,CallbackInfo ci){FE6Verification.delta(target,a);}
}
