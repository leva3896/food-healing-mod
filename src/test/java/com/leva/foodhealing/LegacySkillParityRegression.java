package com.leva.foodhealing;

import com.leva.foodhealing.capability.ShokugiData;
import com.leva.foodhealing.network.PurchaseSkillPacket;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Arrays;

/** Purchase/accounting tests; native loot, durability and combat run in GameTests. */
final class LegacySkillParityRegression {
    private static int assertions;
    private static final String[] IDS = {FoodHealingSkillIds.GATHERING, FoodHealingSkillIds.UNBREAKING,
            FoodHealingSkillIds.PURSUIT, FoodHealingSkillIds.ARMOR_MASTERY};
    private static final long[][] COSTS = {{5, 5, 5}, {10, 20, 30},
            {100, 100, 100, 100, 100, 100, 100, 100, 100}, {20}};

    static void run() {
        long total = FoodHealingSkills.orderedDefinitions().stream()
                .flatMapToLong(d -> Arrays.stream(d.levelCosts())).sum();
        long existing = FoodHealingSkills.orderedDefinitions().stream().limit(18)
                .flatMapToLong(d -> Arrays.stream(d.levelCosts())).sum();
        require(existing == 1150 && total == 2590, "current post-parity totals; four parity definitions retained");
        for (int k = 0; k < IDS.length; k++) {
            String id = IDS[k];
            long[] costs = COSTS[k];
            var definition = FoodHealingSkills.definitions().get(id);
            require(definition.maxLevel() == costs.length && Arrays.equals(definition.levelCosts(), costs), id + " definition");
            ShokugiData data = new ShokugiData();
            long spent = 0;
            for (int level = 0; level < costs.length; level++) {
                // Round-trip the real purchase wire format, including expected current level.
                FriendlyByteBuf wire = new FriendlyByteBuf(Unpooled.buffer());
                PurchaseSkillPacket request;
                try {
                    new PurchaseSkillPacket(id, level).encode(wire);
                    request = new PurchaseSkillPacket(wire);
                } finally { wire.release(); }
                require(request.skillId().equals(id) && request.expectedLevel() == level, "purchase codec");
                data.setUnspentSkillPoints(costs[level] - 1);
                rejectUnchanged(data, id, level, FoodHealingSkills.PurchaseResult.INSUFFICIENT_SP);
                data.setUnspentSkillPoints(costs[level]);
                rejectUnchanged(data, id, level + 1, FoodHealingSkills.PurchaseResult.STALE_REQUEST);
                rejectUnchanged(data, id, -1, FoodHealingSkills.PurchaseResult.STALE_REQUEST);
                require(FoodHealingSkills.purchaseStatus(data, id, level) == FoodHealingSkills.PurchaseResult.SUCCESS,
                        "GUI preview shares next-level cost");
                require(FoodHealingSkills.tryPurchase(data, request.skillId(), request.expectedLevel())
                        == FoodHealingSkills.PurchaseResult.SUCCESS, "sequential purchase");
                spent += costs[level];
                require(data.getSkillLevel(id) == level + 1 && data.getUnspentSkillPoints() == 0
                        && data.getSpentSkillPoints() == spent, "exact long accounting");
                require(data.isSkillDisabled(id) == (level > 0), "initial ON / upgrade retains OFF");
                data.setUnspentSkillPoints(10000);
                rejectUnchanged(data, id, level, FoodHealingSkills.PurchaseResult.STALE_REQUEST);
                data.setSkillDisabled(id, true);
                CompoundTag saved = data.serializeNBT();
                data = load(saved);
                require(data.serializeNBT().equals(saved) && FoodHealingSkills.getEnabledLevel(data, id) == 0,
                        "level and OFF persistence");
            }
            rejectUnchanged(data, id, costs.length, FoodHealingSkills.PurchaseResult.MAX_LEVEL);
            data.setSkillDisabled(id, false);
            require(FoodHealingSkills.getEnabledLevel(data, id) == costs.length, "maximum level attainable");

            ShokugiData large = new ShokugiData();
            large.setUnspentSkillPoints(Long.MAX_VALUE);
            require(FoodHealingSkills.tryPurchase(large, id, 0) == FoodHealingSkills.PurchaseResult.SUCCESS
                    && large.getUnspentSkillPoints() == Long.MAX_VALUE - costs[0], "no int narrowing");
            ShokugiData overflow = new ShokugiData();
            overflow.setUnspentSkillPoints(Long.MAX_VALUE);
            overflow.setSpentSkillPoints(Long.MAX_VALUE - costs[0] + 1);
            rejectUnchanged(overflow, id, 0, FoodHealingSkills.PurchaseResult.INSUFFICIENT_SP);

            for (int invalidSchema : new int[]{-1, 6, Integer.MAX_VALUE}) {
                CompoundTag raw = new ShokugiData().serializeNBT();
                raw.putInt("FoodHealingDataVersion", invalidSchema);
                raw.putLong("UnspentSkillPoints", 10000);
                ShokugiData invalid = load(raw);
                rejectUnchanged(invalid, id, 0, FoodHealingSkills.PurchaseResult.LEGACY_MIGRATION_PENDING);
                require(FoodHealingSkills.getEffectiveLevel(invalid, id) == 0, "future schema grants no skill");
            }
            CompoundTag pending = new ShokugiData().serializeNBT();
            pending.putBoolean("LegacyMigrationPending", true);
            rejectUnchanged(load(pending), id, 0, FoodHealingSkills.PurchaseResult.LEGACY_MIGRATION_PENDING);
            ShokugiData invalidLevel = new ShokugiData();
            invalidLevel.setSkillLevel(id, costs.length + 1);
            invalidLevel.setUnspentSkillPoints(10000);
            rejectUnchanged(invalidLevel, id, costs.length + 1, FoodHealingSkills.PurchaseResult.MAX_LEVEL);
        }
        existingOwners();
        legacyRespec();
        System.out.println("LegacySkillParityRegression PASS assertions=" + assertions
                + " existingGroup=1150 finiteTotal=2590 schema=5 protocol=7");
    }

    private static void existingOwners() {
        ShokugiData old = new ShokugiData();
        for (String id : IDS) { old.setSkillLevel(id, 1); old.setSkillDisabled(id, true); }
        old.setUnspentSkillPoints(1234);
        old.setSpentSkillPoints(13); // Actual old 2+3+4+4, not retrospectively repriced.
        CompoundTag saved = old.serializeNBT();
        ShokugiData read = load(saved);
        require(read.serializeNBT().equals(saved), "existing v3 owner must not be repriced or level-repaired");
        require(FoodHealingSkills.tryPurchase(read, IDS[0], 1) == FoodHealingSkills.PurchaseResult.SUCCESS,
                "existing owner may buy next level at new price");
        require(read.getSpentSkillPoints() == 18 && read.getUnspentSkillPoints() == 1229
                && read.getSkillLevel(IDS[0]) == 2 && read.isSkillDisabled(IDS[0]), "prospective cost only");
    }

    private static void legacyRespec() {
        for (int level : new int[]{16, 19, 999}) {
            CompoundTag raw = new CompoundTag();
            raw.putInt("ShokugiLevel", level); raw.putInt("EatCount", 0);
            ShokugiData data = load(raw);
            require(data.getUnspentSkillPoints() == level && data.getSpentSkillPoints() == 0
                    && data.getAcquiredSkills().isEmpty(), "v2 full refund, no inferred stages");
            require(data.serializeNBT().getCompound("LegacyV2Backup").equals(raw), "immutable legacy evidence");
            CompoundTag saved = data.serializeNBT();
            data.deserializeNBT(raw);
            require(data.serializeNBT().equals(saved), "exactly once raw replay");
            require(load(saved).serializeNBT().equals(saved), "respec persistence");
        }
    }

    private static ShokugiData load(CompoundTag nbt) {
        ShokugiData result = new ShokugiData(); result.deserializeNBT(nbt); return result;
    }

    private static void rejectUnchanged(ShokugiData data, String id, int expected,
                                        FoodHealingSkills.PurchaseResult reason) {
        CompoundTag before = data.serializeNBT();
        require(FoodHealingSkills.tryPurchase(data, id, expected) == reason, "reject " + id + ": " + reason);
        require(before.equals(data.serializeNBT()), "rejected purchase mutated canonical/SP");
    }

    private static void require(boolean value, String message) {
        assertions++;
        if (!value) throw new AssertionError(message);
    }
}
