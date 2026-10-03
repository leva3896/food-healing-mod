package com.leva.foodhealing.client;

public final class HealthHudLayoutRegression {
    private HealthHudLayoutRegression() {
    }

    public static void run() {
        requirePosition(HealthHudLayout.place(427, 240, 120, 189, 90, 14),
                120, 189, "preferred anchor");
        requirePosition(HealthHudLayout.place(854, 480, 336, 429, 90, 14),
                336, 429, "larger GUI-scaled screen");
        requirePosition(HealthHudLayout.place(320, 180, -100, 500, 90, 14),
                2, 164, "left and bottom clamp");
        requirePosition(HealthHudLayout.place(320, 180, 500, -100, 90, 14),
                228, 2, "right and top clamp");
    }

    private static void requirePosition(HealthHudLayout.Position position, int x, int y, String scenario) {
        require(position.x() == x && position.y() == y,
                scenario + " HUD position was " + position + " instead of " + x + "," + y);
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

}
