package com.example.examplemod.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashSet;
import java.util.Set;

/**
 * IFoodDiversityDataの実装
 * NBTシリアライズ対応でワールド保存時にデータを永続化
 */
public class FoodDiversityData implements IFoodDiversityData, INBTSerializable<CompoundTag> {

    // 現在カウント中の食べた食べ物（5種類達成後にリセット）
    private Set<String> currentEatenFoods = new HashSet<>();

    // これまでに食べた全ての食べ物（永続的に保持、リセットされない）
    private Set<String> allEatenFoods = new HashSet<>();

    private int maxHealthBonus = 0;

    @Override
    public boolean addEatenFood(String foodId) {
        // 既に一度でも食べたことがあるなら追加しない
        if (allEatenFoods.contains(foodId)) {
            return false;
        }

        // 新しい食べ物を両方のセットに追加
        allEatenFoods.add(foodId);
        currentEatenFoods.add(foodId);
        return true;
    }

    @Override
    public int getUniqueFoodCount() {
        return currentEatenFoods.size();
    }

    @Override
    public void resetFoodCount() {
        // 現在カウント中のセットのみクリア（永続的なセットはそのまま）
        currentEatenFoods.clear();
    }

    @Override
    public void addMaxHealthBonus(int amount) {
        maxHealthBonus += amount;
    }

    @Override
    public int getMaxHealthBonus() {
        return maxHealthBonus;
    }

    @Override
    public Set<String> getEatenFoods() {
        return new HashSet<>(currentEatenFoods);
    }

    @Override
    public Set<String> getAllEatenFoods() {
        return new HashSet<>(allEatenFoods);
    }

    @Override
    public void copyFrom(IFoodDiversityData source) {
        this.currentEatenFoods = new HashSet<>(source.getEatenFoods());
        this.allEatenFoods = new HashSet<>(source.getAllEatenFoods());
        this.maxHealthBonus = source.getMaxHealthBonus();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        // 現在カウント中の食べ物をリストとして保存
        ListTag currentFoodList = new ListTag();
        for (String food : currentEatenFoods) {
            currentFoodList.add(StringTag.valueOf(food));
        }
        tag.put("CurrentEatenFoods", currentFoodList);

        // 全ての食べた食べ物を保存（永続的）
        ListTag allFoodList = new ListTag();
        for (String food : allEatenFoods) {
            allFoodList.add(StringTag.valueOf(food));
        }
        tag.put("AllEatenFoods", allFoodList);

        // 最大体力ボーナスを保存
        tag.putInt("MaxHealthBonus", maxHealthBonus);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        // 現在カウント中の食べ物を復元
        currentEatenFoods.clear();
        ListTag currentFoodList = tag.getList("CurrentEatenFoods", Tag.TAG_STRING);
        for (int i = 0; i < currentFoodList.size(); i++) {
            currentEatenFoods.add(currentFoodList.getString(i));
        }

        // 全ての食べた食べ物を復元
        allEatenFoods.clear();
        ListTag allFoodList = tag.getList("AllEatenFoods", Tag.TAG_STRING);
        for (int i = 0; i < allFoodList.size(); i++) {
            allEatenFoods.add(allFoodList.getString(i));
        }

        // 旧形式のデータがあれば移行（下位互換性）
        if (allFoodList.isEmpty() && tag.contains("EatenFoods")) {
            ListTag oldFoodList = tag.getList("EatenFoods", Tag.TAG_STRING);
            for (int i = 0; i < oldFoodList.size(); i++) {
                String food = oldFoodList.getString(i);
                currentEatenFoods.add(food);
                allEatenFoods.add(food);
            }
        }

        // 最大体力ボーナスを復元
        maxHealthBonus = tag.getInt("MaxHealthBonus");
    }
}
