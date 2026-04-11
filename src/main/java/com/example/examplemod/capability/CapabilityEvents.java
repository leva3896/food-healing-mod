package com.example.examplemod.capability;

import com.example.examplemod.FoodHealingMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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
        // Only run on server, and ensures data is kept across death/dimension change
        event.getOriginal().reviveCaps();
        
        event.getOriginal().getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(oldData -> {
            event.getEntity().getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(newData -> {
                newData.copyFrom(oldData);
            });
        });
        
        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(net.minecraftforge.event.entity.EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide() && event.getEntity() instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            serverPlayer.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
                com.example.examplemod.network.PacketHandler.INSTANCE.send(
                    net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new com.example.examplemod.network.ShokugiSyncPacket(cap.getLevel(), cap.getEatCount(), cap.getDisabledSkills())
                );
            });
        }
    }
}
