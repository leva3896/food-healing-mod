package com.leva.foodhealing;

import java.util.Set;

public final class FoodHealingBaseStatIds {
    public static final String BASE_DEFENSE = id("base_defense");
    public static final String BASE_DAMAGE_REDUCTION_LINEAR = id("base_damage_reduction_linear");
    public static final String BASE_DAMAGE_REDUCTION_TRANSCENDENCE = id("base_damage_reduction_transcendence");
    public static final String HIGH_DIFFICULTY_REDUCTION_LINEAR = id("high_difficulty_reduction_linear");
    public static final String HIGH_DIFFICULTY_REDUCTION_TRANSCENDENCE = id("high_difficulty_reduction_transcendence");
    public static final String BASE_MAX_HEALTH = id("base_max_health");
    public static final String RECOVERY_MULTIPLIER = id("recovery_multiplier");
    public static final String BASE_OUTGOING_DAMAGE = id("base_outgoing_damage");
    public static final String TACZ_BASE_OUTGOING_DAMAGE = id("tacz_base_outgoing_damage");

    public static final Set<String> ALL = Set.of(
            BASE_DEFENSE,
            BASE_DAMAGE_REDUCTION_LINEAR,
            BASE_DAMAGE_REDUCTION_TRANSCENDENCE,
            HIGH_DIFFICULTY_REDUCTION_LINEAR,
            HIGH_DIFFICULTY_REDUCTION_TRANSCENDENCE,
            BASE_MAX_HEALTH,
            RECOVERY_MULTIPLIER,
            BASE_OUTGOING_DAMAGE,
            TACZ_BASE_OUTGOING_DAMAGE);

    private FoodHealingBaseStatIds() {
    }

    private static String id(String path) {
        return FoodHealingMod.MODID + ":" + path;
    }
}
