package com.leva.foodhealing;

import com.leva.foodhealing.capability.ShokugiProvider;
import com.leva.foodhealing.compat.SuperbWarfareCompat;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/** Core/optional-absence regression, not an actual Superb Warfare integration fixture. */
@GameTestHolder(FoodHealingMod.MODID)
@PrefixGameTestTemplate(false)
public final class FoodHealingSuperbWarfareGameTests {
    @GameTest(template = "empty")
    public static void vanillaProjectileDoesNotGainDedicatedFactor(GameTestHelper helper) {
        var shooter = helper.makeMockSurvivalPlayer();
        var data = shooter.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(AssertionError::new);
        data.setLevel(200);
        data.setBaseStatPoints(FoodHealingBaseStatIds.BASE_OUTGOING_DAMAGE, 10);
        data.setBaseStatPoints(FoodHealingBaseStatIds.TACZ_BASE_OUTGOING_DAMAGE, 100);
        var target = helper.spawnWithNoFreeWill(EntityType.COW, 0, 1, 0);
        var arrow = new Arrow(helper.getLevel(), shooter);
        var source = helper.getLevel().damageSources().arrow(arrow, shooter);
        helper.assertTrue(!SuperbWarfareCompat.isSupportedGunfire(source), "vanilla route accepted");
        var event = new LivingHurtEvent(target, source, 10F);
        DamageEventHandler.onLivingHurt(event);
        helper.assertTrue(event.getAmount() == 20F, "dedicated or TaCZ factor leaked to vanilla arrow");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void genericAndOtherPlayerRemainIndependent(GameTestHelper helper) {
        var first = helper.makeMockSurvivalPlayer();
        first.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(AssertionError::new).setLevel(200);
        var other = helper.makeMockSurvivalPlayer();
        var target = helper.spawnWithNoFreeWill(EntityType.COW, 0, 1, 0);
        for (var source : new net.minecraft.world.damagesource.DamageSource[]{
                helper.getLevel().damageSources().generic(), helper.getLevel().damageSources().playerAttack(other)}) {
            helper.assertTrue(!SuperbWarfareCompat.isSupportedGunfire(source), "unrelated source accepted");
            var event = new LivingHurtEvent(target, source, 10F);
            DamageEventHandler.onLivingHurt(event);
            helper.assertTrue(event.getAmount() == 10F, "unrelated attack changed");
        }
        helper.succeed();
    }
}
