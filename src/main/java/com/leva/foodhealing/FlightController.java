package com.leva.foodhealing;

import com.leva.foodhealing.capability.ShokugiProvider;
import com.leva.foodhealing.compat.flight.FlightProviders;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/** Owns only actual false-to-true grants, keyed by the ServerPlayer instance, never UUID/NBT. */
public final class FlightController {
    private static final Set<ServerPlayer> OWNED = Collections.newSetFromMap(new IdentityHashMap<>());
    private FlightController() { }

    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player instanceof ServerPlayer player) evaluate(player);
    }

    public static void evaluate(ServerPlayer player) {
        boolean eligible = player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .map(FlightGrantPolicy::eligible).orElse(false);
        var abilities = player.getAbilities();
        boolean vanilla = player.isCreative() || player.isSpectator();
        var decision = FlightGrantPolicy.evaluate(abilities.mayfly, abilities.flying, OWNED.contains(player),
                vanilla, eligible, vanilla ? FlightAuthority.NEUTRAL : FlightProviders.query(player));
        if (decision.owns()) OWNED.add(player); else OWNED.remove(player);
        if (abilities.mayfly != decision.mayfly() || abilities.flying != decision.flying()) {
            abilities.mayfly = decision.mayfly();
            abilities.flying = decision.flying();
            player.onUpdateAbilities();
        }
    }

    public static boolean owns(ServerPlayer player) { return OWNED.contains(player); }

    @SubscribeEvent
    public static void logout(PlayerEvent.PlayerLoggedOutEvent event) { OWNED.remove(event.getEntity()); }

    @SubscribeEvent
    public static void clone(PlayerEvent.Clone event) {
        OWNED.remove(event.getOriginal());
        OWNED.remove(event.getEntity());
    }

    @SubscribeEvent
    public static void stopped(ServerStoppedEvent event) { OWNED.clear(); }
}
