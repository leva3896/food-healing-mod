package com.example.examplemod;

import com.example.examplemod.capability.FoodDiversityProvider;
import com.example.examplemod.capability.IFoodDiversityData;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.UUID;

/**
 * 食べ物の多様性を追跡し、5種類ごとに最大体力を2増加させるハンドラー
 */
public class FoodDiversityHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    // Capability識別用のResourceLocation
    public static final ResourceLocation FOOD_DIVERSITY_CAP = new ResourceLocation(FoodHealingMod.MODID,
            "food_diversity");

    // AttributeModifier用のUUID
    private static final UUID HEALTH_BONUS_UUID = UUID.fromString("a5f8c2d1-3e7b-4a2c-9f1d-6e8b4c2a1d3f");

    /**
     * Capabilityを登録
     */
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IFoodDiversityData.class);
        LOGGER.info("[FoodHealing] Food diversity capability registered!");
    }

    /**
     * プレイヤーにCapabilityをアタッチ
     */
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<net.minecraft.world.entity.Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(FOOD_DIVERSITY_CAP, new FoodDiversityProvider());
        }
    }

    /**
     * 死亡時にデータを保持（リスポーン後も維持）
     */
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(FoodDiversityProvider.FOOD_DIVERSITY).ifPresent(oldData -> {
                event.getEntity().getCapability(FoodDiversityProvider.FOOD_DIVERSITY).ifPresent(newData -> {
                    newData.copyFrom(oldData);
                    // 最大体力ボーナスを再適用
                    applyHealthBonus(event.getEntity(), newData.getMaxHealthBonus());
                });
            });
            event.getOriginal().invalidateCaps();
        }
    }

    /**
     * ログイン時に最大体力ボーナスを適用
     */
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        player.getCapability(FoodDiversityProvider.FOOD_DIVERSITY).ifPresent(data -> {
            if (data.getMaxHealthBonus() > 0) {
                applyHealthBonus(player, data.getMaxHealthBonus());
                LOGGER.info("[FoodHealing] Restored {} max health bonus for player {}",
                        data.getMaxHealthBonus(), player.getName().getString());
            }
        });
    }

    /**
     * 食事完了時に食べ物の種類を追跡
     */
    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        // サーバーサイドのみ
        if (player.level().isClientSide()) {
            return;
        }

        ItemStack itemStack = event.getItem();
        FoodProperties foodProperties = itemStack.getItem().getFoodProperties(itemStack, player);

        if (foodProperties == null) {
            return;
        }

        // 食べ物のRegistryNameを取得
        ResourceLocation foodId = ForgeRegistries.ITEMS.getKey(itemStack.getItem());
        if (foodId == null) {
            return;
        }

        String foodIdString = foodId.toString();

        player.getCapability(FoodDiversityProvider.FOOD_DIVERSITY).ifPresent(data -> {
            // 新しい食べ物を追加（既に食べていた場合はfalse）
            boolean isNew = data.addEatenFood(foodIdString);

            if (isNew) {
                int currentCount = data.getUniqueFoodCount();
                int foodsRequired = FoodHealingConfig.COMMON.foodsRequiredForBonus.get();
                int healthIncrease = FoodHealingConfig.COMMON.healthIncreasePerBonus.get();

                LOGGER.info("[FoodHealing] New food type eaten: {}. Unique foods: {}/{}",
                        foodIdString, currentCount, foodsRequired);

                // 必要種類数達成したら最大体力を増加
                if (currentCount >= foodsRequired) {
                    data.addMaxHealthBonus(healthIncrease);
                    data.resetFoodCount();

                    // 最大体力を増加
                    applyHealthBonus(player, data.getMaxHealthBonus());

                    // レベルアップ音を再生
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, 1.0f);

                    // プレイヤーにメッセージを表示
                    player.sendSystemMessage(
                            Component.literal("全身から活力が漲ってきた、最大体力が2増加した！")
                                    .withStyle(ChatFormatting.GREEN)
                                    .withStyle(ChatFormatting.BOLD));

                    LOGGER.info("[FoodHealing] Player {} max health increased! Total bonus: {}",
                            player.getName().getString(), data.getMaxHealthBonus());
                }
            }
        });
    }

    /**
     * 最大体力ボーナスを適用
     */
    private static void applyHealthBonus(Player player, int totalBonus) {
        if (totalBonus <= 0)
            return;

        var healthAttribute = player.getAttribute(Attributes.MAX_HEALTH);
        if (healthAttribute == null)
            return;

        // 既存のModifierを削除
        healthAttribute.removeModifier(HEALTH_BONUS_UUID);

        // 新しいModifierを追加
        AttributeModifier modifier = new AttributeModifier(
                HEALTH_BONUS_UUID,
                "Food Diversity Health Bonus",
                totalBonus,
                AttributeModifier.Operation.ADDITION);
        healthAttribute.addPermanentModifier(modifier);
    }
}
