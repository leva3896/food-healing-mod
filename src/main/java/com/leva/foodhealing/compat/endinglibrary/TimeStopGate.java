package com.leva.foodhealing.compat.endinglibrary;

import net.minecraftforge.fml.loading.FMLLoader;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.HexFormat;

/** Metadata/archive checks only: safe to load without either optional mod. */
public final class TimeStopGate {
    private static boolean initialized, supported;
    private TimeStopGate() { }
    public static synchronized void initialize() {
        if (initialized) return;
        if (FMLLoader.getLoadingModList() == null) return;
        initialized = true;
        supported = matches("fantasy_ending", "2.7.20", "E32FD4BA2E48FCF2C84F71AA07155C63D6BFA2178BB840899307DA0DA5E5D141")
                && matches("ending_library", "2.1.19fix", "0E29AF51DABD4E0EA8A315C1886E61F3ABEE62045F196EB6913D7F4DBA6CDC34");
    }
    private static boolean matches(String id, String version, String hash) {
        try {
            var list = FMLLoader.getLoadingModList();
            if (list == null) return false;
            var mod = list.getMods().stream().filter(m -> m.getModId().equals(id)).findFirst();
            if (mod.isEmpty() || !mod.get().getVersion().toString().equals(version)) return false;
            var path = mod.get().getOwningFile().getFile().getFilePath();
            return HexFormat.of().withUpperCase().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(Files.readAllBytes(path))).equals(hash);
        } catch (Exception unavailable) { return false; }
    }
    public static boolean supported() { initialize(); return supported; }
}
