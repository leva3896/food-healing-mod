package com.leva.foodhealing;

import com.google.gson.JsonParser;
import com.leva.foodhealing.capability.ShokugiData;
import net.minecraft.nbt.CompoundTag;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.leva.foodhealing.FoodHealingSkills.PurchaseResult.*;

/** Locked acquisition conditions, separate from effect activation and optional target availability. */
public final class OpenPrerequisiteRegression {
    private static int assertions;

    private OpenPrerequisiteRegression() { }

    public static void main(String[] args) { run(); }

    public static void run() {
        assertions = 0;
        lockedNode(FoodHealingSkillIds.PURIFICATION_MASTERY, FoodHealingSkillIds.PURIFICATION, 100);
        lockedNode(FoodHealingSkillIds.TRUTH_MASTERY, FoodHealingSkillIds.PURIFICATION_MASTERY, 500);
        multipleRequirementsRemainConjunctive();
        readyPurchaseAndReplay();
        optionalAbsenceAndExistingGates();
        localizedReasons();
        MasteryPurchaseRegression.run();
        System.out.println("OPEN_PREREQUISITE_UNIT_PASS groups=6 assertions=" + assertions);
    }

    private static void lockedNode(String id, String parent, long cost) {
        var definition = FoodHealingSkills.definitions().get(id);
        require(!definition.openPrerequisites(), "resolved graph still marked OPEN: " + id);
        require(definition.maxLevel() == 1 && definition.levelCosts()[0] == cost, "node cost/level changed");
        require(definition.requirements().equals(List.of(new FoodHealingSkills.SkillRequirement(parent, 1))),
                "unapproved parent node/level added");
        var data = new ShokugiData();
        data.setLevel(0); // No new Shokugi-level acquisition condition.
        data.setUnspentSkillPoints(cost);
        rejectUnchanged(data, id, 0, PREREQUISITE_MISSING);
        data.setSkillLevel(parent, 1); // Test fixture; never a production readiness bypass.
        data.setSkillDisabled(parent, true);
        require(FoodHealingSkills.hasRequiredSkills(data, definition.requirements()), "owned OFF parent rejected");
        require(FoodHealingSkills.isPurchaseImplementationReady(id), "verified mastery is not ready");
        CompoundTag preview = data.serializeNBT();
        require(FoodHealingSkills.purchaseStatus(data, id, 0) == SUCCESS, "OFF-parent preview not ready");
        require(preview.equals(data.serializeNBT()), "preview mutated state");
        data.setUnspentSkillPoints(cost - 1);
        rejectUnchanged(data, id, 0, INSUFFICIENT_SP);
        data.setUnspentSkillPoints(cost);
        for (int invalid : new int[]{-1, Integer.MIN_VALUE, Integer.MAX_VALUE, 1}) {
            rejectUnchanged(data, id, invalid, STALE_REQUEST);
        }
        require(FoodHealingSkills.tryPurchase(data, id, 0) == SUCCESS, "exact-cost mastery purchase failed");
        require(data.getUnspentSkillPoints() == 0 && data.getSpentSkillPoints() == cost
                && data.getSkillLevel(id) == 1 && !data.isSkillDisabled(id)
                && data.isSkillDisabled(parent), "exact debit/default ON/parent OFF changed");
        rejectUnchanged(data, id, 0, STALE_REQUEST);
        data.setSkillDisabled(id, true);
        rejectUnchanged(data, id, 1, MAX_LEVEL);
        var restored = new ShokugiData();
        restored.deserializeNBT(data.serializeNBT());
        require(restored.serializeNBT().equals(data.serializeNBT()), "ownership/save/toggle changed");
    }

    private static void multipleRequirementsRemainConjunctive() {
        var list = new ArrayList<>(List.of(new FoodHealingSkills.SkillRequirement(FoodHealingSkillIds.GUTS, 5),
                new FoodHealingSkills.SkillRequirement(FoodHealingSkillIds.PURIFICATION, 1)));
        var definition = new FoodHealingSkills.SkillDefinition("test:multiple", 1, new long[]{1}, list, false);
        list.clear();
        var data = new ShokugiData();
        data.setSkillLevel(FoodHealingSkillIds.GUTS, 4);
        data.setSkillLevel(FoodHealingSkillIds.PURIFICATION, 1);
        require(!FoodHealingSkills.hasRequiredSkills(data, definition.requirements()), "required level ignored");
        data.setSkillLevel(FoodHealingSkillIds.GUTS, 5);
        data.setSkillDisabled(FoodHealingSkillIds.GUTS, true);
        require(FoodHealingSkills.hasRequiredSkills(data, definition.requirements()), "multiple list/ownership lost");
        data.setSkillLevel(FoodHealingSkillIds.PURIFICATION, 0);
        require(!FoodHealingSkills.hasRequiredSkills(data, definition.requirements()), "AND became OR");
    }

    private static void readyPurchaseAndReplay() {
        var data = new ShokugiData();
        data.setSkillLevel(FoodHealingSkillIds.HEROICS, 5);
        data.setSkillDisabled(FoodHealingSkillIds.HEROICS, true);
        data.setUnspentSkillPoints(100);
        CompoundTag before = data.serializeNBT();
        require(FoodHealingSkills.purchaseStatus(data, FoodHealingSkillIds.TRUE_HEROICS, 0) == SUCCESS,
                "ready node rejected with acquired OFF parent");
        require(before.equals(data.serializeNBT()), "GUI preview spent SP");
        require(FoodHealingSkills.tryPurchase(data, FoodHealingSkillIds.TRUE_HEROICS, 0) == SUCCESS,
                "ready transaction rejected");
        require(data.getUnspentSkillPoints() == 0 && data.getSpentSkillPoints() == 100
                && data.getSkillLevel(FoodHealingSkillIds.TRUE_HEROICS) == 1, "incorrect atomic cost/ownership");
        rejectUnchanged(data, FoodHealingSkillIds.TRUE_HEROICS, 0, STALE_REQUEST);
        rejectUnchanged(data, "invalid:unknown", 0, UNKNOWN_SKILL);
        data.setUnspentSkillPoints(1);
        data.setSpentSkillPoints(Long.MAX_VALUE);
        before = data.serializeNBT();
        require(FoodHealingSkills.tryPurchase(data, FoodHealingSkillIds.FIRE_RESISTANCE, 0) == INSUFFICIENT_SP,
                "existing overflow guard lost");
        require(data.serializeNBT().equals(before), "SP accounting overflow partially mutated canonical data");
    }

    private static void optionalAbsenceAndExistingGates() {
        var data = new ShokugiData();
        data.setUnspentSkillPoints(1000);
        rejectUnchanged(data, FoodHealingSkillIds.TACZ_AMMO_CONSERVATION, 0, OPTIONAL_MOD_MISSING);
        require(FoodHealingSkills.tryPurchase(data, FoodHealingSkillIds.FLIGHT, 0) == SUCCESS,
                "exact-provider Flight needs no optional MOD to purchase");
        rejectUnchanged(data, FoodHealingSkillIds.FLIGHT, 0, STALE_REQUEST);
        rejectUnchanged(data, FoodHealingSkillIds.BREAK_REALM_MASTERY, 0, IMPLEMENTATION_PENDING);
        CompoundTag unknown = new CompoundTag();
        unknown.putInt("FoodHealingDataVersion", 999);
        data.deserializeNBT(unknown);
        rejectUnchanged(data, FoodHealingSkillIds.PURIFICATION_MASTERY, 0, LEGACY_MIGRATION_PENDING);
    }

    private static void localizedReasons() {
        var reasons = List.of(PREREQUISITE_MISSING, INSUFFICIENT_SP, IMPLEMENTATION_PENDING, OPTIONAL_MOD_MISSING);
        require(reasons.stream().map(FoodHealingSkills.PurchaseResult::messageKey).distinct().count() == 4,
                "A/B/C/D purchase reasons collapsed");
        for (String lang : List.of("en_us", "ja_jp")) {
            try (var stream = OpenPrerequisiteRegression.class.getResourceAsStream(
                    "/assets/foodhealing/lang/" + lang + ".json")) {
                require(stream != null, "missing localized resources");
                var json = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
                for (var result : FoodHealingSkills.PurchaseResult.values()) {
                    require(json.has(result.messageKey()), "missing localized purchase result: " + result);
                }
                String pending = json.get(IMPLEMENTATION_PENDING.messageKey()).getAsString();
                require(!pending.contains("OPEN") && !pending.contains("未確定"), "effect gate still blames unlocked prerequisites");
            } catch (java.io.IOException exception) { throw new AssertionError(exception); }
        }
    }

    private static void rejectUnchanged(ShokugiData data, String id, int expected,
                                        FoodHealingSkills.PurchaseResult result) {
        CompoundTag before = data.serializeNBT();
        require(FoodHealingSkills.purchaseStatus(data, id, expected) == result, "wrong read-only reason: " + id + "/" + result);
        require(before.equals(data.serializeNBT()), "preview changed canonical state");
        require(FoodHealingSkills.tryPurchase(data, id, expected) == result, "server/preview disagreement: " + id);
        require(before.equals(data.serializeNBT()), "rejection changed SP/skills/toggles/other fields");
    }

    private static void require(boolean condition, String message) {
        assertions++;
        if (!condition) throw new AssertionError(message);
    }
}
