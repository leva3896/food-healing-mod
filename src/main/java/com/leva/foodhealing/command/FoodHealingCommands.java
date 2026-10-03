package com.leva.foodhealing.command;

import com.leva.foodhealing.FoodHealingConfig;
import com.leva.foodhealing.FoodHealingMod;
import com.leva.foodhealing.capability.CapabilityEvents;
import com.leva.foodhealing.capability.IShokugiData;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FoodHealingMod.MODID)
public final class FoodHealingCommands {
    private FoodHealingCommands() {
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("foodhealing")
                .then(Commands.literal("syokugi")
                        .then(Commands.literal("level").executes(FoodHealingCommands::executeLevel))
                        .then(Commands.literal("count").executes(FoodHealingCommands::executeCount))
                        .then(Commands.literal("setlevel")
                                .requires(source -> source.hasPermission(2))
                                .then(Commands.argument("level", LongArgumentType.longArg(0L))
                                        .executes(FoodHealingCommands::executeSetLevel)))
                        .then(Commands.literal("setcount")
                                .requires(source -> source.hasPermission(2))
                                .then(Commands.argument("count", LongArgumentType.longArg(0L))
                                        .executes(FoodHealingCommands::executeSetCount)))
                        .then(Commands.literal("setskillpoint")
                                .requires(source -> source.hasPermission(2))
                                .then(Commands.argument("amount", LongArgumentType.longArg(0L))
                                        .executes(FoodHealingCommands::executeSetSkillPoint)))
                        .then(Commands.literal("addskillpoint")
                                .requires(source -> source.hasPermission(2))
                                .then(Commands.argument("amount", LongArgumentType.longArg(0L))
                                        .executes(FoodHealingCommands::executeAddSkillPoint)))));
    }

    private static int executeLevel(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(data ->
                    player.sendSystemMessage(Component.translatable("command.foodhealing.level", data.getLevel())));
            return 1;
        } catch (Exception exception) {
            context.getSource().sendFailure(Component.translatable("command.foodhealing.player_only"));
            return 0;
        }
    }

    private static int executeCount(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(data ->
                    player.sendSystemMessage(Component.translatable("command.foodhealing.count",
                            data.getEatCount(), FoodHealingConfig.nutritionThreshold())));
            return 1;
        } catch (Exception exception) {
            context.getSource().sendFailure(Component.translatable("command.foodhealing.player_only"));
            return 0;
        }
    }

    private static int executeSetLevel(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            long newLevel = LongArgumentType.getLong(context, "level");
            player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(data -> {
                data.setLevel(newLevel);
                CapabilityEvents.syncToClient(player);
                context.getSource().sendSuccess(() -> Component.translatable(
                        "command.foodhealing.setlevel.success", newLevel), true);
            });
            return 1;
        } catch (Exception exception) {
            context.getSource().sendFailure(Component.translatable("command.foodhealing.player_only"));
            return 0;
        }
    }

    private static int executeSetCount(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            long newCount = LongArgumentType.getLong(context, "count");
            player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(data -> {
                data.setEatCount(newCount);
                CapabilityEvents.syncToClient(player);
                context.getSource().sendSuccess(() -> Component.translatable(
                        "command.foodhealing.setcount.success", newCount), true);
            });
            return 1;
        } catch (Exception exception) {
            context.getSource().sendFailure(Component.translatable("command.foodhealing.player_only"));
            return 0;
        }
    }

    private static int executeSetSkillPoint(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            long amount = LongArgumentType.getLong(context, "amount");
            IShokugiData data = player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                    .orElseThrow(() -> new IllegalStateException("Shokugi capability is unavailable"));
            data.setUnspentSkillPoints(amount);
            CapabilityEvents.syncToClient(player);
            context.getSource().sendSuccess(() -> Component.translatable(
                    "command.foodhealing.setskillpoint.success", amount), true);
            return 1;
        } catch (Exception exception) {
            context.getSource().sendFailure(Component.translatable("command.foodhealing.player_only"));
            return 0;
        }
    }

    private static int executeAddSkillPoint(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            long amount = LongArgumentType.getLong(context, "amount");
            IShokugiData data = player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                    .orElseThrow(() -> new IllegalStateException("Shokugi capability is unavailable"));
            long current = data.getUnspentSkillPoints();
            if (current > Long.MAX_VALUE - amount) {
                context.getSource().sendFailure(Component.translatable(
                        "command.foodhealing.addskillpoint.overflow", current, amount));
                return 0;
            }

            long updated = current + amount;
            data.setUnspentSkillPoints(updated);
            CapabilityEvents.syncToClient(player);
            context.getSource().sendSuccess(() -> Component.translatable(
                    "command.foodhealing.addskillpoint.success", amount, updated), true);
            return 1;
        } catch (Exception exception) {
            context.getSource().sendFailure(Component.translatable("command.foodhealing.player_only"));
            return 0;
        }
    }
}
