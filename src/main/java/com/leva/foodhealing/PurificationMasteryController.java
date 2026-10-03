package com.leva.foodhealing;

import com.leva.foodhealing.capability.IShokugiData;

/** Effect eligibility is independent of the still-closed purchase readiness gate. */
public final class PurificationMasteryController {
    private PurificationMasteryController() { }

    public static boolean isEnabled(IShokugiData data) {
        // Use acquired canonical levels, never the pending legacy effective-level fallback.
        return data != null && !data.isLegacyMigrationPending()
                && data.getSkillLevel(FoodHealingSkillIds.PURIFICATION) == 1
                && data.getSkillLevel(FoodHealingSkillIds.PURIFICATION_MASTERY) == 1
                && !data.isSkillDisabled(FoodHealingSkillIds.PURIFICATION)
                && !data.isSkillDisabled(FoodHealingSkillIds.PURIFICATION_MASTERY);
    }
}
