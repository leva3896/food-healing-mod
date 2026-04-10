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
                player.sendSystemMessage(Component.literal("§e--- 習得済み 食義スキル一覧 ---"));
                if (lv >= 1) player.sendSystemMessage(createClickable("§f[Lv1] 耐火の心得", "耐火"));
                if (lv >= 2) player.sendSystemMessage(createClickable("§f[Lv2] 水月と暗視の心得", "水月と暗視"));
                if (lv >= 3) player.sendSystemMessage(createClickable("§f[Lv3] 早食いⅠ", "早食い"));
                if (lv >= 4) player.sendSystemMessage(createClickable("§f[Lv4] 軽業の心得", "軽業"));
                if (lv >= 5) player.sendSystemMessage(createClickable("§c[Lv5] 炎の加護", "炎"));
                if (lv >= 6) player.sendSystemMessage(createClickable("§f[Lv6] 爆破耐性の心得", "爆破耐性"));
                if (lv >= 7) player.sendSystemMessage(createClickable("§f[Lv7] 浄化の心得", "浄化"));
                if (lv >= 8) player.sendSystemMessage(createClickable("§f[Lv8] 豊穣の心得", "豊穣"));
                if (lv >= 9) player.sendSystemMessage(createClickable("§c[Lv9] 屠殺の心得", "屠殺"));
                if (lv >= 10) player.sendSystemMessage(createClickable("§f[Lv10] 火事場力解放", "火事場力"));
                if (lv >= 11) player.sendSystemMessage(createClickable("§f[Lv11] 満足感Ⅰ (TaCZ弾薬10%節約 / ミニガン冷却)", "満足感"));
                if (lv >= 12) player.sendSystemMessage(createClickable("§f[Lv12] 満足感Ⅱ (TaCZ弾薬20%節約)", "満足感"));
                if (lv >= 13) player.sendSystemMessage(createClickable("§f[Lv13] 満足感Ⅲ (TaCZ弾薬30%節約)", "満足感"));
                if (lv >= 14) player.sendSystemMessage(createClickable("§c[Lv14] 採取の心得Ⅰ", "採取"));
                if (lv >= 15) player.sendSystemMessage(createClickable("§c[Lv15] 採取の心得Ⅱ (TaCZ弾薬50%節約)", "採取"));
                if (lv >= 16) player.sendSystemMessage(createClickable("§c[Lv16] 採取の心得Ⅲ (TaCZ弾薬60%節約)", "採取"));
                if (lv >= 17) player.sendSystemMessage(createClickable("§c[Lv17] 不壊の心得Ⅰ (防具破壊完全無効化)", "不壊"));
                if (lv >= 18) player.sendSystemMessage(createClickable("§c[Lv18] 不壊の心得Ⅱ", "不壊"));
                if (lv >= 19) player.sendSystemMessage(createClickable("§c[Lv19] 不壊の心得Ⅲ", "不壊"));
                if (lv >= 20) player.sendSystemMessage(createClickable("§f[Lv20] 防具の極意 (TaCZ弾薬100%節約)", "防具の極意"));
                if (lv >= 30) player.sendSystemMessage(createClickable("§c[Lv30] 飛翔の心得", "飛翔"));
                if (lv >= 100) player.sendSystemMessage(createClickable("§f[Lv100] 金剛の心得", "金剛"));
                if (lv >= 991) player.sendSystemMessage(createClickable("§c[Lv991~999] 追撃の心得 極", "追撃"));

                if (lv == 0) player.sendSystemMessage(Component.literal("§7まだ習得しているスキルはありません。"));
                
                player.sendSystemMessage(Component.literal("§a§n※スキル名をクリックすると詳細を確認・ON/OFFの切替ができます。"));
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
                        .withHoverEvent(new net.minecraft.network.chat.HoverEvent(net.minecraft.network.chat.HoverEvent.Action.SHOW_TEXT, net.minecraft.network.chat.Component.literal("クリックして詳細と切替メニューを開く"))));
    }

    private static int executeToggle(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            String rawName = StringArgumentType.getString(context, "skillName");
            player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
                String key = getSkillKey(rawName);
                if (key == null) {
                    player.sendSystemMessage(Component.literal("§cエラー: スキル「" + rawName + "」は見つかりませんでした。"));
                    return;
                }
                int required = getRequiredLevel(key);
                if (cap.getLevel() < required) {
                    player.sendSystemMessage(Component.literal("§cエラー: そのスキルはまだ習得していません！ (必要Lv:" + required + ")"));
                    return;
                }
                cap.toggleSkill(key);
                boolean disabled = cap.isSkillDisabled(key);
                com.example.examplemod.network.PacketHandler.INSTANCE.send(
                        net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> player),
                        new com.example.examplemod.network.ShokugiSyncPacket(cap.getLevel(), cap.getEatCount(), cap.getDisabledSkills())
                );
                String state = disabled ? "§c[無効(OFF)]" : "§a[有効(ON)]";
                player.sendSystemMessage(Component.literal("§e『" + key + "』§fのスキルを " + state + " §fに切り替えました！"));
                player.sendSystemMessage(Component.literal("§7(もう一度説明欄のボタンを押すと元に戻せます)"));
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
                    player.sendSystemMessage(Component.literal("§7スキル「" + skillName + "」の詳細が見つかりません。名前の一部（不壊など）を入れてみてください。"));
                    return;
                }
                player.sendSystemMessage(Component.literal(getSkillDetailText(key)));

                // 習得状況による表示分けとON/OFF UI
                if (cap.getLevel() < getRequiredLevel(key)) {
                    player.sendSystemMessage(Component.literal("§8[未修得] あなたの現在の食義レベルではまだON/OFF操作はできません。必要Lv:" + getRequiredLevel(key)));
                } else {
                    boolean disabled = cap.isSkillDisabled(key);
                    String stateText = disabled ? "§c【 現在の状態: OFF 】" : "§a【 現在の状態: ON 】";
                    net.minecraft.network.chat.MutableComponent toggleBtn = net.minecraft.network.chat.Component.literal("\n" + stateText + " §7(ココをクリックで即時切り替え)")
                            .withStyle(net.minecraft.network.chat.Style.EMPTY
                                    .withClickEvent(new net.minecraft.network.chat.ClickEvent(net.minecraft.network.chat.ClickEvent.Action.RUN_COMMAND, "/foodhealing syokugi toggle " + key))
                                    .withHoverEvent(new net.minecraft.network.chat.HoverEvent(net.minecraft.network.chat.HoverEvent.Action.SHOW_TEXT, net.minecraft.network.chat.Component.literal("ON/OFFを切り替えます"))));
                    player.sendSystemMessage(toggleBtn);
                }
            });
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Error"));
        }
        return 1;
    }

    private static String getSkillKey(String name) {
        if (name.contains("不壊") || name.equals("不壊")) return "不壊";
        if (name.contains("採取") || name.equals("採取")) return "採取";
        if (name.contains("屠殺") || name.equals("屠殺")) return "屠殺";
        if (name.contains("追撃") || name.equals("追撃")) return "追撃";
        if (name.contains("炎") || name.equals("炎")) return "炎";
        if (name.contains("火事場") || name.equals("火事場力")) return "火事場力";
        if (name.contains("満足感") || name.contains("TaCZ") || name.equals("満足感")) return "満足感";
        if (name.contains("耐火") || name.equals("耐火")) return "耐火";
        if (name.contains("水月") || name.contains("暗視") || name.equals("水月と暗視")) return "水月と暗視";
        if (name.contains("早食い") || name.equals("早食い")) return "早食い";
        if (name.contains("軽業") || name.equals("軽業")) return "軽業";
        if (name.contains("爆破") || name.equals("爆破耐性")) return "爆破耐性";
        if (name.contains("浄化") || name.equals("浄化")) return "浄化";
        if (name.contains("豊穣") || name.equals("豊穣")) return "豊穣";
        if (name.contains("極意") || name.equals("防具の極意")) return "防具の極意";
        if (name.contains("飛翔") || name.equals("飛翔")) return "飛翔";
        if (name.contains("金剛") || name.equals("金剛")) return "金剛";
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

    private static String getSkillDetailText(String key) {
        switch (key) {
            case "不壊": return "§e【不壊の心得】§r\nバニラの「耐久力（Unbreaking）」エンチャントと完全に重複・重ね掛けが機能します。\nさらにLv17からはL2 Hostilityの防具破壊（Durability Destroyerなど）を完全無効化するメタ特性も追加されます。";
            case "採取": return "§e【採取の心得】§r\n幸運エンチャントで増幅されたアイテム（鉱石・作物に加え、特例としてグロウストーン・粘土 等）のドロップ量が大幅に増加します。エンチャントによる増加判定の後に掛け算処理が入るため、幸運レベルが高ければ高いほど獲得量が爆発的に増えます。";
            case "屠殺": return "§e【屠殺の心得】§r\n牛や豚などの友好MOBを倒した際のドロップアイテムが通常の3倍になります。ドロップ増加（Looting）エンチャントと重掛け（重複計算）が可能です。";
            case "追撃": return "§e【追撃の心得 極】§r\nLv991以降で発動する最強のやり込み能力。敵への攻撃時、無敵時間を無視して確定で追加ダメージを与えます。Lvが1上がるごとに追撃回数が1回増加し、Lv999到達時には合計9回の追撃がすべて同時に叩き込まれます。";
            case "炎": return "§e【炎の加護】§r\n自身が「燃え上がっている状態（炎上中）」に全ての攻撃による被ダメージが無条件に30%減少します。耐火の心得ですでに炎ダメージは0になるため、燃えている状態を意図的に維持すれば常時ダメージカットの要塞化が可能です。";
            case "火事場力": return "§e【火事場力解放】§r\nHPが最大値の一定割合以下になった際にすべての与ダメージが数倍に跳ね上がる最強のバフ。食義の基本攻撃倍率の後に掛け算（乗算）されるため最終火力が天文学的な数値になります。";
            case "満足感": return "§e【満足感 / TaCZ連携】§r\nTaCZの銃を撃つ際、確率で弾薬を消費せずに撃つ事ができるようになります。レベル11から10%の確率で発動し、最後には完全に無消費（無限弾薬）になります。レベル11以上ならミニガンのオーバーヒート強制解除も内包しています。";
            case "耐火": return "§e【耐火の心得】§r\n常に「火炎耐性」が付与され、マグマや炎を完全に無効化します。";
            case "水月と暗視": return "§e【水月と暗視の心得】§r\n常に「水中呼吸」と「暗視」が付与され、探索のストレスを排除します。";
            case "早食い": return "§e【早食いⅠ】§r\nあらゆる食料を食べる時間が「通常の半分（半減）」になります。";
            case "軽業": return "§e【軽業の心得】§r\n落下ダメージを完全に無効化（0ダメージ）します。";
            case "爆破耐性": return "§e【爆破耐性の心得】§r\nクリーパーやTNTなどのすべての爆発ダメージを強制的に10分の1（90%軽減）にします。";
            case "浄化": return "§e【浄化の心得】§r\n毎ティック、プレイヤーに付与された「デバフ（毒、弱体化など）」を自動で検知し即座に消去します。";
            case "豊穣": return "§e【豊穣の心得】§r\nクラフト画面で食料アイテムを作成した際、完成品のスタック数が自動で2倍になります。";
            case "防具の極意": return "§e【防具の極意】§r\nどれほど巨大な被ダメージを受けても、防具の耐久度減少量を「強制的に1被弾＝1減少」に固定します。不壊の心得と重複して計算されます。";
            case "飛翔": return "§e【飛翔の心得】§r\n常にクリエイティブ飛行が可能になり、空を自由に飛び回れます。";
            case "金剛": return "§e【金剛の心得】§r\n常に強力な「耐性IV」が付与されます。防御ダメージ計算の前に固定で被ダメージを激減させます。";
            default: return "§7詳細が見つかりません。";
        }
    }
}
