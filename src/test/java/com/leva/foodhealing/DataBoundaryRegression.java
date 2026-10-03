package com.leva.foodhealing;

import com.leva.foodhealing.capability.ShokugiData;
import com.leva.foodhealing.network.PurchaseBaseStatPacket;
import com.leva.foodhealing.network.PurchaseSkillPacket;
import com.leva.foodhealing.network.ToggleSkillPacket;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

final class DataBoundaryRegression {
    private DataBoundaryRegression() {
    }

    static void run() {
        long started = System.nanoTime();
        Random random = new Random(0xF0032026L);
        for (int i = 0; i < 5000; i++) {
            long unspent = boundary(random, i);
            long spent = boundary(random, i + 1);
            long cost = boundary(random, i + 2);
            ShokugiData data = new ShokugiData();
            data.setLevel(boundary(random, i + 3));
            data.setEatCount(boundary(random, i + 4));
            data.setUnspentSkillPoints(unspent);
            data.setSpentSkillPoints(spent);
            data.setSkillLevel(FoodHealingSkillIds.GUTS, i % 5 + 1);
            data.setSkillDisabled(FoodHealingSkillIds.GUTS, i % 2 == 0);
            data.setBaseStatPoints(FoodHealingBaseStatIds.BASE_DEFENSE, boundary(random, i + 5));
            data.setRootReservedNutrition(i % 19);
            data.setRootActiveUntil(boundary(random, i + 6));
            CompoundTag before = data.serializeNBT();
            boolean expected = cost > 0 && cost <= unspent
                    && BigInteger.valueOf(spent).add(BigInteger.valueOf(cost))
                    .compareTo(BigInteger.valueOf(Long.MAX_VALUE)) <= 0;
            require(data.trySpendSkillPoints(cost) == expected, "SP transaction acceptance mismatch");
            if (expected) {
                before.putLong("UnspentSkillPoints", unspent - cost);
                before.putLong("SpentSkillPoints", spent + cost);
            }
            require(data.serializeNBT().equals(before), "SP transaction partially changed unrelated data");
            ShokugiData restored = new ShokugiData();
            restored.deserializeNBT(before);
            require(restored.serializeNBT().equals(before), "long NBT round-trip changed progression");
            restored.deserializeNBT(restored.serializeNBT());
            require(restored.serializeNBT().equals(before), "repeated load changed progression");
        }
        malformedNbtIsStable();
        legacyZeroAndMissingFieldsRetainRawBackup();
        packetBoundaries();
        System.out.println("DATA_BOUNDARY_PASS seed=F0032026 cases=5000 elapsedMs="
                + (System.nanoTime() - started) / 1_000_000);
    }

    private static long boundary(Random random, int index) {
        return switch (index % 7) {
            case 0 -> 0;
            case 1 -> Long.MAX_VALUE;
            case 2 -> Long.MAX_VALUE - 1;
            case 3 -> Integer.MAX_VALUE + 1L;
            default -> random.nextLong() & Long.MAX_VALUE;
        };
    }

    private static void malformedNbtIsStable() {
        for (double input : new double[]{Double.NaN, Double.NEGATIVE_INFINITY, -1, Double.POSITIVE_INFINITY}) {
            CompoundTag raw = new CompoundTag();
            raw.putInt("FoodHealingDataVersion", 4);
            for (String key : List.of("ShokugiLevel", "EatCount", "UnspentSkillPoints", "SpentSkillPoints")) {
                raw.putDouble(key, input);
            }
            raw.putString("AcquiredSkills", "wrong type");
            raw.putString("BaseStats", "wrong type");
            raw.putLong("RootReservedNutrition", Long.MIN_VALUE);
            ShokugiData data = new ShokugiData();
            data.deserializeNBT(raw);
            require(data.getLevel() >= 0 && data.getEatCount() >= 0 && data.getUnspentSkillPoints() >= 0
                    && data.getSpentSkillPoints() >= 0, "malformed NBT escaped nonnegative long boundary");
            CompoundTag sanitized = data.serializeNBT();
            data.deserializeNBT(sanitized);
            require(data.serializeNBT().equals(sanitized), "malformed NBT normalization is unstable");
        }
        CompoundTag raw = new CompoundTag();
        raw.putInt("ShokugiLevel", 1000);
        raw.putInt("EatCount", 199);
        raw.putString("UnknownLegacyField", "preserve, do not convert");
        ShokugiData data = new ShokugiData();
        data.deserializeNBT(raw);
        CompoundTag expected = data.serializeNBT();
        raw.putString("UnknownLegacyField", "mutated outside capability");
        for (int i = 0; i < 1000; i++) {
            CompoundTag output = data.serializeNBT();
            require(output.equals(expected), "legacy backup changed over repeated saves");
            data.deserializeNBT(output);
            output.getCompound("LegacyV2Backup").putString("UnknownLegacyField", "outside mutation");
            require(data.getUnspentSkillPoints() == 1000 && !data.isLegacyMigrationPending(),
                    "legacy respec refund changed during repeated saves");
        }
    }

    private static void legacyZeroAndMissingFieldsRetainRawBackup() {
        for (int variant = 0; variant < 5; variant++) {
            CompoundTag raw = new CompoundTag();
            if (variant == 0) raw.putInt("ShokugiLevel", 0);
            if (variant == 1) raw.putInt("ShokugiLevel", -5);
            if (variant == 2) raw.putString("ShokugiLevel", "malformed legacy level");
            if (variant == 4) {
                raw.putInt("FoodHealingDataVersion", 3);
                raw.putBoolean("LegacyMigrationPending", false);
            }
            raw.putInt("EatCount", 199);
            CompoundTag unknown = new CompoundTag();
            unknown.putIntArray("unmapped-sublevel-fixture", new int[]{2, 3, 4});
            unknown.putString("note", "retain only; no OPEN-05 conversion");
            raw.put("UnknownLegacyFields", unknown);
            CompoundTag original = raw.copy();
            ShokugiData data = new ShokugiData();
            data.deserializeNBT(raw);
            for (int i = 0; i < 10; i++) {
                CompoundTag output = data.serializeNBT();
                require(output.getCompound("LegacyV2Backup").equals(original),
                        "zero/missing legacy level lost raw backup variant=" + variant);
                require(data.getLevel() == 0 && data.getEatCount() == (variant == 0 ? 1990 : 199)
                                && data.getUnspentSkillPoints() == 0 && data.getSpentSkillPoints() == 0
                                && data.getAcquiredSkills().isEmpty() && data.getBaseStats().isEmpty()
                                && data.isLegacyMigrationPending() == (variant != 0),
                        "valid zero must complete; malformed/unsupported legacy must retain the safety gate");
                data.deserializeNBT(output);
                output.getCompound("LegacyV2Backup").putString("external mutation", "must not leak");
            }
        }
    }

    private static void packetBoundaries() {
        for (long value : new long[]{0, Integer.MAX_VALUE + 1L, Long.MAX_VALUE, -1, Long.MIN_VALUE}) {
            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
            try {
                PurchaseBaseStatPacket original = new PurchaseBaseStatPacket(FoodHealingBaseStatIds.BASE_DEFENSE, value);
                original.encode(buffer);
                require(original.equals(new PurchaseBaseStatPacket(buffer)), "long packet lost precision");
            } finally { buffer.release(); }
        }
        for (Function<FriendlyByteBuf, ?> decoder : List.<Function<FriendlyByteBuf, ?>>of(
                PurchaseSkillPacket::new, PurchaseBaseStatPacket::new, ToggleSkillPacket::new)) {
            FriendlyByteBuf oversized = new FriendlyByteBuf(Unpooled.buffer());
            FriendlyByteBuf truncated = new FriendlyByteBuf(Unpooled.buffer());
            try {
                oversized.writeUtf("x".repeat(129));
                oversized.writeLong(0);
                rejected(() -> decoder.apply(oversized), "oversized identifier accepted");
                truncated.writeUtf(FoodHealingSkillIds.GUTS);
                rejected(() -> decoder.apply(truncated), "truncated packet accepted");
            } finally { oversized.release(); truncated.release(); }
        }
        FriendlyByteBuf malformed = new FriendlyByteBuf(Unpooled.buffer());
        try {
            malformed.writeUtf(FoodHealingBaseStatIds.BASE_DEFENSE);
            for (int i = 0; i < 11; i++) malformed.writeByte(0x80);
            rejected(() -> new PurchaseBaseStatPacket(malformed), "overlong VarLong accepted");
        } finally { malformed.release(); }

    }

    private static void rejected(Runnable operation, String message) {
        try { operation.run(); } catch (RuntimeException expected) { return; }
        throw new AssertionError(message);
    }

    private static void require(boolean value, String message) {
        if (!value) throw new AssertionError(message);
    }
}
