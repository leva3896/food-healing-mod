package com.leva.foodhealing;

import com.leva.foodhealing.capability.ShokugiData;
import com.leva.foodhealing.compat.SuperbWarfareCompat;
import net.minecraft.nbt.CompoundTag;

public final class SuperbWarfareRegression {
    private static int assertions;

    public static void main(String[] args) { run(); }

    public static void run() {
        assertions = 0;
        for (long level : new long[]{0, 1, 200, Integer.MAX_VALUE, (long) Integer.MAX_VALUE + 1, Long.MAX_VALUE}) {
            ShokugiData fresh = new ShokugiData();
            fresh.setLevel(level);
            fresh.setBaseStatPoints(FoodHealingBaseStatIds.TACZ_BASE_OUTGOING_DAMAGE, 999);
            fresh.setUnspentSkillPoints(300);
            CompoundTag before = fresh.serializeNBT();
            double factor = SuperbWarfareCompat.shokugiMultiplier(fresh);
            require(factor == 1D + (double) level && Double.isFinite(factor), "fresh factor at " + level);
            require(before.equals(fresh.serializeNBT()), "factor mutated canonical/SP");
            ShokugiData copy = new ShokugiData();
            copy.deserializeNBT(before);
            require(SuperbWarfareCompat.shokugiMultiplier(copy) == factor, "canonical round trip");
            CompoundTag raw = new CompoundTag();
            raw.putLong("ShokugiLevel", level);
            raw.putLong("EatCount", 3);
            ShokugiData migrated = new ShokugiData();
            migrated.deserializeNBT(raw);
            require(!migrated.isLegacyMigrationPending(), "valid respec did not complete");
            require(SuperbWarfareCompat.shokugiMultiplier(migrated) == factor, "migrated factor");
            require(migrated.serializeNBT().getCompound("LegacyV2Backup").equals(raw), "raw evidence changed");
        }
        ShokugiData malformed = new ShokugiData();
        CompoundTag bad = new CompoundTag();
        bad.putInt("FoodHealingDataVersion", 99);
        bad.putLong("ShokugiLevel", 200);
        bad.putLong("LegacyShokugiLevel", 999);
        malformed.deserializeNBT(bad);
        require(malformed.isLegacyMigrationPending(), "unknown version escaped pending");
        require(SuperbWarfareCompat.shokugiMultiplier(malformed) == 1D, "unvalidated current level enabled bonus");

        CompoundTag raw = new CompoundTag();
        raw.putLong("ShokugiLevel", 2);
        raw.putLong("EatCount", 3);
        ShokugiData edited = new ShokugiData();
        edited.deserializeNBT(raw);
        CompoundTag pending = edited.serializeNBT();
        pending.putBoolean("LegacyMigrationPending", true);
        pending.putLong("ShokugiLevel", 200);
        edited.deserializeNBT(pending);
        require(edited.isLegacyMigrationPending(), "edited pending silently respeced");
        require(SuperbWarfareCompat.shokugiMultiplier(edited) == 3D, "pending must use validated legacy only");
        require(edited.serializeNBT().getCompound("LegacyV2Backup").equals(raw), "pending backup changed");

        require(FoodHealingBaseStats.multiplyPositiveDamage(Float.MAX_VALUE, 201D) == Float.MAX_VALUE, "overflow");
        require(FoodHealingBaseStats.multiplyPositiveDamage(Float.NaN, 201D) == 0F, "NaN");
        require(FoodHealingBaseStats.multiplyPositiveDamage(-1F, 201D) == 0F, "negative");
        require(FoodHealingBaseStats.multiplyPositiveDamage(0F, 201D) == 0F, "zero");
        require(FoodHealingBaseStats.multiplyPositiveDamage(Float.MIN_VALUE, 201D) > 0F, "positive underflow");
        require(FoodHealingBaseStats.multiplyPositiveDamage(Float.POSITIVE_INFINITY, 201D) == Float.MAX_VALUE, "infinity");
        System.out.println("SUPERBWARFARE_UNIT_PASS assertions=" + assertions);
    }

    private static void require(boolean valid, String message) {
        assertions++;
        if (!valid) throw new AssertionError(message);
    }
}
