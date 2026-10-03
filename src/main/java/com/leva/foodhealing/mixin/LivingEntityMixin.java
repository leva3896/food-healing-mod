package com.leva.foodhealing.mixin;

import com.leva.foodhealing.HeroicsController;
import com.leva.foodhealing.capability.ShokugiProvider;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Redirect(
            method = "getDamageAfterArmorAbsorb",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterAbsorb(FFF)F"))
    private float foodhealing$applyHeroicsEffectiveArmor(float damage, float armor, float toughness) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.level().isClientSide() || !(entity instanceof Player player)) {
            return CombatRules.getDamageAfterAbsorb(damage, armor, toughness);
        }

        return player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .map(data -> {
                    if (HeroicsController.armorMultiplier(player, data) == 1.0D) {
                        return CombatRules.getDamageAfterAbsorb(damage, armor, toughness);
                    }
                    float reduced = CombatRules.getDamageAfterAbsorb(damage,
                            HeroicsController.effectiveArmor(player, data),
                            HeroicsController.effectiveToughness(player, data));
                    // Expanded Armor can invert vanilla clamp's min/max. Preserve its
                    // MAX_ARMOR absorption ceiling before the separate dedicated DR layer.
                    float minimumRemaining = 1.0F - CombatRules.MAX_ARMOR / CombatRules.ARMOR_PROTECTION_DIVIDER;
                    return Math.max(damage * minimumRemaining, reduced);
                })
                .orElseGet(() -> CombatRules.getDamageAfterAbsorb(damage, armor, toughness));
    }
}
