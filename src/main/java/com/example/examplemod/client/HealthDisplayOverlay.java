package com.example.examplemod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * 体力をハートではなく数値で表示するオーバーレイ
 * ハートが表示されていた位置に「HP: 現在値 / 最大値」を表示
 */
@OnlyIn(Dist.CLIENT)
public class HealthDisplayOverlay {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderGuiOverlayPre(RenderGuiOverlayEvent.Pre event) {
        // バニラのハート表示をキャンセルし、代わりに数値を描画
        if (event.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type()) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;

            // クリエイティブモードの場合は何もしない（バニラのまま）
            if (player != null && player.isCreative()) {
                return;
            }

            // ハート表示をキャンセル
            event.setCanceled(true);

            // 代わりに数値を描画
            renderHealthText(event.getGuiGraphics());
        }
    }

    private static void renderHealthText(GuiGraphics guiGraphics) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null) {
            return;
        }

        Font font = mc.font;

        // 現在HP と 最大HP を取得
        float currentHealth = player.getHealth();
        float maxHealth = player.getMaxHealth();

        // 表示テキスト（大きな数値を読みやすくフォーマット）
        String healthText = String.format("HP: %s / %s",
                formatHealth(currentHealth), formatHealth(maxHealth));

        // ハートが表示されていた位置を計算
        // バニラのハート位置: 画面下部中央から少し左上
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        // ハートの位置（ホットバーの上、左側）
        int x = screenWidth / 2 - 91; // ホットバーの左端
        int y = screenHeight - 39; // ホットバーの上

        // 背景を描画（読みやすくするため）
        int textWidth = font.width(healthText);
        guiGraphics.fill(x - 2, y - 2, x + textWidth + 2, y + 10, 0x80000000);

        // テキスト描画（白色、影付き）
        guiGraphics.drawString(font, healthText, x, y, 0xFFFFFF);
    }

    /**
     * 体力値を読みやすい形式にフォーマット
     * 1000未満: 小数点1桁表示
     * 1000以上: K（千）, M（百万）, B（十億）, T（兆）で短縮表示
     */
    private static String formatHealth(float health) {
        if (health < 1000) {
            return String.format("%.1f", health);
        } else if (health < 1_000_000) {
            return String.format("%.2fK", health / 1000);
        } else if (health < 1_000_000_000) {
            return String.format("%.2fM", health / 1_000_000);
        } else if (health < 1_000_000_000_000L) {
            return String.format("%.2fB", health / 1_000_000_000);
        } else {
            return String.format("%.2fT", health / 1_000_000_000_000L);
        }
    }
}
