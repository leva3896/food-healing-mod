package com.leva.foodhealing;

import com.leva.foodhealing.capability.IShokugiData;

/** Eligibility for new nullification, independent of the closed purchase readiness gate. */
public final class TruthMasteryController {
    public static final double RANGE = 75.0;
    private TruthMasteryController() { }

    public static boolean isEnabled(IShokugiData data) {
        return PurificationMasteryController.isEnabled(data)
                && data.getSkillLevel(FoodHealingSkillIds.TRUTH_MASTERY) == 1
                && !data.isSkillDisabled(FoodHealingSkillIds.TRUTH_MASTERY);
    }

    public static boolean contains(double px, double py, double pz, double x, double y, double z) {
        return Math.abs(px - x) <= RANGE && Math.abs(py - y) <= RANGE && Math.abs(pz - z) <= RANGE;
    }
}
