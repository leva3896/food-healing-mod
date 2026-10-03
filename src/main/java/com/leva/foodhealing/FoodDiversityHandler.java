package com.leva.foodhealing;

import com.leva.foodhealing.capability.FoodDiversityProvider;
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
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
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
    static final UUID HEALTH_BONUS_UUID = UUID.fromString("a5f8c2d1-3e7b-4a2c-9f1d-6e8b4c2a1d3f");

    /**
     * プレイヤーにCapabilityをアタッチ
     */
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<net.minecraft.world.entity.Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(FOOD_DIVERSITY_CAP, new FoodDiversityProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        player.getCapability(FoodDiversityProvider.FOOD_DIVERSITY).ifPresent(data -> {
            applyHealthBonus(player, data.getMaxHealthBonus());
            LOGGER.info("[FoodHealing] Restored {} max health bonus for player {}",
                    data.getMaxHealthBonus(), player.getName().getString());
            syncToClient(player);
        });
    }

    /**
     * ワールド参加時に確実にデータを同期する（ログイン時のパケット空振り対策）
     */
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide() && event.getEntity() instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            serverPlayer.getCapability(FoodDiversityProvider.FOOD_DIVERSITY).ifPresent(data -> {
                applyHealthBonus(serverPlayer, data.getMaxHealthBonus());
            });
            syncToClient(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) {
            return;
        }
        player.getCapability(FoodDiversityProvider.FOOD_DIVERSITY).ifPresent(data -> {
            applyHealthBonus(player, data.getMaxHealthBonus());
            syncToClient(player);
        });
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) {
            return;
        }
        player.getCapability(FoodDiversityProvider.FOOD_DIVERSITY).ifPresent(data -> {
            applyHealthBonus(player, data.getMaxHealthBonus());
            syncToClient(player);
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
                            Component.translatable("message.foodhealing.diversity_bonus")
                                    .withStyle(ChatFormatting.GREEN)
                                    .withStyle(ChatFormatting.BOLD));

                    LOGGER.info("[FoodHealing] Player {} max health increased! Total bonus: {}",
                            player.getName().getString(), data.getMaxHealthBonus());
                }
                
                // Sync data after a new food discovery update
                syncToClient(player);
            }
        });
    }

    /**
     * Send current diversity data state to client.
     */
    public static void syncToClient(Player player) {
        if (!player.level().isClientSide() && player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            player.getCapability(FoodDiversityProvider.FOOD_DIVERSITY).ifPresent(cap -> {
                if (cap instanceof com.leva.foodhealing.capability.FoodDiversityData data) {
                    com.leva.foodhealing.network.PacketHandler.INSTANCE.send(
                            net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> serverPlayer),
                            new com.leva.foodhealing.network.FoodDiversitySyncPacket(data.serializeNBT())
                    );
                }
            });
        }
    }

    /**
     * 最大体力ボーナスを適用
     */
    public static void applyHealthBonus(Player player, int totalBonus) {
        if (player.level().isClientSide()) {
            return;
        }

        var healthAttribute = player.getAttribute(Attributes.MAX_HEALTH);
        if (healthAttribute == null)
            return;

        healthAttribute.removeModifier(HEALTH_BONUS_UUID);

        if (totalBonus <= 0)
            return;

        AttributeModifier modifier = new AttributeModifier(
                HEALTH_BONUS_UUID,
                "Food Diversity Health Bonus",
                totalBonus,
                AttributeModifier.Operation.ADDITION);
        healthAttribute.addPermanentModifier(modifier);
    }
}
