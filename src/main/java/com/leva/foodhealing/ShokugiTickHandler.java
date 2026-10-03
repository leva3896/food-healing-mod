package com.leva.foodhealing;

import com.leva.foodhealing.capability.ShokugiProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShokugiTickHandler {
    static final UUID HEROICS_ARMOR_UUID = HeroicsController.LEGACY_ARMOR_MODIFIER_UUID;
    static final UUID HEROICS_TOUGHNESS_UUID = HeroicsController.LEGACY_TOUGHNESS_MODIFIER_UUID;
    private static final String OWNED_EFFECTS_KEY = FoodHealingMod.MODID + ":owned_effects";
    private static final int MANAGED_EFFECT_DURATION = 240;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        
        Player player = event.player;
        if (player.level().isClientSide()) return; // Only process server-side

        player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
            if (FoodHealingSkills.isEnabled(cap, FoodHealingSkillIds.FIRE_RESISTANCE)) {
                applyManagedEffect(player, MobEffects.FIRE_RESISTANCE, 0);
            } else {
                clearManagedEffectOwnership(player, MobEffects.FIRE_RESISTANCE);
            }

            if (FoodHealingSkills.isEnabled(cap, FoodHealingSkillIds.WATER_NIGHT_VISION)) {
                applyManagedEffect(player, MobEffects.WATER_BREATHING, 0);
                applyManagedEffect(player, MobEffects.NIGHT_VISION, 0);
            } else {
                clearManagedEffectOwnership(player, MobEffects.WATER_BREATHING);
                clearManagedEffectOwnership(player, MobEffects.NIGHT_VISION);
            }

            if (FoodHealingSkills.isEnabled(cap, FoodHealingSkillIds.PURIFICATION)) {
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

            if (FoodHealingSkills.isEnabled(cap, FoodHealingSkillIds.KONGO)) {
                applyManagedEffect(player, MobEffects.DAMAGE_RESISTANCE, 3);
            } else {
                clearManagedEffectOwnership(player, MobEffects.DAMAGE_RESISTANCE);
            }

            clearLegacyHeroicsAttributeModifiers(player);
        });
    }

    private static void applyManagedEffect(Player player, MobEffect effect, int amplifier) {
        String effectId = effectId(effect);
        if (effectId == null) {
            return;
        }

        CompoundTag ownership = player.getPersistentData().getCompound(OWNED_EFFECTS_KEY);
        MobEffectInstance current = player.getEffect(effect);
        long now = player.level().getGameTime();
        if (current == null) {
            if (player.addEffect(new MobEffectInstance(
                    effect, MANAGED_EFFECT_DURATION, amplifier, false, false, true))) {
                recordManagedEffect(ownership, effectId, amplifier, now + MANAGED_EFFECT_DURATION);
                player.getPersistentData().put(OWNED_EFFECTS_KEY, ownership);
            }
            return;
        }

        if (!ownership.contains(effectId, CompoundTag.TAG_COMPOUND)) {
            return;
        }
        CompoundTag marker = ownership.getCompound(effectId);
        long expectedRemaining = Math.max(0L, marker.getLong("Until") - now);
        if (marker.getInt("Amplifier") != current.getAmplifier()
                || Math.abs((long) current.getDuration() - expectedRemaining) > 3L) {
            ownership.remove(effectId);
            player.getPersistentData().put(OWNED_EFFECTS_KEY, ownership);
            return;
        }

        if (current.getDuration() < 200 && player.addEffect(new MobEffectInstance(
                effect, MANAGED_EFFECT_DURATION, amplifier, false, false, true))) {
            recordManagedEffect(ownership, effectId, amplifier, now + MANAGED_EFFECT_DURATION);
            player.getPersistentData().put(OWNED_EFFECTS_KEY, ownership);
        }
    }

    private static void clearManagedEffectOwnership(Player player, MobEffect effect) {
        String effectId = effectId(effect);
        if (effectId == null) {
            return;
        }
        CompoundTag ownership = player.getPersistentData().getCompound(OWNED_EFFECTS_KEY);
        if (ownership.contains(effectId)) {
            ownership.remove(effectId);
            player.getPersistentData().put(OWNED_EFFECTS_KEY, ownership);
        }
    }

    private static void recordManagedEffect(CompoundTag ownership, String effectId, int amplifier, long until) {
        CompoundTag marker = new CompoundTag();
        marker.putInt("Amplifier", amplifier);
        marker.putLong("Until", until);
        ownership.put(effectId, marker);
    }

    private static String effectId(MobEffect effect) {
        var id = ForgeRegistries.MOB_EFFECTS.getKey(effect);
        return id == null ? null : id.toString();
    }

    private static void clearLegacyHeroicsAttributeModifiers(Player player) {
        removeOwnedModifier(player.getAttribute(Attributes.ARMOR), HEROICS_ARMOR_UUID);
        removeOwnedModifier(player.getAttribute(Attributes.ARMOR_TOUGHNESS), HEROICS_TOUGHNESS_UUID);
    }

    private static void removeOwnedModifier(AttributeInstance attribute, UUID modifierId) {
        if (attribute != null && attribute.getModifier(modifierId) != null) {
            attribute.removeModifier(modifierId);
        }
    }
}
