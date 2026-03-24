package com.example.examplemod;

import com.example.examplemod.capability.ShokugiProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ShokugiTickHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        
        Player player = event.player;
        if (player.level().isClientSide()) return; // Only process server-side

        player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
            int level = cap.getLevel();
            if (level == 0) return;

            // Lv 1: Permanent Fire Resistance
            if (level >= 1) {
                applyPermanentEffect(player, MobEffects.FIRE_RESISTANCE, 0);
            }

            // Lv 2: Permanent Water Breathing & Night Vision
            if (level >= 2) {
                applyPermanentEffect(player, MobEffects.WATER_BREATHING, 0);
                applyPermanentEffect(player, MobEffects.NIGHT_VISION, 0);
            }

            // Lv 5: Creative Flight
            if (level >= 5) {
                if (!player.getAbilities().mayfly) {
                    player.getAbilities().mayfly = true;
                    player.onUpdateAbilities();
                }
            } else {
                // Failsafe incase level is lost or reduced via capability overwrite logic
                if (!player.isCreative() && !player.isSpectator() && player.getAbilities().mayfly) {
                    player.getAbilities().mayfly = false;
                    player.getAbilities().flying = false;
                    player.onUpdateAbilities();
                }
            }

            // Lv 7: Debuff Immunity
            if (level >= 7) {
                List<MobEffect> toRemove = new ArrayList<>();
                for (MobEffectInstance instance : player.getActiveEffects()) {
                    if (instance.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                        toRemove.add(instance.getEffect());
                    }
                }
                for (MobEffect effect : toRemove) {
                    player.removeEffect(effect);
                }
            }

            // Lv 100: Permanent Resistance 4 (amplifier 3)
            if (level >= 100) {
                applyPermanentEffect(player, MobEffects.DAMAGE_RESISTANCE, 3);
            }
        });
    }

    private static void applyPermanentEffect(Player player, MobEffect effect, int amplifier) {
        MobEffectInstance current = player.getEffect(effect);
        // If they don't have it, or it was downgraded by another mod, or the duration is low (< 400 ticks / 20 seconds)
        // Note: Night vision flickers at < 200 ticks, so 400 is very safe.
        if (current == null || current.getAmplifier() < amplifier || current.getDuration() < 400) {
            // Apply a very long duration so we don't spam apply every tick,
            // but if they drink milk it's immediately put back the next tick.
            player.addEffect(new MobEffectInstance(effect, 10000, amplifier, false, false, true));
        }
    }
}
