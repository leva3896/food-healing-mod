package com.leva.foodhealing.mixin.l2hostility;

import com.leva.foodhealing.compat.l2hostility.L2HostilityAdapter;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = {"dev.xkmc.l2hostility.content.traits.highlevel.CorrosionTrait",
        "dev.xkmc.l2hostility.content.traits.highlevel.ErosionTrait"}, remap = false)
public abstract class EquipmentDamageTraitMixin {
    @Inject(method = "onHurtTarget", at = @At("HEAD"), cancellable = true, require = 1, expect = 1, allow = 1)
    private void foodhealing$truth(int rank, LivingEntity attacker, AttackCache attack, TraitEffectCache cache, CallbackInfo ci) {
        // Only Truth cancels this trait's numeric bonus. Purification acts below slot selection.
        if (L2HostilityAdapter.nullified((MobTrait) (Object) this, attacker)) ci.cancel();
    }
}
