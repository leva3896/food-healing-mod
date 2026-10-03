package com.leva.foodhealing.mixin.l2hostility;

import com.leva.foodhealing.compat.l2hostility.L2HostilityAdapter;
import com.leva.foodhealing.compat.l2hostility.L2MobTraitAccess;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.chunk.RegionalDifficultyModifier;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

@Pseudo
@Mixin(targets = "dev.xkmc.l2hostility.content.capability.mob.MobTraitCap", remap = false)
public abstract class MobTraitCapMixin implements L2MobTraitAccess {
    @Shadow private boolean ticking;
    @Unique private int foodhealing$iterationDepth;
    @Override public boolean foodhealing$canPrune() { return !ticking && foodhealing$iterationDepth == 0; }

    @Redirect(method = "traitEvent", at = @At(value = "INVOKE",
            target = "Ljava/util/LinkedHashMap;forEach(Ljava/util/function/BiConsumer;)V"), require = 1, expect = 1, allow = 1)
    private void foodhealing$iterate(LinkedHashMap<MobTrait, Integer> map, BiConsumer<MobTrait, Integer> consumer) {
        foodhealing$iterationDepth++;
        try { map.forEach(consumer); } finally { foodhealing$iterationDepth--; }
    }

    @Inject(method = "init", at = @At("RETURN"), require = 1)
    private void foodhealing$init(Level level, LivingEntity entity, RegionalDifficultyModifier difficulty, CallbackInfo ci) {
        if (entity instanceof Mob mob) L2HostilityAdapter.consider(mob, true);
    }
    @Inject(method = "copyFrom", at = @At("RETURN"), require = 1)
    private void foodhealing$copy(LivingEntity original, LivingEntity recipient, MobTraitCap source, CallbackInfo ci) {
        if (recipient instanceof Mob mob) L2HostilityAdapter.consider(mob, true);
    }
    @Inject(method = "clearPending", at = @At("RETURN"), require = 1)
    private void foodhealing$pending(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() && entity instanceof Mob mob) L2HostilityAdapter.consider(mob, true);
    }
    @Inject(method = "tick", at = @At("HEAD"), require = 1, expect = 1, allow = 1)
    private void foodhealing$beforeTick(LivingEntity entity, CallbackInfo ci) {
        if (entity instanceof Mob mob && !mob.level().isClientSide()) {
            if (Math.floorMod(mob.tickCount, 20) == Math.floorMod(mob.getId(), 20)) L2HostilityAdapter.consider(mob, true);
            else L2HostilityAdapter.prune(mob, (MobTraitCap) (Object) this);
        }
    }
    @Inject(method = "tick", at = @At("RETURN"), require = 1, expect = 1, allow = 1)
    private void foodhealing$afterTick(LivingEntity entity, CallbackInfo ci) {
        if (entity instanceof Mob mob) L2HostilityAdapter.prune(mob, (MobTraitCap) (Object) this);
    }
}
