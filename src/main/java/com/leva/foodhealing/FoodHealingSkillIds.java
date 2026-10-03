package com.leva.foodhealing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class FoodHealingSkillIds {
    public static final String FIRE_RESISTANCE = id("fire_resistance");
    public static final String WATER_NIGHT_VISION = id("water_night_vision");
    public static final String FAST_EATING = id("fast_eating");
    public static final String ACROBATICS = id("acrobatics");
    public static final String FLAME_BLESSING = id("flame_blessing");
    public static final String EXPLOSION_RESISTANCE = id("explosion_resistance");
    public static final String PURIFICATION = id("purification");
    public static final String FOOD_PRODUCTION_MASTERY = id("food_production_mastery");
    public static final String SLAUGHTER = id("slaughter");
    public static final String HEROICS = id("heroics");
    public static final String SATISFACTION = id("satisfaction");
    public static final String GATHERING = id("gathering");
    public static final String UNBREAKING = id("unbreaking");
    public static final String ARMOR_MASTERY = id("armor_mastery");
    public static final String FLIGHT = id("flight");
    public static final String KONGO = id("kongo");
    public static final String PURSUIT = id("pursuit");
    public static final String GUTS = id("guts");
    public static final String TRUE_GUTS = id("true_guts");
    public static final String TRUE_HEROICS = id("true_heroics");
    public static final String BREAK_REALM_MASTERY = id("break_realm_mastery");
    public static final String PURIFICATION_MASTERY = id("purification_mastery");
    public static final String TRUTH_MASTERY = id("truth_mastery");
    public static final String TACZ_AMMO_CONSERVATION = id("tacz_ammo_conservation");

    public static final String QUARRYING = id("quarrying");
    public static final String IMMOVABLE_MASTERY = id("immovable_mastery");

    private static final Map<String, String> ALIASES = createAliases();

    private FoodHealingSkillIds() {
    }

    public static String id(String path) {
        return FoodHealingMod.MODID + ":" + path;
    }

    public static String normalize(String rawSkillId) {
        if (rawSkillId == null) {
            return "";
        }

        String trimmed = rawSkillId.trim();
        if (trimmed.isEmpty()) {
            return "";
        }

        String exact = ALIASES.get(trimmed);
        if (exact != null) {
            return exact;
        }

        String lower = trimmed.toLowerCase(Locale.ROOT);
        String lowerAlias = ALIASES.get(lower);
        if (lowerAlias != null) {
            return lowerAlias;
        }

        if (lower.indexOf(':') >= 0) {
            return lower;
        }

        return id(lower.replace(' ', '_'));
    }

    public static Set<String> normalizeAll(Set<String> skillIds) {
        Set<String> normalized = new HashSet<>();
        for (String skillId : skillIds) {
            String normalizedId = normalize(skillId);
            if (!normalizedId.isEmpty()) {
                normalized.add(normalizedId);
            }
        }
        return normalized;
    }

    private static Map<String, String> createAliases() {
        Map<String, String> aliases = new HashMap<>();
        addAliases(aliases, FIRE_RESISTANCE, "耐火", "fireproof", "taika");
        addAliases(aliases, WATER_NIGHT_VISION, "水月と暗視", "suigetsu", "anshi", "abyssal");
        addAliases(aliases, FAST_EATING, "早食い", "hayagui", "fast_eating");
        addAliases(aliases, ACROBATICS, "軽業", "karuwaza", "acrobatics");
        addAliases(aliases, FLAME_BLESSING, "炎", "honoo", "flame_blessing");
        addAliases(aliases, EXPLOSION_RESISTANCE, "爆破耐性", "bakuha", "blast_resistance");
        addAliases(aliases, PURIFICATION, "浄化", "joka", "purification");
        addAliases(aliases, FOOD_PRODUCTION_MASTERY, "豊穣", "食料生産の極意", "hojo", "food_production_mastery");
        addAliases(aliases, SLAUGHTER, "屠殺", "tosatsu", "butcher", "slaughter");
        addAliases(aliases, HEROICS, "火事場力", "火事場", "kajiba", "heroics");
        addAliases(aliases, SATISFACTION, "満足感", "manzoku", "satisfaction");
        addAliases(aliases, GATHERING, "採取", "saishu", "gatherer", "gathering");
        addAliases(aliases, UNBREAKING, "不壊", "fukai", "indestructible", "unbreaking");
        addAliases(aliases, ARMOR_MASTERY, "防具の極意", "gokui", "armor_mastery");
        addAliases(aliases, FLIGHT, "飛翔", "hisho", "flight");
        addAliases(aliases, KONGO, "金剛", "kongo");
        addAliases(aliases, PURSUIT, "追撃", "tsuigeki", "pursuit");
        addAliases(aliases, GUTS, "根性", "guts");
        addAliases(aliases, TRUE_GUTS, "真・根性", "true_guts");
        addAliases(aliases, TRUE_HEROICS, "真・火事場", "true_heroics");
        addAliases(aliases, BREAK_REALM_MASTERY, "破界の極意", "break_realm_mastery");
        addAliases(aliases, PURIFICATION_MASTERY, "浄化の極意", "purification_mastery");
        addAliases(aliases, TRUTH_MASTERY, "真実の極意", "truth_mastery");
        addAliases(aliases, TACZ_AMMO_CONSERVATION, "弾薬節約", "tacz_ammo_conservation");
        return aliases;
    }

    private static void addAliases(Map<String, String> aliases, String id, String... values) {
        aliases.put(id, id);
        for (String value : values) {
            aliases.put(value, id);
            aliases.put(value.toLowerCase(Locale.ROOT), id);
        }
    }
}
