package com.leva.foodhealing.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IShokugiData extends INBTSerializable<CompoundTag> {
    long getLevel();
    void setLevel(long level);
    void addLevel(long amount);

    long getEatCount();
    void setEatCount(long count);
    void addEatCount(long amount);
    boolean addNutritionUnits(long units, long threshold);

    long getUnspentSkillPoints();
    void setUnspentSkillPoints(long points);
    void addUnspentSkillPoints(long points);

    long getSpentSkillPoints();
    void setSpentSkillPoints(long points);
    boolean trySpendSkillPoints(long points);

    int getSkillLevel(String skillId);
    void setSkillLevel(String skillId, int level);
    java.util.Map<String, Integer> getAcquiredSkills();
    boolean isFlightAcquisitionValid();

    long getBaseStatPoints(String statId);
    void setBaseStatPoints(String statId, long points);
    java.util.Map<String, Long> getBaseStats();

    boolean isLegacyMigrationPending();
    long getLegacyShokugiLevel();
    long getLegacyEatCount();

    int getRootAccumulatedNutrition();
    void setRootAccumulatedNutrition(int nutrition);
    long getRootAccumulationDeadline();
    void setRootAccumulationDeadline(long gameTime);
    long getRootActiveUntil();
    void setRootActiveUntil(long gameTime);
    long getRootCooldownUntil();
    void setRootCooldownUntil(long gameTime);
    int getRootReservedNutrition();
    void setRootReservedNutrition(int nutrition);

    java.util.Set<String> getDisabledSkills();
    void setDisabledSkills(java.util.Set<String> skills);
    boolean isSkillDisabled(String skillId);
    void setSkillDisabled(String skillId, boolean disabled);
    void toggleSkill(String skillId);

    void copyFrom(IShokugiData source);
}
