package com.leva.foodhealing.mixin.tacz;

import com.leva.foodhealing.compat.tacz.TaczAmmoAdapter;
import com.tacz.guns.api.entity.ShootResult;
import com.tacz.guns.entity.shooter.ShooterDataHolder;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.function.Supplier;

@Pseudo
@Mixin(targets = "com.tacz.guns.entity.shooter.LivingEntityShoot", remap = false)
public abstract class TaczShootMixin {
    @Shadow @Final private LivingEntity shooter;
    @Shadow @Final private ShooterDataHolder data;
    @Unique private long foodhealing$lastTimestamp = Long.MIN_VALUE;
    @Unique private long foodhealing$baseTimestamp = Long.MIN_VALUE;

    @Inject(method = "shoot", at = @At("HEAD"), cancellable = true, require = 1)
    private void foodhealing$shotBoundary(Supplier<Float> pitch, Supplier<Float> yaw, long timestamp,
                                         CallbackInfoReturnable<ShootResult> cir) {
        if (TaczAmmoAdapter.needsBolt(shooter, shooter.getMainHandItem())) {
            cir.setReturnValue(ShootResult.NEED_BOLT);
            return;
        }
        if (TaczAmmoAdapter.level(shooter, shooter.getMainHandItem()) > 0
                && foodhealing$baseTimestamp == data.baseTimestamp && timestamp <= foodhealing$lastTimestamp) {
            cir.setReturnValue(ShootResult.COOL_DOWN);
        }
    }
    @Inject(method = "shoot", at = @At("RETURN"), require = 1)
    private void foodhealing$accepted(Supplier<Float> pitch, Supplier<Float> yaw, long timestamp,
                                     CallbackInfoReturnable<ShootResult> cir) {
        if (cir.getReturnValue() == ShootResult.SUCCESS) {
            foodhealing$baseTimestamp = data.baseTimestamp;
            foodhealing$lastTimestamp = timestamp;
        }
    }
}
