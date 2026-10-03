package com.leva.foodhealing;

import com.leva.foodhealing.capability.*;
import com.leva.foodhealing.loot.GatheringLootModifier;
import com.leva.foodhealing.network.*;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.ReferenceCountUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraft.nbt.*;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.gametest.*;
import net.minecraftforge.network.*;
import java.io.*;
import java.util.*;
import java.util.function.Consumer;

/** Dev-only native route tests. Existing distribution filter excludes this entire class family. */
@GameTestHolder(FoodHealingMod.MODID)
@PrefixGameTestTemplate(false)
public final class FoodHealingRebalanceGameTests {
    private static final String Q = FoodHealingSkillIds.QUARRYING, I = FoodHealingSkillIds.IMMOVABLE_MASTERY;
    private static final Vec3 MOTION = new Vec3(.12, .23, -.17);

    @GameTest(template="empty")
    public static void rebalanceRegisteredNewSkillsSaveSync(GameTestHelper h) throws Exception {
        try (Fixture f = new Fixture(h)) {
            f.data().setUnspentSkillPoints(51);
            for (String id : new String[]{Q,I}) {
                f.dispatch(new PurchaseSkillPacket(id,0));
                h.assertTrue(f.data().getSkillLevel(id)==1 && !f.data().isSkillDisabled(id), "native purchase/default ON");
                CompoundTag before=f.data().serializeNBT();
                f.dispatch(new PurchaseSkillPacket(id,0)); f.dispatch(new PurchaseSkillPacket(id,2));
                h.assertTrue(before.equals(f.data().serializeNBT()), "replay/future debit");
                f.dispatch(new ToggleSkillPacket(id,true));
            }
            h.assertTrue(f.data().getSpentSkillPoints()==51 && f.data().getUnspentSkillPoints()==0, "exact 1+50 SP");
            f.assertSynced();
            ByteArrayOutputStream out=new ByteArrayOutputStream(); NbtIo.writeCompressed(f.data().serializeNBT(),out);
            ShokugiData restored=new ShokugiData(); restored.deserializeNBT(NbtIo.readCompressed(new ByteArrayInputStream(out.toByteArray())));
            h.assertTrue(restored.serializeNBT().equals(f.data().serializeNBT()), "normal NBT reload ownership/SP/OFF");
            CompoundTag canonical=f.data().serializeNBT();
            try(Fixture replacement=new Fixture(h)) {
                MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerEvent.Clone(replacement.player,f.player,false));
                h.assertTrue(replacement.data().serializeNBT().equals(canonical), "canonical lifecycle clone");
            } finally {f.player.reviveCaps();}
        }
        h.succeed();
    }

    @GameTest(template="empty")
    public static void rebalanceAmmoNewPricesAndGate(GameTestHelper h) {
        try (Fixture f=new Fixture(h)) {
            f.data().setUnspentSkillPoints(500);
            boolean ready=com.leva.foodhealing.compat.TaczAmmoCompatibility.ready();
            for(int n=0;n<10;n++) {
                f.dispatch(new PurchaseSkillPacket(FoodHealingSkillIds.TACZ_AMMO_CONSERVATION,n));
                h.assertTrue(f.data().getSpentSkillPoints()==(ready?50L*(n+1):0),"TaCZ stage exact price/absence gate");
            }
            h.assertTrue(f.data().getSkillLevel(FoodHealingSkillIds.TACZ_AMMO_CONSERVATION)==(ready?10:0),"TaCZ level");
        }
        h.succeed();
    }

    @GameTest(template="empty")
    public static void quarryingNativeDestroyProgress(GameTestHelper h) {
        try (Fixture f=new Fixture(h)) {
            BlockPos p=h.absolutePos(BlockPos.ZERO); f.player.setOnGround(true);
            for(int variant=0;variant<4;variant++) {
                f.player.removeAllEffects(); ItemStack pick=new ItemStack(Items.DIAMOND_PICKAXE);
                if(variant==1) pick.enchant(Enchantments.BLOCK_EFFICIENCY,5);
                if(variant==2) f.player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED,100,1));
                if(variant==3) f.player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN,100,1));
                f.player.setItemInHand(InteractionHand.MAIN_HAND,pick); f.skill(Q,0);
                float stone=Blocks.STONE.defaultBlockState().getDestroyProgress(f.player,h.getLevel(),p);
                float deep=Blocks.DEEPSLATE.defaultBlockState().getDestroyProgress(f.player,h.getLevel(),p);
                near(h,deep*2,stone,"native hardness ratio"); f.skill(Q,1);
                near(h,Blocks.DEEPSLATE.defaultBlockState().getDestroyProgress(f.player,h.getLevel(),p),stone,"ON stone-equivalent variant="+variant);
                near(h,Blocks.STONE.defaultBlockState().getDestroyProgress(f.player,h.getLevel(),p),stone,"stone unchanged");
                for(Block b:NEGATIVE_BLOCKS) {
                    float on=b.defaultBlockState().getDestroyProgress(f.player,h.getLevel(),p);
                    f.data().setSkillDisabled(Q,true);
                    near(h,b.defaultBlockState().getDestroyProgress(f.player,h.getLevel(),p),on,"non-deepslate speed");
                    f.data().setSkillDisabled(Q,false);
                }
                f.data().setSkillDisabled(Q,true);
                near(h,Blocks.DEEPSLATE.defaultBlockState().getDestroyProgress(f.player,h.getLevel(),p),deep,"OFF native speed");
            }
        }
        h.succeed();
    }

    @GameTest(template="empty")
    public static void quarryingOneDrawAndFinalList(GameTestHelper h) {
        try (Fixture f=new Fixture(h)) {
            f.skill(Q,1); BlockPos pos=h.absolutePos(BlockPos.ZERO);
            var modifier=new GatheringLootModifier(new LootItemCondition[0]);
            for(int selection : new int[]{0,1,2}) for(int gathering=0;gathering<=4;gathering++) {
                long seed=seedFor(selection); RandomSource expected=RandomSource.create(seed); expected.nextDouble();
                f.skill(FoodHealingSkillIds.GATHERING,Math.min(gathering,3));
                f.data().setSkillDisabled(FoodHealingSkillIds.GATHERING,gathering==4);
                int qty=gathering==0||gathering==4?1:gathering*2;
                ItemStack tool=new ItemStack(Items.DIAMOND_PICKAXE);tool.enchant(Enchantments.BLOCK_FORTUNE,3);
                BlockState state=Blocks.STONE.defaultBlockState(); LootContext c=context(h,f.player,pos,state,tool,seed);
                QuarryingController.harvest(f.player,pos,state,()->{
                    ItemStack nativeDrop=new ItemStack(Items.COBBLESTONE); var input=ObjectArrayList.of(nativeDrop);
                    var result=modifier.apply(input,c);
                    h.assertTrue(result.size()==(selection==0?1:2) && nativeDrop.getCount()==1,"one exclusive stack, native unchanged");
                    if(selection>0) h.assertTrue(result.get(1).is(selection==1?Items.IRON_ORE:Items.COPPER_ORE)
                            && result.get(1).getCount()==qty,"iron/copper quantity, no Fortune bonus");
                    h.assertTrue(modifier.apply(result,c)==result,"same harvest rolled/added twice");
                });
                h.assertTrue(c.getRandom().nextDouble()==expected.nextDouble(),"exactly one random draw");
            }
        }
        h.succeed();
    }

    private static final Block[] NEGATIVE_BLOCKS={Blocks.COBBLESTONE,Blocks.COBBLED_DEEPSLATE,Blocks.POLISHED_DEEPSLATE,
            Blocks.GRANITE,Blocks.DIORITE,Blocks.ANDESITE,Blocks.TUFF,Blocks.DIAMOND_ORE,Blocks.DEEPSLATE_IRON_ORE};
    @GameTest(template="empty")
    public static void quarryingNoBonusWithoutHarvestAuthority(GameTestHelper h) {
        try(Fixture f=new Fixture(h)) {
            f.skill(Q,1);f.skill(FoodHealingSkillIds.GATHERING,3);BlockPos pos=h.absolutePos(BlockPos.ZERO);
            for(Block block:NEGATIVE_BLOCKS) noBonus(h,f,pos,block.defaultBlockState());
            net.minecraftforge.registries.ForgeRegistries.BLOCKS.getEntries().stream()
                    .filter(e->!e.getKey().location().getNamespace().equals("minecraft")).findFirst().ifPresent(e->{
                        noBonus(h,f,pos,e.getValue().defaultBlockState());
                        com.mojang.logging.LogUtils.getLogger().info("QUARRY_MODDED_NEGATIVE {}",e.getKey().location());
                    });
            f.data().setSkillDisabled(Q,true);noBonus(h,f,pos,Blocks.STONE.defaultBlockState());
            f.data().setSkillDisabled(Q,false);
            LootContext c=context(h,f.player,pos,Blocks.STONE.defaultBlockState(),ItemStack.EMPTY,seedFor(1));
            var input=ObjectArrayList.of(new ItemStack(Items.COBBLESTONE));
            h.assertTrue(QuarryingController.addBonus(input,c)==input,"external dropResources/machine had no native harvest scope");
            LootParams exploded=new LootParams.Builder(h.getLevel()).withParameter(LootContextParams.ORIGIN,Vec3.atCenterOf(pos))
                    .withParameter(LootContextParams.THIS_ENTITY,f.player).withParameter(LootContextParams.BLOCK_STATE,Blocks.STONE.defaultBlockState())
                    .withParameter(LootContextParams.TOOL,ItemStack.EMPTY).withParameter(LootContextParams.EXPLOSION_RADIUS,1F).create(LootContextParamSets.BLOCK);
            LootContext ec=new LootContext.Builder(exploded).withOptionalRandomSeed(seedFor(1)).create(null);
            QuarryingController.harvest(f.player,pos,Blocks.STONE.defaultBlockState(),()->
                    h.assertTrue(QuarryingController.addBonus(input,ec)==input,"explosion context was authorized"));
            h.assertTrue(ec.getRandom().nextDouble()==RandomSource.create(seedFor(1)).nextDouble(),"explosion consumed bonus RNG");
            var fake=FakePlayerFactory.getMinecraft(h.getLevel());
            fake.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(d->d.setSkillLevel(Q,1));
            QuarryingController.harvest(fake,pos,Blocks.STONE.defaultBlockState(),()->
                    h.assertTrue(QuarryingController.addBonus(input,context(h,fake,pos,Blocks.STONE.defaultBlockState(),ItemStack.EMPTY,seedFor(1)))==input,"fake player authorized"));
            f.skill(Q,0);noBonus(h,f,pos,Blocks.STONE.defaultBlockState());
        }
        h.succeed();
    }

    @GameTest(template="empty",timeoutTicks=400)
    public static void quarryingActualNativeHarvest(GameTestHelper h) {
        try(Fixture f=new Fixture(h)) {
            f.skill(Q,1);f.skill(FoodHealingSkillIds.GATHERING,3);
            BlockPos pos=new BlockPos(h.absolutePos(BlockPos.ZERO).getX(),100,h.absolutePos(BlockPos.ZERO).getZ());
            f.player.setPos(pos.getX()+.5,101,pos.getZ()+2.5);
            for(Block b:new Block[]{Blocks.STONE,Blocks.DEEPSLATE}) for(int selected=1;selected<=2;selected++) {
                ItemStack pick=new ItemStack(Items.DIAMOND_PICKAXE);
                pick.enchant(selected==1?Enchantments.SILK_TOUCH:Enchantments.BLOCK_FORTUNE,selected==1?1:3);
                f.player.setItemInHand(InteractionHand.MAIN_HAND,pick);
                h.getLevel().setBlockAndUpdate(pos,b.defaultBlockState());
                long seed=seedForNative(h,f,pos,b.defaultBlockState(),pick,selected);
                h.assertTrue(f.player.gameMode.destroyBlock(pos),"native destroy failed");
                List<ItemEntity> drops=h.getLevel().getEntitiesOfClass(ItemEntity.class,new AABB(pos).inflate(2));
                int iron=0,copper=0,nativeCount=0;
                for(ItemEntity entity:drops) {
                    ItemStack s=entity.getItem(); if(s.is(Items.IRON_ORE))iron+=s.getCount();else if(s.is(Items.COPPER_ORE))copper+=s.getCount();else nativeCount+=s.getCount();
                    entity.discard();
                }
                h.assertTrue(nativeCount==1 && iron==(selected==1?6:0) && copper==(selected==2?6:0),
                        "native harvest block="+b+" seed="+seed+" native="+nativeCount+" iron="+iron+" copper="+copper);
                com.mojang.logging.LogUtils.getLogger().info("QUARRY_NATIVE block={} seed={} native={} iron={} copper={}",b,seed,nativeCount,iron,copper);
            }
            for(int mode=0;mode<3;mode++) {
                f.player.setGameMode(mode==1?GameType.CREATIVE:GameType.SURVIVAL);
                f.player.setItemInHand(InteractionHand.MAIN_HAND,mode==0?ItemStack.EMPTY:new ItemStack(Items.DIAMOND_PICKAXE));
                h.getLevel().setBlockAndUpdate(pos,Blocks.STONE.defaultBlockState());
                Consumer<BlockEvent.BreakEvent> cancel=e->{if(e.getPlayer()==f.player)e.setCanceled(true);};
                if(mode==2)MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST,cancel);
                try { f.player.gameMode.destroyBlock(pos); } finally { if(mode==2)MinecraftForge.EVENT_BUS.unregister(cancel); }
                h.assertTrue(h.getLevel().getEntitiesOfClass(ItemEntity.class,new AABB(pos).inflate(2)).isEmpty(),"wrong tool/creative/cancel produced bonus");
            }
            h.getLevel().removeBlock(pos,false);
        }
        h.succeed();
    }

    @GameTest(template="empty")
    public static void immovableDamageSourcesAndIndependentLayers(GameTestHelper h) {
        try(Fixture f=new Fixture(h)) {
            var s=h.getLevel().damageSources(); Cow cow=new Cow(EntityType.COW,h.getLevel());
            DamageSource[] sources={s.mobAttack(cow),s.arrow(new Arrow(h.getLevel(),cow),cow),s.explosion(cow,cow),
                    s.inFire(),s.fall(),s.magic(),s.starve(),s.genericKill(),s.fellOutOfWorld(),s.generic()};
            for(DamageSource source:sources) {
                f.skill(I,1);near(h,damage(f,source,100),50,"numeric reached damage hook "+source);
                f.data().setSkillDisabled(I,true);near(h,damage(f,source,100),100,"OFF numeric");
                f.skill(I,0);near(h,damage(f,source,100),100,"unowned numeric");
            }
            f.skill(I,1);f.skill(FoodHealingSkillIds.EXPLOSION_RESISTANCE,1);near(h,damage(f,s.explosion(cow,cow),100),5,"explosion multiplicative");
            f.skill(FoodHealingSkillIds.EXPLOSION_RESISTANCE,0);f.skill(FoodHealingSkillIds.FLAME_BLESSING,1);
            f.player.setSecondsOnFire(10);near(h,damage(f,s.generic(),100),35,"flame multiplicative");f.player.clearFire();f.skill(FoodHealingSkillIds.FLAME_BLESSING,0);
            f.skill(FoodHealingSkillIds.KONGO,1);near(h,damage(f,s.generic(),100),10,"Kongo target80% then half");f.skill(FoodHealingSkillIds.KONGO,0);
            f.data().setBaseStatPoints(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR,50);near(h,damage(f,s.generic(),100),25,"base multiplicative");
            f.data().setBaseStatPoints(FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_LINEAR,50);near(h,damage(f,s.generic(),100),12.5,"high layer multiplicative");
        }
        h.succeed();
    }

    @GameTest(template="empty")
    public static void immovableNativeDamageAndForcedDeathBoundary(GameTestHelper h) {
        for(int kind=0;kind<3;kind++) try(Fixture f=new Fixture(h)) {
            f.skill(I,1);h.assertTrue(f.player.hurt(h.getLevel().damageSources().generic(),4),"native numeric hurt");
            near(h,f.player.getHealth(),18,"native HP20->18");
            if(kind==0)f.player.setHealth(0);
            if(kind==1)f.player.kill();
            if(kind==2)f.player.discard();
            h.assertTrue(kind==2?f.player.isRemoved():!f.player.isAlive(),"immovable accidentally prevented direct death/removal");
        }
        h.succeed();
    }

    @GameTest(template="empty")
    public static void immovableNativeKnockbackRetainsExistingMotion(GameTestHelper h) {
        try(Fixture f=new Fixture(h)) {
            for(int mode=0;mode<3;mode++) {
                f.skill(I,mode==2?0:1);f.data().setSkillDisabled(I,mode==1);
                f.player.setOnGround(true);f.player.setDeltaMovement(MOTION);f.player.knockback(1,1,0);
                h.assertTrue(f.player.getDeltaMovement().equals(MOTION)==(mode==0),"direct knockback ON/OFF/unowned");
            }
            Cow cow=new Cow(EntityType.COW,h.getLevel());cow.setOnGround(true);cow.knockback(1,1,0);
            h.assertTrue(cow.getDeltaMovement().lengthSqr()>0,"nonplayer knockback leaked");
        }
        h.succeed();
    }

    @GameTest(template="empty")
    public static void immovableNativeMobMelee(GameTestHelper h) {
        for(boolean on:new boolean[]{true,false})try(Fixture f=new Fixture(h)) {
            f.skill(I,1);f.data().setSkillDisabled(I,!on);
            Zombie z=new Zombie(h.getLevel());z.setNoAi(true);z.setPos(f.player.getX()-1,f.player.getY(),f.player.getZ());
            z.getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue(2);
            f.player.setDeltaMovement(MOTION);f.player.setOnGround(true);
            h.assertTrue(z.doHurtTarget(f.player),"native zombie attack");
            h.assertTrue(f.player.getDeltaMovement().equals(MOTION)==on,"mob melee extra impulse");
        }
        h.succeed();
    }

    @GameTest(template="empty")
    public static void immovablePlayerEnchantAndMotionPacket(GameTestHelper h) {
        boolean pvp=h.getLevel().getServer().isPvpAllowed();h.getLevel().getServer().setPvpAllowed(true);
        try {
            for(boolean on:new boolean[]{true,false})try(Fixture attacker=new Fixture(h);Fixture victim=new Fixture(h)) {
                victim.skill(I,1);victim.data().setSkillDisabled(I,!on);
                ItemStack sword=new ItemStack(Items.WOODEN_SWORD);sword.enchant(Enchantments.KNOCKBACK,2);
                attacker.player.setItemInHand(InteractionHand.MAIN_HAND,sword);
                attacker.player.setPos(0,100,1);victim.player.setPos(0,100,0);victim.player.setOnGround(true);
                victim.player.setDeltaMovement(MOTION);victim.drain();attacker.player.attack(victim.player);
                ClientboundSetEntityMotionPacket packet=victim.packet(ClientboundSetEntityMotionPacket.class);
                h.assertTrue(packet!=null,"native melee motion packet missing");
                boolean unchanged=packet.getXa()==(int)(MOTION.x*8000)&&packet.getYa()==(int)(MOTION.y*8000)&&packet.getZa()==(int)(MOTION.z*8000);
                h.assertTrue(unchanged==on,"client motion packet added knockback while ON / missing OFF");
                h.assertTrue(victim.player.getDeltaMovement().equals(MOTION),"native player attack restores prior server vector");
            }
        } finally {h.getLevel().getServer().setPvpAllowed(pvp);}
        h.succeed();
    }

    @GameTest(template="empty")
    public static void immovableNativePunchArrow(GameTestHelper h) {
        for(boolean on:new boolean[]{true,false})try(Fixture f=new Fixture(h)) {
            f.skill(I,1);f.data().setSkillDisabled(I,!on);
            TestArrow arrow=new TestArrow(h);arrow.setOwner(new Cow(EntityType.COW,h.getLevel()));
            arrow.setDeltaMovement(1,0,0);arrow.setBaseDamage(1);arrow.setKnockback(2);
            f.player.setDeltaMovement(MOTION);arrow.hit(f.player);
            h.assertTrue(f.player.getHealth()<20,"arrow numeric damage retained");
            h.assertTrue(f.player.getDeltaMovement().equals(MOTION)==on,"native arrow direct push immunity");
        }
        h.succeed();
    }

    @GameTest(template="empty")
    public static void immovableNativeExplosionAndClientPacket(GameTestHelper h) {
        // A random GameTest terrain seed can bury the old y=100 blast site.
        // Prepare an unobstructed test volume; do not change the native impulse expectations.
        for (BlockPos pos : BlockPos.betweenClosed(-4,249,-3,3,254,3)) {
            h.getLevel().setBlockAndUpdate(pos,Blocks.AIR.defaultBlockState());
        }
        for(boolean on:new boolean[]{true,false})try(Fixture f=new Fixture(h)) {
            f.player.setPos(0,250,0);
            float exposure=net.minecraft.world.level.Explosion.getSeenPercent(new Vec3(-2,250,0),f.player);
            h.assertTrue(exposure==1.0F,"explosion fixture must have full native exposure");
            f.skill(I,1);f.data().setSkillDisabled(I,!on);f.player.setDeltaMovement(MOTION);f.drain();
            var explosion=h.getLevel().explode(null,f.player.getX()-2,f.player.getY(),f.player.getZ(),1.5F,Level.ExplosionInteraction.NONE);
            h.assertTrue(f.player.getHealth()<20 && f.player.isAlive(),"native explosion numeric damage retained");
            h.assertTrue(f.player.getDeltaMovement().equals(MOTION)==on,"native server explosion impulse");
            Vec3 kick=explosion.getHitPlayers().get(f.player);
            ClientboundExplodePacket packet=f.packet(ClientboundExplodePacket.class);
            h.assertTrue(kick!=null && packet!=null,"native explosion map/packet missing");
            h.assertTrue((kick.lengthSqr()==0)==on,"explosion map impulse");
            h.assertTrue((packet.getKnockbackX()==0&&packet.getKnockbackY()==0&&packet.getKnockbackZ()==0)==on,"explosion client packet impulse");
        }
        h.succeed();
    }

    @GameTest(template="empty")
    public static void immovableMovementIsNotGloballyFrozen(GameTestHelper h) {
        try(Fixture f=new Fixture(h)) {
            f.skill(I,1);Vec3 start=f.player.position();f.player.move(MoverType.SELF,new Vec3(.5,0,0));
            near(h,f.player.getX()-start.x,.5,"walking move");
            f.player.setDeltaMovement(Vec3.ZERO);f.player.setOnGround(true);f.player.jumpFromGround();
            h.assertTrue(f.player.getDeltaMovement().y>0,"jump suppressed");
            f.player.setNoGravity(false);f.player.setOnGround(false);f.player.setDeltaMovement(Vec3.ZERO);f.player.travel(Vec3.ZERO);
            h.assertTrue(f.player.getDeltaMovement().y<0,"gravity suppressed");
            f.player.teleportTo(f.player.getX()+2,100,f.player.getZ());near(h,f.player.getX()-start.x,2.5,"teleport suppressed");
            f.player.setDeltaMovement(MOTION);h.assertTrue(f.player.getDeltaMovement().equals(MOTION),"external intentional motion was overwritten");
        }
        h.succeed();
    }

    private static float damage(Fixture f,DamageSource source,float amount){var e=new LivingDamageEvent(f.player,source,amount);DamageEventHandler.onLivingDamage(e);return e.getAmount();}
    private static void near(GameTestHelper h,double a,double b,String why){h.assertTrue(Math.abs(a-b)<.00001,why+" actual="+a+" expected="+b);}
    private static long seedFor(int selection){for(long n=1;n<100000;n++)if(QuarryingController.selectBonus(RandomSource.create(n).nextDouble())==selection)return n;throw new AssertionError("seed");}
    private static long seedForNative(GameTestHelper h,Fixture f,BlockPos pos,BlockState state,ItemStack tool,int selection){
        LootTable table=h.getLevel().getServer().getLootData().getLootTable(state.getBlock().getLootTable());
        var id=state.getBlock().getLootTable();net.minecraft.resources.ResourceLocation sequence=null;
        try(var reader=h.getLevel().getServer().getResourceManager().getResource(
                new net.minecraft.resources.ResourceLocation(id.getNamespace(),"loot_tables/"+id.getPath()+".json")).orElseThrow().openAsReader()) {
            var json=com.google.gson.JsonParser.parseReader(reader).getAsJsonObject();
            if(json.has("random_sequence"))sequence=new net.minecraft.resources.ResourceLocation(json.get("random_sequence").getAsString());
        }catch(IOException e){throw new AssertionError(e);}
        var params=new LootParams.Builder(h.getLevel()).withParameter(LootContextParams.ORIGIN,Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.THIS_ENTITY,f.player).withParameter(LootContextParams.BLOCK_STATE,state)
                .withParameter(LootContextParams.TOOL,tool).create(LootContextParamSets.BLOCK);
        // Use the real table's RNG implementation and native loot draws, without harvest authority.
        RandomSource random=sequence==null?h.getLevel().getRandom():h.getLevel().getRandomSequence(sequence);
        for(long seed=1;seed<100000;seed++) {
            random.setSeed(seed);LootContext c=new LootContext.Builder(params).create(sequence);table.getRandomItems(c,ignored->{});
            if(QuarryingController.selectBonus(c.getRandom().nextDouble())==selection){
                random.setSeed(seed);com.mojang.logging.LogUtils.getLogger().info("QUARRY_NATIVE_SEED sequence={} rng={} seed={}",sequence,random.getClass().getName(),seed);return seed;
            }
        }
        throw new AssertionError("native loot seed");
    }
    private static LootContext context(GameTestHelper h,ServerPlayer p,BlockPos pos,BlockState state,ItemStack tool,long seed){
        var params=new LootParams.Builder(h.getLevel()).withParameter(LootContextParams.ORIGIN,Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.THIS_ENTITY,p).withParameter(LootContextParams.BLOCK_STATE,state)
                .withParameter(LootContextParams.TOOL,tool).create(LootContextParamSets.BLOCK);
        return new LootContext.Builder(params).withOptionalRandomSeed(seed).create(null);
    }
    private static void noBonus(GameTestHelper h,Fixture f,BlockPos pos,BlockState state){
        long seed=seedFor(1);LootContext c=context(h,f.player,pos,state,ItemStack.EMPTY,seed);var loot=ObjectArrayList.of(new ItemStack(Items.COBBLESTONE));
        QuarryingController.harvest(f.player,pos,state,()->h.assertTrue(QuarryingController.addBonus(loot,c)==loot,"negative bonus"));
        h.assertTrue(c.getRandom().nextDouble()==RandomSource.create(seed).nextDouble(),"negative consumed RNG");
    }
    private static final class TestArrow extends Arrow {
        TestArrow(GameTestHelper h){super(EntityType.ARROW,h.getLevel());}
        void hit(Entity target){onHitEntity(new EntityHitResult(target));}
    }
    private static final class Fixture implements AutoCloseable {
        final GameTestHelper h;final ServerPlayer player;final Connection connection;final EmbeddedChannel channel;
        Fixture(GameTestHelper h){this.h=h;player=new ServerPlayer(h.getLevel().getServer(),h.getLevel(),new GameProfile(UUID.randomUUID(),"fh-rebalance"));
            connection=new Connection(PacketFlow.SERVERBOUND);channel=new EmbeddedChannel(connection);
            h.getLevel().getServer().getPlayerList().placeNewPlayer(connection,player);player.setPos(0,100,0);player.setNoGravity(true);
            for(int i=0;i<65;i++)player.tick();}
        IShokugiData data(){return player.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(IllegalStateException::new);}
        void skill(String id,int n){data().setSkillLevel(id,n);data().setSkillDisabled(id,false);}
        void dispatch(Object msg){ICustomPacket<?> p=(ICustomPacket<?>)PacketHandler.INSTANCE.toVanillaPacket(msg,NetworkDirection.PLAY_TO_SERVER);
            try{h.assertTrue(NetworkHooks.onCustomPayload(p,connection),"registered packet");}finally{p.getInternalData().release();}}
        void drain(){Object o;while((o=channel.readOutbound())!=null)ReferenceCountUtil.release(o);}
        <T>T packet(Class<T> type){channel.runPendingTasks();T found=null;Object o;while((o=channel.readOutbound())!=null){if(type.isInstance(o))found=type.cast(o);ReferenceCountUtil.release(o);}return found;}
        void assertSynced(){ICustomPacket<?> expected=(ICustomPacket<?>)PacketHandler.INSTANCE.toVanillaPacket(new ShokugiSyncPacket(data().serializeNBT()),NetworkDirection.PLAY_TO_CLIENT);
            boolean found=false;channel.runPendingTasks();Object o;try{while((o=channel.readOutbound())!=null){if(o instanceof ICustomPacket<?> actual)found|=ByteBufUtil.equals(expected.getInternalData(),actual.getInternalData());ReferenceCountUtil.release(o);}}
            finally{expected.getInternalData().release();}h.assertTrue(found,"canonical server sync");}
        public void close(){h.getLevel().getServer().getPlayerList().remove(player);drain();channel.finishAndReleaseAll();}
    }
}
