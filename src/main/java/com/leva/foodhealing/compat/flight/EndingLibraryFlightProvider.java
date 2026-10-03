package com.leva.foodhealing.compat.flight;

import com.leva.foodhealing.FlightAuthority;
import com.mega.endinglib.proxy.CommonProxy;
import net.minecraft.server.level.ServerPlayer;

/** Exact 2.1.19fix override authority. No capability setters or modifyAbilities calls. */
final class EndingLibraryFlightProvider {
    private EndingLibraryFlightProvider() { }
    static FlightAuthority query(ServerPlayer player) {
        return CommonProxy.getCameraCapOptional(player).map(cap -> {
            int mayfly = cap.getAbilityMayfly(), flying = cap.getAbilityFlying();
            return new FlightAuthority(mayfly > 0 || flying > 0, mayfly < 0 || flying < 0, true);
        }).orElse(FlightAuthority.UNKNOWN);
    }
}
