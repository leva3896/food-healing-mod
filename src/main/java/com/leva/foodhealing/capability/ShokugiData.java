package com.leva.foodhealing.capability;

import com.leva.foodhealing.FoodHealingBaseStatIds;
import com.leva.foodhealing.FoodHealingSkillIds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ShokugiData implements IShokugiData {
    private static final int CURRENT_SCHEMA_VERSION = 5;

    private CompoundTag countMigrationInput = new CompoundTag();
    private String countMigrationError = "";
    private long level;
    private long eatCount;
    private long unspentSkillPoints;
    private long spentSkillPoints;
    private final Map<String, Integer> acquiredSkills = new HashMap<>();
    // Preserve malformed Flight entries in their existing schema location; never coerce them into ownership.
    private ListTag invalidFlightEntries = new ListTag();

    @Override
    public boolean isFlightAcquisitionValid() { return invalidFlightEntries.isEmpty(); }
    private final Map<String, Long> baseStats = new HashMap<>();
    private Set<String> disabledSkills = new HashSet<>();

    private boolean legacyMigrationPending;
    private long legacyShokugiLevel;
    private long legacyEatCount;
    private CompoundTag legacyV2Backup = new CompoundTag();
    private int rootAccumulatedNutrition;
    private long rootAccumulationDeadline;
    private long rootActiveUntil;
    private long rootCooldownUntil;
    private int rootReservedNutrition;

    @Override
    public long getLevel() {
        return level;
    }

    @Override
    public void setLevel(long level) {
        this.level = Math.max(0L, level);
    }

    @Override
    public void addLevel(long amount) {
        this.level = saturatedAdd(this.level, amount);
    }

    @Override
    public long getEatCount() {
        return eatCount;
    }

    @Override
    public void setEatCount(long count) {
        this.eatCount = Math.max(0L, count);
    }

    @Override
    public void addEatCount(long amount) {
        this.eatCount = saturatedAdd(this.eatCount, amount);
    }

    @Override
    public boolean addNutritionUnits(long units, long threshold) {
        if (legacyMigrationPending || !countMigrationError.isEmpty()) return false;
        final com.leva.foodhealing.NutritionProgress.Update update;
        try {
            update = com.leva.foodhealing.NutritionProgress.add(eatCount, level, unspentSkillPoints, units, threshold);
        } catch (IllegalArgumentException | ArithmeticException invalid) {
            com.mojang.logging.LogUtils.getLogger().warn("[FoodHealing] Rejected atomic Nutrition progression: {}", invalid.getMessage());
            return false;
        }
        eatCount = update.count(); level = update.level(); unspentSkillPoints = update.unspent();
        return true;
    }

    @Override
    public long getUnspentSkillPoints() {
        return unspentSkillPoints;
    }

    @Override
    public void setUnspentSkillPoints(long points) {
        this.unspentSkillPoints = Math.max(0L, points);
    }

    @Override
    public void addUnspentSkillPoints(long points) {
        this.unspentSkillPoints = saturatedAdd(this.unspentSkillPoints, points);
    }

    @Override
    public long getSpentSkillPoints() {
        return spentSkillPoints;
    }

    @Override
    public void setSpentSkillPoints(long points) {
        this.spentSkillPoints = Math.max(0L, points);
    }

    @Override
    public boolean trySpendSkillPoints(long points) {
        if (points <= 0L || unspentSkillPoints < points || spentSkillPoints > Long.MAX_VALUE - points) {
            return false;
        }
        unspentSkillPoints -= points;
        spentSkillPoints += points;
        return true;
    }

    @Override
    public int getSkillLevel(String skillId) {
        return acquiredSkills.getOrDefault(FoodHealingSkillIds.normalize(skillId), 0);
    }

    @Override
    public void setSkillLevel(String skillId, int skillLevel) {
        String normalized = FoodHealingSkillIds.normalize(skillId);
        if (normalized.isEmpty()) {
            return;
        }
        if (skillLevel <= 0) {
            acquiredSkills.remove(normalized);
        } else {
            acquiredSkills.put(normalized, skillLevel);
        }
    }

    @Override
    public Map<String, Integer> getAcquiredSkills() {
        return Map.copyOf(acquiredSkills);
    }

    @Override
    public long getBaseStatPoints(String statId) {
        return baseStats.getOrDefault(statId, 0L);
    }

    @Override
    public void setBaseStatPoints(String statId, long points) {
        if (!FoodHealingBaseStatIds.ALL.contains(statId)) {
            return;
        }
        if (points <= 0L) {
            baseStats.remove(statId);
        } else {
            baseStats.put(statId, points);
        }
    }

    @Override
    public Map<String, Long> getBaseStats() {
        return Map.copyOf(baseStats);
    }

    @Override
    public boolean isLegacyMigrationPending() {
        return legacyMigrationPending;
    }

    @Override
    public long getLegacyShokugiLevel() {
        return legacyShokugiLevel;
    }

    @Override
    public long getLegacyEatCount() {
        return legacyEatCount;
    }

    @Override
    public int getRootAccumulatedNutrition() {
        return rootAccumulatedNutrition;
    }

    @Override
    public void setRootAccumulatedNutrition(int nutrition) {
        rootAccumulatedNutrition = Math.max(0, Math.min(18, nutrition));
    }

    @Override
    public long getRootAccumulationDeadline() {
        return rootAccumulationDeadline;
    }

    @Override
    public void setRootAccumulationDeadline(long gameTime) {
        rootAccumulationDeadline = Math.max(0L, gameTime);
    }

    @Override
    public long getRootActiveUntil() {
        return rootActiveUntil;
    }

    @Override
    public void setRootActiveUntil(long gameTime) {
        rootActiveUntil = Math.max(0L, gameTime);
    }

    @Override
    public long getRootCooldownUntil() {
        return rootCooldownUntil;
    }

    @Override
    public void setRootCooldownUntil(long gameTime) {
        rootCooldownUntil = Math.max(0L, gameTime);
    }

    @Override
    public int getRootReservedNutrition() {
        return rootReservedNutrition;
    }

    @Override
    public void setRootReservedNutrition(int nutrition) {
        rootReservedNutrition = Math.max(0, Math.min(18, nutrition));
    }

    @Override
    public Set<String> getDisabledSkills() {
        return Set.copyOf(disabledSkills);
    }

    @Override
    public void setDisabledSkills(Set<String> skills) {
        disabledSkills = FoodHealingSkillIds.normalizeAll(skills);
    }

    @Override
    public boolean isSkillDisabled(String skillId) {
        return disabledSkills.contains(FoodHealingSkillIds.normalize(skillId));
    }

    @Override
    public void setSkillDisabled(String skillId, boolean disabled) {
        String normalized = FoodHealingSkillIds.normalize(skillId);
        if (normalized.isEmpty()) {
            return;
        }
        if (disabled) {
            disabledSkills.add(normalized);
        } else {
            disabledSkills.remove(normalized);
        }
    }

    @Override
    public void toggleSkill(String skillId) {
        String normalized = FoodHealingSkillIds.normalize(skillId);
        if (normalized.isEmpty()) {
            return;
        }
        setSkillDisabled(normalized, !isSkillDisabled(normalized));
    }

    @Override
    public void copyFrom(IShokugiData source) {
        deserializeNBT(source.serializeNBT());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("FoodHealingDataVersion", CURRENT_SCHEMA_VERSION);
        if (!countMigrationError.isEmpty()) {
            nbt.putString("CountMigrationError", countMigrationError);
            nbt.put("CountMigrationInput", countMigrationInput.copy());
        }
        nbt.putLong("ShokugiLevel", level);
        nbt.putLong("EatCount", eatCount);
        nbt.putLong("UnspentSkillPoints", unspentSkillPoints);
        nbt.putLong("SpentSkillPoints", spentSkillPoints);

        ListTag acquiredList = new ListTag();
        acquiredSkills.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
            if (entry.getKey().equals(FoodHealingSkillIds.FLIGHT) && !invalidFlightEntries.isEmpty()) return;
            CompoundTag skill = new CompoundTag();
            skill.putString("Id", entry.getKey());
            skill.putInt("Level", entry.getValue());
            acquiredList.add(skill);
        });
        invalidFlightEntries.forEach(entry -> acquiredList.add(entry.copy()));
        nbt.put("AcquiredSkills", acquiredList);

        CompoundTag stats = new CompoundTag();
        baseStats.forEach(stats::putLong);
        nbt.put("BaseStats", stats);

        ListTag disabledList = new ListTag();
        disabledSkills.stream().sorted().forEach(skill -> disabledList.add(StringTag.valueOf(skill)));
        nbt.put("DisabledSkills", disabledList);

        nbt.putBoolean("LegacyMigrationPending", legacyMigrationPending);
        nbt.putLong("LegacyShokugiLevel", legacyShokugiLevel);
        nbt.putLong("LegacyEatCount", legacyEatCount);
        nbt.putInt("RootAccumulatedNutrition", rootAccumulatedNutrition);
        nbt.putLong("RootAccumulationDeadline", rootAccumulationDeadline);
        nbt.putLong("RootActiveUntil", rootActiveUntil);
        nbt.putLong("RootCooldownUntil", rootCooldownUntil);
        nbt.putInt("RootReservedNutrition", rootReservedNutrition);
        if (legacyMigrationPending || !legacyV2Backup.isEmpty()) {
            nbt.put("LegacyV2Backup", legacyV2Backup.copy());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        int sourceVersion = nbt.getInt("FoodHealingDataVersion");
        boolean currentSchema = nbt.contains("FoodHealingDataVersion", Tag.TAG_INT)
                && (sourceVersion == CURRENT_SCHEMA_VERSION || sourceVersion == 4);
        if (!currentSchema && !legacyMigrationPending && validLegacyProgress(nbt)
                && legacyV2Backup.equals(nbt)) {
            // The same provider may receive the old payload twice; do not undo later purchases.
            return;
        }
        countMigrationError = nbt.getString("CountMigrationError");
        countMigrationInput = nbt.getCompound("CountMigrationInput").copy();
        boolean convertedLegacy = false;
        level = readNonNegativeLong(nbt, "ShokugiLevel");
        eatCount = readNonNegativeLong(nbt, "EatCount");
        unspentSkillPoints = readNonNegativeLong(nbt, "UnspentSkillPoints");
        spentSkillPoints = readNonNegativeLong(nbt, "SpentSkillPoints");

        acquiredSkills.clear();
        ListTag acquiredList = nbt.getList("AcquiredSkills", Tag.TAG_COMPOUND);
        invalidFlightEntries = new ListTag();
        ListTag flightEntries = new ListTag();
        boolean invalidFlight = false;
        for (int i = 0; i < acquiredList.size(); i++) {
            CompoundTag skill = acquiredList.getCompound(i);
            String skillId = FoodHealingSkillIds.normalize(skill.getString("Id"));
            if (skillId.equals(FoodHealingSkillIds.FLIGHT)) {
                flightEntries.add(skill.copy());
                invalidFlight |= !skill.contains("Level", Tag.TAG_INT) || skill.getInt("Level") != 1;
            }
            int skillLevel = Math.max(0, skill.getInt("Level"));
            if (!skillId.isEmpty() && skillLevel > 0) {
                acquiredSkills.put(skillId, skillLevel);
            }
        }
        if (invalidFlight || flightEntries.size() > 1) invalidFlightEntries = flightEntries;

        baseStats.clear();
        CompoundTag stats = nbt.getCompound("BaseStats");
        for (String statId : FoodHealingBaseStatIds.ALL) {
            long points = readNonNegativeLong(stats, statId);
            if (points > 0L) {
                baseStats.put(statId, points);
            }
        }

        disabledSkills.clear();
        ListTag disabledList = nbt.getList("DisabledSkills", Tag.TAG_STRING);
        for (int i = 0; i < disabledList.size(); i++) {
            String skillId = FoodHealingSkillIds.normalize(disabledList.getString(i));
            if (!skillId.isEmpty()) {
                disabledSkills.add(skillId);
            }
        }

        legacyMigrationPending = !currentSchema || nbt.getBoolean("LegacyMigrationPending");
        legacyShokugiLevel = nbt.contains("LegacyShokugiLevel", Tag.TAG_ANY_NUMERIC)
                ? readNonNegativeLong(nbt, "LegacyShokugiLevel")
                : (legacyMigrationPending ? level : 0L);
        legacyEatCount = nbt.contains("LegacyEatCount", Tag.TAG_ANY_NUMERIC)
                ? readNonNegativeLong(nbt, "LegacyEatCount")
                : (legacyMigrationPending ? eatCount : 0L);

        rootAccumulatedNutrition = Math.max(0, Math.min(18, nbt.getInt("RootAccumulatedNutrition")));
        rootAccumulationDeadline = readNonNegativeLong(nbt, "RootAccumulationDeadline");
        rootActiveUntil = readNonNegativeLong(nbt, "RootActiveUntil");
        rootCooldownUntil = readNonNegativeLong(nbt, "RootCooldownUntil");
        rootReservedNutrition = Math.max(0, Math.min(18, nbt.getInt("RootReservedNutrition")));

        if (!currentSchema) {
            // An unversioned v2 compound is evidence in full, including unknown/nested fields.
            legacyV2Backup = nbt.copy();
        } else if (nbt.contains("LegacyV2Backup", Tag.TAG_COMPOUND)) {
            legacyV2Backup = nbt.getCompound("LegacyV2Backup").copy();
        } else if (legacyMigrationPending) {
            legacyV2Backup = nbt.copy();
        } else {
            legacyV2Backup = new CompoundTag();
        }

        if (!currentSchema) {
            unspentSkillPoints = 0L;
            spentSkillPoints = 0L;
            acquiredSkills.clear();
            baseStats.clear();
            clearRootState();
        }
        if (legacyMigrationPending && !validLegacyProgress(legacyV2Backup)) {
            // Pending is a purchase gate, not evidence that an unknown schema owns v2 effects.
            legacyShokugiLevel = 0L;
            legacyEatCount = 0L;
        }
        if (legacyMigrationPending && validLegacyProgress(legacyV2Backup)
                && (!currentSchema || isUnchangedPendingScaffold(nbt))) {
            // 2026-09-08 user-approved full respec. Assignment, never an additive refund.
            level = legacyV2Backup.getLong("ShokugiLevel");
            eatCount = legacyV2Backup.getLong("EatCount");
            legacyShokugiLevel = level;
            legacyEatCount = eatCount;
            unspentSkillPoints = level;
            spentSkillPoints = 0L;
            acquiredSkills.clear();
            baseStats.clear();
            disabledSkills.clear();
            clearRootState();
            legacyMigrationPending = false;
            convertedLegacy = true;
        }
        if ((sourceVersion == 4 || convertedLegacy) && countMigrationError.isEmpty()) {
            try {
                if (legacyMigrationPending || !nonNegativeIntegral(nbt, "EatCount"))
                    throw new IllegalArgumentException("Unknown/pending count provenance or type");
                eatCount = com.leva.foodhealing.NutritionProgress.convert(eatCount,
                        com.leva.foodhealing.FoodHealingConfig.legacyCountThreshold(),
                        com.leva.foodhealing.FoodHealingConfig.nutritionThreshold());
            } catch (IllegalArgumentException | ArithmeticException invalid) {
                countMigrationInput = nbt.copy();
                countMigrationError = invalid.getMessage();
                legacyMigrationPending = true;
                com.mojang.logging.LogUtils.getLogger().warn("[FoodHealing] Count migration pending; raw retained: {}", countMigrationError);
            }
        }
        if (!countMigrationError.isEmpty()) legacyMigrationPending = true;
    }

    private boolean isUnchangedPendingScaffold(CompoundTag canonical) {
        // Do not overwrite v3 edits made while migration was pending or guess how to merge them.
        return nonNegativeIntegral(canonical, "ShokugiLevel") && nonNegativeIntegral(canonical, "EatCount")
                && nonNegativeIntegral(canonical, "UnspentSkillPoints") && nonNegativeIntegral(canonical, "SpentSkillPoints")
                && canonical.get("AcquiredSkills") instanceof ListTag list && list.isEmpty()
                && canonical.contains("BaseStats", Tag.TAG_COMPOUND) && canonical.getCompound("BaseStats").isEmpty()
                && level == legacyV2Backup.getLong("ShokugiLevel")
                && eatCount == legacyV2Backup.getLong("EatCount")
                && unspentSkillPoints == 0L && spentSkillPoints == 0L
                && acquiredSkills.isEmpty() && baseStats.isEmpty();
    }

    private static boolean validLegacyProgress(CompoundTag raw) {
        return !raw.contains("FoodHealingDataVersion")
                && nonNegativeIntegral(raw, "ShokugiLevel") && nonNegativeIntegral(raw, "EatCount")
                && convertibleLegacyCount(raw.getLong("EatCount"))
                && (!raw.contains("DisabledSkills") || raw.get("DisabledSkills") instanceof ListTag list
                && (list.isEmpty() || list.getElementType() == Tag.TAG_STRING));
    }

    private static boolean convertibleLegacyCount(long count) {
        try {
            com.leva.foodhealing.NutritionProgress.convert(count,
                com.leva.foodhealing.FoodHealingConfig.legacyCountThreshold(),
                com.leva.foodhealing.FoodHealingConfig.nutritionThreshold());
            return true;
        } catch (IllegalArgumentException | ArithmeticException invalid) { return false; }
    }

    private static boolean nonNegativeIntegral(CompoundTag raw, String key) {
        Tag value = raw.get(key);
        return value != null && value.getId() >= Tag.TAG_BYTE && value.getId() <= Tag.TAG_LONG
                && raw.getLong(key) >= 0L;
    }

    private void clearRootState() {
        rootAccumulatedNutrition = 0;
        rootAccumulationDeadline = 0L;
        rootActiveUntil = 0L;
        rootCooldownUntil = 0L;
        rootReservedNutrition = 0;
    }

    private static long readNonNegativeLong(CompoundTag tag, String key) {
        return Math.max(0L, tag.getLong(key));
    }

    private static long saturatedAdd(long current, long amount) {
        if (amount <= 0L) {
            return current;
        }
        return current > Long.MAX_VALUE - amount ? Long.MAX_VALUE : current + amount;
    }
}
