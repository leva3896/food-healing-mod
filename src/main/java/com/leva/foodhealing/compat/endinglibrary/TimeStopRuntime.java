package com.leva.foodhealing.compat.endinglibrary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.level.ServerPlayer;
public final class TimeStopRuntime {
    public static void initialize() { MinecraftForge.EVENT_BUS.register(TimeStopRuntime.class); }
    @SubscribeEvent public static void stopped(ServerStoppedEvent e) { AuthorityContext.close(e.getServer()); }
    @SubscribeEvent public static void leave(EntityLeaveLevelEvent e) { Ownership.lifecycle(e.getEntity(),"entity-leave"); }
    @SubscribeEvent public static void login(PlayerEvent.PlayerLoggedInEvent e) { if(e.getEntity() instanceof ServerPlayer p)SessionRegistry.login(p); }
    @SubscribeEvent public static void logout(PlayerEvent.PlayerLoggedOutEvent e) { if(e.getEntity() instanceof ServerPlayer p)SessionRegistry.revoke(p,"logout"); }
    @SubscribeEvent public static void dimension(PlayerEvent.PlayerChangedDimensionEvent e) { if(e.getEntity() instanceof ServerPlayer p)SessionRegistry.login(p); }
    @SubscribeEvent public static void clonePlayer(PlayerEvent.Clone e) { if(e.getOriginal() instanceof ServerPlayer p)SessionRegistry.revoke(p,"clone"); }
    @SubscribeEvent public static void tick(TickEvent.ServerTickEvent e) {
        if(e.phase!=TickEvent.Phase.END)return;
        var server=ServerLifecycleHooks.getCurrentServer(); if(server==null)return;
        var c=AuthorityContext.of(server); if(c.closed)return;
        for(var l:server.getAllLevels()){InitialStart.serverEnd(l);EntryDeserialize.serverEnd(l);Ownership.reconcile(l);}
        c.engine.serverEnd();
        for(var p:server.getPlayerList().getPlayers())SessionRegistry.tick(p);
    }
}
