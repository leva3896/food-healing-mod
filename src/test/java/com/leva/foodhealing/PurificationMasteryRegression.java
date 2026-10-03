package com.leva.foodhealing;

import com.leva.foodhealing.capability.ShokugiData;
import net.minecraft.nbt.CompoundTag;

final class PurificationMasteryRegression {
    private static int assertions;
    static void run() {
        assertions = 0;
        for (int parent : new int[]{0, 1, 2}) for (int mastery : new int[]{0, 1, 2}) {
            for (boolean parentOff : new boolean[]{false, true}) for (boolean masteryOff : new boolean[]{false, true}) {
                ShokugiData data = new ShokugiData();
                data.setSkillLevel(FoodHealingSkillIds.PURIFICATION, parent);
                data.setSkillLevel(FoodHealingSkillIds.PURIFICATION_MASTERY, mastery);
                data.setSkillDisabled(FoodHealingSkillIds.PURIFICATION, parentOff);
                data.setSkillDisabled(FoodHealingSkillIds.PURIFICATION_MASTERY, masteryOff);
                data.setUnspentSkillPoints(1000);
                CompoundTag before = data.serializeNBT();
                boolean expected = parent == 1 && mastery == 1 && !parentOff && !masteryOff;
                check(PurificationMasteryController.isEnabled(data) == expected, "ownership/toggle eligibility");
                check(before.equals(data.serializeNBT()), "effect predicate mutated data");
                ShokugiData copy = new ShokugiData(); copy.deserializeNBT(before);
                check(PurificationMasteryController.isEnabled(copy) == expected, "canonical round trip");
                before.putBoolean("LegacyMigrationPending", true);
                copy.deserializeNBT(before);
                check(!PurificationMasteryController.isEnabled(copy), "pending data admitted");
            }
        }
        ShokugiData data = new ShokugiData();
        data.setSkillLevel(FoodHealingSkillIds.PURIFICATION, 1);
        data.setUnspentSkillPoints(100);
        for (boolean off : new boolean[]{false, true}) {
            data.setSkillLevel(FoodHealingSkillIds.PURIFICATION_MASTERY, 0);
            data.setUnspentSkillPoints(100);
            data.setSpentSkillPoints(0);
            data.setSkillDisabled(FoodHealingSkillIds.PURIFICATION, off);
            check(FoodHealingSkills.tryPurchase(data, FoodHealingSkillIds.PURIFICATION_MASTERY)
                    == FoodHealingSkills.PurchaseResult.SUCCESS, "ready purchase failed");
            check(data.getUnspentSkillPoints() == 0 && data.getSpentSkillPoints() == 100
                    && data.isSkillDisabled(FoodHealingSkillIds.PURIFICATION) == off, "purchase changed cost/parent toggle");
        }
        data.setSkillLevel(FoodHealingSkillIds.PURIFICATION_MASTERY, 1);
        CompoundTag unknown = data.serializeNBT(); unknown.putInt("FoodHealingDataVersion", 99);
        data.deserializeNBT(unknown);
        check(!PurificationMasteryController.isEnabled(data), "unknown schema admitted");
        check(!PurificationMasteryController.isEnabled(null), "missing data admitted");
        System.out.println("Purification Mastery eligibility: " + assertions + " assertions passed");
    }
    private static void check(boolean condition, String message) {
        assertions++;
        if (!condition) throw new AssertionError(message);
    }
}
