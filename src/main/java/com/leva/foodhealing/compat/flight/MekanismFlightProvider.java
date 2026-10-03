package com.leva.foodhealing.compat.flight;

import com.leva.foodhealing.FlightAuthority;
import mekanism.common.CommonPlayerTickHandler;
import mekanism.common.registries.MekanismItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;

/** Only the approved chest/module route; readiness does not require active flying. */
final class MekanismFlightProvider {
    private MekanismFlightProvider() { }
    static FlightAuthority query(ServerPlayer player) {
        boolean positive = player.getItemBySlot(EquipmentSlot.CHEST).is(MekanismItems.MEKASUIT_BODYARMOR.get())
                && CommonPlayerTickHandler.isGravitationalModulationReady(player);
        return new FlightAuthority(positive, false, true);
    }
}
