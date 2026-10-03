package com.leva.foodhealing.client;

import com.leva.foodhealing.PurificationMasteryController;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.leva.foodhealing.network.TimeStopLeasePacket;
import com.mega.endinglib.util.time.TimeStopUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/** A short-lived view of server permission, bound to actual current client objects. */
public final class TimeStopClientLease {
    private static TimeStopLeasePacket lease;
    private static Connection connection;
    private static ClientPacketListener listener;
    private static ClientLevel level;
    private static LocalPlayer player;
    private static long expires,session,revision,sequence;
    private static void clear() { lease=null;expires=0; }
    private static void reset() { clear();connection=null;listener=null;level=null;player=null;session=revision=sequence=0; }
    public static void receive(TimeStopLeasePacket m,Connection origin) {
        var mc=Minecraft.getInstance();
        if(mc.getConnection()==null||mc.player==null||mc.level==null||mc.getConnection().getConnection()!=origin
                ||!origin.isConnected()||!mc.player.getUUID().equals(m.player())
                ||!mc.level.dimension().location().equals(m.dimension()))return;
        if(connection!=origin||listener!=mc.getConnection()||level!=mc.level||player!=mc.player) {
            reset();connection=origin;listener=mc.getConnection();level=mc.level;player=mc.player;
        }
        if(m.session()<=0||m.revision()<=0||m.expiryMillis()<1||m.expiryMillis()>1000){clear();return;}
        if(session!=0&&session!=m.session()){clear();return;}
        session=m.session();if(m.revision()<=revision)return;revision=m.revision();
        if(!m.allowed()){clear();return;}
        if(m.epoch()<=0||m.sequence()<=0||m.sequence()<sequence){clear();return;}
        if(lease!=null&&m.epoch()<lease.epoch()){clear();return;}
        sequence=m.sequence();lease=m;expires=System.nanoTime()+m.expiryMillis()*1_000_000L;
    }
    public static boolean valid(Entity entity) {
        var mc=Minecraft.getInstance();
        if(lease==null||entity!=mc.player)return false;
        if(entity!=player||mc.level!=level||mc.getConnection()!=listener||listener==null
                ||listener.getConnection()!=connection||!connection.isConnected()||System.nanoTime()>=expires){clear();return false;}
        return lease.allowed()&&lease.session()==session&&lease.dimension().equals(level.dimension().location())
                &&TimeStopUtils.isTimeStop&&TimeStopUtils.andSameDimension(level)
                &&player.getCapability(ShokugiProvider.SHOKUGI_CAPA).map(PurificationMasteryController::isEnabled).orElse(false);
    }
    @SubscribeEvent public static void logout(ClientPlayerNetworkEvent.LoggingOut e){reset();}
    @SubscribeEvent public static void login(ClientPlayerNetworkEvent.LoggingIn e){reset();}
    @SubscribeEvent public static void clonePlayer(ClientPlayerNetworkEvent.Clone e){reset();}
    @SubscribeEvent public static void unload(LevelEvent.Unload e){if(e.getLevel().isClientSide())reset();}
    @SubscribeEvent public static void render(TickEvent.RenderTickEvent e){
        if(e.phase!=TickEvent.Phase.END||connection==null)return;
        var mc=Minecraft.getInstance();
        if(!connection.isConnected()||mc.getConnection()!=listener||mc.level!=level||mc.player!=player)reset();
        else if(lease!=null&&System.nanoTime()>=expires)clear();
    }
}
