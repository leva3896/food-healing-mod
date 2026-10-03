package com.leva.foodhealing;

import com.leva.foodhealing.capability.IShokugiData;
import com.leva.foodhealing.capability.ShokugiData;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.leva.foodhealing.loot.GatheringLootModifier;
import com.leva.foodhealing.network.*;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.ReferenceCountUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.network.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

/** Dev-only tests, excluded from the distribution by the existing GameTests class filter. */
@GameTestHolder(FoodHealingMod.MODID)
@PrefixGameTestTemplate(false)
public final class FoodHealingLegacyParityGameTests {
    private static final Item[] ITEMS = {Items.DIAMOND_CHESTPLATE, Items.DIAMOND_SWORD,
            Items.DIAMOND_PICKAXE, Items.BOW, Items.CROSSBOW, Items.SHIELD, Items.FISHING_ROD};

    @GameTest(template = "empty")
    public static void parityRegisteredPurchasesSaveAndSync(GameTestHelper h) throws Exception {
        try (Fixture f = new Fixture(h)) {
            String[] ids = {FoodHealingSkillIds.GATHERING, FoodHealingSkillIds.UNBREAKING,
                    FoodHealingSkillIds.PURSUIT, FoodHealingSkillIds.ARMOR_MASTERY};
            long[][] costs = {{5,5,5}, {10,20,30}, {100,100,100,100,100,100,100,100,100}, {20}};
            f.data().setUnspentSkillPoints(995);
            long spent = 0;
            for (int k = 0; k < ids.length; k++) {
                for (int n = 0; n < costs[k].length; n++) {
                    f.dispatch(new PurchaseSkillPacket(ids[k], n)); spent += costs[k][n];
                    h.assertTrue(f.data().getSkillLevel(ids[k]) == n + 1
                            && f.data().getSpentSkillPoints() == spent && f.data().getUnspentSkillPoints() == 995 - spent,
                            "registered server purchase exact stage/SP");
                    h.assertTrue(f.data().isSkillDisabled(ids[k]) == (n > 0), "upgrade preserves OFF");
                    CompoundTag before = f.data().serializeNBT();
                    f.dispatch(new PurchaseSkillPacket(ids[k], n));
                    f.dispatch(new PurchaseSkillPacket(ids[k], n + 2));
                    h.assertTrue(f.data().serializeNBT().equals(before), "stale/future request changed SP");
                    f.dispatch(new ToggleSkillPacket(ids[k], true));
                }
            }
            CompoundTag saved = f.data().serializeNBT();
            f.assertSynced();
            ByteArrayOutputStream disk = new ByteArrayOutputStream(); NbtIo.writeCompressed(saved, disk);
            ShokugiData reloaded = new ShokugiData();
            reloaded.deserializeNBT(NbtIo.readCompressed(new ByteArrayInputStream(disk.toByteArray())));
            h.assertTrue(reloaded.serializeNBT().equals(saved), "compressed normal NBT reload levels/SP/toggles");
            FriendlyByteBuf wire = new FriendlyByteBuf(Unpooled.buffer());
            FriendlyByteBuf copy = new FriendlyByteBuf(Unpooled.buffer());
            try {
                new ShokugiSyncPacket(saved).encode(wire); new ShokugiSyncPacket(wire).encode(copy);
                ShokugiData client = new ShokugiData(); client.deserializeNBT(copy.readNbt());
                h.assertTrue(client.serializeNBT().equals(saved), "client canonical decode did not retain upper levels");
            } finally { wire.release(); copy.release(); }
        }
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void parityGatheringFinalListMatrix(GameTestHelper h) {
        try (Fixture f = new Fixture(h)) {
            var modifier = new GatheringLootModifier(new LootItemCondition[0]);
            LootContext context = context(h, f.player, Blocks.DIAMOND_ORE.defaultBlockState(), ItemStack.EMPTY, 77);
            for (int level = 0; level <= 3; level++) {
                f.data().setSkillLevel(FoodHealingSkillIds.GATHERING, level);
                for (int count : new int[]{1, 3, 64}) {
                    ItemStack source = new ItemStack(Items.DIAMOND, count);
                    source.getOrCreateTag().putString("parity", "preserve");
                    var input = ObjectArrayList.of(source);
                    var output = modifier.apply(input, context);
                    int multiplier = level == 0 ? 1 : level * 2;
                    h.assertTrue(count(output) == count * multiplier, "generated final-list multiplier");
                    h.assertTrue(source.getCount() == count && input.size() == 1, "source list was mutated");
                    for (ItemStack stack : output) h.assertTrue(stack.getCount() <= stack.getMaxStackSize()
                            && source.getTag().equals(stack.getTag()), "split count/NBT");
                    f.data().setSkillDisabled(FoodHealingSkillIds.GATHERING, true);
                    h.assertTrue(modifier.apply(input, context) == input, "OFF must return unchanged list");
                    f.data().setSkillDisabled(FoodHealingSkillIds.GATHERING, false);
                }
            }
            var unrelated = ObjectArrayList.of(new ItemStack(Items.DIAMOND, 3));
            h.assertTrue(modifier.apply(unrelated, context(h, f.player, Blocks.STONE.defaultBlockState(), ItemStack.EMPTY, 77))
                    == unrelated, "unrelated block loot changed");
            LootParams noBlock = new LootParams.Builder(h.getLevel()).withParameter(LootContextParams.ORIGIN, Vec3.ZERO)
                    .withParameter(LootContextParams.THIS_ENTITY, f.player).create(new LootContextParamSet.Builder()
                            .optional(LootContextParams.ORIGIN).optional(LootContextParams.THIS_ENTITY).build());
            // An entity/death loot context has no BLOCK_STATE and must be unchanged.
            h.assertTrue(modifier.apply(unrelated, new LootContext.Builder(noBlock).create(null)) == unrelated,
                    "non-block/player loot changed");
        }
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void parityGatheringNativeFortune(GameTestHelper h) {
        try (Fixture f = new Fixture(h)) {
            ItemStack tool = new ItemStack(Items.DIAMOND_PICKAXE); tool.enchant(Enchantments.BLOCK_FORTUNE, 3);
            BlockState ore = Blocks.DIAMOND_ORE.defaultBlockState();
            LootTable table = h.getLevel().getServer().getLootData().getLootTable(ore.getBlock().getLootTable());
            for (long seed : new long[]{1, 91, 456, 9999}) {
                f.data().setSkillLevel(FoodHealingSkillIds.GATHERING, 0);
                List<ItemStack> base = new java.util.ArrayList<>();
                table.getRandomItems(context(h, f.player, ore, tool, seed), base::add);
                f.data().setSkillLevel(FoodHealingSkillIds.GATHERING, 3);
                List<ItemStack> multiplied = new java.util.ArrayList<>();
                table.getRandomItems(context(h, f.player, ore, tool, seed), multiplied::add);
                h.assertTrue(count(base) >= 1 && count(multiplied) == count(base) * 6, "native Fortune N -> 6N");
                com.mojang.logging.LogUtils.getLogger().info("PARITY_FORTUNE seed={} nativeBase={} gathered={}", seed, count(base), count(multiplied));
            }
        }
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void parityDurabilityCapBothNativeEntrances(GameTestHelper h) {
        try (Fixture f = new Fixture(h)) {
            f.data().setSkillLevel(FoodHealingSkillIds.ARMOR_MASTERY, 1);
            for (Item item : ITEMS) for (int amount : new int[]{0, 1, 2, 5, 100, Integer.MAX_VALUE - 100}) {
                for (boolean direct : new boolean[]{false, true}) {
                    ItemStack stack = tagged(item); f.player.script = new ScriptRandom(1, 0);
                    int[] broken = {0};
                    damage(stack, amount, f, direct, broken);
                    h.assertTrue(stack.getDamageValue() == Math.min(amount, 1) && stack.getCount() == 1 && broken[0] == 0,
                            "standard cap item=" + item + " amount=" + amount + " direct=" + direct);
                    h.assertTrue(f.player.script.floats == 0 && f.player.script.ints == 0, "cap alone consumed RNG");
                    h.assertTrue("kept".equals(stack.getTag().getString("parity")), "cap changed metadata");
                }
            }
            for (boolean disabled : new boolean[]{true, false}) {
                f.data().setSkillDisabled(FoodHealingSkillIds.ARMOR_MASTERY, disabled);
                if (!disabled) f.data().setSkillLevel(FoodHealingSkillIds.ARMOR_MASTERY, 0);
                for (Item item : ITEMS) for (boolean direct : new boolean[]{false, true}) {
                    ItemStack stack = tagged(item); damage(stack, 5, f, direct, new int[1]);
                    h.assertTrue(stack.getDamageValue() == 5, "OFF/unowned cap changed amount");
                }
            }
        }
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void parityUnbreakingExactBoundariesAndOneRoll(GameTestHelper h) {
        try (Fixture f = new Fixture(h)) {
            float[] chances = {.9F, .95F, 29F / 30F};
            for (int level = 1; level <= 3; level++) {
                f.data().setSkillLevel(FoodHealingSkillIds.UNBREAKING, level);
                for (boolean cap : new boolean[]{false, true}) {
                    f.data().setSkillLevel(FoodHealingSkillIds.ARMOR_MASTERY, cap ? 1 : 0);
                    for (boolean success : new boolean[]{false, true}) for (Item item : ITEMS)
                        for (boolean direct : new boolean[]{false, true}) {
                            float roll = success ? Math.nextDown(chances[level-1]) : chances[level-1];
                            f.player.script = new ScriptRandom(roll, 0);
                            // Fishing rods have only 64 durability; break behavior has its own case.
                            int original = item == Items.FISHING_ROD ? 5 : 100;
                            ItemStack stack = tagged(item); damage(stack, original, f, direct, new int[1]);
                            h.assertTrue(stack.getDamageValue() == (success ? 0 : cap ? 1 : original), "exact FH probability/cap: item="
                                    + item + " level=" + level + " cap=" + cap + " success=" + success + " direct=" + direct
                                    + " damage=" + stack.getDamageValue() + " count=" + stack.getCount() + " floats=" + f.player.script.floats);
                            h.assertTrue(f.player.script.floats == 1 && f.player.script.ints == 0,
                                    "FH roll must occur once at common entry, direct=" + direct);
                        }
                }
            }
        }
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void parityDurabilityNegativeRng(GameTestHelper h) {
        try (Fixture f = new Fixture(h)) {
            for (int variant = 0; variant < 6; variant++) {
                f.data().setSkillLevel(FoodHealingSkillIds.UNBREAKING, variant == 0 ? 0 : 3);
                f.data().setSkillDisabled(FoodHealingSkillIds.UNBREAKING, variant == 1);
                f.player.script = new ScriptRandom(0, 0);
                ItemStack stack = new ItemStack(variant == 2 ? Items.BREAD : Items.DIAMOND_PICKAXE);
                if (variant == 5) stack.getOrCreateTag().putBoolean("Unbreakable", true);
                int amount = variant == 3 ? 0 : 5;
                stack.hurt(amount, f.player.script, variant == 4 ? null : f.player);
                h.assertTrue(f.player.script.floats == 0 && f.player.script.ints == 0, "negative consumes FH RNG " + variant);
                h.assertTrue(stack.getDamageValue() == ((variant == 0 || variant == 1 || variant == 4) ? 5 : 0),
                        "negative changed native durability");
            }
            for (float invalid : new float[]{Float.NaN, Float.POSITIVE_INFINITY, -1F}) {
                h.assertTrue(DurabilityTransactions.adjustDamage(f.data(), new ItemStack(Items.DIAMOND_SWORD), 5, invalid) == 5,
                        "invalid roll saved wear");
            }
        }
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void parityVanillaEnchantAfterFoodHealing(GameTestHelper h) {
        try (Fixture f = new Fixture(h)) {
            f.data().setSkillLevel(FoodHealingSkillIds.ARMOR_MASTERY, 1);
            for (int fhLevel = 1; fhLevel <= 3; fhLevel++) {
                f.data().setSkillLevel(FoodHealingSkillIds.UNBREAKING, fhLevel);
                for (Item item : new Item[]{Items.DIAMOND_SWORD, Items.DIAMOND_CHESTPLATE})
                    for (int enchant : new int[]{0, 1, 3}) for (boolean fhSave : new boolean[]{true, false})
                        for (boolean vanillaSave : new boolean[]{true, false}) for (boolean direct : new boolean[]{false, true}) {
                            ItemStack stack = tagged(item); if (enchant > 0) stack.enchant(Enchantments.UNBREAKING, enchant);
                            f.player.script = new ScriptRandom(fhSave ? 0F : 1F, vanillaSave ? 1 : 0);
                            damage(stack, 100, f, direct, new int[1]);
                            h.assertTrue(stack.getDamageValue() == (fhSave || (enchant > 0 && vanillaSave) ? 0 : 1), "native enchant result");
                            h.assertTrue(f.player.script.ints == (!fhSave && enchant > 0 ? 1 : 0), "native enchant sees remaining 0/1");
                            h.assertTrue(f.player.script.floats == 1 + (!fhSave && enchant > 0 && item instanceof ArmorItem ? 1 : 0),
                                    "FH roll then native armor branch; no replacement/double roll");
                        }
            }
        }
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void parityDurabilityBreakBoundary(GameTestHelper h) {
        try (Fixture f = new Fixture(h)) {
            for (int remaining : new int[]{1, 2, 100}) for (boolean direct : new boolean[]{false, true})
                for (boolean cap : new boolean[]{false, true}) {
                    f.data().setSkillLevel(FoodHealingSkillIds.ARMOR_MASTERY, cap ? 1 : 0);
                    ItemStack stack = tagged(Items.DIAMOND_SWORD); int max = stack.getMaxDamage();
                    stack.setDamageValue(max - remaining);
                    int[] broken = {0}; boolean result = damage(stack, 5, f, direct, broken);
                    int wear = cap ? 1 : 5; boolean breaks = wear >= remaining;
                    if (direct) h.assertTrue(result == breaks && broken[0] == 0 && stack.getCount() == 1
                            && stack.getDamageValue() == max - remaining + wear, "direct hurt reports break, caller owns shrink");
                    else h.assertTrue(broken[0] == (breaks ? 1 : 0) && stack.getCount() == (breaks ? 0 : 1)
                            && stack.getDamageValue() == (breaks ? 0 : max - remaining + wear), "native break callback/shrink/reset");
                    if (!stack.isEmpty()) h.assertTrue("kept".equals(stack.getTag().getString("parity")), "break boundary metadata");
                }
        }
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void parityPursuitAllLevelsNativeDamage(GameTestHelper h) {
        try (Fixture f = new Fixture(h)) {
            f.data().setBaseStatPoints(FoodHealingBaseStatIds.BASE_OUTGOING_DAMAGE, 100);
            for (int level = 0; level <= 9; level++) {
                f.data().setSkillLevel(FoodHealingSkillIds.PURSUIT, level);
                CountingCow target = new CountingCow(h.getLevel()); target.invulnerableTime = 13;
                DamageSource source = h.getLevel().damageSources().playerAttack(f.player);
                DamageEventHandler.onLivingDamage(new LivingDamageEvent(target, source, 1F));
                h.assertTrue(target.calls == level && target.lastSource == (level == 0 ? null : source), "extra count/source at " + level);
                h.assertTrue(close(target.getHealth(), 10F - level) && target.invulnerableTime == 13,
                        "FH outgoing not repeated / iframe restored level=" + level);
                f.data().setSkillDisabled(FoodHealingSkillIds.PURSUIT, true);
                DamageEventHandler.onLivingDamage(new LivingDamageEvent(target, source, 1F));
                h.assertTrue(target.calls == level, "OFF added a hit");
                f.data().setSkillDisabled(FoodHealingSkillIds.PURSUIT, false);
            }
        }
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void parityPursuitNativeVictimDefenseAndPlayerScope(GameTestHelper h) {
        boolean pvp = h.getLevel().getServer().isPvpAllowed();
        h.getLevel().getServer().setPvpAllowed(true);
        try (Fixture f = new Fixture(h); Fixture victim = new Fixture(h)) {
            f.data().setSkillLevel(FoodHealingSkillIds.PURSUIT, 2);
            CountingCow target = new CountingCow(h.getLevel());
            target.getAttribute(Attributes.ARMOR).setBaseValue(10);
            target.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 0));
            target.invulnerableTime = 7;
            DamageSource source = h.getLevel().damageSources().playerAttack(f.player);
            DamageEventHandler.onLivingDamage(new LivingDamageEvent(target, source, 2F));
            float defended = net.minecraft.world.damagesource.CombatRules.getDamageAfterAbsorb(2F, 10F, 0F) * .8F;
            h.assertTrue(target.calls == 2 && close(target.getHealth(), 10F - 2 * defended) && target.invulnerableTime == 7,
                    "each follow-up must traverse native armor and Resistance");
            victim.data().setBaseStatPoints(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR, 50);
            h.assertTrue(victim.player.canHarmPlayer(f.player), "fixture PvP must be permitted before measurement");
            victim.player.invulnerableTime = 11;
            DamageEventHandler.onLivingDamage(new LivingDamageEvent(victim.player, source, 4F));
            // Original event 4 -> 2; each separate follow-up 2 -> 1 at victim's 50% layer.
            h.assertTrue(close(victim.player.getHealth(), 18F) && victim.player.invulnerableTime == 11,
                    "Player native victim defense: health=" + victim.player.getHealth() + " iframe=" + victim.player.invulnerableTime
                            + " invulnerable=" + victim.player.isInvulnerableTo(source));
        } finally { h.getLevel().getServer().setPvpAllowed(pvp); }
        h.succeed();
    }

    @GameTest(template = "empty")
    public static void parityPursuitDeathAndExceptionRestoreGuard(GameTestHelper h) {
        try (Fixture f = new Fixture(h)) {
            f.data().setSkillLevel(FoodHealingSkillIds.PURSUIT, 9);
            DamageSource source = h.getLevel().damageSources().playerAttack(f.player);
            CountingCow dying = new CountingCow(h.getLevel()); dying.setHealth(2); dying.invulnerableTime = 17;
            DamageEventHandler.onLivingDamage(new LivingDamageEvent(dying, source, 3F));
            h.assertTrue(dying.calls == 1 && !dying.isAlive() && dying.invulnerableTime == 17, "death must stop remaining transactions");
            CountingCow throwing = new CountingCow(h.getLevel()); throwing.fail = true; throwing.invulnerableTime = 19;
            boolean threw = false;
            try { DamageEventHandler.onLivingDamage(new LivingDamageEvent(throwing, source, 1F)); }
            catch (DeliberateFailure expected) { threw = true; }
            h.assertTrue(threw && throwing.calls == 1 && throwing.invulnerableTime == 19, "exception restores iframe");
            CountingCow after = new CountingCow(h.getLevel());
            DamageEventHandler.onLivingDamage(new LivingDamageEvent(after, source, .1F));
            h.assertTrue(after.calls == 9, "exception leaked recursion guard");
        }
        h.succeed();
    }

    private static LootContext context(GameTestHelper h, ServerPlayer player, BlockState state, ItemStack tool, long seed) {
        LootParams params = new LootParams.Builder(h.getLevel()).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(h.absolutePos(BlockPos.ZERO)))
                .withParameter(LootContextParams.BLOCK_STATE, state).withParameter(LootContextParams.TOOL, tool)
                .withParameter(LootContextParams.THIS_ENTITY, player).create(LootContextParamSets.BLOCK);
        return new LootContext.Builder(params).withOptionalRandomSeed(seed).create(null);
    }
    private static int count(List<ItemStack> stacks) { return stacks.stream().mapToInt(ItemStack::getCount).sum(); }
    private static boolean close(float a, float b) { return Math.abs(a-b) < .0001F; }
    private static ItemStack tagged(Item item) { ItemStack s = new ItemStack(item); s.getOrCreateTag().putString("parity", "kept"); return s; }
    private static boolean damage(ItemStack stack, int amount, Fixture f, boolean direct, int[] broken) {
        if (direct) return stack.hurt(amount, f.player.getRandom(), f.player);
        stack.hurtAndBreak(amount, f.player, p -> broken[0]++); return broken[0] != 0;
    }
    private static final class ScriptRandom extends LegacyRandomSource {
        final float first; final int integer; int floats; int ints;
        ScriptRandom(float first, int integer) { super(123); this.first=first; this.integer=integer; }
        @Override public float nextFloat() { return floats++ == 0 ? first : 1F; }
        @Override public int nextInt(int bound) { ints++; return Math.min(integer, bound-1); }
    }
    private static final class TestPlayer extends ServerPlayer {
        ScriptRandom script;
        TestPlayer(GameTestHelper h) { super(h.getLevel().getServer(), h.getLevel(), new GameProfile(UUID.randomUUID(), "fh-parity")); }
        @Override public RandomSource getRandom() { return script == null ? super.getRandom() : script; }
    }
    private static final class Fixture implements AutoCloseable {
        final GameTestHelper h; final TestPlayer player; final Connection connection; final EmbeddedChannel channel;
        Fixture(GameTestHelper h) {
            this.h=h; player=new TestPlayer(h); connection=new Connection(PacketFlow.SERVERBOUND); channel=new EmbeddedChannel(connection);
            h.getLevel().getServer().getPlayerList().placeNewPlayer(connection, player);
            player.setPos(0, 100, 0); player.setNoGravity(true);
            // Let native login protection expire; do not override the damage eligibility gate.
            for (int i=0; i<65; i++) player.tick();
        }
        IShokugiData data() { return player.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(IllegalStateException::new); }
        void dispatch(Object message) {
            ICustomPacket<?> packet=(ICustomPacket<?>) PacketHandler.INSTANCE.toVanillaPacket(message, NetworkDirection.PLAY_TO_SERVER);
            try { h.assertTrue(NetworkHooks.onCustomPayload(packet, connection), "registered packet was not handled"); }
            finally { packet.getInternalData().release(); }
        }
        void assertSynced() {
            ICustomPacket<?> expected=(ICustomPacket<?>) PacketHandler.INSTANCE.toVanillaPacket(
                    new ShokugiSyncPacket(data().serializeNBT()), NetworkDirection.PLAY_TO_CLIENT);
            boolean found=false; channel.runPendingTasks(); Object message;
            try {
                while ((message=channel.readOutbound()) != null) {
                    if (message instanceof ICustomPacket<?> actual)
                        found |= ByteBufUtil.equals(expected.getInternalData(), actual.getInternalData());
                    ReferenceCountUtil.release(message);
                }
            } finally { expected.getInternalData().release(); }
            h.assertTrue(found, "purchase/toggle handler did not emit current canonical sync");
        }
        @Override public void close() {
            h.getLevel().getServer().getPlayerList().remove(player);
            Object msg; while ((msg=channel.readOutbound())!=null) ReferenceCountUtil.release(msg);
            channel.finishAndReleaseAll();
        }
    }
    private static final class DeliberateFailure extends RuntimeException { }
    private static final class CountingCow extends Cow {
        int calls; DamageSource lastSource; boolean fail;
        CountingCow(Level level) { super(EntityType.COW, level); }
        @Override public boolean hurt(DamageSource source, float amount) {
            calls++; lastSource=source; if (fail) throw new DeliberateFailure(); return super.hurt(source, amount);
        }
    }
}
