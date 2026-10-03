package com.leva.foodhealing.mixin.tacz;

import com.leva.foodhealing.compat.tacz.TaczAmmoAdapter;
import com.tacz.guns.api.item.gun.AbstractGunItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.tacz.guns.resource.index.CommonGunIndex;

@Pseudo
@Mixin(targets = "com.tacz.guns.entity.shooter.LivingEntityBolt", remap = false)
public abstract class TaczBoltMixin {
    @Shadow @Final private LivingEntity shooter;

    @Redirect(method = "lambda$bolt$0", at = @At(value = "INVOKE",
            target = "Lcom/tacz/guns/api/item/gun/AbstractGunItem;hasBulletInBarrel(Lnet/minecraft/world/item/ItemStack;)Z"), require = 1)
    private boolean foodhealing$needsCycle(AbstractGunItem gun, ItemStack stack) {
        return !TaczAmmoAdapter.needsBolt(shooter, stack) && gun.hasBulletInBarrel(stack);
    }
    @Redirect(method = "lambda$bolt$0", at = @At(value = "INVOKE",
            target = "Lcom/tacz/guns/api/item/gun/AbstractGunItem;getCurrentAmmoCount(Lnet/minecraft/world/item/ItemStack;)I"), require = 1)
    private int foodhealing$retainedRoundAvailable(AbstractGunItem gun, ItemStack stack) {
        return TaczAmmoAdapter.needsBolt(shooter, stack) ? Math.max(1, gun.getCurrentAmmoCount(stack))
                : gun.getCurrentAmmoCount(stack);
    }
    @Redirect(method = "lambda$bolt$0", at = @At(value = "INVOKE",
            target = "Lcom/tacz/guns/api/item/gun/AbstractGunItem;hasInventoryAmmo(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Z)Z"), require = 1)
    private boolean foodhealing$retainedInventoryRound(AbstractGunItem gun, LivingEntity player, ItemStack stack, boolean check) {
        return TaczAmmoAdapter.needsBolt(shooter, stack) || gun.hasInventoryAmmo(player, stack, check);
    }
    @Inject(method = "lambda$tickBolt$1", at = @At("RETURN"), require = 1)
    private void foodhealing$cycleFinished(AbstractGunItem gun, ItemStack stack, CommonGunIndex index,
                                          CallbackInfoReturnable<Boolean> cir) {
        if (Boolean.FALSE.equals(cir.getReturnValue())) TaczAmmoAdapter.finishBolt(shooter, stack);
    }
}
