package com.leva.foodhealing;

import com.google.gson.stream.JsonReader;
import com.leva.foodhealing.capability.ShokugiData;
import com.leva.foodhealing.network.PurchaseMessages;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

final class LocalizationRegression {
    private static int assertions;
    static void run() {
        Map<String, String> ja = read("ja_jp"), en = read("en_us");
        require(ja.keySet().equals(en.keySet()), "language key parity");
        for (String prefix : List.of("command.foodhealing.skills.", "command.foodhealing.toggle.",
                "command.foodhealing.detail.", "state.foodhealing.")) {
            require(ja.keySet().stream().noneMatch(k -> k.startsWith(prefix)), "obsolete chat UI keys: " + prefix);
        }
        require(ja.keySet().stream().noneMatch(k -> k.startsWith("skill.foodhealing.")
                && (k.endsWith(".title") || k.endsWith(".desc"))), "obsolete v2 command skill labels");
        for (String key : ja.keySet()) {
            require(!ja.get(key).contains("食義") && !ja.get(key).contains("購入しました"), "old Japanese: " + key);
            require(!en.get(key).contains("Purchased"), "old English success: " + key);
            require(tokens(ja.get(key), "%[0-9$]*[sd]").equals(tokens(en.get(key), "%[0-9$]*[sd]")), "format arguments: " + key);
            if (key.endsWith(".description") || key.startsWith("stat.") && key.endsWith(".desc")) {
                require(tokens(ja.get(key), "[0-9]+(?:\\.[0-9]+)?").equals(tokens(en.get(key), "[0-9]+(?:\\.[0-9]+)?")), "numeric parity: " + key);
                require(!Pattern.compile("MobEffect|ServerPlayer|canonical|packet|GLM|RNG|ItemStack|hook|transaction|damageable|activeUntil|冪等").matcher(ja.get(key)+en.get(key)).find(), "technical tooltip: " + key);
            }
        }
        for (var d : FoodHealingSkills.orderedDefinitions()) {
            String key = "skill.foodhealing." + d.id().split(":")[1];
            require(ja.containsKey(key + ".name") && ja.containsKey(key + ".description"), "dynamic skill key: " + d.id());
        }
        for (String stat : List.of("base_defense", "base_damage_reduction", "high_difficulty_reduction", "base_max_health", "recovery_multiplier", "base_outgoing_damage", "tacz_base_outgoing_damage")) {
            require(ja.containsKey("stat.foodhealing." + stat) && ja.containsKey("stat.foodhealing." + stat + ".desc"), "dynamic stat key");
        }
        try (var paths = Files.walk(Path.of("src/main/java"))) {
            var keyPattern = Pattern.compile("(?:translatable|Tooltip\\.create)\\(\"((?:gui|message|command|skill|stat|hud|state|tooltip|key)\\.foodhealing\\.[a-z0-9_.]+)\"");
            for (Path file : paths.filter(p -> p.toString().endsWith(".java")).toList()) {
                var matches = keyPattern.matcher(Files.readString(file));
                while (matches.find()) require(ja.containsKey(matches.group(1)), "literal call-site key: " + matches.group(1));
            }
        } catch (Exception e) { throw new AssertionError(e); }
        skillMessages();
        var data = new ShokugiData(); data.setUnspentSkillPoints(5);
        var result = FoodHealingBaseStats.tryPurchase(data, FoodHealingBaseStatIds.BASE_DEFENSE, 0);
        Component msg = PurchaseMessages.stat(result, FoodHealingBaseStatIds.BASE_DEFENSE);
        require(key(msg).equals("message.foodhealing.stat.increased"), "base success");
        require(key((Component) ((TranslatableContents) msg.getContents()).getArgs()[0]).equals("stat.foodhealing.base_defense"), "base name");
        var saved = data.serializeNBT();
        require(key(PurchaseMessages.stat(FoodHealingBaseStats.tryPurchase(data, FoodHealingBaseStatIds.BASE_DEFENSE, 1), FoodHealingBaseStatIds.BASE_DEFENSE)).equals("message.foodhealing.stat.insufficient_sp"), "base insufficient");
        require(key(PurchaseMessages.stat(FoodHealingBaseStats.tryPurchase(data, FoodHealingBaseStatIds.BASE_DEFENSE, 0), FoodHealingBaseStatIds.BASE_DEFENSE)).equals("message.foodhealing.stat.stale"), "base replay");
        require(saved.equals(data.serializeNBT()), "message failures change no SP/state");
        System.out.println("LOCALIZATION REGRESSION PASS: " + assertions + " assertions; 26 skills / 7 stats / ja-en keys and numeric parity");
    }
    private static void skillMessages() {
        var data = new ShokugiData(); data.setUnspentSkillPoints(11);
        for (String id : List.of(FoodHealingSkillIds.FIRE_RESISTANCE, FoodHealingSkillIds.SATISFACTION)) {
            var result = FoodHealingSkills.tryPurchase(data,id,0);
            require(result == FoodHealingSkills.PurchaseResult.SUCCESS, "max1/leveled first transaction");
            Component msg = PurchaseMessages.skill(result,id,data.getSkillLevel(id));
            require(key(msg).equals("message.foodhealing.skill.learned"), "first learned key");
            require(key((Component)((TranslatableContents)msg.getContents()).getArgs()[0]).equals("skill.foodhealing."+id.split(":")[1]+".name"), "localized name argument");
        }
        String id=FoodHealingSkillIds.SATISFACTION;
        var result=FoodHealingSkills.tryPurchase(data,id,1);
        Component msg=PurchaseMessages.skill(result,id,data.getSkillLevel(id));
        require(key(msg).equals("message.foodhealing.skill.level_up") && ((TranslatableContents)msg.getContents()).getArgs()[1].equals(2), "canonical level2");
        var saved=data.serializeNBT();
        for (var pair : List.of(new Object[]{id,1,FoodHealingSkills.PurchaseResult.STALE_REQUEST}, new Object[]{id,2,FoodHealingSkills.PurchaseResult.INSUFFICIENT_SP}, new Object[]{FoodHealingSkillIds.FIRE_RESISTANCE,1,FoodHealingSkills.PurchaseResult.MAX_LEVEL})) {
            result=FoodHealingSkills.tryPurchase(data,(String)pair[0],(Integer)pair[1]);
            require(result==pair[2], "rejected transaction reason");
            require(key(PurchaseMessages.skill(result,(String)pair[0],999)).equals(result.messageKey()), "failure cannot select a success message");
            require(saved.equals(data.serializeNBT()), "failure unchanged");
        }
    }
    private static Map<String,String> read(String lang) {
        Map<String,String> result=new LinkedHashMap<>();
        try (var stream=LocalizationRegression.class.getResourceAsStream("/assets/foodhealing/lang/"+lang+".json"); var reader=new JsonReader(new InputStreamReader(Objects.requireNonNull(stream),StandardCharsets.UTF_8))) {
            reader.beginObject();
            while(reader.hasNext()) { String key=reader.nextName(); require(!result.containsKey(key),"duplicate key "+key); result.put(key,reader.nextString()); }
            reader.endObject();
        } catch(Exception e) {throw new AssertionError(e);}
        return result;
    }
    private static List<String> tokens(String s,String pattern) {return Pattern.compile(pattern).matcher(s).results().map(m->m.group()).sorted().toList();}
    private static String key(Component c) {return ((TranslatableContents)c.getContents()).getKey();}
    private static void require(boolean b,String message) {assertions++;if(!b)throw new AssertionError(message);}
}
