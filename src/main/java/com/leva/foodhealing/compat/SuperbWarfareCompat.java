package com.leva.foodhealing.compat;

import com.leva.foodhealing.capability.IShokugiData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

/** The projectile gunfire route audited in Superb Warfare 0.8.9.1 (993063bed). */
public final class SuperbWarfareCompat {
    private static final String MOD_ID = "superbwarfare";
    private static final ResourceLocation PROJECTILE = new ResourceLocation(MOD_ID, "projectile");
    private static final Set<String> GUNFIRE = Set.of(
            "gunfire", "gunfire_absolute", "gunfire_headshot", "gunfire_headshot_absolute");

    private SuperbWarfareCompat() {
    }

    public static boolean isSupportedGunfire(DamageSource source) {
        Entity direct = source.getDirectEntity();
        if (!(source.getEntity() instanceof Player) || direct == null
                || !ModList.get().getModContainerById(MOD_ID)
                .map(mod -> "0.8.9.1".equals(mod.getModInfo().getVersion().toString())).orElse(false)) {
            return false;
        }
        // No optional class linkage, owner traversal, explosion or namespace-only inference.
        if (!direct.getClass().getName().equals(
                "com.atsuishio.superbwarfare.entity.projectile.ProjectileEntity")
                || !PROJECTILE.equals(ForgeRegistries.ENTITY_TYPES.getKey(direct.getType()))) {
            return false;
        }
        return source.typeHolder().unwrapKey().map(key -> MOD_ID.equals(key.location().getNamespace())
                && GUNFIRE.contains(key.location().getPath())).orElse(false);
    }

    public static double shokugiMultiplier(IShokugiData data) {
        // Pending data must not gain v3 effects from its unvalidated current level.
        long level = data.isLegacyMigrationPending() ? data.getLegacyShokugiLevel() : data.getLevel();
        return 1.0D + (double) Math.max(0L, level);
    }
}
