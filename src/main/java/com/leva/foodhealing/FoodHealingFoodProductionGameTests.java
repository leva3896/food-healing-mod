package com.leva.foodhealing;

import com.leva.foodhealing.capability.IShokugiData;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.leva.foodhealing.extension.FoodProductionResultSlotExtension;
import com.mojang.authlib.GameProfile;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.SmokerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.util.UUID;

@GameTestHolder(FoodHealingMod.MODID)
@PrefixGameTestTemplate(false)
public final class FoodHealingFoodProductionGameTests {
    private static final String EMPTY_TEMPLATE = "empty";

    private FoodHealingFoodProductionGameTests() {
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void craftingPlanDoublesFoodAndPreservesMetadata(GameTestHelper helper) {
        Player player = skilledPlayer(helper);
        ItemStack base = new ItemStack(Items.BREAD, 4);
        CompoundTag tag = new CompoundTag();
        tag.putString("foodhealing_test", "preserved");
        base.setTag(tag);

        FoodProductionTransactions.CraftingResultPlan plan =
                FoodProductionTransactions.planPersonalCraftingResult(player, base);
        helper.assertTrue(plan.displayedResult().getCount() == 8,
                "crafting output 4 was not doubled to 8");
        helper.assertTrue(plan.overflow().isEmpty(),
                "non-overflow crafting result created an overflow stack");
        helper.assertTrue(ItemStack.isSameItemSameTags(base, plan.displayedResult()),
                "crafting result doubling lost item metadata");

        ItemStack largeBase = new ItemStack(Items.BREAD, 40);
        largeBase.setTag(tag.copy());
        FoodProductionTransactions.CraftingResultPlan overflowPlan =
                FoodProductionTransactions.planPersonalCraftingResult(player, largeBase);
        helper.assertTrue(overflowPlan.displayedResult().getCount() == 64,
                "overflow crafting result did not cap the visible stack at 64");
        helper.assertTrue(overflowPlan.overflow().getCount() == 16,
                "overflow crafting result did not retain the remaining 16 items");
        helper.assertTrue(ItemStack.isSameItemSameTags(largeBase, overflowPlan.overflow()),
                "crafting overflow lost item metadata");

        Player unskilled = helper.makeMockSurvivalPlayer();
        FoodProductionTransactions.CraftingResultPlan unchanged =
                FoodProductionTransactions.planPersonalCraftingResult(unskilled, base);
        helper.assertTrue(unchanged.displayedResult().getCount() == 4 && unchanged.overflow().isEmpty(),
                "unskilled player received doubled crafting output");
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void craftingNormalAndShiftClickConsumeOneRecipe(GameTestHelper helper) {
        Player player = skilledPlayer(helper);

        CraftingMenu normalMenu = primedCraftingMenu(player, new ItemStack(Items.BREAD, 40));
        normalMenu.clicked(0, 0, ClickType.PICKUP, player);
        helper.assertTrue(normalMenu.getCarried().getCount() == 64,
                "normal crafting click did not take the capped doubled result");
        helper.assertTrue(countInventoryItem(player, Items.BREAD) == 16,
                "normal crafting overflow was not delivered in the result-slot transaction");
        helper.assertTrue(normalMenu.getSlot(1).getItem().isEmpty(),
                "normal crafting click did not consume exactly one recipe ingredient");

        clearInventory(player);
        CraftingMenu shiftMenu = primedCraftingMenu(player, new ItemStack(Items.BREAD, 4));
        shiftMenu.clicked(0, 0, ClickType.QUICK_MOVE, player);
        helper.assertTrue(countInventoryItem(player, Items.BREAD) == 8,
                "shift-click crafting output 4 was not moved as 8 items");
        helper.assertTrue(shiftMenu.getSlot(1).getItem().isEmpty(),
                "shift-click crafting did not consume exactly one recipe ingredient");

        clearInventory(player);
        CraftingMenu overflowShiftMenu = primedCraftingMenu(player, new ItemStack(Items.BREAD, 40));
        overflowShiftMenu.clicked(0, 0, ClickType.QUICK_MOVE, player);
        helper.assertTrue(countInventoryItem(player, Items.BREAD) == 80,
                "shift-click crafting overflow did not deliver all doubled output");
        helper.assertTrue(overflowShiftMenu.getSlot(1).getItem().isEmpty(),
                "overflow shift-click crafting consumed more than one recipe transaction");
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void personalTwoByTwoCraftingDoublesOneResult(GameTestHelper helper) {
        ServerPlayer player = new ServerPlayer(
                helper.getLevel().getServer(), helper.getLevel(),
                new GameProfile(UUID.randomUUID(), "foodhealing-2x2-test"));
        Connection connection = new Connection(PacketFlow.SERVERBOUND);
        EmbeddedChannel channel = new EmbeddedChannel(connection);
        try {
            helper.getLevel().getServer().getPlayerList().placeNewPlayer(connection, player);
            shokugi(player).setSkillLevel(FoodHealingSkillIds.FOOD_PRODUCTION_MASTERY, 1);
            InventoryMenu menu = player.inventoryMenu;
            menu.getSlot(1).set(new ItemStack(Items.WHEAT));
            FoodProductionTransactions.CraftingResultPlan plan =
                    FoodProductionTransactions.planPersonalCraftingResult(player, new ItemStack(Items.BREAD));
            menu.getSlot(0).set(plan.displayedResult().copy());
            if (!(menu.getSlot(0) instanceof FoodProductionResultSlotExtension extension)) {
                throw new GameTestAssertException("2x2 ResultSlotMixin extension is missing");
            }
            extension.foodhealing$setCraftingOverflow(plan.overflow());

            menu.clicked(0, 0, ClickType.PICKUP, player);
            helper.assertTrue(menu.getCarried().getCount() == 2,
                    "2x2 crafting output 1 was not doubled to 2");
            helper.assertTrue(menu.getSlot(1).getItem().isEmpty(),
                    "2x2 crafting did not consume exactly one recipe ingredient");
        } finally {
            channel.finishAndReleaseAll();
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void actualCraftingMenusPublishDoubledRecipeResult(GameTestHelper helper) {
        ServerPlayer player = new ServerPlayer(
                helper.getLevel().getServer(), helper.getLevel(),
                new GameProfile(UUID.randomUUID(), "foodhealing-actual-crafting-test"));
        Connection connection = new Connection(PacketFlow.SERVERBOUND);
        EmbeddedChannel channel = new EmbeddedChannel(connection);
        try {
            helper.getLevel().getServer().getPlayerList().placeNewPlayer(connection, player);
            IShokugiData data = shokugi(player);
            data.setSkillLevel(FoodHealingSkillIds.FOOD_PRODUCTION_MASTERY, 1);

            BlockPos tablePosition = helper.absolutePos(BlockPos.ZERO.above());
            helper.getLevel().setBlockAndUpdate(tablePosition, Blocks.CRAFTING_TABLE.defaultBlockState());
            CraftingMenu tableMenu = new CraftingMenu(5, player.getInventory(),
                    ContainerLevelAccess.create(helper.getLevel(), tablePosition));
            tableMenu.getSlot(1).set(new ItemStack(Items.WHEAT));
            tableMenu.getSlot(2).set(new ItemStack(Items.WHEAT));
            tableMenu.getSlot(3).set(new ItemStack(Items.WHEAT));
            helper.assertTrue(tableMenu.getSlot(0).getItem().is(Items.BREAD)
                            && tableMenu.getSlot(0).getItem().getCount() == 2,
                    "actual 3x3 wheat recipe did not publish bread x2 in the result slot");
            tableMenu.clicked(0, 0, ClickType.PICKUP, player);
            helper.assertTrue(tableMenu.getCarried().is(Items.BREAD)
                            && tableMenu.getCarried().getCount() == 2,
                    "normal click did not take the actual doubled bread result");
            assertCraftingSlotsEmpty(helper, tableMenu, 1, 3,
                    "normal click did not consume exactly one actual bread recipe");

            tableMenu.setCarried(ItemStack.EMPTY);
            clearInventory(player);
            tableMenu.getSlot(1).set(new ItemStack(Items.WHEAT));
            tableMenu.getSlot(2).set(new ItemStack(Items.WHEAT));
            tableMenu.getSlot(3).set(new ItemStack(Items.WHEAT));
            tableMenu.clicked(0, 0, ClickType.QUICK_MOVE, player);
            helper.assertTrue(countInventoryItem(player, Items.BREAD) == 2,
                    "shift-click did not move the actual doubled bread result");
            assertCraftingSlotsEmpty(helper, tableMenu, 1, 3,
                    "shift-click did not consume exactly one actual bread recipe");

            data.setSkillDisabled(FoodHealingSkillIds.FOOD_PRODUCTION_MASTERY, true);
            tableMenu.getSlot(1).set(new ItemStack(Items.WHEAT));
            tableMenu.getSlot(2).set(new ItemStack(Items.WHEAT));
            tableMenu.getSlot(3).set(new ItemStack(Items.WHEAT));
            helper.assertTrue(tableMenu.getSlot(0).getItem().is(Items.BREAD)
                            && tableMenu.getSlot(0).getItem().getCount() == 1,
                    "disabled Food Production changed the actual 3x3 recipe result");

            data.setSkillDisabled(FoodHealingSkillIds.FOOD_PRODUCTION_MASTERY, false);
            data.setSkillLevel(FoodHealingSkillIds.FOOD_PRODUCTION_MASTERY, 0);
            tableMenu.getSlot(3).set(ItemStack.EMPTY);
            tableMenu.getSlot(3).set(new ItemStack(Items.WHEAT));
            helper.assertTrue(tableMenu.getSlot(0).getItem().is(Items.BREAD)
                            && tableMenu.getSlot(0).getItem().getCount() == 1,
                    "unskilled player received a doubled actual 3x3 recipe result");

            data.setSkillLevel(FoodHealingSkillIds.FOOD_PRODUCTION_MASTERY, 1);
            InventoryMenu inventoryMenu = player.inventoryMenu;
            inventoryMenu.getSlot(1).set(new ItemStack(Items.PUMPKIN));
            inventoryMenu.getSlot(2).set(new ItemStack(Items.SUGAR));
            inventoryMenu.getSlot(3).set(new ItemStack(Items.EGG));
            helper.assertTrue(inventoryMenu.getSlot(0).getItem().is(Items.PUMPKIN_PIE)
                            && inventoryMenu.getSlot(0).getItem().getCount() == 2,
                    "actual 2x2 pumpkin pie recipe did not publish result x2");
        } finally {
            channel.finishAndReleaseAll();
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void furnaceManualAndShiftExtractionDoubleOnlyForOwner(GameTestHelper helper) {
        Player player = skilledPlayer(helper);

        SimpleContainer manualFurnace = furnaceWithOutput(new ItemStack(Items.BAKED_POTATO, 40));
        FurnaceMenu manualMenu = new FurnaceMenu(1, player.getInventory(), manualFurnace, new SimpleContainerData(4));
        manualMenu.clicked(2, 0, ClickType.PICKUP, player);
        helper.assertTrue(manualMenu.getCarried().getCount() == 64,
                "manual furnace extraction did not return a doubled stack within the item limit");
        helper.assertTrue(manualFurnace.getItem(2).getCount() == 8,
                "manual furnace extraction consumed the wrong number of base outputs");

        clearInventory(player);
        SimpleContainer shiftFurnace = furnaceWithOutput(new ItemStack(Items.BAKED_POTATO, 40));
        FurnaceMenu shiftMenu = new FurnaceMenu(2, player.getInventory(), shiftFurnace, new SimpleContainerData(4));
        shiftMenu.clicked(2, 0, ClickType.QUICK_MOVE, player);
        helper.assertTrue(countInventoryItem(player, Items.BAKED_POTATO) == 80,
                "shift-click furnace extraction did not deliver doubled output");
        helper.assertTrue(shiftFurnace.getItem(2).isEmpty(),
                "shift-click furnace extraction did not consume the extracted base output");

        Player unskilled = helper.makeMockSurvivalPlayer();
        SimpleContainer unskilledFurnace = furnaceWithOutput(new ItemStack(Items.BAKED_POTATO, 40));
        FurnaceMenu unskilledMenu = new FurnaceMenu(
                3, unskilled.getInventory(), unskilledFurnace, new SimpleContainerData(4));
        unskilledMenu.clicked(2, 0, ClickType.QUICK_MOVE, unskilled);
        helper.assertTrue(countInventoryItem(unskilled, Items.BAKED_POTATO) == 40,
                "unskilled player received doubled furnace output");
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void campfireDoublesOnlyPlayerOwnedPlacementAndPersistsOwner(GameTestHelper helper) {
        BlockPos position = helper.absolutePos(BlockPos.ZERO.above());
        BlockState state = Blocks.CAMPFIRE.defaultBlockState();
        helper.getLevel().setBlockAndUpdate(position, state);
        CampfireBlockEntity campfire = requireCampfire(helper, position);

        Player skilled = skilledPlayer(helper);
        ItemStack skilledInput = new ItemStack(Items.BEEF);
        helper.assertTrue(campfire.placeFood(skilled, skilledInput, 1),
                "skilled player could not place food on the campfire");
        CompoundTag saved = campfire.saveWithFullMetadata();
        campfire.clearContent();
        campfire.load(saved);
        CampfireBlockEntity.cookTick(helper.getLevel(), position, state, campfire);
        helper.assertTrue(totalDroppedItems(helper, position) == 2,
                "skilled campfire placement did not produce two food items after NBT reload");

        discardDrops(helper, position);
        Player unskilled = helper.makeMockSurvivalPlayer();
        ItemStack unskilledInput = new ItemStack(Items.BEEF);
        helper.assertTrue(campfire.placeFood(unskilled, unskilledInput, 1),
                "unskilled player could not place food on the campfire");
        CampfireBlockEntity.cookTick(helper.getLevel(), position, state, campfire);
        helper.assertTrue(totalDroppedItems(helper, position) == 1,
                "unskilled campfire placement produced a doubled result");

        discardDrops(helper, position);
        ItemStack automatedInput = new ItemStack(Items.BEEF);
        helper.assertTrue(campfire.placeFood(null, automatedInput, 1),
                "non-player campfire input could not be placed");
        CampfireBlockEntity.cookTick(helper.getLevel(), position, state, campfire);
        helper.assertTrue(totalDroppedItems(helper, position) == 1,
                "non-player campfire input produced a doubled result");
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void smokerMenuDoublesButHopperAutomationStaysNormal(GameTestHelper helper) {
        Player player = skilledPlayer(helper);
        SimpleContainer smoker = furnaceWithOutput(new ItemStack(Items.BAKED_POTATO, 4));
        SmokerMenu smokerMenu = new SmokerMenu(
                4, player.getInventory(), smoker, new SimpleContainerData(4));
        smokerMenu.clicked(2, 0, ClickType.QUICK_MOVE, player);
        helper.assertTrue(countInventoryItem(player, Items.BAKED_POTATO) == 8,
                "manual smoker extraction did not double food output");
        helper.assertTrue(smoker.getItem(2).isEmpty(),
                "manual smoker extraction did not consume the base output");

        BlockPos hopperPosition = helper.absolutePos(BlockPos.ZERO.above());
        BlockPos furnacePosition = hopperPosition.above();
        helper.getLevel().setBlockAndUpdate(furnacePosition, Blocks.FURNACE.defaultBlockState());
        helper.getLevel().setBlockAndUpdate(hopperPosition, Blocks.HOPPER.defaultBlockState());
        if (!(helper.getLevel().getBlockEntity(furnacePosition)
                instanceof net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity furnace)) {
            throw new GameTestAssertException("furnace block entity is missing");
        }
        if (!(helper.getLevel().getBlockEntity(hopperPosition) instanceof HopperBlockEntity hopper)) {
            throw new GameTestAssertException("hopper block entity is missing");
        }
        furnace.setItem(2, new ItemStack(Items.BAKED_POTATO, 4));
        for (int transfer = 0; transfer < 4; transfer++) {
            HopperBlockEntity.suckInItems(helper.getLevel(), hopper);
        }
        helper.assertTrue(countContainerItem(hopper, Items.BAKED_POTATO) == 4,
                "hopper automation changed the furnace output multiplier");
        helper.assertTrue(furnace.getItem(2).isEmpty(),
                "hopper automation did not extract the four base outputs");
        helper.succeed();
    }

    private static CraftingMenu primedCraftingMenu(Player player, ItemStack baseResult) {
        CraftingMenu menu = new CraftingMenu(1, player.getInventory(), ContainerLevelAccess.NULL);
        menu.getSlot(1).set(new ItemStack(Items.WHEAT));
        FoodProductionTransactions.CraftingResultPlan plan =
                FoodProductionTransactions.planPersonalCraftingResult(player, baseResult);
        menu.getSlot(0).set(plan.displayedResult().copy());
        if (!(menu.getSlot(0) instanceof FoodProductionResultSlotExtension extension)) {
            throw new GameTestAssertException("ResultSlotMixin extension is missing");
        }
        extension.foodhealing$setCraftingOverflow(plan.overflow());
        return menu;
    }

    private static SimpleContainer furnaceWithOutput(ItemStack result) {
        SimpleContainer container = new SimpleContainer(3);
        container.setItem(2, result);
        return container;
    }

    private static CampfireBlockEntity requireCampfire(GameTestHelper helper, BlockPos position) {
        if (helper.getLevel().getBlockEntity(position) instanceof CampfireBlockEntity campfire) {
            return campfire;
        }
        throw new GameTestAssertException("campfire block entity is missing");
    }

    private static int totalDroppedItems(GameTestHelper helper, BlockPos position) {
        return nearbyDrops(helper, position).stream()
                .mapToInt(drop -> drop.getItem().getCount())
                .sum();
    }

    private static void discardDrops(GameTestHelper helper, BlockPos position) {
        nearbyDrops(helper, position).forEach(Entity::discard);
    }

    private static java.util.List<ItemEntity> nearbyDrops(GameTestHelper helper, BlockPos position) {
        return helper.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(position).inflate(2.0D));
    }

    private static Player skilledPlayer(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        shokugi(player).setSkillLevel(FoodHealingSkillIds.FOOD_PRODUCTION_MASTERY, 1);
        return player;
    }

    private static IShokugiData shokugi(Player player) {
        return player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .orElseThrow(() -> new GameTestAssertException("Shokugi capability is missing"));
    }

    private static int countInventoryItem(Player player, net.minecraft.world.item.Item item) {
        int total = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(item)) {
                total += stack.getCount();
            }
        }
        return total;
    }

    private static int countContainerItem(net.minecraft.world.Container container,
                                          net.minecraft.world.item.Item item) {
        int total = 0;
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack stack = container.getItem(slot);
            if (stack.is(item)) {
                total += stack.getCount();
            }
        }
        return total;
    }

    private static void clearInventory(Player player) {
        player.getInventory().clearContent();
    }

    private static void assertCraftingSlotsEmpty(GameTestHelper helper, CraftingMenu menu,
                                                  int firstSlot, int lastSlot, String message) {
        for (int slot = firstSlot; slot <= lastSlot; slot++) {
            helper.assertTrue(menu.getSlot(slot).getItem().isEmpty(), message);
        }
    }
}
