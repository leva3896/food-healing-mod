package com.leva.foodhealing.mixin.endinglibrary;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.leva.foodhealing.client.TimeStopClientLease;
@Mixin(targets="com.mega.endinglib.util.time.TimeStopUtils",remap=false)
public class ClientInputBridge {
    @Inject(method="canMove(Lnet/minecraft/world/entity/Entity;)Z",at=@At("RETURN"),cancellable=true,remap=false)
    private static void bridge(Entity e,CallbackInfoReturnable<Boolean> ci) {
        if(e.level().isClientSide()&&!ci.getReturnValue()&&TimeStopClientLease.valid(e))ci.setReturnValue(true);
    }
}
