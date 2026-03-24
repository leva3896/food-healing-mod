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

    void copyFrom(IShokugiData source);
}
