package com.leva.foodhealing.compat.flight;

import net.minecraftforge.fml.ModList;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.HexFormat;

/** Archive identity only; loading this gate never links Mekanism gameplay classes. */
public final class MekanismFlightGate {
    public static final String VERSION = "10.4.16";
    public static final String SHA256 = "3B0D191FA4A45E662F9725991D76A30192F30186ABB2BA694B873B5F7B9358D2";
    private static Boolean supported;
    private MekanismFlightGate() { }

    public static boolean matches(String id, String version, String hash) {
        return "mekanism".equals(id) && VERSION.equals(version) && SHA256.equalsIgnoreCase(hash);
    }

    public static synchronized boolean supported() {
        if (supported != null) return supported;
        var list = ModList.get();
        if (list == null) return false;
        supported = false;
        var mod = list.getModContainerById("mekanism");
        if (mod.isEmpty() || !VERSION.equals(mod.get().getModInfo().getVersion().toString())) return false;
        try {
            var info = mod.get().getModInfo();
            var digest = MessageDigest.getInstance("SHA-256");
            try (var input = Files.newInputStream(info.getOwningFile().getFile().getFilePath())) {
                byte[] buffer = new byte[8192];
                for (int read; (read = input.read(buffer)) != -1;) digest.update(buffer, 0, read);
            }
            supported = matches(info.getModId(), info.getVersion().toString(), HexFormat.of().formatHex(digest.digest()));
        } catch (Exception unavailable) {
            com.mojang.logging.LogUtils.getLogger().warn("[FoodHealing] Mekanism flight archive identity unavailable; provider unsupported", unavailable);
        }
        return supported;
    }
}
