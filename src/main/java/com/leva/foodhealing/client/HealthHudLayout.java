package com.leva.foodhealing.client;

final class HealthHudLayout {
    private static final int SCREEN_MARGIN = 2;

    private HealthHudLayout() {
    }

    static Position place(int screenWidth, int screenHeight, int preferredX, int preferredY,
                          int hudWidth, int hudHeight) {
        int x = clamp(preferredX, SCREEN_MARGIN, Math.max(SCREEN_MARGIN, screenWidth - hudWidth - SCREEN_MARGIN));
        int y = clamp(preferredY, SCREEN_MARGIN, Math.max(SCREEN_MARGIN, screenHeight - hudHeight - SCREEN_MARGIN));
        return new Position(x, y);
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    record Position(int x, int y) {
    }
}
