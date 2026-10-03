package com.leva.foodhealing;

import com.leva.foodhealing.capability.ShokugiData;
import net.minecraft.nbt.*;

final class LegacyRespecRegression {
    private LegacyRespecRegression() { }

    static void run() {
        for (long level : new long[]{0, 1, 2, 100, 1000, Integer.MAX_VALUE + 1L, Long.MAX_VALUE}) {
            CompoundTag raw = raw(level);
            ShokugiData data = load(raw);
            assertRespec(data, raw);
            CompoundTag expected = data.serializeNBT();
            for (int i = 0; i < 100; i++) {
                data.deserializeNBT(raw);
                require(data.serializeNBT().equals(expected), "raw deserialize added a second refund");
                data.deserializeNBT(expected);
                require(data.serializeNBT().equals(expected), "canonical reload changed refund");
            }
        }
        for (long count : new long[]{0, 199, 1000, Long.MAX_VALUE}) {
            CompoundTag raw = raw(0);
            raw.putLong("EatCount", count);
            if (count < 200) assertRespec(load(raw), raw);
            else {
                ShokugiData invalid = load(raw);
                require(invalid.isLegacyMigrationPending() && invalid.getUnspentSkillPoints()==0
                        && invalid.serializeNBT().getCompound("LegacyV2Backup").equals(raw),
                        "out-of-range old partial count must preserve raw without refund");
            }
        }
        for (int mask = 1; mask < 8; mask++) {
            CompoundTag raw = raw(1000);
            // Synthetic opaque legacy sublevels: never interpreted as conversion keys.
            CompoundTag unknown = new CompoundTag();
            if ((mask & 1) != 0) unknown.putInt("Gathering", 3);
            if ((mask & 2) != 0) unknown.putInt("Unbreaking", 3);
            if ((mask & 4) != 0) unknown.putInt("Pursuit", 9);
            unknown.putIntArray("array", new int[]{1, 2, 9});
            unknown.putLongArray("long-array", new long[]{Long.MIN_VALUE, Long.MAX_VALUE});
            unknown.putByteArray("byte-array", new byte[]{-1, 0, 1});
            raw.put("UnknownSublevels", unknown);
            raw.putLong("RootActiveUntil", 9999);
            raw.putInt("RootReservedNutrition", 18);
            ListTag disabled = new ListTag();
            disabled.add(StringTag.valueOf(FoodHealingSkillIds.GATHERING));
            raw.put("DisabledSkills", disabled);
            ShokugiData data = load(raw);
            assertRespec(data, raw);
            CompoundTag snapshot = data.serializeNBT();
            data.setUnspentSkillPoints(7);
            data.setSkillDisabled(FoodHealingSkillIds.GUTS, true);
            data.deserializeNBT(data.serializeNBT());
            require(data.getUnspentSkillPoints() == 7, "completed migration refunded again");
            require(data.serializeNBT().getCompound("LegacyV2Backup").equals(raw), "v3 overwrote raw evidence");
            snapshot.getCompound("LegacyV2Backup").putString("external", "mutation");
            raw.getCompound("UnknownSublevels").putInt("external", 1);
            require(!data.serializeNBT().getCompound("LegacyV2Backup").contains("external"), "backup alias");
            require(!data.serializeNBT().getCompound("LegacyV2Backup").getCompound("UnknownSublevels")
                    .contains("external"), "input backup alias");
        }
        invalidInputs();
        pendingScaffold();
        ordinaryPurchasesAfterMigration();
        System.out.println("LEGACY_RESPEC_PASS validLong=7 counts=4 sublevelMasks=7 invalid=14 pending=true purchases=5/10/100");
    }

    private static void invalidInputs() {
        for (int variant = 0; variant < 14; variant++) {
            CompoundTag raw = raw(2);
            switch (variant) {
                case 0 -> raw.putLong("ShokugiLevel", -1);
                case 1 -> raw.putString("ShokugiLevel", "2");
                case 2 -> raw.remove("ShokugiLevel");
                case 3 -> raw.putDouble("ShokugiLevel", 2.0);
                case 4 -> raw.putDouble("ShokugiLevel", Double.NaN);
                case 5 -> raw.putDouble("ShokugiLevel", Double.POSITIVE_INFINITY);
                case 6 -> raw.putInt("EatCount", -1);
                case 7 -> raw.remove("EatCount");
                case 8 -> raw.putString("EatCount", "35");
                case 9 -> raw.putInt("FoodHealingDataVersion", 6);
                case 10 -> raw.putString("FoodHealingDataVersion", "4");
                case 11 -> raw.putInt("FoodHealingDataVersion", 3);
                case 12 -> raw.putString("DisabledSkills", "wrong list type");
                case 13 -> {
                    ListTag wrong = new ListTag();
                    wrong.add(IntTag.valueOf(1));
                    raw.put("DisabledSkills", wrong);
                }
            }
            ShokugiData data = load(raw);
            for (int i = 0; i < 3; i++) {
                require(data.isLegacyMigrationPending() && data.getUnspentSkillPoints() == 0,
                        "invalid legacy generated SP/released gate variant=" + variant);
                require(data.serializeNBT().getCompound("LegacyV2Backup").equals(raw), "invalid raw lost");
                for (String skill : FoodHealingSkills.definitions().keySet()) {
                    require(FoodHealingSkills.getEffectiveLevel(data, skill) == 0,
                            "invalid input inferred legacy skill effect variant=" + variant);
                }
                require(FoodHealingSkills.tryPurchase(data, FoodHealingSkillIds.GATHERING)
                        == FoodHealingSkills.PurchaseResult.LEGACY_MIGRATION_PENDING, "invalid purchase gate");
                data.deserializeNBT(data.serializeNBT());
            }
        }
    }

    private static void pendingScaffold() {
        CompoundTag raw = raw(2);
        CompoundTag pending = new ShokugiData().serializeNBT();
        pending.putLong("ShokugiLevel", 2);
        pending.putLong("EatCount", 35);
        pending.putLong("LegacyShokugiLevel", 2);
        pending.putLong("LegacyEatCount", 35);
        pending.putBoolean("LegacyMigrationPending", true);
        pending.put("LegacyV2Backup", raw.copy());
        assertRespec(load(pending), raw);
        pending.putLong("UnspentSkillPoints", 7);
        ShokugiData changed = load(pending);
        require(changed.isLegacyMigrationPending() && changed.getUnspentSkillPoints() == 7,
                "ambiguous edited scaffold must not overwrite existing SP");
        require(changed.serializeNBT().getCompound("LegacyV2Backup").equals(raw), "pending backup lost");
    }

    private static void ordinaryPurchasesAfterMigration() {
        ShokugiData data = load(raw(115));
        require(data.getAcquiredSkills().isEmpty(), "migration auto purchased");
        String[] ids = {FoodHealingSkillIds.GATHERING, FoodHealingSkillIds.UNBREAKING, FoodHealingSkillIds.PURSUIT};
        long[] remaining = {110, 100, 0};
        for (int i = 0; i < ids.length; i++) {
            require(FoodHealingSkills.tryPurchase(data, ids[i], 0) == FoodHealingSkills.PurchaseResult.SUCCESS,
                    "normal purchase after refund rejected");
            require(data.getUnspentSkillPoints() == remaining[i] && data.getSpentSkillPoints() == 115 - remaining[i]
                    && data.getSkillLevel(ids[i]) == 1, "ordinary cost/state mismatch");
        }
        CompoundTag purchased = data.serializeNBT();
        for (int i = 0; i < 10; i++) {
            data.deserializeNBT(data.serializeNBT());
            require(data.serializeNBT().equals(purchased), "purchased migration refunded or reset");
            data.deserializeNBT(raw(115));
            require(data.serializeNBT().equals(purchased), "replayed original raw refunded after purchase");
        }
    }

    private static CompoundTag raw(long level) {
        CompoundTag raw = new CompoundTag();
        raw.putLong("ShokugiLevel", level);
        raw.putInt("EatCount", 35);
        return raw;
    }

    private static ShokugiData load(CompoundTag raw) {
        ShokugiData data = new ShokugiData();
        data.deserializeNBT(raw);
        return data;
    }

    private static void assertRespec(ShokugiData data, CompoundTag raw) {
        require(data.getLevel() == raw.getLong("ShokugiLevel") && data.getEatCount() == raw.getLong("EatCount") * 10
                && data.getUnspentSkillPoints() == raw.getLong("ShokugiLevel") && data.getSpentSkillPoints() == 0
                && data.getAcquiredSkills().isEmpty() && data.getBaseStats().isEmpty()
                && data.getDisabledSkills().isEmpty() && !data.isLegacyMigrationPending(), "respec state mismatch");
        require(data.getRootAccumulatedNutrition() == 0 && data.getRootAccumulationDeadline() == 0
                && data.getRootActiveUntil() == 0 && data.getRootCooldownUntil() == 0 && data.getRootReservedNutrition() == 0,
                "legacy inferred Root state");
        require(data.serializeNBT().getCompound("LegacyV2Backup").equals(raw), "raw evidence mismatch");
        for (String id : FoodHealingSkills.definitions().keySet()) {
            require(FoodHealingSkills.getEffectiveLevel(data, id) == 0, "legacy skill remained active after respec");
        }
    }

    private static void require(boolean value, String message) {
        if (!value) throw new AssertionError(message);
    }
}
