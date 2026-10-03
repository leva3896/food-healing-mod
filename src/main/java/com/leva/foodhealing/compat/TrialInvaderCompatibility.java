package com.leva.foodhealing.compat;

import io.github.kosianodangoo.trialmonolith.common.entity.AbstractDelayedTraceableEntity;
import io.github.kosianodangoo.trialmonolith.common.entity.invadermonolith.InvaderMonolithEntity;
import io.github.kosianodangoo.trialmonolith.common.init.TrialMonolithEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

/** External linkage reached only from the version-gated 1.4.9 beam Mixins. */
public final class TrialInvaderCompatibility {
    private TrialInvaderCompatibility() { }

    public static boolean blocksBeam(AbstractDelayedTraceableEntity beam, Entity target) {
        if (beam.level().isClientSide() || target.level() != beam.level()
                || !TrialMonolithCompatibility.blocksSoulAddition(target)) return false;
        LivingEntity owner = beam.getOwner();
        return owner instanceof InvaderMonolithEntity
                && owner.getType() == TrialMonolithEntities.INVADER_MONOLITH.get()
                && owner.level() == beam.level();
    }
}
