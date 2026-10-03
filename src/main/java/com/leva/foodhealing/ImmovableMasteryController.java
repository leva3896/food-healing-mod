package com.leva.foodhealing;

import com.leva.foodhealing.capability.ShokugiProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/** Blocks only attack knockback at its native source; never repairs motion each tick. */
public final class ImmovableMasteryController {
    private ImmovableMasteryController() { }
    public static boolean protects(Entity entity) {
        return entity instanceof ServerPlayer player && player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .map(data -> FoodHealingSkills.isCanonicalSkillEnabled(data, FoodHealingSkillIds.IMMOVABLE_MASTERY))
                .orElse(false);
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void knockback(LivingKnockBackEvent event) {
        if (protects(event.getEntity())) event.setCanceled(true);
    }
}
