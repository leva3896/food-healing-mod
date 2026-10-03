package com.leva.foodhealing.client;

import com.leva.foodhealing.FoodHealingMod;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FoodHealingMod.MODID, value = Dist.CLIENT)
public final class ClientSatisfactionState {
    private static ItemStack expectedStack = ItemStack.EMPTY;
    private static boolean preserve;

    private ClientSatisfactionState() {
    }

    public static synchronized void accept(ItemStack stack, boolean shouldPreserve) {
        expectedStack = stack == null ? ItemStack.EMPTY : stack.copy();
        preserve = shouldPreserve && !expectedStack.isEmpty();
    }

    public static synchronized Decision consume(ItemStack actualStack) {
        if (expectedStack.isEmpty() || !ItemStack.matches(expectedStack, actualStack)) {
            return Decision.NONE;
        }
        Decision result = preserve ? Decision.PRESERVE : Decision.CONSUME;
        clear();
        return result;
    }

    public static synchronized void clear() {
        expectedStack = ItemStack.EMPTY;
        preserve = false;
    }

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        clear();
    }

    public enum Decision {
        NONE,
        CONSUME,
        PRESERVE
    }
}
