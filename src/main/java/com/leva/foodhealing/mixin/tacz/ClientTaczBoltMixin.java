package com.leva.foodhealing.mixin.tacz;

import com.leva.foodhealing.compat.tacz.client.ClientTaczBoltState;
import com.tacz.guns.api.item.IGun;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Pseudo
@Mixin(targets = "com.tacz.guns.client.gameplay.LocalPlayerBolt", remap = false)
public abstract class ClientTaczBoltMixin {
    @Shadow @Final private LocalPlayer player;

    @Redirect(method = {"lambda$bolt$0", "tickAutoBolt"}, at = @At(value = "INVOKE",
            target = "Lcom/tacz/guns/api/item/IGun;hasBulletInBarrel(Lnet/minecraft/world/item/ItemStack;)Z"), require = 2)
    private boolean foodhealing$needsCycle(IGun gun, ItemStack stack) {
        return !ClientTaczBoltState.pending(player, stack) && gun.hasBulletInBarrel(stack);
    }
    @Redirect(method = "lambda$bolt$0", at = @At(value = "INVOKE",
            target = "Lcom/tacz/guns/api/item/IGun;getCurrentAmmoCount(Lnet/minecraft/world/item/ItemStack;)I"), require = 1)
    private int foodhealing$retainedRoundAvailable(IGun gun, ItemStack stack) {
        return ClientTaczBoltState.pending(player, stack) ? Math.max(1, gun.getCurrentAmmoCount(stack))
                : gun.getCurrentAmmoCount(stack);
    }
    @Redirect(method = "lambda$bolt$0", at = @At(value = "INVOKE",
            target = "Lcom/tacz/guns/api/item/IGun;hasInventoryAmmo(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Z)Z"), require = 1)
    private boolean foodhealing$retainedInventoryRound(IGun gun, LivingEntity owner, ItemStack stack, boolean check) {
        return ClientTaczBoltState.pending(player, stack) || gun.hasInventoryAmmo(owner, stack, check);
    }
}
