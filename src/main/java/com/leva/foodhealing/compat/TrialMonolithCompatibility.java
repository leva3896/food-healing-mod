package com.leva.foodhealing.compat;

import com.leva.foodhealing.PurificationMasteryController;
import com.leva.foodhealing.capability.ShokugiProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

/** Called only by the exact-version Damage Cube call-site Mixins. No external class linkage here. */
public final class TrialMonolithCompatibility {
    public static final String MOD_ID = "the_trial_monolith";
    public static final String SUPPORTED_VERSION = "1.4.9";

    private TrialMonolithCompatibility() { }

    public static boolean blocksSoulAddition(Entity target) {
        return target instanceof ServerPlayer player && !player.level().isClientSide()
                && player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .map(PurificationMasteryController::isEnabled).orElse(false);
    }
}
