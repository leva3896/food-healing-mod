package com.example.examplemod.capability;

import net.minecraft.nbt.CompoundTag;

public class ShokugiData implements IShokugiData {
    private int level = 0;
    private int eatCount = 0;

    @Override
    public int getLevel() { return level; }
    
    @Override
    public void setLevel(int level) { this.level = level; }
    
    @Override
    public void addLevel(int amount) { this.level += amount; }

    @Override
    public int getEatCount() { return eatCount; }
    
    @Override
    public void setEatCount(int count) { this.eatCount = count; }
    
    @Override
    public void addEatCount(int amount) { this.eatCount += amount; }

    @Override
    public void copyFrom(IShokugiData source) {
        this.level = source.getLevel();
        this.eatCount = source.getEatCount();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("ShokugiLevel", this.level);
        nbt.putInt("EatCount", this.eatCount);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.level = nbt.getInt("ShokugiLevel");
        this.eatCount = nbt.getInt("EatCount");
    }
}
