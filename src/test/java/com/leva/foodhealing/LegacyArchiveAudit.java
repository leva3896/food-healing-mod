package com.leva.foodhealing;

import com.leva.foodhealing.capability.ShokugiData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipFile;

/** Read-only inspection of existing backups. A legacy-looking tag is not proof of a v2 world. */
public final class LegacyArchiveAudit {
    private LegacyArchiveAudit() { }

    public static void main(String[] args) throws Exception {
        Path backupRoot = Path.of(args[0]).toRealPath();
        Path archive = Path.of(args[1]).toRealPath();
        if (!archive.startsWith(backupRoot) || !archive.toString().endsWith(".zip")) {
            throw new IllegalArgumentException("Only an existing project backup ZIP may be inspected");
        }
        String before = hash(Files.readAllBytes(archive));
        Map<String, Integer> playerVersions = new TreeMap<>();
        int levels = 0;
        int players = 0;
        int snapshots = 0;
        try (ZipFile zip = new ZipFile(archive.toFile())) {
            for (var entry : zip.stream().sorted(java.util.Comparator.comparing(e -> e.getName())).toList()) {
                String name = entry.getName().replace('\\', '/');
                boolean level = name.endsWith("/level.dat") || name.endsWith("/level.dat_old");
                boolean player = name.contains("/playerdata/") && name.endsWith(".dat");
                if (!level && !player) continue;
                byte[] bytes;
                try (var input = zip.getInputStream(entry)) { bytes = input.readAllBytes(); }
                CompoundTag root = NbtIo.readCompressed(new ByteArrayInputStream(bytes));
                if (level) {
                    levels++;
                    CompoundTag data = root.getCompound("Data");
                    System.out.println("LEVEL entry=" + ascii(name) + " sha256=" + hash(bytes)
                            + " keys=" + root.getAllKeys() + " dataKeys=" + data.getAllKeys()
                            + " version=" + data.getCompound("Version") + " time=" + data.getLong("Time")
                            + " lastPlayed=" + data.getLong("LastPlayed"));
                    for (String key : new String[]{"fml", "FML", "forge", "Forge"}) {
                        if (root.contains(key)) metadata(root.getCompound(key));
                        if (data.contains(key)) metadata(data.getCompound(key));
                    }
                    if (data.contains("Player", Tag.TAG_COMPOUND)) {
                        describe(name + "/Data.Player", data.getCompound("Player"));
                        snapshots += verifyV3Snapshot(data.getCompound("Player"));
                    }
                } else {
                    players++;
                    CompoundTag progress = root.getCompound("ForgeCaps").getCompound("foodhealing:shokugi_data");
                    String group = name.substring(0, name.indexOf("/playerdata/")) + " schema="
                            + progress.getInt("FoodHealingDataVersion") + " backup="
                            + progress.contains("LegacyV2Backup", Tag.TAG_COMPOUND);
                    playerVersions.merge(group, 1, Integer::sum);
                    snapshots += verifyV3Snapshot(root);
                    if (name.startsWith("saves/") || progress.getInt("FoodHealingDataVersion") < 4) describe(name, root);
                }
            }
        }
        if (!before.equals(hash(Files.readAllBytes(archive)))) throw new AssertionError("Archive changed during audit");
        playerVersions.forEach((key, count) -> System.out.println("PLAYER_GROUP " + ascii(key) + " count=" + count));
        System.out.println("V3_SNAPSHOT_ROUNDTRIP_PASS count=" + snapshots + " scope=IN_MEMORY_ONLY_NO_WORLD_LOAD");
        System.out.println("ARCHIVE_AUDIT_COMPLETE levels=" + levels + " players=" + players
                + " sha256=" + before + " sourceUnchanged=true provenance=NOT_PROVEN_BY_SCHEMA");
    }

    private static void describe(String location, CompoundTag player) {
        CompoundTag caps = player.getCompound("ForgeCaps");
        CompoundTag progress = caps.getCompound("foodhealing:shokugi_data");
        System.out.println(ascii("PLAYER entry=" + location + " caps=" + caps.getAllKeys() + " progress=" + progress));
    }

    private static int verifyV3Snapshot(CompoundTag player) {
        CompoundTag input = player.getCompound("ForgeCaps").getCompound("foodhealing:shokugi_data");
        if (input.getInt("FoodHealingDataVersion") != 4) return 0;
        CompoundTag original = input.copy();
        ShokugiData progress = new ShokugiData();
        for (int i = 0; i < 20; i++) {
            progress.deserializeNBT(i == 0 ? input : progress.serializeNBT());
            if (!original.equals(progress.serializeNBT()) || !input.equals(original)) {
                throw new AssertionError("Existing v3 snapshot changed during canonical round-trip");
            }
        }
        return 1;
    }

    private static String ascii(String value) {
        StringBuilder escaped = new StringBuilder();
        for (char c : value.toCharArray()) {
            if (c >= 32 && c <= 126) escaped.append(c);
            else escaped.append(String.format("\\u%04x", (int) c));
        }
        return escaped.toString();
    }

    private static void metadata(CompoundTag tag) {
        System.out.println("MOD_METADATA keys=" + tag.getAllKeys()
                + " loadingMods=" + tag.getList("LoadingModList", Tag.TAG_COMPOUND));
    }

    private static String hash(byte[] bytes) throws Exception {
        return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
    }
}
