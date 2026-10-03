package com.leva.foodhealing.compat.flight;

import com.leva.foodhealing.FlightAuthority;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;

/** Metadata-only gates precede all linkage to optional gameplay/API classes. */
public final class FlightProviders {
    private static boolean reportedFailure;
    private FlightProviders() { }

    public static boolean exact(String id, String version) {
        return ModList.get().getModContainerById(id)
                .map(mod -> version.equals(mod.getModInfo().getVersion().toString())).orElse(false);
    }

    public static FlightAuthority query(ServerPlayer player) {
        try {
            FlightAuthority result = FlightAuthority.NEUTRAL;
            if (exact("ending_library", "2.1.19fix")) result = EndingLibraryFlightProvider.query(player);
            if (exact("fantasy_ending", "2.7.20") && exact("ending_library", "2.1.19fix")
                    && exact("curios", "5.14.1+1.20.1")) {
                result = result.and(FantasyEndingFlightProvider.query(player));
            }
            if (MekanismFlightGate.supported()) result = result.and(MekanismFlightProvider.query(player));
            if (AvaritiaFlightGate.supported()) result = result.and(AvaritiaFlightProvider.query(player));
            return result;
        } catch (RuntimeException | LinkageError failure) {
            if (!reportedFailure) {
                reportedFailure = true;
                com.mojang.logging.LogUtils.getLogger().error("[FoodHealing] Flight provider query unavailable; abilities preserved", failure);
            }
            return FlightAuthority.UNKNOWN;
        }
    }
}
