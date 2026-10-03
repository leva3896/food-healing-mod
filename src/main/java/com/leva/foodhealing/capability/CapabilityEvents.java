package com.leva.foodhealing.capability;

import com.leva.foodhealing.FoodHealingBaseStats;
import com.leva.foodhealing.FoodDiversityHandler;
import com.leva.foodhealing.FoodHealingMod;
import com.leva.foodhealing.PlayerHealthLifecycle;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FoodHealingMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityEvents {

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(ShokugiProvider.SHOKUGI_CAPA).isPresent()) {
                event.addCapability(new ResourceLocation(FoodHealingMod.MODID, "shokugi_data"), new ShokugiProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        PlayerHealthLifecycle.captureCloneHealth(
                event.getOriginal(), event.getEntity(), event.isWasDeath());

        // Copy all Food Healing player capabilities during one revive/invalidate window.
        event.getOriginal().reviveCaps();

        event.getOriginal().getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(oldData -> {
            event.getEntity().getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(newData -> {
                newData.copyFrom(oldData);
                FoodHealingBaseStats.rebuildOwnedModifiers(event.getEntity(), newData);
            });
        });
        event.getOriginal().getCapability(FoodDiversityProvider.FOOD_DIVERSITY).ifPresent(oldData -> {
            event.getEntity().getCapability(FoodDiversityProvider.FOOD_DIVERSITY).ifPresent(newData -> {
                newData.copyFrom(oldData);
                FoodDiversityHandler.applyHealthBonus(event.getEntity(), newData.getMaxHealthBonus());
            });
        });

        event.getOriginal().invalidateCaps();
        syncToClient(event.getEntity());
        FoodDiversityHandler.syncToClient(event.getEntity());
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(net.minecraftforge.event.entity.EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide() && event.getEntity() instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            rebuildOwnedModifiers(serverPlayer);
            syncToClient(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        rebuildOwnedModifiers(event.getEntity());
        syncToClient(event.getEntity());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        rebuildAllOwnedModifiers(player);
        PlayerHealthLifecycle.clampCurrentHealth(player);
        syncAllToClient(player);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        rebuildAllOwnedModifiers(player);
        PlayerHealthLifecycle.restoreRespawnHealth(player, event.isEndConquered());
        syncAllToClient(player);
    }

    public static void rebuildOwnedModifiers(Player player) {
        if (player.level().isClientSide()) {
            return;
        }
        player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .ifPresent(data -> FoodHealingBaseStats.rebuildOwnedModifiers(player, data));
    }

    private static void rebuildAllOwnedModifiers(Player player) {
        rebuildOwnedModifiers(player);
        player.getCapability(FoodDiversityProvider.FOOD_DIVERSITY)
                .ifPresent(data -> FoodDiversityHandler.applyHealthBonus(player, data.getMaxHealthBonus()));
    }

    private static void syncAllToClient(Player player) {
        syncToClient(player);
        FoodDiversityHandler.syncToClient(player);
    }

    public static void syncToClient(Player player) {
        if (player.level().isClientSide() || !(player instanceof net.minecraft.server.level.ServerPlayer serverPlayer)) {
            return;
        }

        serverPlayer.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
            com.leva.foodhealing.network.PacketHandler.INSTANCE.send(
                net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> serverPlayer),
                new com.leva.foodhealing.network.ShokugiSyncPacket(cap.serializeNBT())
            );
        });
    }
}
