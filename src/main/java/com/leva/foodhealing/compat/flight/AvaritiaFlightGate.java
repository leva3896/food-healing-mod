package com.leva.foodhealing.compat.flight;

import net.minecraftforge.fml.ModList;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.HexFormat;

/** Exact archive boundary; never links Avaritia classes. */
public final class AvaritiaFlightGate {
    public static final String VERSION = "4.0.3";
    public static final String SHA256 = "48F23CEA99D1D2E6CED4215B9FE3F9F45641B2CD5650C8725A3EE75B500483BB";
    private static Boolean supported;
    private AvaritiaFlightGate() { }

    public static boolean matches(String id, String version, String hash) {
        return "avaritia".equals(id) && VERSION.equals(version) && SHA256.equalsIgnoreCase(hash);
    }

    public static synchronized boolean supported() {
        if (supported != null) return supported;
        var list = ModList.get();
        if (list == null) return false;
        var mod = list.getModContainerById("avaritia");
        if (mod.isEmpty() || !VERSION.equals(mod.get().getModInfo().getVersion().toString())) {
            supported = false;
            return false;
        }
        try {
            var info = mod.get().getModInfo();
            var digest = MessageDigest.getInstance("SHA-256");
            try (var input = Files.newInputStream(info.getOwningFile().getFile().getFilePath())) {
                byte[] buffer = new byte[8192];
                for (int read; (read = input.read(buffer)) != -1;) digest.update(buffer, 0, read);
            }
            supported = matches(info.getModId(), info.getVersion().toString(), HexFormat.of().formatHex(digest.digest()));
            return supported;
        } catch (Exception unavailable) {
            // Query failure remains UNKNOWN; a known version/hash mismatch is inactive.
            throw new IllegalStateException("Avaritia Flight archive identity unavailable", unavailable);
        }
    }
}
