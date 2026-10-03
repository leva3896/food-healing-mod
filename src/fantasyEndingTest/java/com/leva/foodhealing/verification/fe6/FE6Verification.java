package com.leva.foodhealing.verification.fe6;

import com.google.gson.GsonBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import com.leva.foodhealing.*;
import com.leva.foodhealing.capability.*;
import com.leva.foodhealing.compat.fantasyending.*;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.*;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;

@Mod("foodhealing_fe6_verification")
public final class FE6Verification {
 static final String P=FoodHealingSkillIds.PURIFICATION,M=FoodHealingSkillIds.PURIFICATION_MASTERY;
 static FE6Verification active; public static double rng=.25; public static int rngCalls;
 MinecraftServer server;ServerLevel level;boolean ran;int ticks;Path root;
 String current="startup";UUID subject;int sites;List<Map<String,Object>> nums=new ArrayList<>();List<Float> deltas=new ArrayList<>();
 List<Map<String,Object>> adds=new ArrayList<>(),results=new ArrayList<>();
 public FE6Verification(){active=this;MinecraftForge.EVENT_BUS.register(this);}
 public static void site(Entity source,Entity target){if(active!=null&&target.getUUID().equals(active.subject))active.sites++;}
 public static void numeric(String layer,LivingEntity e,DamageSource s,float a){if(active!=null&&e.getUUID().equals(active.subject))active.nums.add(Map.of("layer",layer,"source",s.typeHolder().unwrapKey().orElseThrow().location().toString(),"amount",a,"ownerType",s.getEntity()==null?"null":ForgeRegistries.ENTITY_TYPES.getKey(s.getEntity().getType()).toString()));}
 public static void delta(LivingEntity e,float a){if(active!=null&&e.getUUID().equals(active.subject))active.deltas.add(a);}
 @SubscribeEvent(priority=EventPriority.LOWEST,receiveCanceled=true) public void hurt(LivingHurtEvent e){numeric("HURT",e.getEntity(),e.getSource(),e.getAmount());}
 @SubscribeEvent public void added(MobEffectEvent.Added e){if(e.getEntity().getUUID().equals(subject))adds.add(effect(e.getEffectInstance()));}
 @SubscribeEvent public void start(ServerStartedEvent e){server=e.getServer();}
 @SubscribeEvent public void tick(TickEvent.ServerTickEvent e)throws Exception{if(e.phase==TickEvent.Phase.END&&server!=null&&!ran&&++ticks==80){ran=true;run();}}
 void run()throws Exception{
  root=server.getServerDirectory().toPath().toRealPath();level=server.overworld();Map<String,Object> report=new LinkedHashMap<>();
  try{
   check(server.isDedicatedServer()&&root.equals(Path.of(System.getProperty("foodhealing.fe6.root")).toRealPath()),"isolated dedicated root");
   check(ModList.get().getModContainerById("fantasy_ending").orElseThrow().getModInfo().getVersion().toString().equals("2.7.20"),"version");
   level.getGameRules().getRule(GameRules.RULE_DOMOBSPAWNING).set(false,server);level.getGameRules().getRule(GameRules.RULE_NATURAL_REGENERATION).set(false,server);level.getChunk(0,0);
   check(FantasyEndingVersions.supports("2.7.20")&&!FantasyEndingVersions.supports("")&&!FantasyEndingVersions.supports("2.7.21")&&!FantasyEndingVersions.supports("2.7.20-hotfix")&&!FantasyEndingVersions.supports(null),"metadata gate");record(Map.of("gate","supported only 2.7.20"));
   if(Boolean.getBoolean("foodhealing.fe6.supplement")){supplemental();}else{
   for(String path:List.of("dream","star","final","counter","skull")){
    Map<String,Object> on=attack(path,"on","uom",Difficulty.NORMAL,false);
    for(String state:List.of("parent-off","mastery-off","no-parent","no-mastery","pending","invalid","other-player")){
     Map<String,Object> off=attack(path,state,"uom",Difficulty.NORMAL,false);
     check(on.get("numeric").equals(off.get("numeric")),path+" numeric differs ON/OFF "+on.get("numeric")+" / "+off.get("numeric"));
     check(on.get("deltas").equals(off.get("deltas")),path+" delta differs");
    }
   }
   for(String owner:List.of("wither","cow","null"))attack("skull","on",owner,Difficulty.NORMAL,false);
   for(Difficulty d:List.of(Difficulty.HARD,Difficulty.EASY,Difficulty.PEACEFUL))for(String state:List.of("on","mastery-off"))attack("skull",state,"uom",d,false);
   attack("star","on","cow",Difficulty.NORMAL,false);
   rng=.75;attack("star","on","uom",Difficulty.NORMAL,false);rng=.25;
   for(String owner:List.of("uom","cow"))for(Difficulty d:List.of(Difficulty.NORMAL,Difficulty.HARD,Difficulty.EASY))directStar(owner,d);
   for(String path:List.of("dream","star","final","counter","skull"))attack(path,"on","uom",Difficulty.NORMAL,true);
   }
   report.put("status","PASS");
  }catch(Throwable t){report.put("status","FAIL");report.put("failedCase",current);report.put("failure",t.toString());report.put("failedNumeric",nums);report.put("failedSites",sites);report.put("failedAdds",adds);report.put("failedDeltas",deltas);LogUtils.getLogger().error("FE6_FAILURE "+current,t);}
  finally{subject=null;report.put("results",results);report.put("completedCases",results.size());report.put("mods",ModList.get().getMods().stream().map(m->m.getModId()+"="+m.getVersion()).toList());report.put("pid",ProcessHandle.current().pid());server.saveEverything(true,true,true);Files.writeString(root.resolve("fe6-result.json"),new GsonBuilder().setPrettyPrinting().create().toJson(report));LogUtils.getLogger().info("FE6_FINISHED {} {}",report.get("status"),results.size());server.halt(false);}
 }
 Map<String,Object> attack(String path,String state,String owner,Difficulty difficulty,boolean existing)throws Exception{
  current=path+"/"+state+"/"+owner+"/"+difficulty+"/existing="+existing+"/rng="+rng;server.setDifficulty(difficulty,true);
  try(Fixture f=new Fixture(state)){
   Mob uom=uom();Entity attacker=owner.equals("uom")?uom:owner.equals("null")?null:mob(owner);
   MobEffect ban=ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("fantasy_ending","ban_healing"));
   UUID modId=UUID.fromString("22e60928-2222-4000-8888-222200000001");
   if(existing){f.p.addEffect(new MobEffectInstance(ban,900,9));f.p.addEffect(new MobEffectInstance(MobEffects.POISON,900,5));f.p.addEffect(new MobEffectInstance(MobEffects.LUCK,900,0));f.p.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(modId,"foreign",.01,AttributeModifier.Operation.ADDITION));}
   List<Map<String,Object>> before=effects(f.p);var nbt=f.p.getPersistentData().copy();var canonical=f.data.serializeNBT();
   reset(f.p);float hp=f.p.getHealth();int expected=path.equals("dream")?2:1;
   Projectile projectile=null;
   switch(path){
    case "dream" -> {uom.getRandom().setSeed(12345);call(uom,"dreamShadowBeam",new Class[]{Entity.class},f.p);}
    case "star" -> {projectile=star(attacker);call(projectile,"m_6532_",new Class[]{HitResult.class},new BlockHitResult(f.p.position(),Direction.UP,BlockPos.containing(f.p.position()),false));if(rng>.5)expected=0;}
    case "final" -> {uom.tickCount=200;call(uom,"setFinallyDeathTime",new Class[]{int.class},1000);call(uom,"finalSkillAttack",new Class[]{});}
    case "counter" -> {for(int prep=0;prep<4096&&!(Boolean)call(uom,"m_7090_",new Class[]{});prep++){float old=uom.getHealth();uom.setHealth(uom.getMaxHealth()/2);check(uom.getHealth()<old,"native powered preparation made no progress");}check((Boolean)call(uom,"m_7090_",new Class[]{}),"native powered");call(uom,"m_6475_",new Class[]{DamageSource.class,float.class},f.p.damageSources().playerAttack(f.p),10F);}
    case "skull" -> {projectile=EntityType.WITHER_SKULL.create(level);if(attacker!=null)projectile.setOwner(attacker);check(projectile.getOwner()==attacker,"exact native skull owner");projectile.moveTo(0,100,0);level.addFreshEntity(projectile);call(projectile,"m_6532_",new Class[]{HitResult.class},new EntityHitResult(f.p));if(difficulty!=Difficulty.NORMAL&&difficulty!=Difficulty.HARD)expected=0;}
   }
   check(sites==expected,"native site arrival expected="+expected+" actual="+sites);
   boolean protectedCase=state.equals("on")&&owner.equals("uom");int expectedAdds=protectedCase?0:expected;
   check(adds.size()==expectedAdds,"new effect count "+adds+" expected="+expectedAdds);
   if(!existing&&expectedAdds>0){
    if(path.equals("dream")){effectIs(f.p,MobEffects.MOVEMENT_SLOWDOWN,40,4);effectIs(f.p,MobEffects.POISON,100,3);}
    if(path.equals("star"))effectIs(f.p,ban,60,7);
    if(path.equals("final"))effectIs(f.p,ban,20,6);
    if(path.equals("counter"))effectIs(f.p,MobEffects.HARM,1,2);
    if(path.equals("skull"))effectIs(f.p,MobEffects.WITHER,difficulty==Difficulty.HARD?800:200,1);
   }
   if(path.equals("dream"))check(nums.stream().anyMatch(v->v.get("source").equals("fantasy_ending:ds_power")&&((Number)v.get("amount")).floatValue()>=10&&((Number)v.get("amount")).floatValue()<15),"dream DS");
   if(path.equals("counter"))check(nums.stream().anyMatch(v->v.get("layer").equals("FE")&&v.get("source").equals("minecraft:magic")&&Math.abs(((Number)v.get("amount")).floatValue()-1.8F)<.0001),"counter reflection 10*.6*.3");
   if(path.equals("final"))check(deltas.size()>=2&&deltas.stream().allMatch(v->v<0),"native negative delta");
   if(path.equals("skull"))check(nums.stream().anyMatch(v->v.get("layer").equals("HURT_INPUT")&&((Number)v.get("amount")).floatValue()==(owner.equals("null")?5F:8F)),"skull numeric");
   check(!nums.isEmpty(),"numeric absent");
   if(existing){check(before.equals(effects(f.p)),"existing effects changed");check(f.p.getAttribute(Attributes.MOVEMENT_SPEED).getModifier(modId)!=null,"foreign modifier");}
   check(nbt.equals(f.p.getPersistentData()),"foreign NBT changed");check(canonical.equals(f.data.serializeNBT()),"canonical changed");
   Map<String,Object> evidence=new LinkedHashMap<>();evidence.put("sites",sites);evidence.put("adds",List.copyOf(adds));evidence.put("numeric",List.copyOf(nums));evidence.put("deltas",List.copyOf(deltas));evidence.put("beforeHP",hp);evidence.put("afterHP",f.p.getHealth());evidence.put("rngCalls",rngCalls);evidence.put("projectileRemoved",projectile!=null&&projectile.isRemoved());evidence.put("effects",effects(f.p));
   record(evidence);subject=null;if(projectile!=null)projectile.discard();if(attacker!=null&&attacker!=uom)attacker.discard();uom.discard();return evidence;
  }
 }
 void supplemental()throws Exception{
  server.setDifficulty(Difficulty.NORMAL,true);
  for(String name:List.of("native-uom-skull-launch","star-direct-uuid-exclusion","star-owner-exclusion","dream-mob","different-level","counter-unpowered","skull-other-fe-owner")){
   current="supplement/"+name;
   try(Fixture f=new Fixture("on")){
    Mob u=uom();reset(f.p);
    switch(name){
     case "native-uom-skull-launch" -> {
      call(u,"_performRangedAttack",new Class[]{int.class,double.class,double.class,double.class,boolean.class},0,0D,100D,0D,false);
      var spawned=level.getEntitiesOfClass(WitherSkull.class,u.getBoundingBox().inflate(64),e->e.getOwner()==u);
      check(spawned.size()==1,"one native UOM skull with exact owner");var s=spawned.get(0);s.moveTo(0,100,0);
      call(s,"m_6532_",new Class[]{HitResult.class},new EntityHitResult(f.p));
      check(sites==1&&adds.isEmpty()&&s.isRemoved(),"native launch hit protection/lifecycle");
      check(nums.stream().anyMatch(v->v.get("layer").equals("HURT_INPUT")&&v.get("source").equals("minecraft:wither_skull")&&((Number)v.get("amount")).floatValue()==8F),"native launch hurt8");
     }
     case "star-direct-uuid-exclusion" -> {
      var s=star(u);call(s,"m_6532_",new Class[]{HitResult.class},new EntityHitResult(f.p));
      check(sites==0&&adds.isEmpty()&&rngCalls==0&&!nums.isEmpty()&&s.isRemoved(),"native direct target excluded from AOE");
     }
     case "star-owner-exclusion" -> {
      var s=star(f.p);call(s,"m_6532_",new Class[]{HitResult.class},new BlockHitResult(f.p.position(),Direction.UP,BlockPos.containing(f.p.position()),false));
      check(sites==0&&adds.isEmpty()&&nums.isEmpty()&&rngCalls==0,"native owner excluded from AOE");
     }
     case "dream-mob" -> {
      Mob cow=mob("cow");cow.moveTo(0,100,0);subject=cow.getUUID();
      call(u,"dreamShadowBeam",new Class[]{Entity.class},cow);
      check(sites==2&&adds.size()==2,"non-player gets native secondary");effectIs(cow,MobEffects.POISON,100,3);effectIs(cow,MobEffects.MOVEMENT_SLOWDOWN,40,4);cow.discard();
     }
     case "different-level" -> {
      Entity foreign=ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation("fantasy_ending","ultimate_order_manager")).create(server.getLevel(net.minecraft.world.level.Level.NETHER));
      check(!FantasyEndingCompatibility.blocksDirect(foreign,f.p),"different-level owner must not protect");
      var s=EntityType.WITHER_SKULL.create(level);s.setOwner(foreign);check(!FantasyEndingCompatibility.blocksProjectile(s,f.p),"different-level projectile predicate");
     }
     case "counter-unpowered" -> {
      check(!(Boolean)call(u,"m_7090_",new Class[]{}),"native unpowered");
      call(u,"m_6475_",new Class[]{DamageSource.class,float.class},f.p.damageSources().playerAttack(f.p),10F);
      check(sites==0&&adds.isEmpty()&&nums.isEmpty(),"unpowered native no counter branch");
     }
     case "skull-other-fe-owner" -> {
      var other=star(u);var s=EntityType.WITHER_SKULL.create(level);s.setOwner(other);s.moveTo(0,100,0);level.addFreshEntity(s);
      call(s,"m_6532_",new Class[]{HitResult.class},new EntityHitResult(f.p));
      check(sites==1&&adds.size()==1&&s.isRemoved(),"other FE entity owner must retain wither");effectIs(f.p,MobEffects.WITHER,200,1);other.discard();
     }
    }
    record(Map.of("sites",sites,"adds",List.copyOf(adds),"numeric",List.copyOf(nums),"rngCalls",rngCalls,"afterHP",f.p.getHealth()));subject=null;u.discard();
   }
  }
 }
 void directStar(String owner,Difficulty d)throws Exception{
  current="star-direct/"+owner+"/"+d;server.setDifficulty(d,true);
  try(Fixture f=new Fixture("on")){
   Mob a=owner.equals("uom")?uom():mob("cow");Projectile s=star(a);reset(f.p);call(s,"m_5790_",new Class[]{EntityHitResult.class},new EntityHitResult(f.p));
   int expected=owner.equals("uom")||d==Difficulty.EASY?0:4;check(sites==0,"direct unexpectedly hooked");check(adds.size()==expected,"native direct4 "+adds);check(!nums.isEmpty(),"direct numeric");
   record(Map.of("sites",sites,"adds",List.copyOf(adds),"numeric",List.copyOf(nums)));subject=null;s.discard();a.discard();
  }
 }
 Mob uom()throws Exception{Mob e=(Mob)ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation("fantasy_ending","ultimate_order_manager")).create(level);check(e!=null,"registered UOM");e.moveTo(10,100,0);e.setNoAi(true);level.addFreshEntity(e);e.tickCount=200;return e;}
 Mob mob(String id){Mob e=(Mob)(id.equals("wither")?EntityType.WITHER:EntityType.COW).create(level);e.moveTo(10,100,0);e.setNoAi(true);level.addFreshEntity(e);return e;}
 Projectile star(Entity owner)throws Exception{Projectile e=(Projectile)ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation("fantasy_ending","star")).create(level);e.moveTo(0,100,0);e.setOwner(owner);call(e,"setPower",new Class[]{float.class},10F);level.addFreshEntity(e);return e;}
 void reset(ServerPlayer p){subject=p.getUUID();sites=0;rngCalls=0;nums=new ArrayList<>();adds=new ArrayList<>();deltas=new ArrayList<>();}
 static Object call(Object o,String name,Class<?>[] sig,Object...args)throws Exception{Class<?> c=o.getClass();while(c!=null){try{Method m=c.getDeclaredMethod(name,sig);m.setAccessible(true);return m.invoke(o,args);}catch(NoSuchMethodException e){c=c.getSuperclass();}}throw new NoSuchMethodException(name);}
 static Map<String,Object> effect(MobEffectInstance e){return Map.of("id",ForgeRegistries.MOB_EFFECTS.getKey(e.getEffect()).toString(),"duration",e.getDuration(),"amp",e.getAmplifier());}
 static List<Map<String,Object>> effects(LivingEntity e){return e.getActiveEffects().stream().map(FE6Verification::effect).sorted(Comparator.comparing(v->v.get("id").toString())).toList();}
 static void effectIs(LivingEntity p,MobEffect e,int ticks,int amp){var x=p.getEffect(e);check(x!=null&&x.getDuration()==ticks&&x.getAmplifier()==amp,"duration/amp "+e);}
 static void check(boolean b,String s){if(!b)throw new AssertionError(s);}
 void record(Map<String,Object> v){results.add(Map.of("case",current,"status","PASS","observed",v));LogUtils.getLogger().info("FE6_CASE_PASS {}",current);}
 final class Fixture implements AutoCloseable{
  final ServerPlayer p;final IShokugiData data;final Connection c=new Connection(PacketFlow.SERVERBOUND);final EmbeddedChannel ch=new EmbeddedChannel(c);
  Fixture(String state){p=new ServerPlayer(server,level,new GameProfile(UUID.randomUUID(),"fe6-test"));server.getPlayerList().placeNewPlayer(c,p);p.moveTo(0,100,0);p.setNoGravity(true);for(int i=0;i<110;i++)level.tickNonPassenger(p);p.invulnerableTime=0;
   data=p.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(AssertionError::new);data.deserializeNBT(new ShokugiData().serializeNBT());data.setSkillLevel(P,1);data.setSkillLevel(M,1);data.setSpentSkillPoints(103);
   if(state.equals("parent-off"))data.setSkillDisabled(P,true);if(state.equals("mastery-off"))data.setSkillDisabled(M,true);if(state.equals("no-parent"))data.setSkillLevel(P,0);if(state.equals("no-mastery")||state.equals("other-player"))data.setSkillLevel(M,0);
   if(state.equals("pending")||state.equals("invalid")){var n=data.serializeNBT();if(state.equals("pending"))n.putBoolean("LegacyMigrationPending",true);else n.putInt("FoodHealingDataVersion",99);data.deserializeNBT(n);}
   check(PurificationMasteryController.isEnabled(data)==state.equals("on"),"canonical test fixture eligibility");p.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1000);p.setHealth(1000);p.getPersistentData().putString("verification:foreign","retain");
  }
  public void close(){subject=null;server.getPlayerList().remove(p);ch.finishAndReleaseAll();}
 }
}
