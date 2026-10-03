package com.leva.foodhealing;

import com.leva.foodhealing.capability.IShokugiData;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.mojang.authlib.GameProfile;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.util.List;
import java.util.UUID;

@GameTestHolder(FoodHealingMod.MODID)
@PrefixGameTestTemplate(false)
public final class FoodHealingExistingSkillsGameTests {
    private static final String EMPTY_TEMPLATE = "empty";

    private FoodHealingExistingSkillsGameTests() {
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void gatheringMultipliesFinalBlockLootForSkilledPlayer(GameTestHelper helper) {
        BlockPos position = helper.absolutePos(BlockPos.ZERO.above());
        BlockState clay = Blocks.CLAY.defaultBlockState();
        helper.getLevel().setBlockAndUpdate(position, clay);

        Player unskilled = helper.makeMockSurvivalPlayer();
        List<ItemStack> baseDrops = Block.getDrops(
                clay, helper.getLevel(), position, null, unskilled, ItemStack.EMPTY);
        helper.assertTrue(count(baseDrops, Items.CLAY_BALL) == 4,
                "test precondition failed: vanilla clay did not produce four final drops");

        Player skilled = helper.makeMockSurvivalPlayer();
        IShokugiData skilledData = shokugi(skilled);
        skilledData.setSkillLevel(FoodHealingSkillIds.GATHERING, 1);
        List<ItemStack> multipliedDrops = Block.getDrops(
                clay, helper.getLevel(), position, null, skilled, ItemStack.EMPTY);
        helper.assertTrue(count(multipliedDrops, Items.CLAY_BALL) == 8,
                "Gathering did not transform the final clay loot from four to eight items");

        skilledData.setSkillDisabled(FoodHealingSkillIds.GATHERING, true);
        List<ItemStack> disabledDrops = Block.getDrops(
                clay, helper.getLevel(), position, null, skilled, ItemStack.EMPTY);
        helper.assertTrue(count(disabledDrops, Items.CLAY_BALL) == 4,
                "disabled Gathering still transformed final block loot");
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void durabilitySkillsAreScopedAndUseExactSaveChanceBoundary(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        IShokugiData data = shokugi(player);
        ItemStack pickaxe = new ItemStack(Items.IRON_PICKAXE);
        ItemStack chestplate = new ItemStack(Items.IRON_CHESTPLATE);

        data.setSkillLevel(FoodHealingSkillIds.UNBREAKING, 1);
        helper.assertTrue(DurabilityTransactions.adjustDamage(data, pickaxe, 3, 0.899F) == 0,
                "Unbreaking Lv1 did not prevent damage below its 90 percent boundary");
        helper.assertTrue(DurabilityTransactions.adjustDamage(data, pickaxe, 3, 0.90F) == 3,
                "Unbreaking Lv1 prevented damage at or above its 90 percent boundary");
        data.setSkillDisabled(FoodHealingSkillIds.UNBREAKING, true);
        helper.assertTrue(DurabilityTransactions.adjustDamage(data, pickaxe, 3, 0.0F) == 3,
                "disabled Unbreaking still prevented durability damage");

        data.setSkillDisabled(FoodHealingSkillIds.UNBREAKING, false);
        data.setSkillLevel(FoodHealingSkillIds.UNBREAKING, 0);
        data.setSkillLevel(FoodHealingSkillIds.ARMOR_MASTERY, 1);
        helper.assertTrue(DurabilityTransactions.adjustDamage(data, chestplate, 5, 1.0F) == 1,
                "Armor Mastery did not cap one armor damage transaction at one");
        helper.assertTrue(DurabilityTransactions.adjustDamage(data, pickaxe, 5, 1.0F) == 1,
                "Armor Mastery did not cap a standard tool durability transaction");

        data.setSkillDisabled(FoodHealingSkillIds.ARMOR_MASTERY, true);
        helper.assertTrue(DurabilityTransactions.adjustDamage(data, chestplate, 5, 1.0F) == 5,
                "disabled Armor Mastery still capped armor durability damage");
        data.setSkillDisabled(FoodHealingSkillIds.ARMOR_MASTERY, false);
        helper.assertTrue(DurabilityTransactions.adjustDamage(
                        data, new ItemStack(Items.BREAD), 5, 0.0F) == 5,
                "durability skills changed a non-damageable unrelated item");

        RandomSource actual = RandomSource.create(12345L);
        RandomSource expected = RandomSource.create(12345L);
        DurabilityTransactions.adjustDamage(data, pickaxe, 5, actual);
        helper.assertTrue(actual.nextInt() == expected.nextInt(),
                "durability handling consumed RNG when Unbreaking was not active");
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void pursuitAddsOneHitAndRestoresTargetInvulnerabilityState(GameTestHelper helper) {
        Player attacker = helper.makeMockSurvivalPlayer();
        IShokugiData data = shokugi(attacker);
        data.setSkillLevel(FoodHealingSkillIds.PURSUIT, 1);
        Cow target = helper.spawnWithNoFreeWill(EntityType.COW, 0, 1, 0);
        float fullHealth = target.getMaxHealth();
        target.setHealth(fullHealth);
        target.invulnerableTime = 13;

        LivingDamageEvent original = new LivingDamageEvent(
                target, helper.getLevel().damageSources().playerAttack(attacker), 3.0F);
        DamageEventHandler.onLivingDamage(original);
        helper.assertTrue(close(target.getHealth(), fullHealth - 3.0F),
                "Pursuit did not apply exactly one fresh-v3 follow-up hit");
        helper.assertTrue(target.invulnerableTime == 13,
                "Pursuit did not restore the target's original invulnerability state");

        data.setSkillDisabled(FoodHealingSkillIds.PURSUIT, true);
        target.setHealth(fullHealth);
        LivingDamageEvent disabled = new LivingDamageEvent(
                target, helper.getLevel().damageSources().playerAttack(attacker), 3.0F);
        DamageEventHandler.onLivingDamage(disabled);
        helper.assertTrue(close(target.getHealth(), fullHealth) && target.invulnerableTime == 13,
                "disabled Pursuit applied a follow-up hit or changed invulnerability state");
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void managedSkillsPreserveForeignEffectsAndPurifyOnlyHarmfulEffects(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        IShokugiData data = shokugi(player);
        data.setSkillLevel(FoodHealingSkillIds.FIRE_RESISTANCE, 1);
        data.setSkillLevel(FoodHealingSkillIds.WATER_NIGHT_VISION, 1);
        data.setSkillLevel(FoodHealingSkillIds.KONGO, 1);
        data.setSkillLevel(FoodHealingSkillIds.PURIFICATION, 1);

        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 600, 1));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 4));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 2));
        player.addEffect(new MobEffectInstance(MobEffects.POISON, 600, 0));
        runFoodHealingSkillTick(player);

        helper.assertTrue(amplifier(player, MobEffects.FIRE_RESISTANCE) == 1,
                "managed Fire Resistance overwrote a stronger foreign effect");
        helper.assertTrue(amplifier(player, MobEffects.DAMAGE_RESISTANCE) == 4,
                "Kongo overwrote a stronger foreign Resistance effect");
        helper.assertTrue(amplifier(player, MobEffects.MOVEMENT_SPEED) == 2,
                "Purification removed an unrelated beneficial effect");
        helper.assertTrue(player.getEffect(MobEffects.POISON) == null,
                "Purification did not remove a harmful effect");
        helper.assertTrue(player.hasEffect(MobEffects.WATER_BREATHING)
                        && player.hasEffect(MobEffects.NIGHT_VISION),
                "Water/Night Vision skill did not add its managed effects");

        player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 600, 1));
        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 600, 1));
        data.setSkillDisabled(FoodHealingSkillIds.FIRE_RESISTANCE, true);
        data.setSkillDisabled(FoodHealingSkillIds.WATER_NIGHT_VISION, true);
        data.setSkillDisabled(FoodHealingSkillIds.KONGO, true);
        data.setSkillDisabled(FoodHealingSkillIds.PURIFICATION, true);
        data.setSkillLevel(FoodHealingSkillIds.FLIGHT, 1);
        data.setSkillDisabled(FoodHealingSkillIds.FLIGHT, true);
        player.getAbilities().mayfly = false;
        player.addEffect(new MobEffectInstance(MobEffects.POISON, 600, 0));
        runFoodHealingSkillTick(player);

        helper.assertTrue(amplifier(player, MobEffects.FIRE_RESISTANCE) == 1
                        && amplifier(player, MobEffects.DAMAGE_RESISTANCE) == 4
                        && amplifier(player, MobEffects.WATER_BREATHING) == 1
                        && amplifier(player, MobEffects.NIGHT_VISION) == 1,
                "disabling Food Healing skills removed a foreign-owned effect");
        helper.assertTrue(player.hasEffect(MobEffects.POISON),
                "disabled Purification still removed a newly applied harmful effect");
        helper.assertTrue(!player.getAbilities().mayfly,
                "disabled Flight granted a server-side flight permission");

        LivingDamageEvent disabledKongo = new LivingDamageEvent(
                player, helper.getLevel().damageSources().generic(), 100.0F);
        DamageEventHandler.onLivingDamage(disabledKongo);
        helper.assertTrue(close(disabledKongo.getAmount(), 100.0F),
                "disabled Kongo still applied Food Healing's damage reduction layer");
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void fastEatingAndDefensiveSkillsUseExactToggleBoundaries(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        IShokugiData data = shokugi(player);

        data.setSkillLevel(FoodHealingSkillIds.FAST_EATING, 1);
        LivingEntityUseItemEvent.Start fastEating = new LivingEntityUseItemEvent.Start(
                player, new ItemStack(Items.BREAD), 32);
        FoodHealingHandler.onItemUseStart(fastEating);
        helper.assertTrue(fastEating.getDuration() == 16,
                "Fast Eating did not halve a 32-tick food use duration");
        FoodHealingTransactions.clearPlayer(player.getUUID());

        data.setSkillDisabled(FoodHealingSkillIds.FAST_EATING, true);
        LivingEntityUseItemEvent.Start disabledFastEating = new LivingEntityUseItemEvent.Start(
                player, new ItemStack(Items.BREAD), 32);
        FoodHealingHandler.onItemUseStart(disabledFastEating);
        helper.assertTrue(disabledFastEating.getDuration() == 32,
                "disabled Fast Eating still changed food use duration");
        FoodHealingTransactions.clearPlayer(player.getUUID());

        data.setSkillLevel(FoodHealingSkillIds.ACROBATICS, 1);
        LivingDamageEvent fall = new LivingDamageEvent(
                player, helper.getLevel().damageSources().fall(), 10.0F);
        DamageEventHandler.onLivingDamage(fall);
        helper.assertTrue(fall.isCanceled(), "Acrobatics did not cancel fall damage");
        data.setSkillDisabled(FoodHealingSkillIds.ACROBATICS, true);
        LivingDamageEvent disabledFall = new LivingDamageEvent(
                player, helper.getLevel().damageSources().fall(), 10.0F);
        DamageEventHandler.onLivingDamage(disabledFall);
        helper.assertTrue(!disabledFall.isCanceled(),
                "disabled Acrobatics still canceled fall damage");

        data.setSkillLevel(FoodHealingSkillIds.FIRE_RESISTANCE, 1);
        LivingDamageEvent fire = new LivingDamageEvent(
                player, helper.getLevel().damageSources().onFire(), 10.0F);
        DamageEventHandler.onLivingDamage(fire);
        helper.assertTrue(fire.isCanceled(), "Fire Resistance did not cancel fire damage");
        data.setSkillDisabled(FoodHealingSkillIds.FIRE_RESISTANCE, true);
        LivingDamageEvent disabledFire = new LivingDamageEvent(
                player, helper.getLevel().damageSources().onFire(), 10.0F);
        DamageEventHandler.onLivingDamage(disabledFire);
        helper.assertTrue(!disabledFire.isCanceled(),
                "disabled Fire Resistance still canceled fire damage");

        data.setSkillLevel(FoodHealingSkillIds.EXPLOSION_RESISTANCE, 1);
        LivingDamageEvent explosion = new LivingDamageEvent(
                player, helper.getLevel().damageSources().explosion(null, null), 100.0F);
        DamageEventHandler.onLivingDamage(explosion);
        helper.assertTrue(close(explosion.getAmount(), 10.0F),
                "Explosion Resistance did not leave exactly 10 percent damage");
        data.setSkillDisabled(FoodHealingSkillIds.EXPLOSION_RESISTANCE, true);
        LivingDamageEvent disabledExplosion = new LivingDamageEvent(
                player, helper.getLevel().damageSources().explosion(null, null), 100.0F);
        DamageEventHandler.onLivingDamage(disabledExplosion);
        helper.assertTrue(close(disabledExplosion.getAmount(), 100.0F),
                "disabled Explosion Resistance still reduced explosion damage");

        data.setSkillLevel(FoodHealingSkillIds.FLAME_BLESSING, 1);
        player.setSecondsOnFire(5);
        LivingDamageEvent burning = new LivingDamageEvent(
                player, helper.getLevel().damageSources().generic(), 100.0F);
        DamageEventHandler.onLivingDamage(burning);
        helper.assertTrue(close(burning.getAmount(), 70.0F),
                "Flame Blessing did not leave exactly 70 percent damage while burning");
        data.setSkillDisabled(FoodHealingSkillIds.FLAME_BLESSING, true);
        LivingDamageEvent disabledBurning = new LivingDamageEvent(
                player, helper.getLevel().damageSources().generic(), 100.0F);
        DamageEventHandler.onLivingDamage(disabledBurning);
        helper.assertTrue(close(disabledBurning.getAmount(), 100.0F),
                "disabled Flame Blessing still reduced damage");
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void satisfactionUsesOneRollAndPreservesTheOriginalFoodStack(GameTestHelper helper) {
        ServerPlayer player = new ServerPlayer(
                helper.getLevel().getServer(), helper.getLevel(),
                new GameProfile(UUID.randomUUID(), "foodhealing-satisfaction-test"));
        Connection connection = new Connection(PacketFlow.SERVERBOUND);
        EmbeddedChannel channel = new EmbeddedChannel(connection);
        try {
            helper.getLevel().getServer().getPlayerList().placeNewPlayer(connection, player);
            IShokugiData data = shokugi(player);
            data.setSkillLevel(FoodHealingSkillIds.SATISFACTION, 3);

            long preserveSeed = seedForSatisfactionResult(true);
            RandomSource expectedAfterPreserve = RandomSource.create(preserveSeed);
            expectedAfterPreserve.nextFloat();
            player.getRandom().setSeed(preserveSeed);
            player.getFoodData().setFoodLevel(10);
            ItemStack preservedStew = new ItemStack(Items.MUSHROOM_STEW);
            preservedStew.getOrCreateTag().putString("foodhealing_test", "preserved");
            SatisfactionTransactions.beginFoodUse(player, preservedStew);
            ItemStack preservedResult = preservedStew.finishUsingItem(helper.getLevel(), player);

            helper.assertTrue(preservedResult == preservedStew
                            && preservedResult.is(Items.MUSHROOM_STEW)
                            && preservedResult.getCount() == 1
                            && "preserved".equals(preservedResult.getTag().getString("foodhealing_test")),
                    "successful Satisfaction did not preserve the exact original food stack and NBT");
            helper.assertTrue(player.getFoodData().getFoodLevel() == 16,
                    "successful Satisfaction did not apply the food effect exactly once");
            helper.assertTrue(player.getRandom().nextInt() == expectedAfterPreserve.nextInt(),
                    "successful Satisfaction consumed more than one random roll");

            long consumeSeed = seedForSatisfactionResult(false);
            RandomSource expectedAfterConsume = RandomSource.create(consumeSeed);
            expectedAfterConsume.nextFloat();
            player.getRandom().setSeed(consumeSeed);
            player.getFoodData().setFoodLevel(10);
            ItemStack consumedStew = new ItemStack(Items.MUSHROOM_STEW);
            SatisfactionTransactions.beginFoodUse(player, consumedStew);
            ItemStack consumedResult = consumedStew.finishUsingItem(helper.getLevel(), player);

            helper.assertTrue(consumedResult.is(Items.BOWL) && consumedResult.getCount() == 1,
                    "failed Satisfaction roll did not preserve vanilla container return behavior");
            helper.assertTrue(player.getFoodData().getFoodLevel() == 16,
                    "failed Satisfaction roll did not apply the food effect exactly once");
            helper.assertTrue(player.getRandom().nextInt() == expectedAfterConsume.nextInt(),
                    "failed Satisfaction roll consumed more than one random roll");

            data.setSkillDisabled(FoodHealingSkillIds.SATISFACTION, true);
            long disabledSeed = seedForSatisfactionResult(true);
            RandomSource expectedDisabled = RandomSource.create(disabledSeed);
            player.getRandom().setSeed(disabledSeed);
            ItemStack disabledBread = new ItemStack(Items.BREAD);
            SatisfactionTransactions.beginFoodUse(player, disabledBread);
            disabledBread.finishUsingItem(helper.getLevel(), player);
            helper.assertTrue(player.getRandom().nextInt() == expectedDisabled.nextInt(),
                    "disabled Satisfaction consumed a random roll");
        } finally {
            channel.finishAndReleaseAll();
        }
        helper.succeed();
    }

    private static IShokugiData shokugi(Player player) {
        return player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .orElseThrow(() -> new GameTestAssertException("Shokugi capability is missing"));
    }

    private static int count(List<ItemStack> stacks, net.minecraft.world.item.Item item) {
        return stacks.stream()
                .filter(stack -> stack.is(item))
                .mapToInt(ItemStack::getCount)
                .sum();
    }

    private static void runFoodHealingSkillTick(Player player) {
        ShokugiTickHandler.onPlayerTick(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, player));
    }

    private static int amplifier(Player player, net.minecraft.world.effect.MobEffect effect) {
        MobEffectInstance instance = player.getEffect(effect);
        return instance == null ? -1 : instance.getAmplifier();
    }

    private static long seedForSatisfactionResult(boolean preserve) {
        for (long seed = 0L; seed < 10_000L; seed++) {
            boolean candidatePreserves = RandomSource.create(seed).nextFloat() < 0.75F;
            if (candidatePreserves == preserve) {
                return seed;
            }
        }
        throw new IllegalStateException("Could not find a deterministic Satisfaction test seed");
    }

    private static boolean close(double actual, double expected) {
        return Math.abs(actual - expected) < 1.0E-5D;
    }
}
