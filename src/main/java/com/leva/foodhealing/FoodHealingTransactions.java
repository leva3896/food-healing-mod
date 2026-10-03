package com.leva.foodhealing;
import net.minecraft.world.entity.player.Player;
import java.util.UUID;
/** Food-derived deltas are now identified at FoodData's native item-food call site. */
public final class FoodHealingTransactions {
    private FoodHealingTransactions() { }
    public static void captureFoodUseStart(Player p) { }
    public static void captureFoodLevelBeforeFinish(Player p) { }
    public static void recordFoodConsumed(Player p,int n) { }
    public static void clearPlayer(UUID id) { }
    public static void clearFoodUse(UUID id) { }
    public static void clearAll() { }
    // Historical pure-math regression only; no production caller or runtime reservation.
    static HungerReconciliation reconcileHungerIncrease(int observed,int reserved) {
        return new HungerReconciliation(Math.max(0,observed)-Math.min(Math.max(0,observed),Math.max(0,reserved)),0);
    }
    record HungerReconciliation(int nonFoodUnits,int remainingReservation) { }
}
