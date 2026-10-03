package com.leva.foodhealing;

import com.leva.foodhealing.compat.AmmoConservationChance;
import com.leva.foodhealing.compat.TaczAmmoCompatibility;

final class AmmoConservationRegression {
    static void run() {
        for (int level = 1; level <= 10; level++) {
            int preserved = 0;
            for (int sample = 0; sample < 1000; sample++) {
                if (AmmoConservationChance.preserves(level, (sample + 0.5F) / 1000F)) preserved++;
            }
            require(preserved == level * 100, "locked ten-percent steps");
            require(AmmoConservationChance.preserves(level, Math.nextDown(level / 10F)), "threshold predecessor");
            require(!AmmoConservationChance.preserves(level, level / 10F), "exclusive threshold");
            require(!AmmoConservationChance.preserves(level, 1F), "invalid inclusive roll");
            require(!AmmoConservationChance.preserves(level, Float.NaN), "NaN roll");
            require(!AmmoConservationChance.preserves(level, Float.POSITIVE_INFINITY), "infinite roll");
            require(!AmmoConservationChance.preserves(level, -0.001F), "negative roll");
        }
        for (int invalid : new int[]{Integer.MIN_VALUE, -1, 0, 11, Integer.MAX_VALUE}) {
            require(!AmmoConservationChance.preserves(invalid, 0F), "invalid level");
        }
        require(!TaczAmmoCompatibility.ready(), "no loader must not open optional purchase gate");
        System.out.println("AMMO_CONSERVATION_UNIT_PASS levels=10 samples=10000 invalidInputs=PASS absentGate=CLOSED");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
