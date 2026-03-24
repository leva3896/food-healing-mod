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
     * レベル11で10%、レベル20で100%の確率で弾薬を維持
     */
    @Inject(method = "consumeAmmoFromPlayer", at = @At("HEAD"), cancellable = true, remap = false)
    private void foodhealing$onConsumeAmmo(int neededAmount, CallbackInfoReturnable<Integer> cir) {
        if (shooter instanceof Player player && !player.level().isClientSide) {
            player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
                int level = cap.getLevel();
                if (level >= 11) {
                    float conserveChance = Math.min((level - 10) * 0.1f, 1.0f);
                    if (player.getRandom().nextFloat() < conserveChance) {
                        // 弾薬を消費せずに、要求された量を消費したと判定させる
                        cir.setReturnValue(neededAmount);
                    }
                }
            });
        }
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
