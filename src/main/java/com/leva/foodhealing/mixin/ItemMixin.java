package com.leva.foodhealing.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Redirect(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;canEat(Z)Z"))
    private boolean foodhealing$allowStandardFoodAtFullHunger(Player player, boolean vanillaAlwaysEat) {
        // Item.use remains the canonical path. Passing true only relaxes the hunger-full gate;
        // spectator/creative restrictions inside Player.canEat remain intact.
        return player.canEat(true);
    }
}
