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

            // Lv 17-19: Sharpness (Chance to ignore durability loss) 業物
            if (newAmount > 0) {
                float saveChance = 0.0f;
                if (level >= 19) saveChance = 0.75f;
                else if (level >= 18) saveChance = 0.50f;
                else if (level >= 17) saveChance = 0.25f;

                if (saveChance > 0 && player.getRandom().nextFloat() < saveChance) {
                    newAmount = 0;
                }
            }

            return newAmount;
        }).orElse(amount);
    }
}
