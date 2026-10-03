package com.leva.foodhealing.mixin.tacz;

import com.leva.foodhealing.compat.tacz.TaczAmmoAdapter;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import com.tacz.guns.item.ModernKineticGunScriptAPI;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "com.tacz.guns.item.ModernKineticGunScriptAPI", remap = false)
public abstract class TaczScriptMixin {
    @Shadow private LivingEntity shooter;
    @Shadow private ItemStack itemStack;

    @Redirect(method = "lambda$shootOnce$2", at = @At(value = "INVOKE",
            target = "Lcom/tacz/guns/item/ModernKineticGunScriptAPI;reduceAmmoOnce()Z"), require = 1)
    private boolean foodhealing$consumeShot(ModernKineticGunScriptAPI api) {
        return TaczAmmoAdapter.consumeShot(api, itemStack);
    }

    @Redirect(method = "lambda$shootOnce$2", at = @At(value = "INVOKE",
            target = "Lcom/tacz/guns/resource/pojo/data/gun/GunData;hasHeatData()Z"), require = 1)
    private boolean foodhealing$shotHeat(GunData data) {
        return TaczAmmoAdapter.level(shooter, itemStack) <= 0 && data.hasHeatData();
    }

    @Inject(method = "handleShootHeat", at = @At("HEAD"), cancellable = true, require = 1)
    private void foodhealing$directHeat(CallbackInfo ci) {
        if (TaczAmmoAdapter.level(shooter, itemStack) > 0) ci.cancel();
    }
}
