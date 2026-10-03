package com.leva.foodhealing.client;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;

final class FoodHealingGuiEntryPolicy {
    private FoodHealingGuiEntryPolicy() {
    }

    static boolean isAllowed(Screen screen) {
        return screen != null && isAllowedScreenClass(screen.getClass());
    }

    static boolean isAllowedScreenClass(Class<?> screenClass) {
        return screenClass != null
                && (InventoryScreen.class.isAssignableFrom(screenClass)
                || CreativeModeInventoryScreen.class.isAssignableFrom(screenClass));
    }
}
