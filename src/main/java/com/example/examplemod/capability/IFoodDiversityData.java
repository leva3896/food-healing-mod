package com.example.examplemod.capability;

import java.util.Set;

/**
 * 食べ物の多様性データを管理するインターフェース
 * プレイヤーが食べた食べ物の種類を追跡し、最大体力ボーナスを管理
 */
public interface IFoodDiversityData {

    /**
     * 新しい食べ物を追加（現在のカウント用）
     * 一度でも食べたことがある食べ物は追加されない
     * 
     * @param foodId 食べ物のRegistryName (例: "minecraft:apple")
     * @return 全く新しい食べ物の場合はtrue、既に食べたことがある場合はfalse
     */
    boolean addEatenFood(String foodId);

    /**
     * 現在カウント中の食べた種類数を取得
     */
    int getUniqueFoodCount();

    /**
     * 現在カウント中の食べ物リストをリセット（5種類達成後）
     * 永続的な食べ物リストはリセットしない
     */
    void resetFoodCount();

    /**
     * 最大体力ボーナスを追加
     * 
     * @param amount 追加する体力量
     */
    void addMaxHealthBonus(int amount);

    /**
     * 現在の最大体力ボーナスを取得
     */
    int getMaxHealthBonus();

    /**
     * 現在カウント中の食べ物のセットを取得
     */
    Set<String> getEatenFoods();

    /**
     * これまでに食べた全ての食べ物のセットを取得（永続的）
     */
    Set<String> getAllEatenFoods();

    /**
     * データをコピー（死亡時の復元用）
     */
    void copyFrom(IFoodDiversityData source);
}
