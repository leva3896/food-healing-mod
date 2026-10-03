package com.leva.foodhealing.verification.fe6.mixin;
import com.leva.foodhealing.verification.fe6.FE6Verification;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(targets="com.mega.uom.util.entity.EntityActuallyHurt",remap=false)
public class NumericObserver {
 @Shadow public LivingEntity entity;
 @Inject(method="actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V",at=@At("HEAD"),require=1)
 private void numeric(DamageSource s,float a,CallbackInfo ci){FE6Verification.numeric("FE",entity,s,a);}
 @Inject(method="actuallyHurt0(Lnet/minecraft/world/damagesource/DamageSource;FZ)V",at=@At("HEAD"),require=1)
 private void numeric0(DamageSource s,float a,boolean b,CallbackInfo ci){FE6Verification.numeric("FE0",entity,s,a);}
}
