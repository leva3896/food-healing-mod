package com.example.examplemod.mixin;

import com.example.examplemod.capability.ShokugiProvider;
import com.tacz.guns.api.item.gun.AbstractGunItem;
import com.tacz.guns.item.ModernKineticGunScriptAPI;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ModernKineticGunScriptAPI.class, remap = false)
public abstract class TaCZGunScriptMixin {

    @Shadow private LivingEntity shooter;
    @Shadow private ItemStack itemStack;
    @Shadow private AbstractGunItem abstractGunItem;

    /**
     * 食義レベル11〜20: 弾薬節約スキル
     * 発砲時の弾薬消費（ReduceMagazine, EmptyChamber, InventoryConsume）を確率で無効化
     */
    private boolean foodhealing$shouldConserveAmmo() {
        if (shooter instanceof Player player && !player.level().isClientSide) {
            int level = player.getCapability(ShokugiProvider.SHOKUGI_CAPA).map(cap -> cap.getLevel()).orElse(0);
            if (level >= 11) {
                float conserveChance = Math.min((level - 10) * 0.1f, 1.0f);
                return player.getRandom().nextFloat() < conserveChance;
            }
        }
        return false;
    }

    @org.spongepowered.asm.mixin.injection.Redirect(method = "reduceAmmoOnce", at = @At(value = "INVOKE", target = "Lcom/tacz/guns/api/item/gun/AbstractGunItem;reduceCurrentAmmoCount(Lnet/minecraft/world/item/ItemStack;)V"), remap = false)
    private void foodhealing$conserveMagazineAmmo(AbstractGunItem instance, ItemStack stack) {
        if (!foodhealing$shouldConserveAmmo()) {
            instance.reduceCurrentAmmoCount(stack);
        }
    }

    @org.spongepowered.asm.mixin.injection.Redirect(method = "reduceAmmoOnce", at = @At(value = "INVOKE", target = "Lcom/tacz/guns/api/item/gun/AbstractGunItem;setBulletInBarrel(Lnet/minecraft/world/item/ItemStack;Z)V"), remap = false)
    private void foodhealing$conserveChamberAmmo(AbstractGunItem instance, ItemStack stack, boolean inBarrel) {
        if (!inBarrel && foodhealing$shouldConserveAmmo()) {
            return; // 弾丸を薬室(Barrel)に残したままにする
        }
        instance.setBulletInBarrel(stack, inBarrel);
    }

    @org.spongepowered.asm.mixin.injection.Redirect(method = "reduceAmmoOnce", at = @At(value = "INVOKE", target = "Lcom/tacz/guns/item/ModernKineticGunScriptAPI;consumeAmmoFromPlayer(I)I"), remap = false)
    private int foodhealing$conserveInventoryAmmo(ModernKineticGunScriptAPI instance, int amount) {
        if (foodhealing$shouldConserveAmmo()) {
            return amount; // 消費したと偽装
        }
        return instance.consumeAmmoFromPlayer(amount); // オリジナルの処理をコール
    }

    /**
     * 食義レベル11以降: ミニガン系武器のオーバーヒート無効
     * ミニガン系の発熱量を射撃直後に強制的に0に戻す
     */
    @Inject(method = "shootOnce", at = @At("RETURN"), remap = false)
    private void foodhealing$preventOverheat(boolean consumeAmmo, CallbackInfo ci) {
        if (shooter instanceof Player player && !player.level().isClientSide) {
            player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
                if (cap.getLevel() >= 11 && abstractGunItem != null && itemStack != null) {
                    ResourceLocation gunId = abstractGunItem.getGunId(itemStack);
                    if (gunId != null && gunId.getPath().toLowerCase().contains("minigun")) {
                        // 発熱を即座にリセットし、オーバーヒートを防ぐ
                        abstractGunItem.setHeatAmount(itemStack, 0f);
                        abstractGunItem.setOverheatLocked(itemStack, false);
                    }
                }
            });
        }
    }
}
