package com.leva.foodhealing;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.toml.TomlParser;
import com.electronwill.nightconfig.toml.TomlWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/** Runs before Forge can insert defaults. Config and player schemas have separate lifetimes. */
public final class NutritionConfigMigration {
    public static final String THRESHOLD = "general.shokugiNutritionLevelUpRequirement";
    public static final String MODEL = "general.shokugiCountModelVersion";
    public static final String LEGACY_INPUT = "general.legacyFoodActionThreshold";
    private static final String OLD = "general.shokugiLevelUpRequirement";

    private NutritionConfigMigration() { }

    public static void prepare(Path path) throws IOException {
        boolean existed = Files.exists(path);
        byte[] original = existed ? Files.readAllBytes(path) : new byte[0];
        CommentedConfig data = new TomlParser().parse(new StringReader(
                new String(original, java.nio.charset.StandardCharsets.UTF_8)));
        if (data.contains(MODEL)) {
            if (integral(data.get(MODEL)) != 1L)
                throw new IllegalArgumentException("Unknown count Config model; original file retained");
            positive(data.get(THRESHOLD));
            if (positive(data.get(LEGACY_INPUT)) > 1_000_000L)
                throw new IllegalArgumentException("Invalid frozen legacy Config threshold; original retained");
            return;
        }
        long legacy = data.contains(OLD) ? positive(data.get(OLD)) : 200L;
        if (legacy > 1_000_000L) throw new IllegalArgumentException("Invalid legacy Config threshold");
        long next = data.contains(THRESHOLD) ? positive(data.get(THRESHOLD)) : Math.multiplyExact(legacy, 10L);
        data.set(THRESHOLD, next);
        data.set(LEGACY_INPUT, legacy);
        data.set(MODEL, 1);
        data.setComment(THRESHOLD, "Nutrition/Food-Level units per level and SP; default 2000.");
        data.setComment(LEGACY_INPUT, "Frozen effective legacy threshold at first migration, not historical player metadata.");
        StringWriter text = new StringWriter();
        new TomlWriter().write(data, text);
        Files.createDirectories(path.toAbsolutePath().getParent());
        if (existed) {
            Path backup = path.resolveSibling(path.getFileName() + ".before-nutrition-count");
            if (!Files.exists(backup)) Files.copy(path, backup);
            if (!java.util.Arrays.equals(original, Files.readAllBytes(path)))
                throw new IOException("Config changed during count migration");
        }
        Path temp = Files.createTempFile(path.toAbsolutePath().getParent(), "foodhealing-count-", ".tmp");
        try {
            Files.writeString(temp, text.toString(), java.nio.charset.StandardCharsets.UTF_8);
            // No non-atomic fallback: don't expose partially migrated Config to Forge or next boot.
            Files.move(temp, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temp);
        }
    }

    private static long integral(Object value) {
        if (!(value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long))
            throw new IllegalArgumentException("Count Config must be integral; original retained");
        return ((Number) value).longValue();
    }

    private static long positive(Object value) {
        long n = integral(value);
        if (n <= 0) throw new IllegalArgumentException("Count Config must be positive; original retained");
        return n;
    }
}
