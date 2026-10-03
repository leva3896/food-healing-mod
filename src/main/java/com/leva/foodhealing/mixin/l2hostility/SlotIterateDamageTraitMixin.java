package com.leva.foodhealing.mixin.l2hostility;

import com.leva.foodhealing.compat.l2hostility.L2HostilityAdapter;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.highlevel.SlotIterateDamageTrait;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "dev.xkmc.l2hostility.content.traits.highlevel.SlotIterateDamageTrait", remap = false)
public abstract class SlotIterateDamageTraitMixin {
    @Shadow protected abstract void perform(LivingEntity target, EquipmentSlot slot);

    @Redirect(method = "process", at = @At(value = "INVOKE", target =
            "Ldev/xkmc/l2hostility/content/traits/highlevel/SlotIterateDamageTrait;perform(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"),
            require = 1, expect = 1, allow = 1)
    private void foodhealing$wear(SlotIterateDamageTrait self, LivingEntity target, EquipmentSlot slot,
            int rank, LivingEntity attacker, LivingEntity originalTarget) {
        MobTrait trait = (MobTrait) (Object) this;
        if (!L2HostilityAdapter.nullified(trait, attacker)
                && !L2HostilityAdapter.blocksPersonalEffect(trait, attacker, target)) perform(target, slot);
    }

    @Inject(method = "postHurtImpl", at = @At("HEAD"), cancellable = true, require = 1, expect = 1, allow = 1)
    private void foodhealing$post(int rank, LivingEntity attacker, LivingEntity target, CallbackInfo ci) {
        if (L2HostilityAdapter.nullified((MobTrait) (Object) this, attacker)) ci.cancel();
    }
}
