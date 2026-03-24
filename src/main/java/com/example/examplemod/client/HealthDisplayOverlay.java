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
import com.example.examplemod.FoodHealingConfig;
import com.example.examplemod.capability.ShokugiProvider;

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
            
            // 追加UIを描画
            renderHeroicsAndShokugiText(event.getGuiGraphics());
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

        // 防具を装備している場合、防御力バーと重ならないように上にずらす
        int armorValue = player.getArmorValue();
        if (armorValue > 0) {
            y -= 10; // 防御力バーの分だけ上にずらす
        }

        // コンフィグからオフセット設定を読み込んで適用
        x += FoodHealingConfig.COMMON.overlayOffsetX.get();
        y += FoodHealingConfig.COMMON.overlayOffsetY.get();

        // 背景を描画（読みやすくするため）
        int textWidth = font.width(healthText);
        guiGraphics.fill(x - 2, y - 2, x + textWidth + 2, y + 10, 0x80000000);

        // テキスト描画（白色、影付き）
        guiGraphics.drawString(font, healthText, x, y, 0xFFFFFF);
    }

    private static void renderHeroicsAndShokugiText(GuiGraphics guiGraphics) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        Font font = mc.font;
        float threshold = FoodHealingConfig.COMMON.heroicsThreshold.get().floatValue();
        
        // Render Heroics if active
        if (player.getHealth() <= player.getMaxHealth() * threshold) {
            String heroicsText = "火事場発動中";
            int hX = FoodHealingConfig.COMMON.heroicsTextOffsetX.get();
            int hY = FoodHealingConfig.COMMON.heroicsTextOffsetY.get();
            
            guiGraphics.drawString(font, heroicsText, hX, hY, 0xFF5555, true); // Red color with shadow
        }

        // Render Shokugi
        player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
            int level = cap.getLevel();
            int count = cap.getEatCount();
            int req = FoodHealingConfig.COMMON.shokugiLevelUpRequirement.get();
            
            String shokugiText = String.format("食義Lv: %d (カウント: %d/%d)", level, count, req);
            
            int sX = FoodHealingConfig.COMMON.shokugiTextOffsetX.get();
            int sY = FoodHealingConfig.COMMON.shokugiTextOffsetY.get();
            
            guiGraphics.drawString(font, shokugiText, sX, sY, 0x55FF55, true); // Green color with shadow
        });
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
