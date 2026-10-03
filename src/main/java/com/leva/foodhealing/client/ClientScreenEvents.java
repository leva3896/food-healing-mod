package com.leva.foodhealing.client;

import com.leva.foodhealing.FoodHealingMod;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FoodHealingMod.MODID, value = Dist.CLIENT)
public final class ClientScreenEvents {
    private ClientScreenEvents() {
    }

    @SubscribeEvent
    public static void onInventoryKeyPressed(ScreenEvent.KeyPressed.Pre event) {
        if (FoodHealingGuiEntryPolicy.isAllowed(event.getScreen())
                && FoodHealingKeyMappings.OPEN_GUI.matches(event.getKeyCode(), event.getScanCode())) {
            Minecraft.getInstance().setScreen(new FoodHealingScreen(event.getScreen()));
            event.setCanceled(true);
        }
    }
}
