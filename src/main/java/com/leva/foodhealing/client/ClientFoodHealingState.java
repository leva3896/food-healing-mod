package com.leva.foodhealing.client;

import com.leva.foodhealing.FoodHealingConfig;

public final class ClientFoodHealingState {
    private static long levelUpRequirement = 2000L;

    private ClientFoodHealingState() {
    }

    public static long getLevelUpRequirement() {
        return levelUpRequirement;
    }

    public static void setLevelUpRequirement(long requirement) {
        levelUpRequirement = requirement > 0
                ? requirement
                : FoodHealingConfig.nutritionThreshold();
    }
}
