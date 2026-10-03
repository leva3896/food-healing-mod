package com.leva.foodhealing;

import com.leva.foodhealing.capability.CapabilityEvents;
import com.leva.foodhealing.capability.IShokugiData;
import com.leva.foodhealing.capability.ShokugiProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class RootController {
    private static final int ROOT_THRESHOLD = 18;
    private static final long ACCUMULATION_WINDOW_TICKS = 15L * 20L;
    private static final int[] ACTIVE_TICKS = {0, 40, 80, 120, 200, 300};
    private static final int[] COOLDOWN_TICKS = {0, 400, 300, 200, 100, 0};

    private RootController() {
    }

    public static void onFoodConsumed(ServerPlayer player, int declaredNutrition) {
        if (declaredNutrition <= 0) {
            return;
        }
        player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .ifPresent(data -> recordNutrition(data, declaredNutrition, player.level().getGameTime()));
    }

    public static boolean isActive(ServerPlayer player) {
        return player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .map(data -> FoodHealingSkills.isEnabled(data, FoodHealingSkillIds.GUTS)
                        && data.getRootActiveUntil() > player.level().getGameTime())
                .orElse(false);
    }

    /** Called only by validated server-side player toggle actions, never by NBT/copy/sync. */
    public static void setSkillDisabled(IShokugiData data, String skillId, boolean disabled, long now) {
        boolean enablingTrueRoot = FoodHealingSkillIds.TRUE_GUTS.equals(skillId)
                && data.isSkillDisabled(skillId) && !disabled;
        data.setSkillDisabled(skillId, disabled);
        if (!enablingTrueRoot || !FoodHealingSkills.isEnabled(data, FoodHealingSkillIds.TRUE_GUTS)) {
            return;
        }
        int rootLevel = Math.min(5, FoodHealingSkills.getEnabledLevel(data, FoodHealingSkillIds.GUTS));
        if (rootLevel > 0 && data.getRootActiveUntil() <= now
                && data.getRootReservedNutrition() >= ROOT_THRESHOLD) {
            // Consume before activation; repeated ON requests and the next tick cannot reuse it.
            data.setRootReservedNutrition(0);
            activate(data, rootLevel, now);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()
                || !(event.player instanceof ServerPlayer player)) {
            return;
        }
        player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(data -> {
            boolean changed = advanceState(data, player.level().getGameTime());
            updateVisualEffect(player, data);
            if (changed) {
                CapabilityEvents.syncToClient(player);
            }
        });
    }

    static void recordNutrition(IShokugiData data, int nutrition, long now) {
        int rootLevel = Math.min(5, FoodHealingSkills.getEnabledLevel(data, FoodHealingSkillIds.GUTS));
        if (rootLevel <= 0) {
            resetState(data);
            return;
        }

        advanceState(data, now);

        if (data.getRootActiveUntil() > now) {
            if (FoodHealingSkills.isEnabled(data, FoodHealingSkillIds.TRUE_GUTS)) {
                data.setRootReservedNutrition(saturatingNutritionAdd(data.getRootReservedNutrition(), nutrition));
            }
            return;
        }
        if (now < data.getRootCooldownUntil()) {
            return;
        }

        if (now > data.getRootAccumulationDeadline()) {
            data.setRootAccumulatedNutrition(0);
        }
        int accumulated = saturatingNutritionAdd(data.getRootAccumulatedNutrition(), nutrition);
        if (accumulated >= ROOT_THRESHOLD) {
            activate(data, rootLevel, now);
        } else {
            if (data.getRootAccumulatedNutrition() == 0) {
                data.setRootAccumulationDeadline(safeAdd(now, ACCUMULATION_WINDOW_TICKS));
            }
            data.setRootAccumulatedNutrition(accumulated);
        }
    }

    static boolean advanceState(IShokugiData data, long now) {
        int rootLevel = Math.min(5, FoodHealingSkills.getEnabledLevel(data, FoodHealingSkillIds.GUTS));
        if (rootLevel <= 0) {
            boolean changed = hasState(data);
            resetState(data);
            return changed;
        }

        boolean accumulationExpired = data.getRootAccumulatedNutrition() > 0
                && now > data.getRootAccumulationDeadline();
        if (accumulationExpired) {
            data.setRootAccumulatedNutrition(0);
            data.setRootAccumulationDeadline(0L);
        }
        if (data.getRootActiveUntil() == 0L || now < data.getRootActiveUntil()) {
            return accumulationExpired;
        }

        data.setRootActiveUntil(0L);
        boolean reservationReady = FoodHealingSkills.isEnabled(data, FoodHealingSkillIds.TRUE_GUTS)
                && data.getRootReservedNutrition() >= ROOT_THRESHOLD;
        // Only a completed reservation survives an OFF-state expiry. Partial progress keeps its
        // existing behavior; this does not decide parent-toggle or lifecycle retention policy.
        boolean retainCompleted = data.isSkillDisabled(FoodHealingSkillIds.TRUE_GUTS)
                && FoodHealingSkills.getEffectiveLevel(data, FoodHealingSkillIds.TRUE_GUTS) > 0
                && data.getRootReservedNutrition() >= ROOT_THRESHOLD;
        if (!retainCompleted) {
            data.setRootReservedNutrition(0);
        }
        if (reservationReady) {
            activate(data, rootLevel, now);
        }
        return true;
    }

    private static void activate(IShokugiData data, int rootLevel, long now) {
        long activeUntil = safeAdd(now, ACTIVE_TICKS[rootLevel]);
        data.setRootActiveUntil(activeUntil);
        data.setRootCooldownUntil(safeAdd(activeUntil, COOLDOWN_TICKS[rootLevel]));
        data.setRootAccumulatedNutrition(0);
        data.setRootAccumulationDeadline(0L);
    }

    private static void updateVisualEffect(ServerPlayer player, IShokugiData data) {
        long remaining = data.getRootActiveUntil() - player.level().getGameTime();
        if (remaining <= 0L || !FoodHealingSkills.isEnabled(data, FoodHealingSkillIds.GUTS)) {
            player.removeEffect(FoodHealingMod.GUTS_EFFECT.get());
            return;
        }
        int duration = (int) Math.min(Integer.MAX_VALUE, remaining);
        int amplifier = Math.max(0, Math.min(4, data.getSkillLevel(FoodHealingSkillIds.GUTS) - 1));
        MobEffectInstance current = player.getEffect(FoodHealingMod.GUTS_EFFECT.get());
        if (current == null || current.getAmplifier() != amplifier || current.getDuration() < duration - 2) {
            player.addEffect(new MobEffectInstance(
                    FoodHealingMod.GUTS_EFFECT.get(), duration, amplifier, false, false, true));
        }
    }

    private static int saturatingNutritionAdd(int current, int amount) {
        if (amount <= 0) {
            return Math.max(0, current);
        }
        return Math.min(ROOT_THRESHOLD, current > ROOT_THRESHOLD - amount ? ROOT_THRESHOLD : current + amount);
    }

    private static long safeAdd(long value, long amount) {
        return value > Long.MAX_VALUE - amount ? Long.MAX_VALUE : value + amount;
    }

    private static boolean hasState(IShokugiData data) {
        return data.getRootAccumulatedNutrition() > 0
                || data.getRootAccumulationDeadline() > 0L
                || data.getRootActiveUntil() > 0L
                || data.getRootCooldownUntil() > 0L
                || data.getRootReservedNutrition() > 0;
    }

    private static void resetState(IShokugiData data) {
        data.setRootAccumulatedNutrition(0);
        data.setRootAccumulationDeadline(0L);
        data.setRootActiveUntil(0L);
        data.setRootCooldownUntil(0L);
        data.setRootReservedNutrition(0);
    }
}
