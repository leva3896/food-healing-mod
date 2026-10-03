package com.leva.foodhealing.mixin;
import com.leva.foodhealing.FoodDataOwner;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(Player.class)
public abstract class PlayerFoodDataMixin {
    @Inject(method="getFoodData", at=@At("RETURN"))
    private void foodhealing$owner(CallbackInfoReturnable<FoodData> ci) {
        ((FoodDataOwner)ci.getReturnValue()).foodhealing$bind((Player)(Object)this);
    }
}

