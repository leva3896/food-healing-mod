package com.leva.foodhealing;

import com.leva.foodhealing.capability.ShokugiData;
import com.leva.foodhealing.compat.l2hostility.L2HostilityVersions;
import com.leva.foodhealing.compat.fantasyending.FantasyEndingVersions;
import net.minecraft.nbt.CompoundTag;

import static com.leva.foodhealing.FoodHealingSkills.PurchaseResult.*;

/** Purchase/version-policy contracts, not an external gameplay integration. */
final class MasteryPurchaseRegression {
    private static int assertions;

    static void run() {
        assertions = 0;
        // No loader replacement: production purchase eligibility has no Trial/L2/FE input.
        // Evaluate the real pure Adapter gates alongside it, including a mixed selection.
        String[][] versions = {{null, null}, {"unsupported", "unsupported"},
                {"2.5.19", "2.7.20"}, {"2.5.19", "unsupported"}};
        boolean[][] supported = {{false, false}, {false, false}, {true, true}, {true, false}};
        for (int i = 0; i < versions.length; i++) {
            require(L2HostilityVersions.supports(versions[i][0], "2.5.3", "2.6.1", "0.4.4")
                    == supported[i][0], "L2 exact version gate");
            require(FantasyEndingVersions.supports(versions[i][1]) == supported[i][1], "FE exact version gate");
            for (String id : new String[]{FoodHealingSkillIds.PURIFICATION_MASTERY, FoodHealingSkillIds.TRUTH_MASTERY}) {
                var def = FoodHealingSkills.definitions().get(id);
                String parent = def.requirements().get(0).skillId();
                long cost = def.levelCosts()[0];
                for (boolean off : new boolean[]{false, true}) {
                    ShokugiData data = new ShokugiData();
                    data.setSkillLevel(parent, 1);
                    data.setSkillDisabled(parent, off);
                    data.setUnspentSkillPoints(cost);
                    CompoundTag before = data.serializeNBT();
                    require(FoodHealingSkills.purchaseStatus(data, id, 0) == SUCCESS, "optional configuration denied purchase");
                    require(before.equals(data.serializeNBT()), "preview mutated canonical");
                    require(FoodHealingSkills.tryPurchase(data, id, 0) == SUCCESS, "purchase failed");
                    require(data.getUnspentSkillPoints() == 0 && data.getSpentSkillPoints() == cost
                            && data.getSkillLevel(id) == 1 && !data.isSkillDisabled(id)
                            && data.isSkillDisabled(parent) == off, "exact cost/default ON/parent unchanged");
                    before = data.serializeNBT();
                    require(FoodHealingSkills.tryPurchase(data, id, 0) == STALE_REQUEST, "duplicate accepted");
                    require(before.equals(data.serializeNBT()), "duplicate spent twice");
                    ShokugiData restored = new ShokugiData(); restored.deserializeNBT(before);
                    require(before.equals(restored.serializeNBT()), "pre-existing owner changed on load");
                }
            }
        }
        for (String id : new String[]{FoodHealingSkillIds.PURIFICATION_MASTERY, FoodHealingSkillIds.TRUTH_MASTERY}) {
            var def = FoodHealingSkills.definitions().get(id);
            String parent = def.requirements().get(0).skillId();
            long cost = def.levelCosts()[0];
            for (int invalid : new int[]{2, Integer.MAX_VALUE}) {
                ShokugiData data = new ShokugiData();
                data.setSkillLevel(parent, invalid); data.setUnspentSkillPoints(cost);
                rejected(data, id, STALE_REQUEST);
            }
            ShokugiData data = new ShokugiData();
            data.setSkillLevel(parent, 1); data.setUnspentSkillPoints(Long.MAX_VALUE);
            data.setSpentSkillPoints(Long.MAX_VALUE - cost + 1);
            rejected(data, id, INSUFFICIENT_SP);
            data.setSpentSkillPoints(Long.MAX_VALUE - cost);
            require(FoodHealingSkills.tryPurchase(data, id, 0) == SUCCESS, "valid Long boundary rejected");
            require(data.getSpentSkillPoints() == Long.MAX_VALUE && data.getUnspentSkillPoints() == Long.MAX_VALUE - cost,
                    "Long boundary debit differs");
        }
        // Effect predicates stay independent: purchase never enables a saved-OFF ancestor.
        ShokugiData chain = new ShokugiData(); chain.setUnspentSkillPoints(603);
        require(FoodHealingSkills.tryPurchase(chain, FoodHealingSkillIds.PURIFICATION, 0) == SUCCESS, "normal P purchase");
        chain.setSkillDisabled(FoodHealingSkillIds.PURIFICATION, true);
        require(FoodHealingSkills.tryPurchase(chain, FoodHealingSkillIds.PURIFICATION_MASTERY, 0) == SUCCESS, "P purchase");
        require(!PurificationMasteryController.isEnabled(chain), "purchase bypassed parent effect condition");
        chain.setSkillDisabled(FoodHealingSkillIds.PURIFICATION_MASTERY, true);
        require(FoodHealingSkills.tryPurchase(chain, FoodHealingSkillIds.TRUTH_MASTERY, 0) == SUCCESS, "T purchase");
        require(!TruthMasteryController.isEnabled(chain) && chain.getSpentSkillPoints() == 603, "Truth effect/total changed");
        System.out.println("MASTERY_PURCHASE_UNIT_PASS configurations=4 assertions=" + assertions);
    }

    private static void rejected(ShokugiData data, String id, FoodHealingSkills.PurchaseResult reason) {
        CompoundTag before = data.serializeNBT();
        require(FoodHealingSkills.purchaseStatus(data, id, 0) == reason, "preview refusal differs");
        require(before.equals(data.serializeNBT()), "preview mutation");
        require(FoodHealingSkills.tryPurchase(data, id, 0) == reason, "server refusal differs");
        require(before.equals(data.serializeNBT()), "refusal mutated canonical");
    }

    private static void require(boolean condition, String message) {
        assertions++;
        if (!condition) throw new AssertionError(message);
    }
}
