package com.leva.foodhealing;

import com.leva.foodhealing.capability.FoodDiversityProvider;
import com.leva.foodhealing.capability.CapabilityEvents;
import com.leva.foodhealing.capability.IFoodDiversityData;
import com.leva.foodhealing.capability.IShokugiData;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.mojang.authlib.GameProfile;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.ReferenceCountUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

@GameTestHolder(FoodHealingMod.MODID)
@PrefixGameTestTemplate(false)
public final class FoodHealingPriorityZeroGameTests {
    private static final String EMPTY_TEMPLATE = "empty";
    private static final UUID FOREIGN_ARMOR_UUID = UUID.fromString("954d577f-3247-42bd-a169-cfef18e875b4");
    private static final UUID FOREIGN_MAX_HEALTH_UUID = UUID.fromString("b54838a9-5c73-4684-a6c7-a23aa799907a");

    private FoodHealingPriorityZeroGameTests() {
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void cloneRespawnAndEndReturnPreserveCanonicalData(GameTestHelper helper) {
        Player original = helper.makeMockSurvivalPlayer();
        Player clone = helper.makeMockSurvivalPlayer();

        IShokugiData originalShokugi = shokugi(original);
        originalShokugi.setLevel(1_234L);
        originalShokugi.setEatCount(199L);
        originalShokugi.setUnspentSkillPoints(55L);
        originalShokugi.setSpentSkillPoints(21L);
        originalShokugi.setSkillLevel(FoodHealingSkillIds.FIRE_RESISTANCE, 1);
        originalShokugi.setSkillDisabled(FoodHealingSkillIds.FIRE_RESISTANCE, true);
        originalShokugi.setBaseStatPoints(FoodHealingBaseStatIds.BASE_DEFENSE, 3L);
        originalShokugi.setBaseStatPoints(FoodHealingBaseStatIds.BASE_MAX_HEALTH, 2L);
        originalShokugi.setRootAccumulatedNutrition(17);
        originalShokugi.setRootAccumulationDeadline(500L);

        IFoodDiversityData originalDiversity = diversity(original);
        originalDiversity.addEatenFood("minecraft:apple");
        originalDiversity.addEatenFood("minecraft:bread");
        originalDiversity.addMaxHealthBonus(6);

        AttributeInstance cloneArmor = requireAttribute(clone, Attributes.ARMOR);
        cloneArmor.addPermanentModifier(new AttributeModifier(
                FOREIGN_ARMOR_UUID, "Foreign armor modifier", 7.0D, AttributeModifier.Operation.ADDITION));

        MinecraftForge.EVENT_BUS.post(new PlayerEvent.Clone(clone, original, true));

        assertCanonicalDataCopied(helper, clone, originalShokugi.serializeNBT(), originalDiversity);
        assertOwnedModifiers(helper, clone, 15.0D, 4.0D, 6.0D);
        helper.assertTrue(cloneArmor.getModifier(FOREIGN_ARMOR_UUID) != null,
                "clone rebuild removed a foreign armor modifier");

        MinecraftForge.EVENT_BUS.post(new PlayerEvent.PlayerRespawnEvent(clone, false));
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.PlayerChangedDimensionEvent(
                clone, Level.OVERWORLD, Level.NETHER));
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.PlayerChangedDimensionEvent(
                clone, Level.END, Level.OVERWORLD));
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.PlayerRespawnEvent(clone, true));

        assertCanonicalDataCopied(helper, clone, originalShokugi.serializeNBT(), originalDiversity);
        assertOwnedModifiers(helper, clone, 15.0D, 4.0D, 6.0D);
        helper.assertTrue(cloneArmor.getModifier(FOREIGN_ARMOR_UUID) != null,
                "respawn or dimension rebuild removed a foreign armor modifier");
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void playerNbtRoundTripPreservesCanonicalDataAndOwnedModifiers(GameTestHelper helper) {
        Player original = helper.makeMockSurvivalPlayer();
        IShokugiData originalShokugi = shokugi(original);
        originalShokugi.setLevel(9_876_543_210L);
        originalShokugi.setEatCount(198L);
        originalShokugi.setUnspentSkillPoints(44L);
        originalShokugi.setSpentSkillPoints(32L);
        originalShokugi.setSkillLevel(FoodHealingSkillIds.GUTS, 5);
        originalShokugi.setSkillLevel(FoodHealingSkillIds.TRUE_GUTS, 1);
        originalShokugi.setSkillDisabled(FoodHealingSkillIds.TRUE_GUTS, true);
        originalShokugi.setBaseStatPoints(FoodHealingBaseStatIds.BASE_DEFENSE, 4L);
        originalShokugi.setBaseStatPoints(FoodHealingBaseStatIds.BASE_MAX_HEALTH, 3L);
        originalShokugi.setRootActiveUntil(12_345L);
        originalShokugi.setRootReservedNutrition(18);

        IFoodDiversityData originalDiversity = diversity(original);
        originalDiversity.addEatenFood("minecraft:apple");
        originalDiversity.addEatenFood("minecraft:carrot");
        originalDiversity.addMaxHealthBonus(8);
        FoodHealingBaseStats.rebuildOwnedModifiers(original, originalShokugi);
        FoodDiversityHandler.applyHealthBonus(original, originalDiversity.getMaxHealthBonus());

        CompoundTag expectedShokugi = originalShokugi.serializeNBT();
        Set<String> expectedCurrentFoods = Set.copyOf(originalDiversity.getEatenFoods());
        Set<String> expectedAllFoods = Set.copyOf(originalDiversity.getAllEatenFoods());
        CompoundTag savedPlayer = new CompoundTag();
        original.saveWithoutId(savedPlayer);
        helper.assertTrue(savedPlayer.contains("ForgeCaps", CompoundTag.TAG_COMPOUND),
                "serialized player NBT did not contain Forge capability data");

        Player reloaded = helper.makeMockSurvivalPlayer();
        AttributeInstance reloadedArmor = requireAttribute(reloaded, Attributes.ARMOR);
        reloaded.load(savedPlayer);
        reloadedArmor.addPermanentModifier(new AttributeModifier(
                FOREIGN_ARMOR_UUID, "Foreign armor modifier", 2.0D, AttributeModifier.Operation.ADDITION));
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.PlayerLoggedInEvent(reloaded));

        helper.assertTrue(shokugi(reloaded).serializeNBT().equals(expectedShokugi),
                "player save/reload changed canonical Shokugi data");
        assertDiversity(helper, reloaded, expectedCurrentFoods, expectedAllFoods, 8);
        assertOwnedModifiers(helper, reloaded, 20.0D, 6.0D, 8.0D);
        helper.assertTrue(reloadedArmor.getModifier(FOREIGN_ARMOR_UUID) != null,
                "login rebuild removed a foreign armor modifier after player NBT reload");
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void endReturnHealthClampsToFinalMaximum(GameTestHelper helper) {
        Player original = helper.makeMockSurvivalPlayer();
        Player replacement = helper.makeMockSurvivalPlayer();
        shokugi(original).setBaseStatPoints(FoodHealingBaseStatIds.BASE_MAX_HEALTH, 4L);
        diversity(original).addMaxHealthBonus(4);
        FoodHealingBaseStats.rebuildOwnedModifiers(original, shokugi(original));
        FoodDiversityHandler.applyHealthBonus(original, 4);
        requireAttribute(original, Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(
                FOREIGN_MAX_HEALTH_UUID, "Foreign max health modifier", 68.0D,
                AttributeModifier.Operation.ADDITION));
        helper.assertTrue(close(original.getMaxHealth(), 100.0D),
                "health clamp fixture did not produce max health 100");
        original.setHealth(63.0F);

        MinecraftForge.EVENT_BUS.post(new PlayerEvent.Clone(replacement, original, false));
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.PlayerRespawnEvent(replacement, true));

        helper.assertTrue(close(replacement.getMaxHealth(), 32.0D),
                "End-return clamp fixture did not rebuild final max health 32");
        helper.assertTrue(close(replacement.getHealth(), 32.0D),
                "End-return health was not clamped from 63 to final max health 32");
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE, timeoutTicks = 200)
    public static void actualRespawnAndDimensionTransferPreserveCanonicalData(GameTestHelper helper) {
        ServerPlayer original = new ServerPlayer(
                helper.getLevel().getServer(), helper.getLevel(),
                new GameProfile(UUID.randomUUID(), "foodhealing-lifecycle-test"));
        Connection connection = new Connection(PacketFlow.SERVERBOUND);
        EmbeddedChannel channel = new EmbeddedChannel(connection);
        ForeignMaxHealthCloneListener foreignHealthOwner = new ForeignMaxHealthCloneListener();
        MinecraftForge.EVENT_BUS.register(foreignHealthOwner);
        try {
            helper.getLevel().getServer().getPlayerList().placeNewPlayer(connection, original);
            IShokugiData originalData = shokugi(original);
            originalData.setLevel(4_321L);
            originalData.setEatCount(123L);
            originalData.setUnspentSkillPoints(76L);
            originalData.setSpentSkillPoints(54L);
            originalData.setSkillLevel(FoodHealingSkillIds.HEROICS, 5);
            originalData.setSkillLevel(FoodHealingSkillIds.TRUE_HEROICS, 1);
            originalData.setSkillDisabled(FoodHealingSkillIds.HEROICS, true);
            originalData.setBaseStatPoints(FoodHealingBaseStatIds.BASE_DEFENSE, 2L);
            originalData.setBaseStatPoints(FoodHealingBaseStatIds.BASE_MAX_HEALTH, 4L);
            originalData.setRootAccumulatedNutrition(16);
            originalData.setRootAccumulationDeadline(9_999L);
            diversity(original).addEatenFood("minecraft:bread");
            diversity(original).addMaxHealthBonus(4);
            FoodHealingBaseStats.rebuildOwnedModifiers(original, originalData);
            FoodDiversityHandler.applyHealthBonus(original, 4);
            requireAttribute(original, Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(
                    FOREIGN_MAX_HEALTH_UUID, "Foreign max health modifier", 68.0D,
                    AttributeModifier.Operation.ADDITION));
            helper.assertTrue(close(original.getMaxHealth(), 100.0D),
                    "actual lifecycle fixture did not produce max health 100");
            original.setHealth(63.0F);
            CompoundTag expectedShokugi = originalData.serializeNBT();
            Set<String> expectedFoods = Set.copyOf(diversity(original).getAllEatenFoods());

            ServerPlayer respawned = helper.getLevel().getServer().getPlayerList().respawn(original, false);
            helper.assertTrue(respawned != original,
                    "PlayerList respawn did not replace the ServerPlayer instance");
            helper.assertTrue(shokugi(respawned).serializeNBT().equals(expectedShokugi),
                    "actual death respawn changed canonical Shokugi data");
            assertDiversity(helper, respawned, expectedFoods, expectedFoods, 4);
            assertOwnedModifiers(helper, respawned, 10.0D, 8.0D, 4.0D);
            assertForeignMaxHealth(helper, respawned, 68.0D);
            helper.assertTrue(close(respawned.getHealth(), 100.0D),
                    "actual death respawn did not restore final max health 100");

            ServerLevel nether = helper.getLevel().getServer().getLevel(Level.NETHER);
            ServerLevel end = helper.getLevel().getServer().getLevel(Level.END);
            ServerLevel overworld = helper.getLevel().getServer().getLevel(Level.OVERWORLD);
            helper.assertTrue(nether != null && end != null && overworld != null,
                    "required dimensions are unavailable in the GameTest server");
            ITeleporter teleporter = new FixedTestTeleporter();
            respawned.setHealth(63.0F);
            Entity inNether = respawned.changeDimension(nether, teleporter);
            helper.assertTrue(inNether == respawned && respawned.level().dimension() == Level.NETHER,
                    "ServerPlayer did not complete the actual Nether dimension transfer");
            helper.assertTrue(close(respawned.getHealth(), 63.0D),
                    "Nether transfer changed current health 63");
            Entity backInOverworld = respawned.changeDimension(overworld, teleporter);
            helper.assertTrue(backInOverworld == respawned && respawned.level().dimension() == Level.OVERWORLD,
                    "ServerPlayer did not complete the actual Overworld return transfer");
            helper.assertTrue(close(respawned.getHealth(), 63.0D),
                    "Overworld return changed current health 63");

            Entity inEnd = respawned.changeDimension(end, teleporter);
            helper.assertTrue(inEnd == respawned && respawned.level().dimension() == Level.END,
                    "ServerPlayer did not complete the actual End dimension transfer");
            helper.assertTrue(close(respawned.getHealth(), 63.0D),
                    "End transfer changed current health 63");
            Entity backFromEnd = respawned.changeDimension(overworld, teleporter);
            helper.assertTrue(backFromEnd == respawned && respawned.level().dimension() == Level.OVERWORLD,
                    "ServerPlayer did not complete the actual transfer back from the End");
            helper.assertTrue(close(respawned.getHealth(), 63.0D),
                    "non-portal End transfer changed current health 63");

            helper.assertTrue(shokugi(respawned).serializeNBT().equals(expectedShokugi),
                    "actual dimension transfers changed canonical Shokugi data");
            assertDiversity(helper, respawned, expectedFoods, expectedFoods, 4);
            assertOwnedModifiers(helper, respawned, 10.0D, 8.0D, 4.0D);

            ServerPlayer endReturned = helper.getLevel().getServer().getPlayerList().respawn(respawned, true);
            helper.assertTrue(endReturned != respawned,
                    "End-return respawn did not replace the ServerPlayer instance");
            helper.assertTrue(shokugi(endReturned).serializeNBT().equals(expectedShokugi),
                    "actual End-return respawn changed canonical Shokugi data");
            assertDiversity(helper, endReturned, expectedFoods, expectedFoods, 4);
            assertOwnedModifiers(helper, endReturned, 10.0D, 8.0D, 4.0D);
            assertForeignMaxHealth(helper, endReturned, 68.0D);
            helper.assertTrue(close(endReturned.getMaxHealth(), 100.0D)
                            && close(endReturned.getHealth(), 63.0D),
                    "actual End-return respawn did not preserve current health 63/100");
        } finally {
            MinecraftForge.EVENT_BUS.unregister(foreignHealthOwner);
            channel.finishAndReleaseAll();
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void ownedModifierRebuildIsIdempotent(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        IShokugiData data = shokugi(player);
        data.setBaseStatPoints(FoodHealingBaseStatIds.BASE_DEFENSE, 8L);
        data.setBaseStatPoints(FoodHealingBaseStatIds.BASE_MAX_HEALTH, 5L);
        diversity(player).addMaxHealthBonus(12);

        AttributeInstance armor = requireAttribute(player, Attributes.ARMOR);
        armor.addPermanentModifier(new AttributeModifier(
                FOREIGN_ARMOR_UUID, "Foreign armor modifier", 3.0D, AttributeModifier.Operation.ADDITION));

        for (int i = 0; i < 5; i++) {
            FoodHealingBaseStats.rebuildOwnedModifiers(player, data);
            FoodDiversityHandler.applyHealthBonus(player, 12);
        }

        assertOwnedModifiers(helper, player, 40.0D, 10.0D, 12.0D);
        helper.assertTrue(armor.getModifier(FOREIGN_ARMOR_UUID) != null,
                "idempotent rebuild removed a foreign armor modifier");
        helper.assertTrue(close(armor.getModifier(FOREIGN_ARMOR_UUID).getAmount(), 3.0D),
                "idempotent rebuild changed a foreign armor modifier");
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE, timeoutTicks = 200)
    public static void foodConsumptionHealsAndProgressesExactlyOnce(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        try {
            ItemStack apple = new ItemStack(Items.APPLE);
            int nutrition = apple.getFoodProperties(player).getNutrition();
            double healPerNutrition = FoodHealingConfig.COMMON.healMultiplier.get();

            player.setHealth(1.0F);
            player.getFoodData().setFoodLevel(10);
            HungerChangeHandler.resetPlayerSnapshot(player);

            finishFoodTransaction(player, apple, 10 + nutrition);
            float expectedAfterFood = (float) (1.0D + nutrition * healPerNutrition);
            helper.assertTrue(close(player.getHealth(), expectedAfterFood),
                    "food Finish did not heal by declared nutrition exactly once");

            MinecraftForge.EVENT_BUS.post(new TickEvent.PlayerTickEvent(TickEvent.Phase.START, player));
            helper.assertTrue(close(player.getHealth(), expectedAfterFood),
                    "generic hunger path healed the same food transaction a second time");
            helper.assertTrue(totalFoodTransactions(player) == nutrition,
                    "one food Finish did not create exactly one Shokugi transaction");
            helper.assertTrue(diversity(player).getAllEatenFoods().size() == 1,
                    "one food Finish did not create exactly one Food Diversity discovery");

            player.getFoodData().setFoodLevel(10 + nutrition + 2);
            MinecraftForge.EVENT_BUS.post(new TickEvent.PlayerTickEvent(TickEvent.Phase.START, player));
            float expectedAfterNonFood = (float) (expectedAfterFood + 2.0D * healPerNutrition);
            helper.assertTrue(close(player.getHealth(), expectedAfterNonFood),
                    "a legitimate non-food hunger increase was suppressed after eating");

            player.getFoodData().setFoodLevel(20);
            player.setHealth(1.0F);
            long beforeFullHunger = totalFoodTransactions(player);
            HungerChangeHandler.resetPlayerSnapshot(player);
            finishFoodTransaction(player, apple, 20);
            MinecraftForge.EVENT_BUS.post(new TickEvent.PlayerTickEvent(TickEvent.Phase.START, player));

            helper.assertTrue(close(player.getHealth(), expectedAfterFood),
                    "full-hunger food did not heal by declared nutrition exactly once");
            helper.assertTrue(totalFoodTransactions(player) == beforeFullHunger + nutrition,
                    "full-hunger food did not create exactly one additional Shokugi transaction");
            helper.assertTrue(diversity(player).getAllEatenFoods().size() == 1,
                    "re-eating a known food duplicated Food Diversity progress");
        } finally {
            HungerChangeHandler.clearPlayerData(player.getUUID());
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void playerDeathDropsAreNeverMultiplied(GameTestHelper helper) {
        Player attacker = helper.makeMockSurvivalPlayer();
        shokugi(attacker).setSkillLevel(FoodHealingSkillIds.SLAUGHTER, 1);
        DamageSource source = helper.getLevel().damageSources().playerAttack(attacker);

        Cow cow = helper.spawnWithNoFreeWill(EntityType.COW, 0, 1, 0);
        List<ItemEntity> mobDrops = new ArrayList<>();
        mobDrops.add(drop(helper, new ItemStack(Items.LEATHER, 2)));
        MinecraftForge.EVENT_BUS.post(new LivingDropsEvent(cow, source, mobDrops, 0, true));
        helper.assertTrue(totalItems(mobDrops) == 6L,
                "test precondition failed: Slaughter did not multiply a passive mob drop");

        shokugi(attacker).setSkillDisabled(FoodHealingSkillIds.SLAUGHTER, true);
        Cow disabledCow = helper.spawnWithNoFreeWill(EntityType.COW, 0, 1, 1);
        List<ItemEntity> disabledMobDrops = new ArrayList<>();
        disabledMobDrops.add(drop(helper, new ItemStack(Items.LEATHER, 2)));
        MinecraftForge.EVENT_BUS.post(new LivingDropsEvent(disabledCow, source, disabledMobDrops, 0, true));
        helper.assertTrue(totalItems(disabledMobDrops) == 2L,
                "disabled Slaughter still multiplied a passive mob drop");

        Player victim = helper.makeMockSurvivalPlayer();
        List<ItemEntity> playerDrops = new ArrayList<>();
        playerDrops.add(drop(helper, new ItemStack(Items.DIAMOND, 7)));
        MinecraftForge.EVENT_BUS.post(new LivingDropsEvent(victim, source, playerDrops, 0, true));

        helper.assertTrue(playerDrops.size() == 1,
                "Food Healing added entities to a player death drop collection");
        helper.assertTrue(totalItems(playerDrops) == 7L,
                "Food Healing multiplied player death drops");

        ServerPlayer actualVictim = new ServerPlayer(
                helper.getLevel().getServer(), helper.getLevel(),
                new GameProfile(UUID.randomUUID(), "foodhealing-death-drop-test"));
        Connection connection = new Connection(PacketFlow.SERVERBOUND);
        EmbeddedChannel channel = new EmbeddedChannel(connection);
        try {
            helper.getLevel().getServer().getPlayerList().placeNewPlayer(connection, actualVictim);
            BlockPos deathPos = helper.absolutePos(new BlockPos(1, 2, 1));
            actualVictim.teleportTo(deathPos.getX() + 0.5D, deathPos.getY(), deathPos.getZ() + 0.5D);
            actualVictim.getInventory().clearContent();
            actualVictim.getInventory().setItem(0, new ItemStack(Items.DIAMOND, 7));
            actualVictim.die(source);

            AABB dropArea = new AABB(deathPos).inflate(4.0D);
            List<ItemEntity> actualDrops = helper.getLevel().getEntitiesOfClass(
                    ItemEntity.class, dropArea, entity -> entity.getItem().is(Items.DIAMOND));
            helper.assertTrue(totalItems(actualDrops) == 7L,
                    "actual ServerPlayer death multiplied or lost inventory drops");
        } finally {
            channel.finishAndReleaseAll();
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE, timeoutTicks = 200)
    public static void multiplayerProgressAndSyncRemainPlayerScoped(GameTestHelper helper) {
        ServerPlayer playerA = new ServerPlayer(
                helper.getLevel().getServer(), helper.getLevel(),
                new GameProfile(UUID.randomUUID(), "foodhealing-multiplayer-a"));
        ServerPlayer playerB = new ServerPlayer(
                helper.getLevel().getServer(), helper.getLevel(),
                new GameProfile(UUID.randomUUID(), "foodhealing-multiplayer-b"));
        Connection connectionA = new Connection(PacketFlow.SERVERBOUND);
        Connection connectionB = new Connection(PacketFlow.SERVERBOUND);
        EmbeddedChannel channelA = new EmbeddedChannel(connectionA);
        EmbeddedChannel channelB = new EmbeddedChannel(connectionB);
        try {
            helper.getLevel().getServer().getPlayerList().placeNewPlayer(connectionA, playerA);
            helper.getLevel().getServer().getPlayerList().placeNewPlayer(connectionB, playerB);

            IShokugiData dataA = shokugi(playerA);
            IShokugiData dataB = shokugi(playerB);
            dataA.setUnspentSkillPoints(5L);
            dataB.setUnspentSkillPoints(7L);
            helper.assertTrue(FoodHealingSkills.tryPurchase(
                            dataA, FoodHealingSkillIds.FIRE_RESISTANCE, 0)
                            == FoodHealingSkills.PurchaseResult.SUCCESS,
                    "player A could not complete a server-side skill purchase");
            helper.assertTrue(dataA.getUnspentSkillPoints() == 4L
                            && dataA.getSpentSkillPoints() == 1L
                            && dataA.getSkillLevel(FoodHealingSkillIds.FIRE_RESISTANCE) == 1,
                    "player A purchase did not update only A's canonical transaction");
            helper.assertTrue(dataB.getUnspentSkillPoints() == 7L
                            && dataB.getSpentSkillPoints() == 0L
                            && dataB.getSkillLevel(FoodHealingSkillIds.FIRE_RESISTANCE) == 0,
                    "player A purchase leaked into player B progression");

            dataB.setSkillLevel(FoodHealingSkillIds.FIRE_RESISTANCE, 1);
            dataA.setSkillDisabled(FoodHealingSkillIds.FIRE_RESISTANCE, true);
            helper.assertTrue(dataA.isSkillDisabled(FoodHealingSkillIds.FIRE_RESISTANCE)
                            && !dataB.isSkillDisabled(FoodHealingSkillIds.FIRE_RESISTANCE),
                    "player A toggle state leaked into player B");

            ItemStack apple = new ItemStack(Items.APPLE);
            int nutrition = apple.getFoodProperties(playerA).getNutrition();
            playerA.setHealth(1.0F);
            playerA.getFoodData().setFoodLevel(10);
            playerB.getFoodData().setFoodLevel(10);
            HungerChangeHandler.resetPlayerSnapshot(playerA);
            HungerChangeHandler.resetPlayerSnapshot(playerB);
            finishFoodTransaction(playerA, apple, 10 + nutrition);
            MinecraftForge.EVENT_BUS.post(new TickEvent.PlayerTickEvent(TickEvent.Phase.START, playerA));
            MinecraftForge.EVENT_BUS.post(new TickEvent.PlayerTickEvent(TickEvent.Phase.START, playerB));

            helper.assertTrue(totalFoodTransactions(playerA) == nutrition
                            && diversity(playerA).getAllEatenFoods().size() == 1,
                    "player A food did not create one player-scoped progression transaction");
            helper.assertTrue(totalFoodTransactions(playerB) == 0L
                            && diversity(playerB).getAllEatenFoods().isEmpty(),
                    "player A food progression leaked into player B");

            channelA.runPendingTasks();
            channelB.runPendingTasks();
            drainOutbound(channelA);
            drainOutbound(channelB);
            CapabilityEvents.syncToClient(playerA);
            FoodDiversityHandler.syncToClient(playerA);
            channelA.runPendingTasks();
            channelB.runPendingTasks();
            int playerAPackets = drainOutbound(channelA);
            int playerBPackets = drainOutbound(channelB);
            helper.assertTrue(playerAPackets >= 2,
                    "player A did not receive both explicit capability sync packets");
            helper.assertTrue(playerBPackets == 0,
                    "player A capability sync was sent to player B's connection");
        } finally {
            HungerChangeHandler.clearPlayerData(playerA.getUUID());
            HungerChangeHandler.clearPlayerData(playerB.getUUID());
            channelA.finishAndReleaseAll();
            channelB.finishAndReleaseAll();
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void foodHungerCreditCannotSuppressLaterIndependentGains(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        try {
            for (int postMealFoodLevel : new int[]{11, 10, 9}) {
                player.setHealth(1.0F);
                player.getFoodData().setFoodLevel(10);
                HungerChangeHandler.resetPlayerSnapshot(player);
                finishFoodTransaction(player, new ItemStack(Items.APPLE), 14);
                float afterMeal = player.getHealth();
                player.getFoodData().setFoodLevel(postMealFoodLevel);
                MinecraftForge.EVENT_BUS.post(new TickEvent.PlayerTickEvent(TickEvent.Phase.START, player));
                helper.assertTrue(close(player.getHealth(), afterMeal), "food credit healed twice");

                player.getFoodData().setFoodLevel(postMealFoodLevel + 2);
                MinecraftForge.EVENT_BUS.post(new TickEvent.PlayerTickEvent(TickEvent.Phase.START, player));
                helper.assertTrue(close(player.getHealth(), afterMeal + 2 * FoodHealingConfig.COMMON.healMultiplier.get()),
                        "consumed food credit suppressed an independent hunger gain after snapshot " + postMealFoodLevel);
            }
        } finally {
            HungerChangeHandler.clearPlayerData(player.getUUID());
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE, timeoutTicks = 200)
    public static void endPortalCreditsResponsePreservesPartialHealthAndProgress(GameTestHelper helper) {
        var server = helper.getLevel().getServer();
        ServerLevel end = server.getLevel(Level.END);
        helper.assertTrue(end != null, "End dimension missing");
        ServerPlayer original = new ServerPlayer(server, helper.getLevel(),
                new GameProfile(UUID.randomUUID(), "fh-credits"));
        Connection connection = new Connection(PacketFlow.SERVERBOUND);
        EmbeddedChannel channel = new EmbeddedChannel(connection);
        BlockPos portalPos = new BlockPos(0, 80, 0);
        var oldBlock = end.getBlockState(portalPos);
        try {
            server.getPlayerList().placeNewPlayer(connection, original);
            var data = shokugi(original);
            data.setUnspentSkillPoints(358);
            data.setSpentSkillPoints(142);
            data.setSkillLevel(FoodHealingSkillIds.TRUE_GUTS, 1);
            data.setSkillDisabled(FoodHealingSkillIds.TRUE_GUTS, true);
            data.setBaseStatPoints(FoodHealingBaseStatIds.BASE_MAX_HEALTH, 36);
            diversity(original).addEatenFood("minecraft:apple");
            diversity(original).addMaxHealthBonus(8);
            CapabilityEvents.rebuildOwnedModifiers(original);
            FoodDiversityHandler.applyHealthBonus(original, 8);
            helper.assertTrue(close(original.getMaxHealth(), 100), "credits fixture expected max HP100");
            original.setHealth(63);
            CompoundTag before = data.serializeNBT();
            original.changeDimension(end, new FixedTestTeleporter());
            original.setPos(0.5, 80.4, 0.5);
            end.setBlockAndUpdate(portalPos, Blocks.END_PORTAL.defaultBlockState());
            channel.runPendingTasks();
            drainOutbound(channel);
            Blocks.END_PORTAL.entityInside(end.getBlockState(portalPos), end, portalPos, original);
            helper.assertTrue(original.wonGame, "portal did not enter credits/wonGame path");
            channel.runPendingTasks();
            int credits = 0;
            Object message;
            while ((message = channel.readOutbound()) != null) {
                if (message instanceof ClientboundGameEventPacket event
                        && event.getEvent() == ClientboundGameEventPacket.WIN_GAME) credits++;
                ReferenceCountUtil.release(message);
            }
            helper.assertTrue(credits == 1, "portal should send exactly one credits packet");
            var listener = original.connection;
            listener.handleClientCommand(new ServerboundClientCommandPacket(
                    ServerboundClientCommandPacket.Action.PERFORM_RESPAWN));
            ServerPlayer returned = listener.player;
            helper.assertTrue(returned != original && returned.level().dimension() == Level.OVERWORLD,
                    "credits response did not recreate player in Overworld");
            helper.assertTrue(shokugi(returned).serializeNBT().equals(before), "credits return changed canonical data");
            assertDiversity(helper, returned, Set.of("minecraft:apple"), Set.of("minecraft:apple"), 8);
            helper.assertTrue(close(returned.getHealth(), 63) && close(returned.getMaxHealth(), 100),
                    "credits return did not retain HP63/100");
            listener.handleClientCommand(new ServerboundClientCommandPacket(
                    ServerboundClientCommandPacket.Action.PERFORM_RESPAWN));
            helper.assertTrue(listener.player == returned && shokugi(returned).serializeNBT().equals(before),
                    "duplicate credits response repeated respawn or progression");
        } finally {
            end.setBlockAndUpdate(portalPos, oldBlock);
            if (original.connection != null) server.getPlayerList().remove(original.connection.player);
            channel.finishAndReleaseAll();
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE, timeoutTicks = 200)
    public static void endGatewayAndPearlPreservePlayerStateWithoutRespawn(GameTestHelper helper) {
        var server = helper.getLevel().getServer();
        ServerLevel end = server.getLevel(Level.END);
        helper.assertTrue(end != null, "End dimension missing");
        BlockPos entry = new BlockPos(8, 200, 8);
        BlockPos exit = new BlockPos(12, 200, 8);
        var oldEntry = end.getBlockState(entry);
        var oldExit = end.getBlockState(exit);
        helper.assertTrue(oldEntry.isAir() && oldExit.isAir(), "gateway fixture requires empty test positions");
        ServerPlayer player = new ServerPlayer(server, helper.getLevel(),
                new GameProfile(UUID.randomUUID(), "fh-gateway"));
        Connection connection = new Connection(PacketFlow.SERVERBOUND);
        EmbeddedChannel channel = new EmbeddedChannel(connection);
        ThrownEnderpearl pearl = null;
        try {
            server.getPlayerList().placeNewPlayer(connection, player);
            player.changeDimension(end, new FixedTestTeleporter());
            var data = shokugi(player);
            data.setLevel(3_000_000_000L);
            data.setEatCount(199);
            data.setUnspentSkillPoints(5_000_000_000L);
            data.setSpentSkillPoints(142);
            data.setSkillLevel(FoodHealingSkillIds.GUTS, 5);
            data.setSkillLevel(FoodHealingSkillIds.TRUE_GUTS, 1);
            data.setSkillLevel(FoodHealingSkillIds.HEROICS, 5);
            data.setSkillLevel(FoodHealingSkillIds.TRUE_HEROICS, 1);
            data.setSkillDisabled(FoodHealingSkillIds.TRUE_GUTS, true);
            data.setSkillDisabled(FoodHealingSkillIds.HEROICS, true);
            data.setRootAccumulatedNutrition(9);
            data.setRootAccumulationDeadline(end.getGameTime() + 300);
            for (String stat : FoodHealingBaseStatIds.ALL) data.setBaseStatPoints(stat, 2);
            diversity(player).addEatenFood("minecraft:apple");
            diversity(player).addMaxHealthBonus(8);
            CapabilityEvents.rebuildOwnedModifiers(player);
            FoodDiversityHandler.applyHealthBonus(player, 8);
            var foreignHealth = new AttributeModifier(FOREIGN_MAX_HEALTH_UUID, "Foreign gateway health", 68,
                    AttributeModifier.Operation.ADDITION);
            requireAttribute(player, Attributes.MAX_HEALTH).addPermanentModifier(foreignHealth);
            var foreignArmor = new AttributeModifier(FOREIGN_ARMOR_UUID, "Foreign gateway armor", 7,
                    AttributeModifier.Operation.ADDITION);
            requireAttribute(player, Attributes.ARMOR).addPermanentModifier(foreignArmor);
            player.setHealth(63);
            ItemStack diamonds = new ItemStack(Items.DIAMOND, 7);
            diamonds.getOrCreateTag().putString("fixture", "gateway-owner");
            player.getInventory().setItem(0, diamonds);
            ItemStack expectedItems = diamonds.copy();
            CompoundTag before = data.serializeNBT();
            channel.runPendingTasks();
            drainOutbound(channel);

            // Known exits exercise vanilla transfer without generating a remote island or touching another world.
            end.setBlockAndUpdate(entry, Blocks.END_GATEWAY.defaultBlockState());
            end.setBlockAndUpdate(exit, Blocks.END_GATEWAY.defaultBlockState());
            var outbound = (TheEndGatewayBlockEntity) end.getBlockEntity(entry);
            var inbound = (TheEndGatewayBlockEntity) end.getBlockEntity(exit);
            helper.assertTrue(outbound != null && inbound != null, "gateway block entities missing");
            outbound.setExitPosition(exit, true);
            inbound.setExitPosition(entry, true);
            player.setPos(entry.getX() + 0.5, entry.getY(), entry.getZ() + 0.5);
            for (int leg = 0; leg < 2; leg++) {
                BlockPos source = leg == 0 ? entry : exit;
                BlockPos destination = leg == 0 ? exit : entry;
                TheEndGatewayBlockEntity gateway = leg == 0 ? outbound : inbound;
                if (leg == 1) {
                    // The owner's portal cooldown excludes the player, leaving only its new pearl eligible.
                    pearl = new ThrownEnderpearl(end, player);
                    pearl.setPos(exit.getX() + 0.5, exit.getY(), exit.getZ() + 0.5);
                    helper.assertTrue(end.addFreshEntity(pearl), "pearl fixture could not enter the End");
                }
                TheEndGatewayBlockEntity.teleportTick(end, source, gateway.getBlockState(), gateway);
                helper.assertTrue(close(player.getX(), destination.getX() + 0.5)
                                && close(player.getY(), destination.getY())
                                && close(player.getZ(), destination.getZ() + 0.5),
                        "gateway did not transfer the player/pearl owner to its exit");
                helper.assertTrue(gateway.isCoolingDown() && player.isOnPortalCooldown(),
                        "gateway transfer did not set vanilla cooldowns");
                TheEndGatewayBlockEntity.teleportEntity(end, source, gateway.getBlockState(), player, gateway);
                helper.assertTrue(server.getPlayerList().getPlayer(player.getUUID()) == player
                                && player.connection.player == player && player.level() == end && !player.wonGame,
                        "same-dimension gateway recreated the player or entered the credits path");
                helper.assertTrue(shokugi(player) == data && data.serializeNBT().equals(before),
                        "gateway changed canonical progression, toggles or Root state");
                assertDiversity(helper, player, Set.of("minecraft:apple"), Set.of("minecraft:apple"), 8);
                assertOwnedModifiers(helper, player, 10, 4, 8);
                helper.assertTrue(requireAttribute(player, Attributes.MAX_HEALTH)
                                .getModifier(FOREIGN_MAX_HEALTH_UUID) == foreignHealth
                                && requireAttribute(player, Attributes.ARMOR).getModifier(FOREIGN_ARMOR_UUID) == foreignArmor,
                        "gateway removed/replaced another owner's modifier");
                helper.assertTrue(close(player.getHealth(), 63) && close(player.getMaxHealth(), 100),
                        "gateway changed partial HP63/100");
                helper.assertTrue(ItemStack.matches(player.getInventory().getItem(0), expectedItems),
                        "gateway lost/duplicated the player's NBT inventory item");
                channel.runPendingTasks();
                int positions = 0;
                int credits = 0;
                Object message;
                while ((message = channel.readOutbound()) != null) {
                    if (message instanceof ClientboundPlayerPositionPacket) positions++;
                    if (message instanceof ClientboundGameEventPacket event
                            && event.getEvent() == ClientboundGameEventPacket.WIN_GAME) credits++;
                    ReferenceCountUtil.release(message);
                }
                helper.assertTrue(positions == 1 && credits == 0,
                        "gateway/cooldown produced duplicate position or unexpected credits packets");
            }
            helper.assertTrue(pearl != null && pearl.isRemoved(), "vanilla gateway did not consume the owner's pearl");
            com.mojang.logging.LogUtils.getLogger().info(
                    "FOODHEALING_END_GATEWAY_PASS legs=2 direct=true pearl=true cooldown=true canonical=true hp=63/100");
        } finally {
            if (pearl != null && !pearl.isRemoved()) pearl.discard();
            end.setBlockAndUpdate(entry, oldEntry);
            end.setBlockAndUpdate(exit, oldExit);
            if (player.connection != null) server.getPlayerList().remove(player);
            channel.finishAndReleaseAll();
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE, timeoutTicks = 200)
    public static void repeatedExtremeRebuildsPreserveCanonicalDataAndForeignOwnership(GameTestHelper helper) {
        long started = System.nanoTime();
        Player player = helper.makeMockSurvivalPlayer();
        IShokugiData data = shokugi(player);
        data.setUnspentSkillPoints(Long.MAX_VALUE);
        data.setSpentSkillPoints(142);
        data.setSkillLevel(FoodHealingSkillIds.HEROICS, 5);
        var armor = requireAttribute(player, Attributes.ARMOR);
        AttributeModifier foreign = new AttributeModifier(FOREIGN_ARMOR_UUID, "Foreign stress fixture",
                Double.MAX_VALUE, AttributeModifier.Operation.ADDITION);
        armor.addPermanentModifier(foreign);
        diversity(player).addMaxHealthBonus(8);
        for (int i = 0; i < 2000; i++) {
            long points = switch (i % 3) { case 0 -> 0; case 1 -> 1; default -> Long.MAX_VALUE; };
            data.setBaseStatPoints(FoodHealingBaseStatIds.BASE_DEFENSE, points);
            data.setBaseStatPoints(FoodHealingBaseStatIds.BASE_MAX_HEALTH, points);
            data.setSkillDisabled(FoodHealingSkillIds.HEROICS, i % 2 == 0);
            CompoundTag before = data.serializeNBT();
            CapabilityEvents.rebuildOwnedModifiers(player);
            FoodDiversityHandler.applyHealthBonus(player, 8);
            player.setHealth(player.getMaxHealth() * (i % 2 == 0 ? 0.8F : 0.25F));
            helper.assertTrue(data.serializeNBT().equals(before), "rebuild changed long progression or toggles");
            helper.assertTrue(armor.getModifier(FOREIGN_ARMOR_UUID) == foreign,
                    "rebuild replaced or removed another owner's modifier");
            long ownedCount = armor.getModifiers().stream()
                    .filter(m -> m.getId().equals(FoodHealingBaseStats.BASE_DEFENSE_UUID)).count();
            helper.assertTrue(ownedCount == (points == 0 ? 0 : 1), "repeated rebuild duplicated owned armor");
            float effective = HeroicsController.effectiveArmor(player, data);
            helper.assertTrue(Float.isFinite(effective) && effective >= 0 && Float.isFinite(player.getMaxHealth()),
                    "extreme modifier produced non-finite effective armor/health");
            if (i % 20 == 0) {
                CompoundTag saved = new CompoundTag();
                player.saveWithoutId(saved);
                player.load(saved);
                helper.assertTrue(data.serializeNBT().equals(before), "periodic player load changed canonical data");
                foreign = armor.getModifier(FOREIGN_ARMOR_UUID);
                helper.assertTrue(foreign != null && foreign.getAmount() == Double.MAX_VALUE,
                        "periodic player load changed foreign amount");
            }
        }
        com.mojang.logging.LogUtils.getLogger().info("FOODHEALING_REBUILD_STRESS_PASS cycles=2000 saves=100 elapsedMs={}",
                (System.nanoTime() - started) / 1_000_000);
        helper.succeed();
    }

    private static void finishFoodTransaction(Player player, ItemStack food, int resultingFoodLevel) {
        MinecraftForge.EVENT_BUS.post(new LivingEntityUseItemEvent.Start(
                player, food.copy(), food.getUseDuration()));
        MinecraftForge.EVENT_BUS.post(new LivingEntityUseItemEvent.Tick(player, food.copy(), 1));
        food = food.copy(); // Each call supplies a new consumable stack; native finish mutates it.
        ItemStack original = food.copy();
        ItemStack result = food.finishUsingItem(player.level(), player);
        net.minecraftforge.event.ForgeEventFactory.onItemUseFinish(player, original, 0, result);
    }

    private static void assertCanonicalDataCopied(GameTestHelper helper, Player player,
                                                   net.minecraft.nbt.CompoundTag expectedShokugi,
                                                   IFoodDiversityData expectedDiversity) {
        helper.assertTrue(shokugi(player).serializeNBT().equals(expectedShokugi),
                "clone/respawn/dimension changed canonical Shokugi data");
        IFoodDiversityData actualDiversity = diversity(player);
        helper.assertTrue(actualDiversity.getEatenFoods().equals(expectedDiversity.getEatenFoods()),
                "clone/respawn/dimension changed pending Food Diversity foods");
        helper.assertTrue(actualDiversity.getAllEatenFoods().equals(expectedDiversity.getAllEatenFoods()),
                "clone/respawn/dimension changed Food Diversity history");
        helper.assertTrue(actualDiversity.getMaxHealthBonus() == expectedDiversity.getMaxHealthBonus(),
                "clone/respawn/dimension changed Food Diversity max-health progress");
    }

    private static void assertOwnedModifiers(GameTestHelper helper, Player player,
                                             double expectedArmor, double expectedBaseHealth,
                                             double expectedDiversityHealth) {
        AttributeInstance armor = requireAttribute(player, Attributes.ARMOR);
        AttributeInstance maxHealth = requireAttribute(player, Attributes.MAX_HEALTH);
        assertModifier(helper, armor, FoodHealingBaseStats.BASE_DEFENSE_UUID, expectedArmor,
                "base defense");
        assertModifier(helper, maxHealth, FoodHealingBaseStats.BASE_MAX_HEALTH_UUID, expectedBaseHealth,
                "base max health");
        assertModifier(helper, maxHealth, FoodDiversityHandler.HEALTH_BONUS_UUID, expectedDiversityHealth,
                "Food Diversity health");
    }

    private static void assertModifier(GameTestHelper helper, AttributeInstance attribute, UUID id,
                                       double expectedAmount, String label) {
        AttributeModifier modifier = attribute.getModifier(id);
        helper.assertTrue(modifier != null, label + " modifier is missing");
        helper.assertTrue(close(modifier.getAmount(), expectedAmount), label + " modifier has the wrong amount");
        long matchingIds = attribute.getModifiers().stream()
                .filter(candidate -> candidate.getId().equals(id))
                .count();
        helper.assertTrue(matchingIds == 1L, label + " modifier is duplicated");
    }

    private static void assertForeignMaxHealth(GameTestHelper helper, Player player, double expectedAmount) {
        AttributeModifier modifier = requireAttribute(player, Attributes.MAX_HEALTH)
                .getModifier(FOREIGN_MAX_HEALTH_UUID);
        helper.assertTrue(modifier != null, "foreign max-health modifier is missing");
        helper.assertTrue(close(modifier.getAmount(), expectedAmount),
                "foreign max-health modifier amount changed");
    }

    private static IShokugiData shokugi(Player player) {
        return player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .orElseThrow(() -> new GameTestAssertException("Shokugi capability is missing"));
    }

    private static IFoodDiversityData diversity(Player player) {
        return player.getCapability(FoodDiversityProvider.FOOD_DIVERSITY)
                .orElseThrow(() -> new GameTestAssertException("Food Diversity capability is missing"));
    }

    private static void assertDiversity(GameTestHelper helper, Player player, Set<String> expectedCurrent,
                                        Set<String> expectedAll, int expectedHealthBonus) {
        IFoodDiversityData actual = diversity(player);
        helper.assertTrue(actual.getEatenFoods().equals(expectedCurrent),
                "lifecycle changed pending Food Diversity foods");
        helper.assertTrue(actual.getAllEatenFoods().equals(expectedAll),
                "lifecycle changed Food Diversity history");
        helper.assertTrue(actual.getMaxHealthBonus() == expectedHealthBonus,
                "lifecycle changed Food Diversity max-health progress");
    }

    private static AttributeInstance requireAttribute(Player player,
                                                      net.minecraft.world.entity.ai.attributes.Attribute attribute) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance == null) {
            throw new GameTestAssertException("Required player attribute is missing: " + attribute.getDescriptionId());
        }
        return instance;
    }

    private static ItemEntity drop(GameTestHelper helper, ItemStack stack) {
        return new ItemEntity(helper.getLevel(), 0.0D, 0.0D, 0.0D, stack);
    }

    private static long totalItems(List<ItemEntity> drops) {
        return drops.stream().mapToLong(drop -> drop.getItem().getCount()).sum();
    }

    private static int drainOutbound(EmbeddedChannel channel) {
        int count = 0;
        Object message;
        while ((message = channel.readOutbound()) != null) {
            count++;
            ReferenceCountUtil.release(message);
        }
        return count;
    }

    private static long totalFoodTransactions(Player player) {
        IShokugiData data = shokugi(player);
        long required = FoodHealingConfig.nutritionThreshold();
        return data.getLevel() * required + data.getEatCount();
    }

    private static boolean close(double actual, double expected) {
        return Math.abs(actual - expected) < 1.0E-5D;
    }

    private static final class FixedTestTeleporter implements ITeleporter {
        @Override
        public PortalInfo getPortalInfo(Entity entity, ServerLevel destination,
                                        Function<ServerLevel, PortalInfo> defaultPortalInfo) {
            return new PortalInfo(new Vec3(0.5D, 80.0D, 0.5D), Vec3.ZERO,
                    entity.getYRot(), entity.getXRot());
        }
    }

    private static final class ForeignMaxHealthCloneListener {
        @SubscribeEvent(priority = EventPriority.HIGH)
        public void copyOwnedModifier(PlayerEvent.Clone event) {
            AttributeInstance oldMaxHealth = event.getOriginal().getAttribute(Attributes.MAX_HEALTH);
            AttributeInstance newMaxHealth = event.getEntity().getAttribute(Attributes.MAX_HEALTH);
            if (oldMaxHealth == null || newMaxHealth == null
                    || newMaxHealth.getModifier(FOREIGN_MAX_HEALTH_UUID) != null) {
                return;
            }
            AttributeModifier modifier = oldMaxHealth.getModifier(FOREIGN_MAX_HEALTH_UUID);
            if (modifier != null) {
                newMaxHealth.addPermanentModifier(new AttributeModifier(
                        modifier.getId(), modifier.getName(), modifier.getAmount(), modifier.getOperation()));
            }
        }
    }
}
