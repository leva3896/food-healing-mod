package com.leva.foodhealing.compat;

public final class AmmoConservationChance {
    private AmmoConservationChance() { }

    public static boolean preserves(int level, float roll) {
        return level >= 1 && level <= 10 && Float.isFinite(roll)
                && roll >= 0F && roll < 1F && roll < level / 10F;
    }
}
