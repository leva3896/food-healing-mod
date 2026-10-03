package com.leva.foodhealing;

import com.leva.foodhealing.capability.IShokugiData;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.leva.foodhealing.network.PacketHandler;
import com.leva.foodhealing.network.SatisfactionDecisionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public final class SatisfactionTransactions {
    private static final long DECISION_TTL_TICKS = 20L * 20L;
    private static final Map<UUID, ServerDecision> DECISIONS = new HashMap<>();

    private SatisfactionTransactions() {
    }

    public static void beginFoodUse(ServerPlayer player, ItemStack stack) {
        cleanupExpired(player.level().getGameTime());
        boolean preserve = shouldPreserve(player);
        ItemStack expectedStack = stack.copy();
        DECISIONS.put(player.getUUID(), new ServerDecision(
                expectedStack, preserve, player.level().getGameTime()));
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
                new SatisfactionDecisionPacket(expectedStack, preserve));
    }

    public static boolean consumeDecision(ServerPlayer player, ItemStack stack) {
        cleanupExpired(player.level().getGameTime());
        ServerDecision decision = DECISIONS.remove(player.getUUID());
        if (decision == null) {
            // Custom feeders that bypass the normal use-start event still get one
            // authoritative server roll, but need an adapter for client prediction.
            return shouldPreserve(player);
        }
        return ItemStack.matches(decision.expectedStack(), stack) && decision.preserve();
    }

    public static void cancelFoodUse(ServerPlayer player) {
        DECISIONS.remove(player.getUUID());
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
                SatisfactionDecisionPacket.clear());
    }

    public static void clearPlayer(UUID playerId) {
        DECISIONS.remove(playerId);
    }

    public static void clearAll() {
        DECISIONS.clear();
    }

    private static boolean shouldPreserve(ServerPlayer player) {
        return player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .map(data -> shouldPreserve(data, player))
                .orElse(false);
    }

    private static boolean shouldPreserve(IShokugiData data, ServerPlayer player) {
        int level = FoodHealingSkills.getEnabledLevel(data, FoodHealingSkillIds.SATISFACTION);
        if (level <= 0) {
            return false;
        }
        float chance = Math.min(3, level) * 0.25F;
        return player.getRandom().nextFloat() < chance;
    }

    private static void cleanupExpired(long now) {
        Iterator<Map.Entry<UUID, ServerDecision>> iterator = DECISIONS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, ServerDecision> entry = iterator.next();
            if (now - entry.getValue().createdAtTick() > DECISION_TTL_TICKS) {
                iterator.remove();
            }
        }
    }

    private record ServerDecision(ItemStack expectedStack, boolean preserve, long createdAtTick) {
    }
}
