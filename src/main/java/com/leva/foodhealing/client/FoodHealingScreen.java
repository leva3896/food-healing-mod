package com.leva.foodhealing.client;

import com.leva.foodhealing.FoodHealingBaseStatIds;
import com.leva.foodhealing.FoodHealingBaseStats;
import com.leva.foodhealing.FoodHealingSkillIds;
import com.leva.foodhealing.FoodHealingSkills;
import com.leva.foodhealing.capability.FoodDiversityProvider;
import com.leva.foodhealing.capability.IShokugiData;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.leva.foodhealing.network.PacketHandler;
import com.leva.foodhealing.network.PurchaseBaseStatPacket;
import com.leva.foodhealing.network.PurchaseSkillPacket;
import com.leva.foodhealing.network.PurchaseMessages;
import com.leva.foodhealing.network.ToggleSkillPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public final class FoodHealingScreen extends Screen {
    private static final int ROW_HEIGHT = 25;
    private static final int PURCHASE_BUTTON_WIDTH = 94;
    private static final int TOGGLE_BUTTON_WIDTH = 46;
    private static final int ACTION_GAP = 4;
    private static final List<BaseStatRow> BASE_STATS = List.of(
            new BaseStatRow("base_defense", FoodHealingBaseStatIds.BASE_DEFENSE),
            new BaseStatRow("base_damage_reduction", FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR),
            new BaseStatRow("high_difficulty_reduction", FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_LINEAR),
            new BaseStatRow("base_max_health", FoodHealingBaseStatIds.BASE_MAX_HEALTH),
            new BaseStatRow("recovery_multiplier", FoodHealingBaseStatIds.RECOVERY_MULTIPLIER),
            new BaseStatRow("base_outgoing_damage", FoodHealingBaseStatIds.BASE_OUTGOING_DAMAGE),
            new BaseStatRow("tacz_base_outgoing_damage", FoodHealingBaseStatIds.TACZ_BASE_OUTGOING_DAMAGE));

    private final Screen parent;
    private final List<VisibleRow> visibleRows = new ArrayList<>();
    private ViewMode viewMode = ViewMode.SKILLS;
    private int scrollOffset;
    private int panelX;
    private int panelY;
    private int panelWidth;
    private int panelHeight;
    private int lastStateHash;

    public FoodHealingScreen(Screen parent) {
        super(Component.translatable("gui.foodhealing.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        panelWidth = Math.min(640, Math.max(300, width - 24));
        panelHeight = Math.min(360, Math.max(210, height - 24));
        panelX = (width - panelWidth) / 2;
        panelY = (height - panelHeight) / 2;
        rebuildControls();
    }

    @Override
    public void tick() {
        super.tick();
        int stateHash = currentStateHash();
        if (stateHash != lastStateHash) {
            rebuildControls();
        }
    }

    private void rebuildControls() {
        clearWidgets();
        visibleRows.clear();

        addRenderableWidget(Button.builder(Component.translatable("gui.foodhealing.skills"), button -> {
            viewMode = ViewMode.SKILLS;
            scrollOffset = 0;
            rebuildControls();
        }).bounds(panelX + 12, panelY + 30, 96, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.foodhealing.status"), button -> {
            viewMode = ViewMode.STATUS;
            scrollOffset = 0;
            rebuildControls();
        }).bounds(panelX + 112, panelY + 30, 96, 20).build());
        addRenderableWidget(Button.builder(Component.literal("X"), button -> onClose())
                .bounds(panelX + panelWidth - 32, panelY + 8, 20, 20).build());

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            minecraft.player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(data -> {
                if (viewMode == ViewMode.SKILLS) {
                    addSkillControls(data);
                } else {
                    addBaseStatControls(data);
                }
            });
        }
        lastStateHash = currentStateHash();
    }

    private void addSkillControls(IShokugiData data) {
        List<FoodHealingSkills.SkillDefinition> definitions = FoodHealingSkills.orderedDefinitions();
        int visibleCount = visibleRowCount();
        int maxOffset = Math.max(0, definitions.size() - visibleCount);
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxOffset));
        int end = Math.min(definitions.size(), scrollOffset + visibleCount);

        for (int index = scrollOffset; index < end; index++) {
            FoodHealingSkills.SkillDefinition definition = definitions.get(index);
            int rowY = rowsTop() + (index - scrollOffset) * ROW_HEIGHT;
            String skillId = definition.id();
            int purchasedLevel = data.getSkillLevel(skillId);
            int effectiveLevel = FoodHealingSkills.getEffectiveLevel(data, skillId);
            boolean disabled = data.isSkillDisabled(skillId);

            SkillRowControls.State controls = SkillRowControls.forLevels(
                    purchasedLevel, effectiveLevel, definition.maxLevel());
            int actionRight = panelX + panelWidth - 16;
            int actionLeft = actionRight;

            if (controls.toggleVisible()) {
                int toggleX = actionRight - TOGGLE_BUTTON_WIDTH;
                Button toggle = Button.builder(Component.literal(disabled ? "OFF" : "ON"), button ->
                                PacketHandler.INSTANCE.sendToServer(new ToggleSkillPacket(skillId, !disabled)))
                        .bounds(toggleX, rowY + 2, TOGGLE_BUTTON_WIDTH, 20)
                        .tooltip(Tooltip.create(Component.translatable("gui.foodhealing.toggle")))
                        .build();
                addRenderableWidget(toggle);
                actionLeft = toggleX;
            }
            if (controls.purchaseVisible()) {
                long cost = definition.levelCosts()[purchasedLevel];
                int purchaseRight = controls.toggleVisible()
                        ? actionLeft - ACTION_GAP
                        : actionRight;
                int purchaseX = purchaseRight - PURCHASE_BUTTON_WIDTH;
                Button purchase = Button.builder(
                                Component.translatable("gui.foodhealing.purchase_next_level", cost), button ->
                                PacketHandler.INSTANCE.sendToServer(new PurchaseSkillPacket(skillId, purchasedLevel)))
                        .bounds(purchaseX, rowY + 2, PURCHASE_BUTTON_WIDTH, 20)
                        .tooltip(Tooltip.create(skillPurchaseTooltip(data, definition)))
                        .build();
                purchase.active = canAttemptSkillPurchase(data, definition);
                addRenderableWidget(purchase);
                actionLeft = purchaseX;
            }

            visibleRows.add(new VisibleRow(rowY, actionLeft, skillName(skillId), skillTooltip(definition),
                    skillStateText(data, definition, purchasedLevel, effectiveLevel)));
        }
    }

    private void addBaseStatControls(IShokugiData data) {
        int visibleCount = visibleRowCount();
        int maxOffset = Math.max(0, BASE_STATS.size() - visibleCount);
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxOffset));
        int end = Math.min(BASE_STATS.size(), scrollOffset + visibleCount);

        for (int index = scrollOffset; index < end; index++) {
            BaseStatRow row = BASE_STATS.get(index);
            int rowY = rowsTop() + (index - scrollOffset) * ROW_HEIGHT;
            visibleRows.add(new VisibleRow(rowY, panelX + panelWidth - 102,
                    Component.translatable("stat.foodhealing." + row.translationPath()),
                    List.of(Component.translatable("stat.foodhealing." + row.translationPath() + ".desc")),
                    baseStatValue(data, row.statId())));

            long cost = FoodHealingBaseStats.cost(row.statId());
            Button purchase = Button.builder(Component.literal("+ " + cost + " SP"), button ->
                            PacketHandler.INSTANCE.sendToServer(new PurchaseBaseStatPacket(
                                    row.statId(), FoodHealingBaseStats.purchaseCount(data, row.statId()))))
                    .bounds(panelX + panelWidth - 102, rowY + 2, 86, 20)
                    .tooltip(Tooltip.create(baseStatPurchaseTooltip(row.statId())))
                    .build();
            purchase.active = data.getUnspentSkillPoints() >= cost && isBaseStatAvailable(row.statId());
            addRenderableWidget(purchase);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        graphics.fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, 0xE6101418);
        graphics.fill(panelX, panelY, panelX + panelWidth, panelY + 3, 0xFF52B788);
        graphics.drawString(font, title, panelX + 12, panelY + 11, 0xFFF4F7F5, false);

        renderSummary(graphics);
        for (int i = 0; i < visibleRows.size(); i++) {
            VisibleRow row = visibleRows.get(i);
            int background = i % 2 == 0 ? 0x55303A3D : 0x55232B2E;
            graphics.fill(panelX + 12, row.y(), panelX + panelWidth - 12, row.y() + 23, background);
            graphics.drawString(font, row.title(), panelX + 19, row.y() + 4, 0xFFF2F4F3, false);
            graphics.drawString(font, row.state(), panelX + 19, row.y() + 14, 0xFF9FD8C2, false);
        }
        super.render(graphics, mouseX, mouseY, partialTick);

        for (VisibleRow row : visibleRows) {
            if (mouseX >= panelX + 12 && mouseX < row.actionLeft() - ACTION_GAP
                    && mouseY >= row.y() && mouseY < row.y() + 23) {
                graphics.renderComponentTooltip(font, row.tooltip(), mouseX, mouseY);
                break;
            }
        }
    }

    private void renderSummary(GuiGraphics graphics) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }
        minecraft.player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(data -> {
            Component progression = Component.translatable("gui.foodhealing.progression",
                    data.getLevel(), data.getEatCount(), ClientFoodHealingState.getLevelUpRequirement(),
                    data.getUnspentSkillPoints(), data.getSpentSkillPoints());
            graphics.drawString(font, progression, panelX + 220, panelY + 36, 0xFFD8E2DC, false);
            if (data.isLegacyMigrationPending()) {
                graphics.drawString(font, Component.translatable("gui.foodhealing.legacy_pending")
                        .withStyle(ChatFormatting.YELLOW), panelX + 12, panelY + 57, 0xFFFFD166, false);
            }
        });

        if (viewMode == ViewMode.STATUS) {
            minecraft.player.getCapability(FoodDiversityProvider.FOOD_DIVERSITY).ifPresent(data -> {
                Component diversity = Component.translatable("gui.foodhealing.diversity",
                        data.getUniqueFoodCount(), data.getAllEatenFoods().size(), data.getMaxHealthBonus());
                graphics.drawString(font, diversity, panelX + 12, panelY + 68, 0xFFB7E4C7, false);
            });
            minecraft.player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(data ->
                    graphics.drawString(font, rootStatus(data), panelX + 12, panelY + 79,
                            0xFFFFD166, false));
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int direction = delta > 0.0D ? -1 : delta < 0.0D ? 1 : 0;
        if (direction != 0) {
            scrollOffset += direction;
            rebuildControls();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(parent);
    }

    private int rowsTop() {
        return panelY + 96;
    }

    private int visibleRowCount() {
        return Math.max(3, (panelHeight - 108) / ROW_HEIGHT);
    }

    private int currentStateHash() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return 0;
        }
        int shokugiHash = minecraft.player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .map(data -> data.serializeNBT().hashCode()).orElse(0);
        int diversityHash = minecraft.player.getCapability(FoodDiversityProvider.FOOD_DIVERSITY)
                .map(data -> data.getAllEatenFoods().hashCode() * 31 + data.getMaxHealthBonus()).orElse(0);
        return shokugiHash * 31 + diversityHash;
    }

    private static boolean canAttemptSkillPurchase(IShokugiData data,
                                                    FoodHealingSkills.SkillDefinition definition) {
        return FoodHealingSkills.purchaseStatus(data, definition.id(), data.getSkillLevel(definition.id()))
                == FoodHealingSkills.PurchaseResult.SUCCESS;
    }

    private static Component skillPurchaseTooltip(IShokugiData data,
                                                   FoodHealingSkills.SkillDefinition definition) {
        FoodHealingSkills.PurchaseResult status = FoodHealingSkills.purchaseStatus(
                data, definition.id(), data.getSkillLevel(definition.id()));
        return Component.translatable(status == FoodHealingSkills.PurchaseResult.SUCCESS
                ? "gui.foodhealing.purchase_skill" : status.messageKey());
    }

    private static boolean isBaseStatAvailable(String statId) {
        return FoodHealingBaseStats.isAvailable(statId);
    }

    private static Component baseStatPurchaseTooltip(String statId) {
        if (!isBaseStatAvailable(statId)) {
            return PurchaseMessages.stat(FoodHealingBaseStats.PurchaseResult.OPTIONAL_MOD_MISSING, statId);
        }
        return Component.translatable("gui.foodhealing.purchase_stat");
    }

    private static Component skillName(String skillId) {
        return Component.translatable("skill.foodhealing." + path(skillId) + ".name");
    }

    private static List<Component> skillTooltip(FoodHealingSkills.SkillDefinition definition) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.translatable("skill.foodhealing." + path(definition.id()) + ".description"));
        for (FoodHealingSkills.SkillRequirement requirement : definition.requirements()) {
            tooltip.add(Component.translatable("gui.foodhealing.prerequisite",
                    skillName(requirement.skillId()), requirement.level()));
        }
        if (definition.openPrerequisites()) {
            tooltip.add(Component.translatable("gui.foodhealing.prerequisite.open")
                    .withStyle(ChatFormatting.YELLOW));
        } else if (definition.requirements().isEmpty()) {
            tooltip.add(Component.translatable("gui.foodhealing.prerequisite.none")
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
        return List.copyOf(tooltip);
    }

    private static Component rootStatus(IShokugiData data) {
        Minecraft minecraft = Minecraft.getInstance();
        long now = minecraft.level == null ? 0L : minecraft.level.getGameTime();
        long activeTicks = Math.max(0L, data.getRootActiveUntil() - now);
        if (activeTicks > 0L) {
            return Component.translatable("gui.foodhealing.root.active",
                    secondsCeil(activeTicks), data.getRootReservedNutrition());
        }
        long cooldownTicks = Math.max(0L, data.getRootCooldownUntil() - now);
        if (cooldownTicks > 0L) {
            return Component.translatable("gui.foodhealing.root.cooldown", secondsCeil(cooldownTicks));
        }
        long accumulationTicks = Math.max(0L, data.getRootAccumulationDeadline() - now);
        return Component.translatable("gui.foodhealing.root.accumulation",
                data.getRootAccumulatedNutrition(), secondsCeil(accumulationTicks));
    }

    private static long secondsCeil(long ticks) {
        return ticks / 20L + (ticks % 20L == 0L ? 0L : 1L);
    }

    private static Component skillStateText(IShokugiData data, FoodHealingSkills.SkillDefinition definition,
                                            int purchasedLevel, int effectiveLevel) {
        if (purchasedLevel > 0) {
            return Component.translatable("gui.foodhealing.skill_level", purchasedLevel, definition.maxLevel());
        }
        if (effectiveLevel > 0) {
            return Component.translatable("gui.foodhealing.legacy_skill", effectiveLevel);
        }
        return Component.translatable("gui.foodhealing.unacquired");
    }

    private static Component baseStatValue(IShokugiData data, String statId) {
        if (statId.equals(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR)) {
            return Component.translatable("gui.foodhealing.reduction_value",
                    data.getBaseStatPoints(statId),
                    data.getBaseStatPoints(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_TRANSCENDENCE));
        }
        if (statId.equals(FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_LINEAR)) {
            return Component.translatable("gui.foodhealing.reduction_value",
                    data.getBaseStatPoints(statId),
                    data.getBaseStatPoints(FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_TRANSCENDENCE));
        }
        return Component.translatable("gui.foodhealing.points_value", data.getBaseStatPoints(statId));
    }

    private static String path(String resourceId) {
        ResourceLocation id = ResourceLocation.tryParse(resourceId);
        return id == null ? "unknown" : id.getPath();
    }

    private enum ViewMode {
        SKILLS,
        STATUS
    }

    private record VisibleRow(int y, int actionLeft, Component title, List<Component> tooltip, Component state) {
    }

    private record BaseStatRow(String translationPath, String statId) {
    }
}
