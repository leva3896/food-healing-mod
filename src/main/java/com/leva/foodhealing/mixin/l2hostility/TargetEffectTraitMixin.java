package com.leva.foodhealing.mixin.l2hostility;

import com.leva.foodhealing.compat.l2hostility.L2HostilityAdapter;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2library.base.effects.EffectUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "dev.xkmc.l2hostility.content.traits.base.TargetEffectTrait", remap = false)
public abstract class TargetEffectTraitMixin {
    // Two native call sites: normal recipient and each recipient selected by the reflection ring.
    @Redirect(method = "postHurtImpl", at = @At(value = "INVOKE", target =
            "Ldev/xkmc/l2library/base/effects/EffectUtil;addEffect(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/effect/MobEffectInstance;Ldev/xkmc/l2library/base/effects/EffectUtil$AddReason;Lnet/minecraft/world/entity/Entity;)V"),
            require = 2, expect = 2, allow = 2)
    private void foodhealing$add(LivingEntity target, MobEffectInstance effect, EffectUtil.AddReason reason, Entity attacker) {
        MobTrait trait = (MobTrait) (Object) this;
        if (!L2HostilityAdapter.nullified(trait, attacker)
                && !L2HostilityAdapter.blocksPersonalEffect(trait, attacker, target)) {
            EffectUtil.addEffect(target, effect, reason, attacker);
        }
    }
}
