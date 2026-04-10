package com.example.examplemod.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IShokugiData extends INBTSerializable<CompoundTag> {
    int getLevel();
    void setLevel(int level);
    void addLevel(int amount);

    int getEatCount();
    void setEatCount(int count);
    void addEatCount(int amount);

    java.util.Set<String> getDisabledSkills();
    void setDisabledSkills(java.util.Set<String> skills);
    boolean isSkillDisabled(String skillName);
    void toggleSkill(String skillName);

    void copyFrom(IShokugiData source);
}
