package com.example.examplemod.capability;

import net.minecraft.nbt.CompoundTag;

public class ShokugiData implements IShokugiData {
    private int level = 0;
    private int eatCount = 0;
    private java.util.Set<String> disabledSkills = new java.util.HashSet<>();

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
    public java.util.Set<String> getDisabledSkills() { return this.disabledSkills; }

    @Override
    public void setDisabledSkills(java.util.Set<String> skills) { this.disabledSkills = new java.util.HashSet<>(skills); }

    @Override
    public boolean isSkillDisabled(String skillName) { return this.disabledSkills.contains(skillName); }

    @Override
    public void toggleSkill(String skillName) {
        if (this.disabledSkills.contains(skillName)) {
            this.disabledSkills.remove(skillName);
        } else {
            this.disabledSkills.add(skillName);
        }
    }

    @Override
    public void copyFrom(IShokugiData source) {
        this.level = source.getLevel();
        this.eatCount = source.getEatCount();
        this.disabledSkills = new java.util.HashSet<>(source.getDisabledSkills());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("ShokugiLevel", this.level);
        nbt.putInt("EatCount", this.eatCount);
        
        net.minecraft.nbt.ListTag disabledList = new net.minecraft.nbt.ListTag();
        for (String skill : this.disabledSkills) {
            disabledList.add(net.minecraft.nbt.StringTag.valueOf(skill));
        }
        nbt.put("DisabledSkills", disabledList);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.level = nbt.getInt("ShokugiLevel");
        this.eatCount = nbt.getInt("EatCount");
        
        if (nbt.contains("DisabledSkills")) {
            net.minecraft.nbt.ListTag disabledList = nbt.getList("DisabledSkills", net.minecraft.nbt.Tag.TAG_STRING);
            this.disabledSkills.clear();
            for (int i = 0; i < disabledList.size(); i++) {
                this.disabledSkills.add(disabledList.getString(i));
            }
        }
    }
}
