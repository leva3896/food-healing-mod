package com.leva.foodhealing.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public final class FoodHealingKeyMappings {
    public static final KeyMapping OPEN_GUI = new KeyMapping(
            "key.foodhealing.open_gui",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_S,
            "key.categories.foodhealing");

    private FoodHealingKeyMappings() {
    }
}
