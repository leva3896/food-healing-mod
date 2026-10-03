package com.leva.foodhealing.client;

public final class SkillRowControlsRegression {
    private SkillRowControlsRegression() {
    }

    public static void run() {
        for (int max : new int[]{1, 3, 9}) {
            for (int level = 0; level <= max; level++) {
                requireState(level, level, max, level < max, level > 0, "parity leveled GUI " + level + "/" + max);
            }
        }
        requireState(0, 0, 5, true, false, "unacquired multi-level skill");
        requireState(1, 1, 5, true, true, "level 1 of 5 skill");
        requireState(4, 4, 5, true, true, "level 4 of 5 skill");
        requireState(5, 5, 5, false, true, "max-level skill");
        requireState(0, 3, 5, false, true, "legacy effective skill");
    }

    private static void requireState(int purchasedLevel, int effectiveLevel, int maxLevel,
                                     boolean expectedPurchase, boolean expectedToggle, String scenario) {
        SkillRowControls.State state = SkillRowControls.forLevels(purchasedLevel, effectiveLevel, maxLevel);
        if (state.purchaseVisible() != expectedPurchase || state.toggleVisible() != expectedToggle) {
            throw new AssertionError(scenario + " controls were incorrect: " + state);
        }
    }
}
