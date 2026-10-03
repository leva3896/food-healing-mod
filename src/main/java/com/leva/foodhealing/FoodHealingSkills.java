package com.leva.foodhealing;

import com.leva.foodhealing.capability.IShokugiData;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class FoodHealingSkills {
    private static final Map<String, SkillDefinition> DEFINITIONS = createDefinitions();
    private static final Set<String> IMPLEMENTATION_PENDING = Set.of(
            FoodHealingSkillIds.BREAK_REALM_MASTERY);

    private FoodHealingSkills() {
    }

    public static int getEffectiveLevel(IShokugiData data, String skillId) {
        String normalized = FoodHealingSkillIds.normalize(skillId);
        int purchasedLevel = data.getSkillLevel(normalized);
        if (purchasedLevel > 0) {
            return purchasedLevel;
        }
        return data.isLegacyMigrationPending() ? getLegacyLevel(data.getLegacyShokugiLevel(), normalized) : 0;
    }

    /** New skills require valid canonical ownership, never a legacy fallback. */
    public static boolean isCanonicalSkillEnabled(IShokugiData data, String id) {
        return data != null && !data.isLegacyMigrationPending() && data.getSkillLevel(id) == 1
                && !data.isSkillDisabled(id) && data.getAcquiredSkills().entrySet().stream().allMatch(entry -> {
                    SkillDefinition definition = DEFINITIONS.get(entry.getKey());
                    return definition != null && entry.getValue() >= 1 && entry.getValue() <= definition.maxLevel();
                });
    }

    public static boolean isEnabled(IShokugiData data, String skillId) {
        return getEnabledLevel(data, skillId) > 0;
    }

    public static int getEnabledLevel(IShokugiData data, String skillId) {
        String normalized = FoodHealingSkillIds.normalize(skillId);
        return data.isSkillDisabled(normalized) ? 0 : getEffectiveLevel(data, normalized);
    }

    public static PurchaseResult tryPurchase(IShokugiData data, String rawSkillId) {
        String skillId = FoodHealingSkillIds.normalize(rawSkillId);
        return tryPurchase(data, skillId, data.getSkillLevel(skillId));
    }

    public static PurchaseResult tryPurchase(IShokugiData data, String rawSkillId, int expectedLevel) {
        PurchaseResult status = purchaseStatus(data, rawSkillId, expectedLevel);
        if (status != PurchaseResult.SUCCESS) {
            return status;
        }
        String skillId = FoodHealingSkillIds.normalize(rawSkillId);
        long cost = DEFINITIONS.get(skillId).levelCosts()[expectedLevel];
        // Only the authoritative transaction spends SP; previewing never mutates data.
        if (!data.trySpendSkillPoints(cost)) {
            return PurchaseResult.INSUFFICIENT_SP;
        }
        data.setSkillLevel(skillId, expectedLevel + 1);
        return PurchaseResult.SUCCESS;
    }

    /** Shared, read-only eligibility/reason for the GUI and the server transaction. */
    public static PurchaseResult purchaseStatus(IShokugiData data, String rawSkillId, int expectedLevel) {
        String skillId = FoodHealingSkillIds.normalize(rawSkillId);
        SkillDefinition definition = DEFINITIONS.get(skillId);
        if (definition == null) {
            return PurchaseResult.UNKNOWN_SKILL;
        }
        if (data.isLegacyMigrationPending()) {
            return PurchaseResult.LEGACY_MIGRATION_PENDING;
        }
        if (skillId.equals(FoodHealingSkillIds.FLIGHT) && !data.isFlightAcquisitionValid()) {
            return PurchaseResult.STALE_REQUEST;
        }
        // Mastery readiness does not make malformed canonical ownership purchasable.
        // Read only: never repair levels, refund SP, or change saved toggles here.
        if ((skillId.equals(FoodHealingSkillIds.PURIFICATION_MASTERY)
                || skillId.equals(FoodHealingSkillIds.TRUTH_MASTERY)
                || skillId.equals(FoodHealingSkillIds.QUARRYING)
                || skillId.equals(FoodHealingSkillIds.IMMOVABLE_MASTERY))
                && data.getAcquiredSkills().entrySet().stream().anyMatch(entry -> {
                    SkillDefinition acquired = DEFINITIONS.get(entry.getKey());
                    return acquired == null || entry.getValue() < 1 || entry.getValue() > acquired.maxLevel();
                })) {
            return PurchaseResult.STALE_REQUEST;
        }
        if (definition.openPrerequisites()) {
            return PurchaseResult.OPEN_PREREQUISITES;
        }
        int currentLevel = data.getSkillLevel(skillId);
        if (currentLevel != expectedLevel) {
            return PurchaseResult.STALE_REQUEST;
        }
        if (currentLevel >= definition.maxLevel()) {
            return PurchaseResult.MAX_LEVEL;
        }
        if (!hasRequiredSkills(data, definition.requirements())) {
            return PurchaseResult.PREREQUISITE_MISSING;
        }

        long cost = definition.levelCosts()[currentLevel];
        if (data.getUnspentSkillPoints() < cost || data.getSpentSkillPoints() > Long.MAX_VALUE - cost) {
            return PurchaseResult.INSUFFICIENT_SP;
        }
        if (skillId.equals(FoodHealingSkillIds.TACZ_AMMO_CONSERVATION)
                && !com.leva.foodhealing.compat.TaczAmmoCompatibility.supported()) {
            return PurchaseResult.OPTIONAL_MOD_MISSING;
        }
        if (!isPurchaseImplementationReady(skillId)) {
            return PurchaseResult.IMPLEMENTATION_PENDING;
        }
        return PurchaseResult.SUCCESS;
    }

    public static Map<String, SkillDefinition> definitions() {
        return Map.copyOf(DEFINITIONS);
    }

    public static List<SkillDefinition> orderedDefinitions() {
        return List.copyOf(DEFINITIONS.values());
    }

    public static boolean isPurchaseImplementationReady(String skillId) {
        if (FoodHealingSkillIds.TACZ_AMMO_CONSERVATION.equals(FoodHealingSkillIds.normalize(skillId))) {
            return com.leva.foodhealing.compat.TaczAmmoCompatibility.ready();
        }
        return !IMPLEMENTATION_PENDING.contains(FoodHealingSkillIds.normalize(skillId));
    }

    public static boolean hasRequiredSkills(IShokugiData data, List<SkillRequirement> requirements) {
        return requirements.stream()
                .allMatch(requirement -> data.getSkillLevel(requirement.skillId()) >= requirement.level());
    }

    private static int getLegacyLevel(long legacyLevel, String skillId) {
        if (skillId.equals(FoodHealingSkillIds.FIRE_RESISTANCE)) return legacyLevel >= 1L ? 1 : 0;
        if (skillId.equals(FoodHealingSkillIds.WATER_NIGHT_VISION)) return legacyLevel >= 2L ? 1 : 0;
        if (skillId.equals(FoodHealingSkillIds.FAST_EATING)) return legacyLevel >= 3L ? 1 : 0;
        if (skillId.equals(FoodHealingSkillIds.ACROBATICS)) return legacyLevel >= 4L ? 1 : 0;
        if (skillId.equals(FoodHealingSkillIds.FLAME_BLESSING)) return legacyLevel >= 5L ? 1 : 0;
        if (skillId.equals(FoodHealingSkillIds.EXPLOSION_RESISTANCE)) return legacyLevel >= 6L ? 1 : 0;
        if (skillId.equals(FoodHealingSkillIds.PURIFICATION)) return legacyLevel >= 7L ? 1 : 0;
        if (skillId.equals(FoodHealingSkillIds.FOOD_PRODUCTION_MASTERY)) return legacyLevel >= 8L ? 1 : 0;
        if (skillId.equals(FoodHealingSkillIds.SLAUGHTER)) return legacyLevel >= 9L ? 1 : 0;
        if (skillId.equals(FoodHealingSkillIds.HEROICS)) return legacyLevel >= 10L ? 1 : 0;
        if (skillId.equals(FoodHealingSkillIds.SATISFACTION)) {
            return legacyLevel >= 13L ? 3 : legacyLevel >= 12L ? 2 : legacyLevel >= 11L ? 1 : 0;
        }
        if (skillId.equals(FoodHealingSkillIds.GATHERING)) {
            return legacyLevel >= 16L ? 3 : legacyLevel >= 15L ? 2 : legacyLevel >= 14L ? 1 : 0;
        }
        if (skillId.equals(FoodHealingSkillIds.UNBREAKING)) {
            return legacyLevel >= 19L ? 3 : legacyLevel >= 18L ? 2 : legacyLevel >= 17L ? 1 : 0;
        }
        if (skillId.equals(FoodHealingSkillIds.ARMOR_MASTERY)) return legacyLevel >= 20L ? 1 : 0;
        if (skillId.equals(FoodHealingSkillIds.FLIGHT)) return legacyLevel >= 30L ? 1 : 0;
        if (skillId.equals(FoodHealingSkillIds.KONGO)) return legacyLevel >= 100L ? 1 : 0;
        if (skillId.equals(FoodHealingSkillIds.PURSUIT) && legacyLevel >= 991L) {
            return (int) Math.min(9L, legacyLevel - 990L);
        }
        return 0;
    }

    private static Map<String, SkillDefinition> createDefinitions() {
        Map<String, SkillDefinition> definitions = new LinkedHashMap<>();
        add(definitions, FoodHealingSkillIds.FIRE_RESISTANCE, null, 0, false, 1);
        add(definitions, FoodHealingSkillIds.WATER_NIGHT_VISION, null, 0, false, 1);
        add(definitions, FoodHealingSkillIds.FAST_EATING, null, 0, false, 10);
        add(definitions, FoodHealingSkillIds.ACROBATICS, null, 0, false, 1);
        add(definitions, FoodHealingSkillIds.FLAME_BLESSING, null, 0, false, 2);
        add(definitions, FoodHealingSkillIds.EXPLOSION_RESISTANCE, null, 0, false, 10);
        add(definitions, FoodHealingSkillIds.PURIFICATION, null, 0, false, 3);
        add(definitions, FoodHealingSkillIds.FOOD_PRODUCTION_MASTERY, null, 0, false, 4);
        add(definitions, FoodHealingSkillIds.SLAUGHTER, null, 0, false, 5);
        add(definitions, FoodHealingSkillIds.SATISFACTION, null, 0, false, 5, 5, 5);
        add(definitions, FoodHealingSkillIds.QUARRYING, null, 0, false, 1);
        add(definitions, FoodHealingSkillIds.GATHERING, null, 0, false, 5, 5, 5);
        add(definitions, FoodHealingSkillIds.UNBREAKING, null, 0, false, 10, 20, 30);
        add(definitions, FoodHealingSkillIds.ARMOR_MASTERY, null, 0, false, 20);
        add(definitions, FoodHealingSkillIds.FLIGHT, null, 0, false, 2);
        add(definitions, FoodHealingSkillIds.KONGO, null, 0, false, 50);
        add(definitions, FoodHealingSkillIds.IMMOVABLE_MASTERY, null, 0, false, 50);
        add(definitions, FoodHealingSkillIds.PURSUIT, null, 0, false,
                100, 100, 100, 100, 100, 100, 100, 100, 100);

        add(definitions, FoodHealingSkillIds.GUTS, null, 0, false, 10, 10, 10, 10, 10);
        add(definitions, FoodHealingSkillIds.TRUE_GUTS, FoodHealingSkillIds.GUTS, 5, false, 20);
        add(definitions, FoodHealingSkillIds.HEROICS, null, 0, false, 30, 30, 30, 30, 30);
        add(definitions, FoodHealingSkillIds.TRUE_HEROICS, FoodHealingSkillIds.HEROICS, 5, false, 100);
        add(definitions, FoodHealingSkillIds.BREAK_REALM_MASTERY, null, 0, false, 20);
        add(definitions, FoodHealingSkillIds.PURIFICATION_MASTERY, FoodHealingSkillIds.PURIFICATION, 1, false, 100);
        add(definitions, FoodHealingSkillIds.TRUTH_MASTERY, FoodHealingSkillIds.PURIFICATION_MASTERY, 1, false, 500);
        add(definitions, FoodHealingSkillIds.TACZ_AMMO_CONSERVATION, null, 0, false, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50);
        return definitions;
    }

    private static void add(Map<String, SkillDefinition> definitions, String id, String requiredSkillId,
                            int requiredSkillLevel, boolean openPrerequisites, long... costs) {
        List<SkillRequirement> requirements = requiredSkillId == null
                ? List.of()
                : List.of(new SkillRequirement(requiredSkillId, requiredSkillLevel));
        definitions.put(id, new SkillDefinition(id, costs.length, costs, requirements, openPrerequisites));
    }

    public record SkillDefinition(String id, int maxLevel, long[] levelCosts,
                                  List<SkillRequirement> requirements, boolean openPrerequisites) {
        public SkillDefinition {
            levelCosts = levelCosts.clone();
            requirements = List.copyOf(requirements);
        }

        public long[] levelCosts() {
            return levelCosts.clone();
        }
    }

    public record SkillRequirement(String skillId, int level) {
    }

    public enum PurchaseResult {
        SUCCESS,
        UNKNOWN_SKILL,
        MAX_LEVEL,
        INSUFFICIENT_SP,
        PREREQUISITE_MISSING,
        OPTIONAL_MOD_MISSING,
        OPEN_PREREQUISITES,
        LEGACY_MIGRATION_PENDING,
        IMPLEMENTATION_PENDING,
        STALE_REQUEST;

        public String messageKey() {
            return switch (this) {
                case SUCCESS -> "message.foodhealing.purchase.success";
                case PREREQUISITE_MISSING -> "message.foodhealing.prerequisite_missing";
                case INSUFFICIENT_SP -> "message.foodhealing.insufficient_sp";
                case OPTIONAL_MOD_MISSING -> "message.foodhealing.tacz_required";
                case IMPLEMENTATION_PENDING -> "message.foodhealing.implementation_pending";
                case OPEN_PREREQUISITES -> "message.foodhealing.open_prerequisites";
                case LEGACY_MIGRATION_PENDING -> "message.foodhealing.legacy_purchase_pending";
                case MAX_LEVEL -> "message.foodhealing.max_level";
                case STALE_REQUEST -> "message.foodhealing.stale_purchase";
                case UNKNOWN_SKILL -> "message.foodhealing.unknown_skill";
            };
        }
    }
}
