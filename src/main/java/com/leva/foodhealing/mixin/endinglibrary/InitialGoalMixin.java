package com.leva.foodhealing.mixin.endinglibrary;
import com.mega.uom.common.entity.boss.uom.UomWither;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.leva.foodhealing.compat.endinglibrary.InitialStart;
@Mixin(targets="com.mega.uom.common.entity.goals.TimeStopSkillGoal",remap=false)
public class InitialGoalMixin {
 @Inject(method="timeStop(Lcom/mega/uom/common/entity/boss/uom/UomWither;)V",at=@At("HEAD"),require=1,expect=1,allow=1,remap=false)
 private void entry(UomWither e,CallbackInfo ci){InitialStart.enter(e);}
 @Redirect(method="timeStop(Lcom/mega/uom/common/entity/boss/uom/UomWither;)V",at=@At(value="INVOKE",target="Lcom/mega/endinglib/util/time/TimeStopEntityData;setTimeStopCount(Lnet/minecraft/world/entity/LivingEntity;I)V"),require=1,expect=1,allow=1,remap=false)
 private void setter(LivingEntity e,int n){InitialStart.setter(e,n);}
 @Redirect(method="timeStop(Lcom/mega/uom/common/entity/boss/uom/UomWither;)V",at=@At(value="INVOKE",target="Lcom/mega/endinglib/util/time/TimeStopUtils;use(ZLnet/minecraft/world/entity/LivingEntity;)V"),require=1,expect=1,allow=1,remap=false)
 private void use(boolean enabled,LivingEntity e){InitialStart.use(enabled,e);}
 @Inject(method="timeStop(Lcom/mega/uom/common/entity/boss/uom/UomWither;)V",at=@At("RETURN"),require=1,expect=1,allow=1,remap=false)
 private void returned(UomWither e,CallbackInfo ci){InitialStart.returned(e);}
}
