package com.example.examplemod.mixin;

import com.example.examplemod.capability.ShokugiProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @ModifyVariable(method = "hurt", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int foodhealing$modifyHurtAmount(int amount, int originalAmount, RandomSource random, ServerPlayer player) {
        if (player == null) {
            return amount;
        }

        return player.getCapability(ShokugiProvider.SHOKUGI_CAPA).map(cap -> {
            int level = cap.getLevel();
            int newAmount = amount;

            // Lv 20: Armor Mastery (Cap durability loss to 1) 武器等の耐久最大1ダウン
            if (level >= 20 && newAmount > 1) {
                newAmount = 1;
            }

            // Lv 17-19: Unbreakable buff (Chance to ignore durability loss)
            if (newAmount > 0) {
                float saveChance = 0.0f;
                // Lv19: 1/30 chance to break -> 29/30 chance to save (~96.67%)
                // Lv18: 1/20 chance to break -> 19/20 chance to save (95%)
                // Lv17: 1/10 chance to break -> 9/10 chance to save (90%)
                if (level >= 19) saveChance = 29.0f / 30.0f;
                else if (level >= 18) saveChance = 19.0f / 20.0f;
                else if (level >= 17) saveChance = 9.0f / 10.0f;

                if (saveChance > 0 && player.getRandom().nextFloat() < saveChance) {
                    newAmount = 0;
                }
            }

            return newAmount;
        }).orElse(amount);
    }
}
