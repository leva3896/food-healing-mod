package com.leva.foodhealing.verification.fe6.mixin;
import com.leva.foodhealing.verification.fe6.FE6Verification;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/** Raw native input before Player difficulty scaling, distinct from the Forge hurt event. */
@Mixin(value=ServerPlayer.class,remap=false)
public class PlayerHurtInput {
 @Inject(method="m_6469_(Lnet/minecraft/world/damagesource/DamageSource;F)Z",at=@At("HEAD"),require=1)
 private void input(DamageSource source,float amount,CallbackInfoReturnable<Boolean> ci){
  FE6Verification.numeric("HURT_INPUT",(ServerPlayer)(Object)this,source,amount);
 }
}
