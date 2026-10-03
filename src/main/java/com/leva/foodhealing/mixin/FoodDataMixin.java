package com.leva.foodhealing.mixin;
import com.leva.foodhealing.FoodDataOwner;
import com.leva.foodhealing.HungerChangeHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(FoodData.class)
public abstract class FoodDataMixin implements FoodDataOwner {
    @Unique private Player foodhealing$owner;
    @Unique private int foodhealing$before;
    @Unique private boolean foodhealing$declaredFood;
    @Shadow private int foodLevel;
    @Override public void foodhealing$bind(Player player) { foodhealing$owner=player; }

    @Redirect(method="eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V",
        at=@At(value="INVOKE",target="Lnet/minecraft/world/food/FoodData;eat(IF)V",remap=true), remap=false)
    private void foodhealing$foodCause(FoodData data,int nutrition,float saturation) {
        boolean previous=foodhealing$declaredFood;
        foodhealing$declaredFood=true;
        try { data.eat(nutrition,saturation); } finally { foodhealing$declaredFood=previous; }
    }

    @Inject(method={"eat(IF)V","setFoodLevel"},at=@At("HEAD"))
    private void foodhealing$before(CallbackInfo ci) { foodhealing$before=foodLevel; }

    @Inject(method={"eat(IF)V","setFoodLevel"},at=@At("RETURN"))
    private void foodhealing$after(CallbackInfo ci) {
        if (!foodhealing$declaredFood && foodhealing$owner != null && foodLevel>foodhealing$before)
            HungerChangeHandler.onNonFoodIncrease(foodhealing$owner,(long)foodLevel-foodhealing$before);
    }
}

