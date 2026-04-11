package com.example.examplemod.client;

import com.example.examplemod.FoodHealingMod;
import com.example.examplemod.capability.FoodDiversityProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = FoodHealingMod.MODID, value = Dist.CLIENT)
public class ClientTooltipHandler {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        // ★ 修正点: プレイヤーが null の場合（JEI等のGUI）はクライアントプレイヤーを取得する
        Player player = event.getEntity();
        if (player == null) {
            player = net.minecraft.client.Minecraft.getInstance().player;
        }
        
        // タイトル画面など、完全にプレイヤーが存在しない時だけ中断
        if (player == null) return;
        
        // Ensure the item is food and capable of being eaten
        if (stack.getItem().getFoodProperties(stack, player) != null) {
            ResourceLocation foodId = ForgeRegistries.ITEMS.getKey(stack.getItem());
            if (foodId != null) {
                player.getCapability(FoodDiversityProvider.FOOD_DIVERSITY).ifPresent(data -> {
                    boolean eaten = data.getAllEatenFoods().contains(foodId.toString());
                    
                    if (eaten) {
                        event.getToolTip().add(Component.literal("🍽 既食 (Eaten)").withStyle(ChatFormatting.DARK_GREEN));
                    } else {
                        event.getToolTip().add(Component.literal("🍽 未食 (Not Eaten)").withStyle(ChatFormatting.GRAY));
                    }
                });
            }
        }
    }
}
