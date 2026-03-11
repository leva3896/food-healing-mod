package com.example.examplemod.compat;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.LivingEntity;
import org.slf4j.Logger;

import java.lang.reflect.Method;

public class TrialMonolithCompat {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static boolean theTrialMonolithFound = false;
    private static Class<?> soulProtectionClass;
    private static Method setSoulProtectedMethod;
    private static boolean initialized = false;

    private static void init() {
        if (initialized)
            return;
        initialized = true;

        try {
            soulProtectionClass = Class.forName("io.github.kosianodangoo.trialmonolith.api.mixin.ISoulProtection");
            setSoulProtectedMethod = soulProtectionClass.getMethod("the_trial_monolith$setSoulProtected",
                    boolean.class);
            theTrialMonolithFound = true;
            LOGGER.info(
                    "[FoodHealing] TheTrialMonolith API (ISoulProtection) found via Reflection. Integration active.");
        } catch (ClassNotFoundException e) {
            // TTM is not installed, which is completely fine.
            theTrialMonolithFound = false;
            LOGGER.debug("[FoodHealing] TheTrialMonolith API not found. Skipping integration.");
        } catch (NoSuchMethodException e) {
            theTrialMonolithFound = false;
            LOGGER.error(
                    "[FoodHealing] TheTrialMonolith ISoulProtection class found, but method is missing! Integration disabled.");
        }
    }

    public static void setSoulProtected(LivingEntity entity, boolean protect) {
        init();

        if (!theTrialMonolithFound || entity == null)
            return;

        // Check if the entity implements the interface
        if (soulProtectionClass.isInstance(entity)) {
            try {
                setSoulProtectedMethod.invoke(entity, protect);
            } catch (Exception e) {
                LOGGER.error("[FoodHealing] Failed to invoke setSoulProtected via reflection on {}", entity, e);
            }
        }
    }
}
