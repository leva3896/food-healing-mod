package com.leva.foodhealing.compat;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;

/** The core never resolves optional classes unless the exact supported mod is present. */
public final class TaczAmmoCompatibility {
    public static final String SUPPORTED_VERSION = "1.1.7-hotfix2";
    private static boolean ready;

    private TaczAmmoCompatibility() { }

    public static boolean supported() {
        if (ModList.get() == null) return false;
        return ModList.get().getModContainerById("tacz")
                .map(mod -> SUPPORTED_VERSION.equals(mod.getModInfo().getVersion().toString()))
                .orElse(false);
    }

    public static void initialize() {
        if (supported()) {
            com.leva.foodhealing.compat.tacz.TaczAmmoAdapter.register();
            ready = true;
        }
    }

    public static boolean ready() { return supported() && ready; }

    public static void onEnabled(ServerPlayer player) {
        if (supported()) com.leva.foodhealing.compat.tacz.TaczAmmoAdapter.normalizeActive(player);
    }
}
