package com.leva.foodhealing.mixin.endinglibrary;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.leva.foodhealing.compat.endinglibrary.*;
@Mixin(targets="com.mega.endinglib.util.time.TimeStopUtils",remap=false)
public class UseObserver {
 @Inject(method="use(ZLnet/minecraft/world/entity/LivingEntity;ZIZ)V",at=@At("HEAD"),remap=false)
 private static void head(boolean enabled,LivingEntity e,boolean reset,int duration,boolean sound,CallbackInfo ci){if(InitialStart.commonHead(enabled,e,reset,duration,sound))return;TerminalWitnessBridge.commonHead(enabled,e,reset,duration,sound);Ownership.beforeUse(enabled,e,reset,duration);}
 @Inject(method="use(ZLnet/minecraft/world/entity/LivingEntity;ZIZ)V",at=@At("RETURN"),remap=false)
 private static void returned(boolean enabled,LivingEntity e,boolean reset,int duration,boolean sound,CallbackInfo ci){if(InitialStart.commonReturn(enabled,e))return;TerminalWitnessBridge.commonReturn(enabled,e);Ownership.afterUse(enabled,e,reset,duration);}
}
