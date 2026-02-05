package com.example.examplemod;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * 満腹時でも全ての食べ物を食べられるようにするハンドラー
 * バニラの食べ物およびMODで追加された食べ物にも対応
 */
public class AlwaysEatHandler {

    @SubscribeEvent
    public static void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();

        // 食べ物かどうかチェック（新しいAPI使用）
        if (itemStack.getItem().getFoodProperties(itemStack, player) != null) {
            // 満腹でも食べ物の使用開始を許可
            // プレイヤーの満腹度に関係なく使用を開始させる
            player.startUsingItem(event.getHand());
        }
    }
}
