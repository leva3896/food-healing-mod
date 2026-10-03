package com.leva.foodhealing.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public final class LegacyProjectileCompat {
    private LegacyProjectileCompat() {
    }

    public static boolean isFromOptionalMod(Entity entity, String modId) {
        if (entity == null || !ModList.get().isLoaded(modId)) {
            return false;
        }
        ResourceLocation entityTypeId = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        return entityTypeId != null && entityTypeId.getNamespace().equals(modId);
    }
}
