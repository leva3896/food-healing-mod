package com.leva.foodhealing.command;

import com.leva.foodhealing.FoodHealingMod;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FoodHealingMod.MODID)
public class FoodHealingCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("foodhealing")
            .then(Commands.literal("syokugi")
                .then(Commands.literal("level").executes(FoodHealingCommands::executeLevel))
                .then(Commands.literal("count").executes(FoodHealingCommands::executeCount))
                .then(Commands.literal("toggle")
                    .then(Commands.argument("skillName", StringArgumentType.greedyString())
                        .executes(FoodHealingCommands::executeToggle)
                    )
                )
                .then(Commands.literal("skill")
                    .executes(FoodHealingCommands::executeSkillList)
                    .then(Commands.argument("skillName", StringArgumentType.greedyString())
                        .executes(FoodHealingCommands::executeSkillDetail)
                    )
                )
            )
        );
    }

    private static int executeLevel(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
                player.sendSystemMessage(Component.translatable("command.foodhealing.level", cap.getLevel()));
            });
        } catch (Exception e) {
            context.getSource().sendFailure(Component.translatable("command.foodhealing.player_only"));
        }
        return 1;
    }

    private static int executeCount(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
                player.sendSystemMessage(Component.translatable("command.foodhealing.count", cap.getEatCount(), 1000));
            });
        } catch (Exception e) {
            context.getSource().sendFailure(Component.translatable("command.foodhealing.player_only"));
        }
        return 1;
    }

    private static int executeSkillList(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
                int lv = cap.getLevel();
                player.sendSystemMessage(Component.translatable("command.foodhealing.skills.header"));
                if (lv >= 1) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.taika.title").getString(), "耐火"));
                if (lv >= 2) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.suigetsu_anshi.title").getString(), "水月と暗視"));
                if (lv >= 3) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.hayagui.title").getString(), "早食い"));
                if (lv >= 4) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.karuwaza.title").getString(), "軽業"));
                if (lv >= 5) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.honoo.title").getString(), "炎"));
                if (lv >= 6) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.bakuha.title").getString(), "爆破耐性"));
                if (lv >= 7) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.joka.title").getString(), "浄化"));
                if (lv >= 8) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.hojo.title").getString(), "豊穣"));
                if (lv >= 9) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.tosatsu.title").getString(), "屠殺"));
                if (lv >= 10) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.kajiba.title").getString(), "火事場力"));
                if (lv >= 11) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.manzoku1.title").getString(), "満足感"));
                if (lv >= 12) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.manzoku2.title").getString(), "満足感"));
                if (lv >= 13) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.manzoku3.title").getString(), "満足感"));
                if (lv >= 14) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.saishu1.title").getString(), "採取"));
                if (lv >= 15) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.saishu2.title").getString(), "採取"));
                if (lv >= 16) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.saishu3.title").getString(), "採取"));
                if (lv >= 17) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.fukai1.title").getString(), "不壊"));
                if (lv >= 18) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.fukai2.title").getString(), "不壊"));
                if (lv >= 19) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.fukai3.title").getString(), "不壊"));
                if (lv >= 20) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.gokui.title").getString(), "防具の極意"));
                if (lv >= 30) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.hisho.title").getString(), "飛翔"));
                if (lv >= 100) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.kongo.title").getString(), "金剛"));
                if (lv >= 991) player.sendSystemMessage(createClickable(Component.translatable("skill.foodhealing.tsuigeki.title").getString(), "追撃"));

                if (lv == 0) player.sendSystemMessage(Component.translatable("command.foodhealing.skills.none"));
                
                player.sendSystemMessage(Component.translatable("command.foodhealing.skills.footer"));
            });
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Error"));
        }
        return 1;
    }

    private static net.minecraft.network.chat.MutableComponent createClickable(String text, String key) {
        return net.minecraft.network.chat.Component.literal(text)
                .withStyle(net.minecraft.network.chat.Style.EMPTY
                        .withClickEvent(new net.minecraft.network.chat.ClickEvent(net.minecraft.network.chat.ClickEvent.Action.RUN_COMMAND, "/foodhealing syokugi skill " + key))
                        .withHoverEvent(new net.minecraft.network.chat.HoverEvent(net.minecraft.network.chat.HoverEvent.Action.SHOW_TEXT, net.minecraft.network.chat.Component.translatable("command.foodhealing.skills.hover_detail"))));
    }

    private static int executeToggle(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            String rawName = StringArgumentType.getString(context, "skillName");
            player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
                String key = getSkillKey(rawName);
                if (key == null) {
                    player.sendSystemMessage(Component.translatable("command.foodhealing.toggle.not_found", rawName));
                    return;
                }
                int required = getRequiredLevel(key);
                if (cap.getLevel() < required) {
                    player.sendSystemMessage(Component.translatable("command.foodhealing.toggle.locked", required));
                    return;
                }
                cap.toggleSkill(key);
                boolean disabled = cap.isSkillDisabled(key);
                com.leva.foodhealing.network.PacketHandler.INSTANCE.send(
                        net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> player),
                        new com.leva.foodhealing.network.ShokugiSyncPacket(cap.getLevel(), cap.getEatCount(), cap.getDisabledSkills())
                );
                String state = disabled ? Component.translatable("state.foodhealing.off").getString() : Component.translatable("state.foodhealing.on").getString();
                player.sendSystemMessage(Component.translatable("command.foodhealing.toggle.success", key, state));
                player.sendSystemMessage(Component.translatable("command.foodhealing.toggle.tip"));
            });
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Error"));
        }
        return 1;
    }

    private static int executeSkillDetail(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            String skillName = StringArgumentType.getString(context, "skillName");
            player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
                String key = getSkillKey(skillName);
                if (key == null) {
                    player.sendSystemMessage(Component.translatable("command.foodhealing.detail.not_found", skillName));
                    return;
                }
                player.sendSystemMessage(Component.translatable(getSkillDetailTextKey(key)));

                // 習得状況による表示分けとON/OFF UI
                if (cap.getLevel() < getRequiredLevel(key)) {
                    player.sendSystemMessage(Component.translatable("command.foodhealing.detail.locked", getRequiredLevel(key)));
                } else {
                    boolean disabled = cap.isSkillDisabled(key);
                    String stateText = disabled ? Component.translatable("state.foodhealing.off").getString() : Component.translatable("state.foodhealing.on").getString();
                    net.minecraft.network.chat.MutableComponent toggleBtn = Component.translatable("command.foodhealing.detail.toggle_btn", stateText)
                            .withStyle(net.minecraft.network.chat.Style.EMPTY
                                    .withClickEvent(new net.minecraft.network.chat.ClickEvent(net.minecraft.network.chat.ClickEvent.Action.RUN_COMMAND, "/foodhealing syokugi toggle " + key))
                                    .withHoverEvent(new net.minecraft.network.chat.HoverEvent(net.minecraft.network.chat.HoverEvent.Action.SHOW_TEXT, Component.translatable("command.foodhealing.detail.toggle_hover"))));
                    player.sendSystemMessage(toggleBtn);
                }
            });
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Error"));
        }
        return 1;
    }

    private static String getSkillKey(String name) {
        String lower = name.toLowerCase();
        if (name.contains("不壊") || name.equals("不壊") || lower.contains("fukai") || lower.contains("indestructible")) return "不壊";
        if (name.contains("採取") || name.equals("採取") || lower.contains("saishu") || lower.contains("gatherer")) return "採取";
        if (name.contains("屠殺") || name.equals("屠殺") || lower.contains("tosatsu") || lower.contains("butcher")) return "屠殺";
        if (name.contains("追撃") || name.equals("追撃") || lower.contains("tsuigeki") || lower.contains("pursuit")) return "追撃";
        if (name.contains("炎") || name.equals("炎") || lower.contains("honoo") || lower.contains("blazing")) return "炎";
        if (name.contains("火事場") || name.equals("火事場力") || lower.contains("kajiba") || lower.contains("heroics")) return "火事場力";
        if (name.contains("満足感") || name.contains("TaCZ") || name.equals("満足感") || lower.contains("manzoku") || lower.contains("satisfaction")) return "満足感";
        if (name.contains("耐火") || name.equals("耐火") || lower.contains("taika") || lower.contains("fireproof")) return "耐火";
        if (name.contains("水月") || name.contains("暗視") || name.equals("水月と暗視") || lower.contains("suigetsu") || lower.contains("anshi") || lower.contains("abyssal")) return "水月と暗視";
        if (name.contains("早食い") || name.equals("早食い") || lower.contains("hayagui") || lower.contains("eating")) return "早食い";
        if (name.contains("軽業") || name.equals("軽業") || lower.contains("karuwaza") || lower.contains("acrobatic")) return "軽業";
        if (name.contains("爆破") || name.equals("爆破耐性") || lower.contains("bakuha") || lower.contains("blast")) return "爆破耐性";
        if (name.contains("浄化") || name.equals("浄化") || lower.contains("joka") || lower.contains("purification")) return "浄化";
        if (name.contains("豊穣") || name.equals("豊穣") || lower.contains("hojo") || lower.contains("bountiful")) return "豊穣";
        if (name.contains("極意") || name.equals("防具の極意") || lower.contains("gokui") || lower.contains("essence")) return "防具の極意";
        if (name.contains("飛翔") || name.equals("飛翔") || lower.contains("hisho") || lower.contains("flight")) return "飛翔";
        if (name.contains("金剛") || name.equals("金剛") || lower.contains("kongo") || lower.contains("adamant")) return "金剛";
        return null;
    }

    private static int getRequiredLevel(String key) {
        switch (key) {
            case "耐火": return 1;
            case "水月と暗視": return 2;
            case "早食い": return 3;
            case "軽業": return 4;
            case "炎": return 5;
            case "爆破耐性": return 6;
            case "浄化": return 7;
            case "豊穣": return 8;
            case "屠殺": return 9;
            case "火事場力": return 10;
            case "満足感": return 11;
            case "採取": return 14;
            case "不壊": return 17;
            case "防具の極意": return 20;
            case "飛翔": return 30;
            case "金剛": return 100;
            case "追撃": return 991;
            default: return 9999;
        }
    }

    private static String getSkillDetailTextKey(String key) {
        switch (key) {
            case "不壊": return "skill.foodhealing.fukai.desc";
            case "採取": return "skill.foodhealing.saishu.desc";
            case "屠殺": return "skill.foodhealing.tosatsu.desc";
            case "追撃": return "skill.foodhealing.tsuigeki.desc";
            case "炎": return "skill.foodhealing.honoo.desc";
            case "火事場力": return "skill.foodhealing.kajiba.desc";
            case "満足感": return "skill.foodhealing.manzoku.desc";
            case "耐火": return "skill.foodhealing.taika.desc";
            case "水月と暗視": return "skill.foodhealing.suigetsu_anshi.desc";
            case "早食い": return "skill.foodhealing.hayagui.desc";
            case "軽業": return "skill.foodhealing.karuwaza.desc";
            case "爆破耐性": return "skill.foodhealing.bakuha.desc";
            case "浄化": return "skill.foodhealing.joka.desc";
            case "豊穣": return "skill.foodhealing.hojo.desc";
            case "防具の極意": return "skill.foodhealing.gokui.desc";
            case "飛翔": return "skill.foodhealing.hisho.desc";
            case "金剛": return "skill.foodhealing.kongo.desc";
            default: return "skill.foodhealing.unknown.desc";
        }
    }
}
