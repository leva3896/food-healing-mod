package com.leva.foodhealing.client;

import net.minecraft.client.gui.screens.inventory.BlastFurnaceScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.DispenserScreen;
import net.minecraft.client.gui.screens.inventory.FurnaceScreen;
import net.minecraft.client.gui.screens.inventory.GrindstoneScreen;
import net.minecraft.client.gui.screens.inventory.HopperScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.LecternScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.client.gui.screens.inventory.SmokerScreen;

public final class FoodHealingGuiEntryPolicyRegression {
    private FoodHealingGuiEntryPolicyRegression() {
    }

    public static void run() {
        requireAllowed(InventoryScreen.class, "normal player inventory");
        requireAllowed(CreativeModeInventoryScreen.class, "creative player inventory");

        requireDenied(BlastFurnaceScreen.class, "blast furnace");
        requireDenied(SmokerScreen.class, "smoker");
        requireDenied(GrindstoneScreen.class, "grindstone");
        requireDenied(SmithingScreen.class, "smithing table");
        requireDenied(LecternScreen.class, "lectern");
        requireDenied(ContainerScreen.class, "chest or barrel");
        requireDenied(HopperScreen.class, "hopper");
        requireDenied(DispenserScreen.class, "dropper or dispenser");
        requireDenied(FurnaceScreen.class, "furnace");
        requireDenied(CraftingScreen.class, "crafting table");
    }

    private static void requireAllowed(Class<?> screenClass, String scenario) {
        require(FoodHealingGuiEntryPolicy.isAllowedScreenClass(screenClass),
                scenario + " should allow the Food Healing GUI entry key");
    }

    private static void requireDenied(Class<?> screenClass, String scenario) {
        require(!FoodHealingGuiEntryPolicy.isAllowedScreenClass(screenClass),
                scenario + " must not allow the Food Healing GUI entry key");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
