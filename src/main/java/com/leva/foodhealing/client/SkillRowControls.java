package com.leva.foodhealing.client;

final class SkillRowControls {
    private SkillRowControls() {
    }

    static State forLevels(int purchasedLevel, int effectiveLevel, int maxLevel) {
        boolean toggleVisible = effectiveLevel > 0;
        boolean purchaseVisible = purchasedLevel > 0
                ? purchasedLevel < maxLevel
                : effectiveLevel == 0 && maxLevel > 0;
        return new State(purchaseVisible, toggleVisible);
    }

    record State(boolean purchaseVisible, boolean toggleVisible) {
    }
}
