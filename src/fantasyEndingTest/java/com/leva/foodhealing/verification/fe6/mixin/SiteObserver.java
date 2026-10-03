package com.leva.foodhealing.verification.fe6.mixin;
import com.leva.foodhealing.verification.fe6.FE6Verification;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(targets="com.leva.foodhealing.compat.fantasyending.FantasyEndingCompatibility",remap=false)
public class SiteObserver {
 @Inject(method="blocksDirect",at=@At("HEAD"),require=1)
 private static void observe(Entity source,Entity target,CallbackInfoReturnable<Boolean> ci){FE6Verification.site(source,target);}
}
