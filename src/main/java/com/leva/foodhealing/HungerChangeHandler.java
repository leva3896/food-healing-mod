package com.leva.foodhealing;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import java.util.UUID;
/** Positive mutations observed at their FoodData call sites; never inferred from tick proximity. */
public class HungerChangeHandler {
    public static void onNonFoodIncrease(Player player,long units) {
        if (player.level().isClientSide() || units<=0) return;
        double multiplier=FoodHealingConfig.COMMON.healMultiplier.get();
        double recovery=player.getCapability(com.leva.foodhealing.capability.ShokugiProvider.SHOKUGI_CAPA)
            .map(FoodHealingBaseStats::recoveryMultiplier).orElse(1.0D);
        player.heal(FoodHealingBaseStats.multiplyPositiveDamage((float)(units*multiplier),recovery));
        ShokugiProgression.add(player,units);
        // Non-food progression must never enter RootController.
    }
    public static void clearPlayerData(UUID id) {
        FoodHealingTransactions.clearPlayer(id); SatisfactionTransactions.clearPlayer(id);
    }
    public static void resetPlayerSnapshot(Player player) {
        player.getFoodData(); // bind the runtime FoodData owner; no count/heal from loading state
        FoodHealingTransactions.clearPlayer(player.getUUID());
    }
    @SubscribeEvent public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e) { resetPlayerSnapshot(e.getEntity()); }
    @SubscribeEvent public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent e) { clearPlayerData(e.getEntity().getUUID()); }
    @SubscribeEvent public static void onPlayerClone(PlayerEvent.Clone e) { clearPlayerData(e.getOriginal().getUUID()); resetPlayerSnapshot(e.getEntity()); }
    @SubscribeEvent public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent e) { resetPlayerSnapshot(e.getEntity()); }
    @SubscribeEvent public static void onServerStopped(ServerStoppedEvent e) { FoodHealingTransactions.clearAll(); SatisfactionTransactions.clearAll(); }
}

