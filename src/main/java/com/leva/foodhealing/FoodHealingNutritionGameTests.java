package com.leva.foodhealing;

import com.leva.foodhealing.capability.*;
import com.leva.foodhealing.network.ShokugiSyncPacket;
import com.mojang.authlib.GameProfile;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.buffer.Unpooled;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.*;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.effect.*;
import net.minecraft.world.item.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.gametest.*;
import java.util.UUID;

@GameTestHolder(FoodHealingMod.MODID)
@PrefixGameTestTemplate(false)
public final class FoodHealingNutritionGameTests {
    private static final class Fixture implements AutoCloseable {
        final ServerPlayer p; final EmbeddedChannel channel; final IShokugiData d;
        Fixture(GameTestHelper h) {
            p=new ServerPlayer(h.getLevel().getServer(),h.getLevel(),new GameProfile(UUID.randomUUID(),"fh-count"));
            Connection c=new Connection(PacketFlow.SERVERBOUND);channel=new EmbeddedChannel(c);
            p.server.getPlayerList().placeNewPlayer(c,p);
            d=p.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(IllegalStateException::new);
            p.getAttribute(Attributes.MAX_HEALTH).setBaseValue(200);
            p.getFoodData().setFoodLevel(0);p.setHealth(1);
        }
        ItemStack eat(ItemStack food) {
            p.setItemInHand(InteractionHand.MAIN_HAND,food);
            food.use(p.level(),p,InteractionHand.MAIN_HAND);
            if (!p.isUsingItem()) throw new IllegalStateException("Native food use did not start, including full-hunger path");
            if (!p.isUsingItem()) throw new IllegalStateException("Native food use did not start, including full-hunger path");
            ItemStack before=food.copy();
            ItemStack result=food.finishUsingItem(p.level(),p);
            result=ForgeEventFactory.onItemUseFinish(p,before,0,result);
            p.setItemInHand(InteractionHand.MAIN_HAND,result);p.stopUsingItem();return result;
        }
        public void close(){p.server.getPlayerList().remove(p);HungerChangeHandler.clearPlayerData(p.getUUID());channel.finishAndReleaseAll();}
    }
    @GameTest(template="empty")
    public static void nutritionRegisteredFoodsExactlyOnce(GameTestHelper h) {
        for(Item item:new Item[]{Items.BEETROOT,Items.APPLE,Items.BREAD,Items.COOKED_BEEF}) {
            try(Fixture f=new Fixture(h)) {
                int n=new ItemStack(item).getFoodProperties(f.p).getNutrition();
                ItemStack stack=new ItemStack(item,3);stack.getOrCreateTag().putString("count-test","retained");
                ItemStack result=f.eat(stack);
                h.assertTrue(f.d.getEatCount()==n && f.d.getLevel()==0,"declared units once "+item);
                h.assertTrue(f.p.getHealth()==1+2*n,"HP conversion "+item);
                h.assertTrue(result.getCount()==2&&result.getTag().getString("count-test").equals("retained"),"stack/NBT");
                MinecraftForge.EVENT_BUS.post(new TickEvent.PlayerTickEvent(TickEvent.Phase.START,f.p));
                h.assertTrue(f.d.getEatCount()==n&&f.p.getHealth()==1+2*n,"no later second transaction");
            }
        } h.succeed();
    }
    @GameTest(template="empty")
    public static void nutritionFullHungerAndContainer(GameTestHelper h) {
        try(Fixture f=new Fixture(h)) {
            f.p.getFoodData().setFoodLevel(20);f.d.setEatCount(0);f.p.setHealth(1);
            f.d.setSkillLevel(FoodHealingSkillIds.GUTS,5);
            f.eat(new ItemStack(Items.COOKED_BEEF));
            h.assertTrue(f.d.getEatCount()==8&&f.p.getHealth()==17&&f.p.getFoodData().getFoodLevel()==20,"full hunger");
            h.assertTrue(f.d.getRootAccumulatedNutrition()==8,"Root food nutrition once");
            h.assertTrue(f.p.getCapability(FoodDiversityProvider.FOOD_DIVERSITY).orElseThrow(IllegalStateException::new).getAllEatenFoods().size()==1,"diversity once");
            ItemStack result=f.eat(new ItemStack(Items.MUSHROOM_STEW));
            h.assertTrue(result.is(Items.BOWL)&&result.getCount()==1&&f.d.getEatCount()==14,"final food/container");
        }h.succeed();
    }
    @GameTest(template="empty")
    public static void nutritionNonFoodAndSaturationNeverRoot(GameTestHelper h) {
        try(Fixture f=new Fixture(h)) {
            f.d.setSkillLevel(FoodHealingSkillIds.GUTS,5);f.d.setSkillLevel(FoodHealingSkillIds.TRUE_GUTS,1);
            f.p.getFoodData().setFoodLevel(18);
            h.assertTrue(f.d.getEatCount()==18&&f.p.getHealth()==37,"nonfood +18");
            h.assertTrue(f.d.getRootAccumulatedNutrition()==0&&f.d.getRootActiveUntil()==0&&f.d.getRootReservedNutrition()==0,"no Root entry");
            f.p.getFoodData().setFoodLevel(10);long count=f.d.getEatCount();
            f.p.getFoodData().setFoodLevel(15);h.assertTrue(f.d.getEatCount()==count+5,"10->15");
            f.p.getFoodData().setFoodLevel(19);count=f.d.getEatCount();
            f.p.getFoodData().eat(8,0);h.assertTrue(f.d.getEatCount()==count+1,"19->20 actual delta");
            count=f.d.getEatCount();f.p.getFoodData().eat(8,1);f.p.getFoodData().setSaturation(20);f.p.getFoodData().setFoodLevel(20);f.p.getFoodData().setFoodLevel(10);
            h.assertTrue(f.d.getEatCount()==count,"full/saturation/decrease0");
        }h.succeed();
    }
    @GameTest(template="empty",timeoutTicks=80)
    public static void nutritionIndependentSameTickAndDelayed(GameTestHelper h) {
        Fixture f=new Fixture(h);
        f.eat(new ItemStack(Items.APPLE));
        f.p.getFoodData().setFoodLevel(6);
        h.assertTrue(f.d.getEatCount()==6,"same tick independent gain retained");
        h.runAfterDelay(1,()->{
            f.p.getFoodData().setFoodLevel(9);h.assertTrue(f.d.getEatCount()==9,"one tick independent");
        });
        h.runAfterDelay(8,()->{
            try{f.p.getFoodData().setFoodLevel(14);h.assertTrue(f.d.getEatCount()==14,"delayed independent");h.succeed();}
            finally{f.close();}
        });
    }
    @GameTest(template="empty")
    public static void nutritionSatisfactionKeepsOneTransaction(GameTestHelper h) {
        for(boolean preserve:new boolean[]{true,false})try(Fixture f=new Fixture(h)) {
            f.d.setSkillLevel(FoodHealingSkillIds.SATISFACTION,3);f.d.setSkillLevel(FoodHealingSkillIds.GUTS,5);
            long seed=0;while((net.minecraft.util.RandomSource.create(seed).nextFloat()<.75F)!=preserve)seed++;
            f.p.getRandom().setSeed(seed);
            ItemStack food=new ItemStack(Items.MUSHROOM_STEW);food.getOrCreateTag().putString("identity","one");
            ItemStack result=f.eat(food);
            h.assertTrue(f.d.getEatCount()==6&&f.p.getHealth()==13&&f.d.getRootAccumulatedNutrition()==6,"Satisfaction count/heal/Root once");
            h.assertTrue(preserve?result==food&&result.getCount()==1:result.is(Items.BOWL),"Satisfaction inventory");
            h.assertTrue(f.p.getCapability(FoodDiversityProvider.FOOD_DIVERSITY).orElseThrow(IllegalStateException::new).getAllEatenFoods().size()==1,"Satisfaction diversity once");
        }h.succeed();
    }
    @GameTest(template="empty")
    public static void nutritionHighFoodBonusAndExternalEffects(GameTestHelper h) {
        try(Fixture f=new Fixture(h)) {
            f.eat(new ItemStack(FoodHealingMod.SUPER_FOOD.get()));
            h.assertTrue(f.d.getEatCount()==50&&f.p.getHealth()==101,"high food exact units/HP");
            h.assertTrue(!f.p.hasEffect(MobEffects.DAMAGE_RESISTANCE)&&!f.p.hasEffect(MobEffects.FIRE_RESISTANCE),"no Nutrition19 bonus");
            f.p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,1234,4));
            f.p.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,2345,2));
            f.eat(new ItemStack(FoodHealingMod.SUPER_FOOD.get()));
            h.assertTrue(f.p.getEffect(MobEffects.DAMAGE_RESISTANCE).getDuration()==1234&&f.p.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier()==4,"external Resistance unchanged");
            h.assertTrue(f.p.getEffect(MobEffects.FIRE_RESISTANCE).getDuration()==2345&&f.p.getEffect(MobEffects.FIRE_RESISTANCE).getAmplifier()==2,"external Fire unchanged");
        }
        try(Fixture f=new Fixture(h)) {
            f.d.setSkillLevel(FoodHealingSkillIds.FIRE_RESISTANCE,1);
            f.eat(new ItemStack(FoodHealingMod.SUPER_FOOD.get()));
            h.assertTrue(!f.p.hasEffect(MobEffects.FIRE_RESISTANCE),"food cannot invoke skill bonus");
            ShokugiTickHandler.onPlayerTick(new TickEvent.PlayerTickEvent(TickEvent.Phase.END,f.p));
            h.assertTrue(f.p.hasEffect(MobEffects.FIRE_RESISTANCE)&&!f.p.hasEffect(MobEffects.DAMAGE_RESISTANCE),"formal skill only");
        }h.succeed();
    }
    @GameTest(template="empty")
    public static void nutritionCrossingsAndOverflowDoNotUndoFood(GameTestHelper h) {
        try(Fixture f=new Fixture(h)) {
            f.d.setEatCount(1995);f.d.setUnspentSkillPoints(9);f.d.setSpentSkillPoints(30);
            f.eat(new ItemStack(Items.COOKED_BEEF));
            h.assertTrue(f.d.getEatCount()==3&&f.d.getLevel()==1&&f.d.getUnspentSkillPoints()==10&&f.d.getSpentSkillPoints()==30,"crossing transaction");
            f.d.setEatCount(1999);f.d.setLevel(Long.MAX_VALUE);CompoundTag before=f.d.serializeNBT();
            f.p.setHealth(1);ItemStack result=f.eat(new ItemStack(Items.APPLE,2));
            h.assertTrue(before.equals(f.d.serializeNBT())&&result.getCount()==1&&f.p.getHealth()==9,"overflow rejects progression only");
        }h.succeed();
    }
    @GameTest(template="empty")
    public static void nutritionMigrationAndSyncNbt(GameTestHelper h) {
        try(Fixture f=new Fixture(h)) {
            CompoundTag raw=new CompoundTag();raw.putInt("ShokugiLevel",2);raw.putInt("EatCount",35);f.d.deserializeNBT(raw);
            h.assertTrue(f.d.getEatCount()==350&&f.d.getUnspentSkillPoints()==2,"v2 count350/refund2");
            CompoundTag saved=f.d.serializeNBT();f.d.deserializeNBT(saved);f.d.deserializeNBT(f.d.serializeNBT());
            h.assertTrue(f.d.serializeNBT().equals(saved)&&saved.getCompound("LegacyV2Backup").getInt("EatCount")==35,"migration once/raw35");
            FriendlyByteBuf wire=new FriendlyByteBuf(Unpooled.buffer());
            new ShokugiSyncPacket(saved).encode(wire);CompoundTag received=wire.readNbt();wire.release();
            h.assertTrue(received.getLong("EatCount")==350&&received.getLong("SyncedLevelUpRequirement")==2000,"canonical wire units");
            ShokugiData replica=new ShokugiData();replica.deserializeNBT(received);
            h.assertTrue(replica.serializeNBT().equals(saved),"client canonical decode no migration replay");
        }h.succeed();
    }
}
