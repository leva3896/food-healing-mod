package com.leva.foodhealing;

import com.leva.foodhealing.capability.IShokugiData;

/** Session ownership rules. No external state, persistence, or ability packets here. */
public final class FlightGrantPolicy {
    private FlightGrantPolicy() { }

    public static boolean eligible(IShokugiData data) {
        return data != null && !data.isLegacyMigrationPending() && data.isFlightAcquisitionValid()
                && data.getSkillLevel(FoodHealingSkillIds.FLIGHT) == 1
                && !data.isSkillDisabled(FoodHealingSkillIds.FLIGHT);
    }

    public record Decision(boolean mayfly, boolean flying, boolean owns) { }

    public static Decision evaluate(boolean mayfly, boolean flying, boolean owns,
                                    boolean vanillaAuthority, boolean eligible, FlightAuthority provider) {
        // Native Creative/Spectator always owns these abilities, including during external DENY.
        if (vanillaAuthority) return new Decision(mayfly, flying, false);
        // A failed supported query cannot justify either a grant or a destructive revoke.
        if (!provider.observed()) return new Decision(mayfly, flying, owns);
        if (eligible && !provider.deny()) {
            return new Decision(true, flying, owns || !mayfly);
        }
        if (owns && !provider.positive()) return new Decision(false, false, false);
        return new Decision(mayfly, flying, false);
    }
}
