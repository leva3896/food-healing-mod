package com.leva.foodhealing;

import com.leva.foodhealing.capability.IShokugiData;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.mojang.authlib.GameProfile;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.util.UUID;

@GameTestHolder(FoodHealingMod.MODID)
@PrefixGameTestTemplate(false)
public final class FoodHealingCombatGameTests {
    private static final String EMPTY_TEMPLATE = "empty";
    private static final UUID FOREIGN_ARMOR_UUID = UUID.fromString("2ea46da7-eded-43d9-a261-f984e176f96b");
    private static final UUID FOREIGN_TOUGHNESS_UUID = UUID.fromString("31f8b3a8-b311-4d91-8dc2-9b8ac82d70da");

    private FoodHealingCombatGameTests() {
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void inactiveHeroicsPreservesVanillaArmorInputs(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        Player attacker = helper.makeMockSurvivalPlayer();
        player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1000);
        player.getAttribute(Attributes.ARMOR).setBaseValue(40);
        for (int mode = 0; mode < 3; mode++) {
            IShokugiData data = shokugi(player);
            data.setSkillLevel(FoodHealingSkillIds.HEROICS, mode == 0 ? 0 : 5);
            data.setSkillDisabled(FoodHealingSkillIds.HEROICS, mode == 1);
            player.setHealth(1000);
            player.invulnerableTime = 0;
            float expectedDamage = net.minecraft.world.damagesource.CombatRules.getDamageAfterAbsorb(
                    100, player.getArmorValue(), (float) player.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
            player.hurt(helper.getLevel().damageSources().playerAttack(attacker), 100);
            helper.assertTrue(close(player.getHealth(), 1000 - expectedDamage),
                    "inactive/unowned/OFF Heroics replaced vanilla armor calculation in mode " + mode
                            + " health=" + player.getHealth() + " expected=" + (1000 - expectedDamage)
                            + " difficulty=" + helper.getLevel().getDifficulty());
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void heroicsLargeHitCannotInvertArmorReduction(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        Player attacker = helper.makeMockSurvivalPlayer();
        player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1000);
        IShokugiData data = shokugi(player);
        data.setBaseStatPoints(FoodHealingBaseStatIds.BASE_DEFENSE, 3);
        data.setSkillLevel(FoodHealingSkillIds.HEROICS, 5);
        FoodHealingBaseStats.rebuildOwnedModifiers(player, data);
        player.setHealth(400);
        player.hurt(helper.getLevel().damageSources().playerAttack(attacker), 1000);
        // Armor reaches vanilla's 80% absorption cap, then Heroics applies its separate 50% DR.
        helper.assertTrue(Math.abs(player.getHealth() - 300) < 0.001,
                "effective Armor 480 with a large hit produced negative/zero damage instead of 100; health="
                        + player.getHealth() + " difficulty=" + helper.getLevel().getDifficulty());
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void extremeIndependentReductionsNeverUnderflowToImmunity(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        IShokugiData data = shokugi(player);
        data.setBaseStatPoints(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR, 99);
        data.setBaseStatPoints(FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_LINEAR, 99);
        for (long stages : new long[]{306, 307, 1000, Long.MAX_VALUE}) {
            data.setBaseStatPoints(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_TRANSCENDENCE, stages);
            data.setBaseStatPoints(FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_TRANSCENDENCE, stages);
            LivingDamageEvent damage = new LivingDamageEvent(player, helper.getLevel().damageSources().generic(), 1);
            DamageEventHandler.onLivingDamage(damage);
            helper.assertTrue(!damage.isCanceled() && Float.isFinite(damage.getAmount()) && damage.getAmount() > 0,
                    "two independent positive reductions became complete immunity at stage " + stages);
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void rootProtectsLethalDamageDeathAndDirectZeroOnlyWhileActive(GameTestHelper helper) {
        ServerPlayer player = new ServerPlayer(
                helper.getLevel().getServer(), helper.getLevel(),
                new GameProfile(UUID.randomUUID(), "foodhealing-root-test"));
        Connection connection = new Connection(PacketFlow.SERVERBOUND);
        EmbeddedChannel channel = new EmbeddedChannel(connection);
        try {
            helper.getLevel().getServer().getPlayerList().placeNewPlayer(connection, player);
            IShokugiData data = shokugi(player);
            data.setSkillLevel(FoodHealingSkillIds.GUTS, 1);
            player.setHealth(player.getMaxHealth());

            RootController.onFoodConsumed(player, 18);
            helper.assertTrue(RootController.isActive(player),
                    "declared Nutrition 18 did not activate Root");

            LivingDamageEvent lethalDamage = new LivingDamageEvent(
                    player, helper.getLevel().damageSources().genericKill(), 1000.0F);
            DamageEventHandler.onLivingDamage(lethalDamage);
            helper.assertTrue(close(lethalDamage.getAmount(), player.getHealth() - 1.0F),
                    "active Root did not cap lethal damage at one remaining health");

            player.setHealth(1.0F);
            LivingDamageEvent atOneHealth = new LivingDamageEvent(
                    player, helper.getLevel().damageSources().fellOutOfWorld(), 1.0F);
            DamageEventHandler.onLivingDamage(atOneHealth);
            helper.assertTrue(atOneHealth.isCanceled(),
                    "active Root did not cancel damage at one health");

            player.setHealth(0.0F);
            LivingDeathEvent activeDeath = new LivingDeathEvent(
                    player, helper.getLevel().damageSources().genericKill());
            DamageEventHandler.onLivingDeath(activeDeath);
            helper.assertTrue(activeDeath.isCanceled() && close(player.getHealth(), 1.0F),
                    "active Root did not cancel a death event and restore one health");

            player.setHealth(0.0F);
            DamageEventHandler.onPlayerTick(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, player));
            helper.assertTrue(close(player.getHealth(), 1.0F),
                    "active Root did not recover direct HP zero on the server tick");

            data.setRootActiveUntil(player.level().getGameTime());
            player.setHealth(0.0F);
            LivingDeathEvent expiredDeath = new LivingDeathEvent(
                    player, helper.getLevel().damageSources().genericKill());
            DamageEventHandler.onLivingDeath(expiredDeath);
            helper.assertTrue(!expiredDeath.isCanceled() && close(player.getHealth(), 0.0F),
                    "expired Root continued to provide death protection");
        } finally {
            channel.finishAndReleaseAll();
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void heroicsUsesFinalLevelValuesAtExactThresholds(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        Cow target = helper.spawnWithNoFreeWill(EntityType.COW, 0, 1, 0);
        IShokugiData data = shokugi(player);
        double[] outgoing = {0.0D, 2.0D, 2.5D, 3.0D, 4.0D, 5.0D};

        for (int level = 1; level <= 5; level++) {
            data.setSkillLevel(FoodHealingSkillIds.HEROICS, level);
            player.setHealth(player.getMaxHealth() * 0.40F);

            LivingHurtEvent outgoingEvent = new LivingHurtEvent(
                    target, helper.getLevel().damageSources().playerAttack(player), 10.0F);
            DamageEventHandler.onLivingHurt(outgoingEvent);
            helper.assertTrue(close(outgoingEvent.getAmount(), 10.0D * outgoing[level]),
                    "Heroics outgoing damage did not use the final value for level " + level);

            LivingDamageEvent incomingEvent = new LivingDamageEvent(
                    player, helper.getLevel().damageSources().generic(), 100.0F);
            DamageEventHandler.onLivingDamage(incomingEvent);
            helper.assertTrue(close(incomingEvent.getAmount(), 100.0D * (1.0D - level * 0.10D)),
                    "Heroics DR did not use the final value for level " + level);
        }

        player.setHealth(Math.nextUp(player.getMaxHealth() * 0.40F));
        LivingHurtEvent aboveNormalThreshold = new LivingHurtEvent(
                target, helper.getLevel().damageSources().playerAttack(player), 10.0F);
        DamageEventHandler.onLivingHurt(aboveNormalThreshold);
        helper.assertTrue(close(aboveNormalThreshold.getAmount(), 10.0F),
                "normal Heroics activated above 40 percent health");

        data.setSkillLevel(FoodHealingSkillIds.TRUE_HEROICS, 1);
        player.setHealth(player.getMaxHealth() * 0.80F);

        data.setSkillDisabled(FoodHealingSkillIds.TRUE_HEROICS, true);
        LivingHurtEvent disabledTrueOutgoing = new LivingHurtEvent(
                target, helper.getLevel().damageSources().playerAttack(player), 10.0F);
        DamageEventHandler.onLivingHurt(disabledTrueOutgoing);
        helper.assertTrue(close(disabledTrueOutgoing.getAmount(), 10.0F),
                "disabled True Heroics still applied outgoing damage at 80 percent health");
        LivingDamageEvent disabledTrueIncoming = new LivingDamageEvent(
                player, helper.getLevel().damageSources().generic(), 100.0F);
        DamageEventHandler.onLivingDamage(disabledTrueIncoming);
        helper.assertTrue(close(disabledTrueIncoming.getAmount(), 100.0F),
                "disabled True Heroics still applied damage reduction at 80 percent health");
        helper.assertTrue(close(HeroicsController.armorMultiplier(player, data), 1.0D),
                "disabled True Heroics still applied Armor/Toughness x64 at 80 percent health");

        data.setSkillDisabled(FoodHealingSkillIds.TRUE_HEROICS, false);
        LivingHurtEvent trueOutgoing = new LivingHurtEvent(
                target, helper.getLevel().damageSources().playerAttack(player), 10.0F);
        DamageEventHandler.onLivingHurt(trueOutgoing);
        helper.assertTrue(close(trueOutgoing.getAmount(), 200.0F),
                "True Heroics did not apply x20 outgoing damage at exactly 80 percent health");

        LivingDamageEvent trueIncoming = new LivingDamageEvent(
                player, helper.getLevel().damageSources().generic(), 100.0F);
        DamageEventHandler.onLivingDamage(trueIncoming);
        helper.assertTrue(close(trueIncoming.getAmount(), 1.0F),
                "True Heroics did not apply 99 percent DR at exactly 80 percent health");

        player.setHealth(0.0F);
        LivingDeathEvent trueHeroicsDeath = new LivingDeathEvent(
                player, helper.getLevel().damageSources().genericKill());
        DamageEventHandler.onLivingDeath(trueHeroicsDeath);
        helper.assertTrue(!trueHeroicsDeath.isCanceled(),
                "True Heroics provided direct-death protection without active Root");
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void trueRootToggleControlsReservationWithoutDisablingNormalRoot(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        IShokugiData data = shokugi(player);
        data.setSkillLevel(FoodHealingSkillIds.GUTS, 5);
        data.setSkillLevel(FoodHealingSkillIds.TRUE_GUTS, 1);
        data.setSkillDisabled(FoodHealingSkillIds.GUTS, true);
        long now = player.level().getGameTime();
        RootController.recordNutrition(data, 18, now);
        helper.assertTrue(data.getRootActiveUntil() == 0L
                        && data.getRootAccumulatedNutrition() == 0,
                "disabled normal Root still accumulated Nutrition or activated");

        data.setSkillDisabled(FoodHealingSkillIds.GUTS, false);
        data.setSkillDisabled(FoodHealingSkillIds.TRUE_GUTS, true);

        RootController.recordNutrition(data, 18, now);
        long disabledWindowEnd = data.getRootActiveUntil();
        helper.assertTrue(disabledWindowEnd == now + 300L,
                "disabling True Root also disabled normal Root level 5");
        RootController.recordNutrition(data, 18, now + 1L);
        helper.assertTrue(data.getRootReservedNutrition() == 0,
                "disabled True Root created a new Nutrition reservation");
        RootController.advanceState(data, disabledWindowEnd);
        helper.assertTrue(data.getRootActiveUntil() == 0L,
                "disabled True Root reactivated Root from an OFF-state reservation");

        data.setSkillDisabled(FoodHealingSkillIds.TRUE_GUTS, false);
        RootController.recordNutrition(data, 18, disabledWindowEnd);
        long enabledWindowEnd = data.getRootActiveUntil();
        RootController.recordNutrition(data, 18, disabledWindowEnd + 1L);
        helper.assertTrue(data.getRootReservedNutrition() == 18,
                "re-enabled True Root did not create its one Nutrition reservation");
        RootController.advanceState(data, enabledWindowEnd);
        helper.assertTrue(data.getRootActiveUntil() == enabledWindowEnd + 300L
                        && data.getRootReservedNutrition() == 0,
                "re-enabled True Root did not consume one reservation and reactivate once");

        data.setRootActiveUntil(0L);
        data.setRootCooldownUntil(0L);
        RootController.recordNutrition(data, 18, enabledWindowEnd + 301L);
        data.setSkillDisabled(FoodHealingSkillIds.TRUE_GUTS, true);
        RootController.recordNutrition(data, 18, enabledWindowEnd + 302L);
        helper.assertTrue(data.getRootActiveUntil() == enabledWindowEnd + 601L
                        && data.getRootReservedNutrition() == 0,
                "turning True Root OFF broke normal Root or allowed a new reservation");
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void heroicsLevelFiveEffectiveArmorSurvivesReportedCreeperDamage(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        IShokugiData data = shokugi(player);
        data.setBaseStatPoints(FoodHealingBaseStatIds.BASE_MAX_HEALTH, 4L);
        data.setBaseStatPoints(FoodHealingBaseStatIds.BASE_DEFENSE, 3L);
        data.setSkillLevel(FoodHealingSkillIds.HEROICS, 5);
        FoodHealingBaseStats.rebuildOwnedModifiers(player, data);

        helper.assertTrue(close(player.getMaxHealth(), 28.0D),
                "reported Heroics fixture did not have 28 max health");
        helper.assertTrue(close(player.getAttributeValue(Attributes.ARMOR), 15.0D),
                "reported Heroics fixture did not have 15 base armor");

        player.setHealth(9.9F);
        ShokugiTickHandler.onPlayerTick(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, player));
        float effectiveArmor = HeroicsController.effectiveArmor(player, data);
        float effectiveToughness = HeroicsController.effectiveToughness(player, data);
        Creeper creeper = helper.spawnWithNoFreeWill(EntityType.CREEPER, 0, 1, 0);
        boolean accepted = player.hurt(helper.getLevel().damageSources().explosion(creeper, creeper), 49.0F);

        helper.assertTrue(accepted, "reported armor-applicable damage was not accepted");
        helper.assertTrue(player.isAlive() && player.getHealth() > 0.0F,
                "Heroics level 5 effective Armor x32 plus DR 50 percent did not prevent lethal damage"
                        + " (effective armor=" + effectiveArmor
                        + ", toughness=" + effectiveToughness
                        + ", health=" + player.getHealth() + ")");
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void heroicsEffectiveArmorScalesOffThroughLevelFiveAndReapplies(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        IShokugiData data = shokugi(player);
        AttributeInstance armor = requireAttribute(player, Attributes.ARMOR);
        AttributeInstance toughness = requireAttribute(player, Attributes.ARMOR_TOUGHNESS);
        data.setBaseStatPoints(FoodHealingBaseStatIds.BASE_DEFENSE, 3L);
        FoodHealingBaseStats.rebuildOwnedModifiers(player, data);
        armor.addPermanentModifier(new AttributeModifier(
                FOREIGN_ARMOR_UUID, "Foreign Armor", 5.0D, AttributeModifier.Operation.ADDITION));
        toughness.addPermanentModifier(new AttributeModifier(
                FOREIGN_TOUGHNESS_UUID, "Foreign Toughness", 2.0D, AttributeModifier.Operation.ADDITION));

        assertEffectiveHeroicsAttributes(helper, player, data, 20.0D, 2.0D, "Heroics OFF");
        for (int level = 1; level <= 5; level++) {
            data.setSkillLevel(FoodHealingSkillIds.HEROICS, level);
            player.setHealth(player.getMaxHealth() * 0.40F);
            double multiplier = Math.scalb(1.0D, level);
            assertEffectiveHeroicsAttributes(helper, player, data,
                    20.0D * multiplier, 2.0D * multiplier, "Heroics level " + level);
            helper.assertTrue(close(player.getAttributeValue(Attributes.ARMOR), 20.0D)
                            && close(player.getAttributeValue(Attributes.ARMOR_TOUGHNESS), 2.0D),
                    "Heroics level " + level + " overwrote the legitimate server attributes");
        }

        data.setSkillDisabled(FoodHealingSkillIds.HEROICS, true);
        assertEffectiveHeroicsAttributes(helper, player, data, 20.0D, 2.0D, "Heroics toggled OFF");

        data.setSkillDisabled(FoodHealingSkillIds.HEROICS, false);
        player.setHealth(Math.nextUp(player.getMaxHealth() * 0.40F));
        assertEffectiveHeroicsAttributes(helper, player, data, 20.0D, 2.0D, "Heroics above threshold");

        player.setHealth(player.getMaxHealth() * 0.40F);
        assertEffectiveHeroicsAttributes(helper, player, data, 640.0D, 64.0D,
                "Heroics reapplied below threshold");
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void heroicsLegacyModifiersAreRemovedIdempotentlyAndForeignModifiersRemain(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        IShokugiData data = shokugi(player);
        AttributeInstance armor = requireAttribute(player, Attributes.ARMOR);
        AttributeInstance toughness = requireAttribute(player, Attributes.ARMOR_TOUGHNESS);
        armor.addPermanentModifier(new AttributeModifier(
                FOREIGN_ARMOR_UUID, "Foreign Armor", 5.0D, AttributeModifier.Operation.ADDITION));
        toughness.addPermanentModifier(new AttributeModifier(
                FOREIGN_TOUGHNESS_UUID, "Foreign Toughness", 2.0D, AttributeModifier.Operation.ADDITION));
        armor.addTransientModifier(new AttributeModifier(
                ShokugiTickHandler.HEROICS_ARMOR_UUID, "Legacy Heroics Armor", 31.0D,
                AttributeModifier.Operation.MULTIPLY_TOTAL));
        toughness.addTransientModifier(new AttributeModifier(
                ShokugiTickHandler.HEROICS_TOUGHNESS_UUID, "Legacy Heroics Toughness", 31.0D,
                AttributeModifier.Operation.MULTIPLY_TOTAL));

        data.setSkillLevel(FoodHealingSkillIds.HEROICS, 5);
        player.setHealth(player.getMaxHealth() * 0.40F);
        assertEffectiveHeroicsAttributes(helper, player, data, 160.0D, 64.0D,
                "Heroics with legacy modifiers present");
        for (int rebuild = 0; rebuild < 5; rebuild++) {
            ShokugiTickHandler.onPlayerTick(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, player));
        }
        helper.assertTrue(armor.getModifier(ShokugiTickHandler.HEROICS_ARMOR_UUID) == null
                        && toughness.getModifier(ShokugiTickHandler.HEROICS_TOUGHNESS_UUID) == null,
                "legacy Heroics attribute modifiers were not removed");
        helper.assertTrue(armor.getModifier(FOREIGN_ARMOR_UUID) != null
                        && toughness.getModifier(FOREIGN_TOUGHNESS_UUID) != null,
                "Heroics legacy cleanup removed a foreign attribute modifier");

        player.setHealth(Math.nextUp(player.getMaxHealth() * 0.40F));
        ShokugiTickHandler.onPlayerTick(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, player));
        assertEffectiveHeroicsAttributes(helper, player, data, 5.0D, 2.0D,
                "Heroics deactivated above threshold");
        helper.assertTrue(armor.getModifier(FOREIGN_ARMOR_UUID) != null
                        && toughness.getModifier(FOREIGN_TOUGHNESS_UUID) != null,
                "Heroics deactivation removed a foreign attribute modifier");

        data.setSkillLevel(FoodHealingSkillIds.TRUE_HEROICS, 1);
        player.setHealth(player.getMaxHealth() * 0.80F);
        ShokugiTickHandler.onPlayerTick(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, player));
        assertEffectiveHeroicsAttributes(helper, player, data, 320.0D, 128.0D,
                "True Heroics");
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void normalAndHighDifficultyReductionMultiplyWithoutExactImmunity(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        IShokugiData data = shokugi(player);
        data.setBaseStatPoints(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR, 50L);
        data.setBaseStatPoints(FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_LINEAR, 50L);

        LivingDamageEvent combined = new LivingDamageEvent(
                player, helper.getLevel().damageSources().generic(), 100.0F);
        DamageEventHandler.onLivingDamage(combined);
        helper.assertTrue(close(combined.getAmount(), 25.0F),
                "normal and high-difficulty 50 percent reductions did not multiply to 25 percent remaining");

        data.setBaseStatPoints(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR, 99L);
        data.setBaseStatPoints(FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_LINEAR, 99L);
        LivingDamageEvent post99 = new LivingDamageEvent(
                player, helper.getLevel().damageSources().generic(), 100.0F);
        DamageEventHandler.onLivingDamage(post99);
        helper.assertTrue(post99.getAmount() > 0.0F && close(post99.getAmount(), 0.01F),
                "combined 99 percent reductions became additive immunity or used the wrong remaining value");

        data.setBaseStatPoints(FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_TRANSCENDENCE, 1L);
        LivingDamageEvent transcendent = new LivingDamageEvent(
                player, helper.getLevel().damageSources().generic(), 100.0F);
        DamageEventHandler.onLivingDamage(transcendent);
        helper.assertTrue(transcendent.getAmount() > 0.0F && close(transcendent.getAmount(), 0.001F),
                "post-99 high-difficulty reduction did not remain a positive multiplicative layer");
        helper.succeed();
    }

    private static void assertEffectiveHeroicsAttributes(GameTestHelper helper, Player player, IShokugiData data,
                                                         double expectedArmor, double expectedToughness,
                                                         String label) {
        helper.assertTrue(close(HeroicsController.effectiveArmor(player, data), expectedArmor),
                label + " effective armor was not " + expectedArmor);
        helper.assertTrue(close(HeroicsController.effectiveToughness(player, data), expectedToughness),
                label + " effective toughness was not " + expectedToughness);
    }

    private static IShokugiData shokugi(Player player) {
        return player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .orElseThrow(() -> new GameTestAssertException("Shokugi capability is missing"));
    }

    private static AttributeInstance requireAttribute(Player player,
                                                      net.minecraft.world.entity.ai.attributes.Attribute attribute) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance == null) {
            throw new GameTestAssertException("Required player attribute is missing: "
                    + attribute.getDescriptionId());
        }
        return instance;
    }

    private static boolean close(double actual, double expected) {
        return Math.abs(actual - expected) < 1.0E-5D;
    }
}
