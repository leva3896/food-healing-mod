package com.example.examplemod.command;

import com.example.examplemod.FoodHealingMod;
import com.example.examplemod.capability.ShokugiProvider;
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
                player.sendSystemMessage(Component.literal("§a[FoodHealing] 現在の食義Lv: §e" + cap.getLevel()));
            });
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("プレイヤーのみが実行できるコマンドです。"));
        }
        return 1;
    }

    private static int executeCount(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
                player.sendSystemMessage(Component.literal("§a[FoodHealing] 現在の食義カウント: §e" + cap.getEatCount() + " / 1000"));
            });
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("プレイヤーのみが実行できるコマンドです。"));
        }
        return 1;
    }

    private static int executeSkillList(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
                int lv = cap.getLevel();
                StringBuilder sb = new StringBuilder();
                sb.append("§e--- 習得済み 食義スキル一覧 ---\n");
                if (lv >= 1) sb.append("§f[Lv1] 耐火の心得\n");
                if (lv >= 2) sb.append("§f[Lv2] 水月と暗視の心得\n");
                if (lv >= 3) sb.append("§f[Lv3] 早食いⅠ\n");
                if (lv >= 4) sb.append("§f[Lv4] 軽業の心得\n");
                if (lv >= 5) sb.append("§c[Lv5] 炎の加護\n");
                if (lv >= 6) sb.append("§f[Lv6] 爆破耐性の心得\n");
                if (lv >= 7) sb.append("§f[Lv7] 浄化の心得\n");
                if (lv >= 8) sb.append("§f[Lv8] 豊穣の心得\n");
                if (lv >= 9) sb.append("§c[Lv9] 屠殺の心得\n");
                if (lv >= 10) sb.append("§f[Lv10] 火事場力解放\n");
                if (lv >= 11) sb.append("§f[Lv11] 満足感Ⅰ (TaCZ弾薬10%節約 / ミニガン冷却)\n");
                if (lv >= 12) sb.append("§f[Lv12] 満足感Ⅱ (TaCZ弾薬20%節約)\n");
                if (lv >= 13) sb.append("§f[Lv13] 満足感Ⅲ (TaCZ弾薬30%節約)\n");
                if (lv >= 14) sb.append("§c[Lv14] 採取の心得Ⅰ\n");
                if (lv >= 15) sb.append("§c[Lv15] 採取の心得Ⅱ (TaCZ弾薬50%節約)\n");
                if (lv >= 16) sb.append("§c[Lv16] 採取の心得Ⅲ (TaCZ弾薬60%節約)\n");
                if (lv >= 17) sb.append("§c[Lv17] 不壊の心得Ⅰ (防具破壊完全無効化)\n");
                if (lv >= 18) sb.append("§c[Lv18] 不壊の心得Ⅱ\n");
                if (lv >= 19) sb.append("§c[Lv19] 不壊の心得Ⅲ\n");
                if (lv >= 20) sb.append("§f[Lv20] 防具の極意 (TaCZ弾薬100%節約)\n");
                if (lv >= 30) sb.append("§c[Lv30] 飛翔の心得\n");
                if (lv >= 100) sb.append("§f[Lv100] 金剛の心得\n");
                if (lv >= 991) sb.append("§c[Lv991~999] 追撃の心得 極\n");

                if (lv == 0) sb.append("§7まだ習得しているスキルはありません。\n");
                
                sb.append("§a§n詳細を確認: /foodhealing syokugi skill <スキル名の一部>");
                player.sendSystemMessage(Component.literal(sb.toString()));
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
                String detail = getSkillDetail(skillName);
                player.sendSystemMessage(Component.literal(detail));
            });
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Error"));
        }
        return 1;
    }

    private static String getSkillDetail(String name) {
        if (name.contains("不壊")) {
            return "§e【不壊の心得】§r\nバニラの「耐久力（Unbreaking）」エンチャントと完全に重複・重ね掛けが機能します。\nさらにLv17からはL2 Hostilityの防具破壊（Durability Destroyerなど）を完全無効化するメタ特性も追加されます。";
        } else if (name.contains("採取")) {
            return "§e【採取の心得】§r\n幸運エンチャントで増幅されたアイテム（鉱石・作物に加え、特例としてグロウストーン・粘土 等）のドロップ量が大幅に増加します。エンチャントによる増加判定の後に掛け算処理が入るため、幸運レベルが高ければ高いほど獲得量が爆発的に増えます。";
        } else if (name.contains("屠殺")) {
            return "§e【屠殺の心得】§r\n牛や豚などの友好MOBを倒した際のドロップアイテムが通常の3倍になります。ドロップ増加（Looting）エンチャントと重掛け（重複計算）が可能です。";
        } else if (name.contains("追撃")) {
            return "§e【追撃の心得 極】§r\nLv991以降で発動する最強のやり込み能力。敵への攻撃時、無敵時間を無視して確定で追加ダメージを与えます。Lvが1上がるごとに追撃回数が1回増加し、Lv999到達時には合計9回の追撃がすべて同時に叩き込まれます。";
        } else if (name.contains("炎")) {
            return "§e【炎の加護】§r\n自身が「燃え上がっている状態（炎上中）」に全ての攻撃による被ダメージが無条件に30%減少します。耐火の心得ですでに炎ダメージは0になるため、燃えている状態を意図的に維持すれば常時ダメージカットの要塞化が可能です。";
        } else if (name.contains("火事場")) {
            return "§e【火事場力解放】§r\nHPが最大値の一定割合以下になった際にすべての与ダメージが数倍に跳ね上がる最強のバフ。食義の基本攻撃倍率の後に掛け算（乗算）されるため最終火力が天文学的な数値になります。";
        } else if (name.contains("TaCZ") || name.contains("満足感")) {
            return "§e【満足感 / TaCZ連携】§r\nTaCZの銃を撃つ際、確率で弾薬を消費せずに撃つ事ができるようになります。レベル11から10%の確率で発動し、最後には完全に無消費（無限弾薬）になります。レベル11以上ならミニガンのオーバーヒート強制解除も内包しています。";
        }
        return "§7スキル「" + name + "」の詳細が見つかりません。名前の一部（不壊など）を入れてみてください。";
    }
}
