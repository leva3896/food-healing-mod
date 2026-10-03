package com.leva.foodhealing.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.leva.foodhealing.FoodHealingConfig;
import com.leva.foodhealing.HeroicsController;
import com.leva.foodhealing.capability.ShokugiProvider;

/**
 * 体力をハートではなく数値で表示するオーバーレイ
 * ハートが表示されていた位置に「HP: 現在値 / 最大値」を表示
 */
@OnlyIn(Dist.CLIENT)
public class HealthDisplayOverlay {
    private static final int HUD_BACKGROUND_COLOR = 0xA0000000;
    private static final int HUD_BORDER_COLOR = 0xC0606060;
    private static final int HUD_OUTLINE_COLOR = 0xFF101010;
    // Vanilla chat draws its background at Z=50 and text at Z=100. Z=200 is vanilla's normal GUI foreground layer.
    private static final float HUD_FOREGROUND_Z = 200.0F;

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onRenderGuiOverlayPre(RenderGuiOverlayEvent.Pre event) {
        // バニラのハート表示だけをキャンセルする。数値描画は独立したPostイベントで行う。
        if (event.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type()) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;

            // クリエイティブモードの場合は何もしない（バニラのまま）
            if (player != null && player.isCreative()) {
                return;
            }

            // ハート表示をキャンセル
            event.setCanceled(true);
        }
    }

    public static void renderForegroundOverlay(ForgeGui forgeGui, GuiGraphics guiGraphics, float partialTick,
                                               int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.player == null || mc.player.isCreative()) {
            return;
        }

        guiGraphics.pose().pushPose();
        try {
            guiGraphics.pose().translate(0.0F, 0.0F, HUD_FOREGROUND_Z);
            renderHealthText(guiGraphics, screenWidth, screenHeight);
            renderHeroicsAndShokugiText(guiGraphics);
        } finally {
            guiGraphics.pose().popPose();
        }
    }

    private static void renderHealthText(GuiGraphics guiGraphics, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null) {
            return;
        }

        Font font = mc.font;

        // 現在HP と 最大HP を取得
        float currentHealth = player.getHealth();
        float maxHealth = player.getMaxHealth();
        float absorption = player.getAbsorptionAmount();

        // 表示テキスト（大きな数値を読みやすくフォーマット）
        String healthText = absorption > 0.0F
                ? String.format("HP: %s / %s  ABS: %s", formatHealth(currentHealth), formatHealth(maxHealth), formatHealth(absorption))
                : String.format("HP: %s / %s", formatHealth(currentHealth), formatHealth(maxHealth));

        // ハートが表示されていた位置を計算
        // バニラのハート位置: 画面下部中央から少し左上
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

        int textWidth = font.width(healthText);
        int hudWidth = textWidth + 6;
        int hudHeight = 14;
        HealthHudLayout.Position position = HealthHudLayout.place(
                screenWidth, screenHeight, x - 3, y - 3, hudWidth, hudHeight);
        int left = position.x();
        int top = position.y();
        int right = left + hudWidth;
        int bottom = top + hudHeight;
        x = left + 3;
        y = top + 3;

        // Keep the vanilla-heart anchor stable. Contrast is provided locally so chat layout stays untouched.
        guiGraphics.fill(left, top, right, bottom, HUD_BACKGROUND_COLOR);
        guiGraphics.fill(left, top, right, top + 1, HUD_BORDER_COLOR);
        guiGraphics.fill(left, bottom - 1, right, bottom, HUD_BORDER_COLOR);
        guiGraphics.fill(left, top + 1, left + 1, bottom - 1, HUD_BORDER_COLOR);
        guiGraphics.fill(right - 1, top + 1, right, bottom - 1, HUD_BORDER_COLOR);

        guiGraphics.drawString(font, healthText, x - 1, y, HUD_OUTLINE_COLOR, false);
        guiGraphics.drawString(font, healthText, x + 1, y, HUD_OUTLINE_COLOR, false);
        guiGraphics.drawString(font, healthText, x, y - 1, HUD_OUTLINE_COLOR, false);
        guiGraphics.drawString(font, healthText, x, y + 1, HUD_OUTLINE_COLOR, false);
        guiGraphics.drawString(font, healthText, x, y, 0xFFFFFFFF, true);
    }

    private static void renderHeroicsAndShokugiText(GuiGraphics guiGraphics) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        Font font = mc.font;
        player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
            boolean trueHeroicsActive = HeroicsController.isTrueActive(player, cap);
            boolean heroicsActive = HeroicsController.isNormalActive(player, cap);
            if (trueHeroicsActive || heroicsActive) {
                String heroicsText = net.minecraft.network.chat.Component.translatable("hud.foodhealing.heroics").getString();
                int hX = FoodHealingConfig.COMMON.heroicsTextOffsetX.get();
                int hY = FoodHealingConfig.COMMON.heroicsTextOffsetY.get();
                guiGraphics.drawString(font, heroicsText, hX, hY, 0xFF5555, true);
            }

            long level = cap.getLevel();
            long count = cap.getEatCount();
            long req = ClientFoodHealingState.getLevelUpRequirement();
            
            String shokugiText = net.minecraft.network.chat.Component.translatable("hud.foodhealing.shokugi", level, count, req).getString();
            
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
