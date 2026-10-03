package com.leva.foodhealing.network;

import com.leva.foodhealing.FoodHealingBaseStats;
import com.leva.foodhealing.FoodHealingBaseStatIds;
import com.leva.foodhealing.FoodHealingSkillIds;
import com.leva.foodhealing.FoodHealingSkills;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/** Presentation only; callers pass the authoritative transaction result. */
public final class PurchaseMessages {
    private PurchaseMessages() { }

    public static MutableComponent skill(FoodHealingSkills.PurchaseResult result, String skillId, int level) {
        if (result != FoodHealingSkills.PurchaseResult.SUCCESS) {
            return Component.translatable(result.messageKey());
        }
        String id = FoodHealingSkillIds.normalize(skillId);
        Component name = Component.translatable("skill.foodhealing." + path(id) + ".name");
        return level == 1
                ? Component.translatable("message.foodhealing.skill.learned", name)
                : Component.translatable("message.foodhealing.skill.level_up", name, level);
    }

    public static MutableComponent stat(FoodHealingBaseStats.PurchaseResult result, String statId) {
        if (result == FoodHealingBaseStats.PurchaseResult.SUCCESS) {
            String namePath = path(statId).replace("_linear", "");
            return Component.translatable("message.foodhealing.stat.increased",
                    Component.translatable("stat.foodhealing." + namePath));
        }
        String key = switch (result) {
            case INSUFFICIENT_SP -> "message.foodhealing.stat.insufficient_sp";
            case OPTIONAL_MOD_MISSING -> FoodHealingBaseStatIds.TACZ_BASE_OUTGOING_DAMAGE.equals(statId)
                    ? "message.foodhealing.tacz_base_required" : "message.foodhealing.high_difficulty_required";
            case STALE_REQUEST -> "message.foodhealing.stat.stale";
            case NUMERIC_LIMIT -> "message.foodhealing.stat.limit";
            default -> "message.foodhealing.stat.unknown";
        };
        return Component.translatable(key);
    }

    private static String path(String id) {
        return id.substring(id.indexOf(':') + 1);
    }
}
