package com.leva.foodhealing;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;

public final class PlayerHealthLifecycle {
    private static final String NON_DEATH_HEALTH_KEY = "foodhealing:non_death_clone_health";

    private PlayerHealthLifecycle() {
    }

    public static void captureCloneHealth(Player original, Player replacement, boolean wasDeath) {
        CompoundTag persistentData = replacement.getPersistentData();
        persistentData.remove(NON_DEATH_HEALTH_KEY);
        float health = original.getHealth();
        if (!wasDeath && Float.isFinite(health)) {
            persistentData.putFloat(NON_DEATH_HEALTH_KEY, Math.max(0.0F, health));
        }
    }

    public static void restoreRespawnHealth(Player player, boolean endConquered) {
        CompoundTag persistentData = player.getPersistentData();
        boolean hasCarriedHealth = persistentData.contains(NON_DEATH_HEALTH_KEY, Tag.TAG_FLOAT);
        float carriedHealth = hasCarriedHealth ? persistentData.getFloat(NON_DEATH_HEALTH_KEY) : 0.0F;
        persistentData.remove(NON_DEATH_HEALTH_KEY);

        float maxHealth = player.getMaxHealth();
        if (!Float.isFinite(maxHealth) || maxHealth <= 0.0F) {
            return;
        }
        if (endConquered) {
            if (hasCarriedHealth) {
                player.setHealth(clamp(carriedHealth, maxHealth));
            } else {
                clampCurrentHealth(player);
            }
            return;
        }
        player.setHealth(maxHealth);
    }

    public static void clampCurrentHealth(Player player) {
        float maxHealth = player.getMaxHealth();
        if (!Float.isFinite(maxHealth) || maxHealth <= 0.0F) {
            return;
        }
        player.setHealth(clamp(player.getHealth(), maxHealth));
    }

    private static float clamp(float health, float maxHealth) {
        if (!Float.isFinite(health)) {
            return maxHealth;
        }
        return Math.min(Math.max(0.0F, health), maxHealth);
    }
}
