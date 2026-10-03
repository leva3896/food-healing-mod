package com.leva.foodhealing.verification;

import com.google.gson.GsonBuilder;
import com.leva.foodhealing.*;
import com.leva.foodhealing.capability.*;
import com.leva.foodhealing.network.*;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import io.github.kosianodangoo.trialmonolith.TrialMonolithConfig;
import io.github.kosianodangoo.trialmonolith.common.entity.*;
import io.github.kosianodangoo.trialmonolith.common.entity.invadermonolith.InvaderMonolithEntity;
import io.github.kosianodangoo.trialmonolith.common.entity.invadermonolith.ai.*;
import io.github.kosianodangoo.trialmonolith.common.helper.EntityHelper;
import io.github.kosianodangoo.trialmonolith.common.init.TrialMonolithEntities;
import io.github.kosianodangoo.trialmonolith.common.init.TrialMonolithDamageTypes;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.ReferenceCountUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.item.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.*;

import java.nio.file.*;
import java.util.*;

/** New disposable dedicated run, real native tick/goal/rayTrace/activate. Never part of product Jar. */
@Mod("foodhealing_invader_verification")
public final class TrialInvaderVerification {
    static final String P=FoodHealingSkillIds.PURIFICATION, M=FoodHealingSkillIds.PURIFICATION_MASTERY;
    static final String[] STATES={"on","parent-off","mastery-off","both-off","no-parent","no-mastery","pending","schema","overlevel"};
    final List<Map<String,Object>> results=new ArrayList<>();
    final Map<UUID,List<Map<String,Object>>> hits=new HashMap<>();
    final Map<UUID,List<String>> deaths=new HashMap<>();
    final Map<UUID,List<Float>> finalDamage=new HashMap<>();
    MinecraftServer server;ServerLevel level;Path root;String running="startup";int startupTicks;boolean started;
    public TrialInvaderVerification(){MinecraftForge.EVENT_BUS.register(this);}
    @SubscribeEvent(priority=EventPriority.LOWEST,receiveCanceled=true)
    public void hurt(LivingHurtEvent e){var list=hits.get(e.getEntity().getUUID());if(list!=null)list.add(Map.of(
            "amount",e.getAmount(),"source",e.getSource().typeHolder().unwrapKey().orElseThrow().location().toString(),
            "owner",e.getSource().getEntity()==null?"null":e.getSource().getEntity().getUUID().toString(),"cancelled",e.isCanceled()));}
    @SubscribeEvent(priority=EventPriority.LOWEST,receiveCanceled=true)
    public void death(LivingDeathEvent e){var list=deaths.get(e.getEntity().getUUID());if(list!=null)list.add(e.getSource().typeHolder().unwrapKey().orElseThrow().location()+"/cancel="+e.isCanceled());}
    @SubscribeEvent(priority=EventPriority.LOWEST,receiveCanceled=true)
    public void finalDamage(LivingDamageEvent e){var list=finalDamage.get(e.getEntity().getUUID());if(list!=null){require(!e.isCanceled(),"final damage unexpectedly cancelled");list.add(e.getAmount());}}
    @SubscribeEvent public void start(ServerStartedEvent event){server=event.getServer();}
    @SubscribeEvent public void tick(TickEvent.ServerTickEvent event)throws Exception{
        if(event.phase==TickEvent.Phase.END&&server!=null&&!started&&++startupTicks>=80){started=true;run();}
    }
    public void run()throws Exception{
        level=server.overworld();root=server.getServerDirectory().toPath().toRealPath();
        require(root.equals(Path.of(System.getProperty("foodhealing.invader.verificationRoot")).toRealPath()),"wrong verification root");
        require(server.isDedicatedServer(),"not dedicated");
        Map<String,Object> receipt=new LinkedHashMap<>();receipt.put("pid",ProcessHandle.current().pid());
        receipt.put("loadedMods",ModList.get().getMods().stream().map(m->Map.of("id",m.getModId(),"version",m.getVersion().toString())).toList());
        float small=TrialMonolithConfig.smallBeamSoulDamage, huge=TrialMonolithConfig.hugeBeamSoulDamage;
        receipt.put("originalSmallSoul",small);receipt.put("originalHugeSoul",huge);
        try{
            require(ModList.get().getModContainerById("the_trial_monolith").orElseThrow().getModInfo().getVersion().toString().equals("1.4.9"),"wrong Trial");
            level.getChunk(0,0);level.getChunk(1,0);
            for(String state:STATES)for(boolean flag:new boolean[]{false,true})protection(state,flag);
            for(boolean large:new boolean[]{false,true})for(boolean force:new boolean[]{false,true})for(String state:STATES)beamCase(large,force,state);
            for(boolean large:new boolean[]{false,true})for(boolean force:new boolean[]{false,true})for(float addition:new float[]{1.1F,10.1F})for(boolean enabled:new boolean[]{true,false})threshold(large,force,addition,enabled);
            for(boolean large:new boolean[]{false,true})for(String owner:new String[]{"cow","trial","player","null"})otherOwner(large,owner);
            generated(false);generated(true);ownerSaveReload();simultaneous();nonPlayer();highDamageNegative();buffAndLegitimateRemoval();purchaseAndSave();
            receipt.put("status","PASS");
        }catch(Throwable error){receipt.put("status","FAIL");receipt.put("failedCase",running);receipt.put("failure",error.toString());LogUtils.getLogger().error("INVADER_VERIFICATION_FAIL "+running,error);}
        finally{
            TrialMonolithConfig.smallBeamSoulDamage=small;TrialMonolithConfig.hugeBeamSoulDamage=huge;
            receipt.put("results",results);receipt.put("completedCases",results.size());
            try{server.saveEverything(true,true,true);receipt.put("saveRequested",true);Files.writeString(root.resolve("invader-result.json"),new GsonBuilder().setPrettyPrinting().serializeSpecialFloatingPointValues().create().toJson(receipt));}
            finally{server.halt(false);}
        }
    }
    InvaderMonolithEntity invader(){
        var z=(InvaderMonolithEntity)TrialMonolithEntities.INVADER_MONOLITH.get().create(level);require(z!=null,"missing registered Invader");
        z.moveTo(20,100,0,0,0);level.addFreshEntity(z);return z;
    }
    void state(Fixture f,String state){
        if(state.equals("parent-off")||state.equals("both-off"))f.data.setSkillDisabled(P,true);
        if(state.equals("mastery-off")||state.equals("both-off"))f.data.setSkillDisabled(M,true);
        if(state.equals("no-parent"))f.data.setSkillLevel(P,0);
        if(state.equals("no-mastery"))f.data.setSkillLevel(M,0);
        if(state.equals("pending")||state.equals("schema")){var n=f.data.serializeNBT();if(state.equals("pending"))n.putBoolean("LegacyMigrationPending",true);else n.putInt("FoodHealingDataVersion",99);f.data.deserializeNBT(n);}
        if(state.equals("overlevel"))f.data.setSkillLevel(M,2);
        require(PurificationMasteryController.isEnabled(f.data)==state.equals("on"),"fixture eligibility "+state);
        require(f.data.getSkillLevel(FoodHealingSkillIds.TRUE_GUTS)==0&&f.data.getRootActiveUntil()==0,"Root could hide result");
        require(f.data.getSkillLevel(FoodHealingSkillIds.TRUTH_MASTERY)==0,"Truth not required");
    }
    void protection(String state,boolean flag){
        running="invader-tick/"+state+"/flag="+flag;
        try(Fixture f=new Fixture()){
            state(f,state);EntityHelper.setSoulProtected(f.player,flag);EntityHelper.setSoulDamageForce(f.player,.2F);
            var canonical=f.data.serializeNBT();float hp=f.player.getHealth();var z=invader();
            require(!EntityHelper.hasDimensionalCore(f.player),"core hides clear");
            require(z.DEFAULT_PREDICATE.test(f.player),"native predicate rejected prepared player");z.tick();
            require(z.getTargets().contains(f.player),"native Invader target enumeration missed player: source="+z.position()+" previous="+z.getPosition(0)+" target="+f.player.position()+" targets="+z.getTargets()+" range="+TrialMonolithConfig.invaderMonolithAttackRange);
            boolean expected=flag&&state.equals("on");require(EntityHelper.isSoulProtected(f.player)==expected,"hostile clear predicate");
            near(EntityHelper.getSoulDamage(f.player),.2F,"old Soul changed");require(canonical.equals(f.data.serializeNBT()),"canonical modified");
            if(state.equals("on"))near(f.player.getHealth(),hp,"protected HP repaired/changed");
            record(Map.of("flagBefore",flag,"flagAfter",expected,"soul",EntityHelper.getSoulDamage(f.player),"nativeTargets",true,"canonicalUnchanged",true));
        }
    }
    AbstractDelayedTraceableEntity beam(boolean large,LivingEntity owner,boolean high){
        AbstractDelayedTraceableEntity b=large?(HugeBeamEntity)TrialMonolithEntities.HUGE_BEAM.get().create(level):(SmallBeamEntity)TrialMonolithEntities.SMALL_BEAM.get().create(level);
        require(b!=null,"beam absent");b.moveTo(0,100,0,0,0);b.setOwner(owner);b.setHighDimensional(high);return b;
    }
    void activate(AbstractDelayedTraceableEntity b){if(b instanceof HugeBeamEntity h)h.activate();else ((SmallBeamEntity)b).activate();}
    void configured(boolean force){TrialMonolithConfig.smallBeamSoulDamage=force?1F:.1F;TrialMonolithConfig.hugeBeamSoulDamage=force?1F:.1F;}
    void beamCase(boolean large,boolean force,String state){
        running="beam/"+(large?"huge":"small")+"/force="+force+"/"+state;
        try(Fixture f=new Fixture()){
            state(f,state);EntityHelper.setSoulProtected(f.player,force);EntityHelper.setSoulDamageForce(f.player,.2F);configured(force);
            var canonical=f.data.serializeNBT();float hp=f.player.getHealth();var z=invader();var b=beam(large,z,force);
            require(!f.player.isInvulnerableTo(TrialMonolithDamageTypes.laserAttack(level,z)),"native immunity hides numeric");clear(f.player);activate(b);
            boolean enabled=state.equals("on");near(EntityHelper.getSoulDamage(f.player),enabled?.2F:.3F,"Soul addition");
            numeric(f.player,large&&enabled?0:1,large?Float.MAX_VALUE:5F,z);
            if(large&&enabled){near(f.player.getHealth(),hp,"huge exception HP");require(!f.player.isDeadOrDying()&&deaths.get(f.id).isEmpty(),"huge protected death");}
            if(!large){require(finalDamage.get(f.id).equals(List.of(5F)),"small final numeric lost");
                // Native Trial Soul Protection overrides effective getHealth; it is not raw numeric HP.
                if(!force)require(f.player.getHealth()<hp,"small numeric lost");}
            if(large&&!enabled&&!force)require(f.player.isDeadOrDying()&&!deaths.get(f.id).isEmpty(),"unprotected huge lethal missing");
            require(EntityHelper.isSoulProtected(f.player)==force,"beam changed immunity flag");require(canonical.equals(f.data.serializeNBT()),"beam changed canonical");
            record(Map.of("soul",EntityHelper.getSoulDamage(f.player),"hpBefore",hp,"hpAfter",f.player.getHealth(),"numeric",hits.get(f.id),"finalNumeric",finalDamage.get(f.id),"death",deaths.get(f.id),"sourceOwner",z.getUUID().toString()));
        }
    }
    void otherOwner(boolean large,String kind){
        running="shared-beam-other-owner/"+large+"/"+kind;
        try(Fixture f=new Fixture();Fixture playerOwner=kind.equals("player")?new Fixture():null){
            LivingEntity owner=switch(kind){case "cow"->new Cow(EntityType.COW,level);case "trial"->(LivingEntity)TrialMonolithEntities.TRIAL_MONOLITH.get().create(level);case "player"->playerOwner.player;default->null;};
            if(owner!=null){owner.moveTo(20,100,0,0,0);if(!(owner instanceof ServerPlayer))level.addFreshEntity(owner);}
            configured(false);clear(f.player);activate(beam(large,owner,false));near(EntityHelper.getSoulDamage(f.player),.1F,"other-owner Soul was protected");
            numeric(f.player,1,large?Float.MAX_VALUE:5F,owner);if(large)require(f.player.isDeadOrDying(),"other owner huge death suppressed");
            record(Map.of("soul",EntityHelper.getSoulDamage(f.player),"numeric",hits.get(f.id),"death",deaths.get(f.id)));
        }
    }
    void threshold(boolean large,boolean force,float addition,boolean enabled){
        running="native-beam-soul-threshold/large="+large+"/force="+force+"/addition="+addition+"/enabled="+enabled;
        try(Fixture f=new Fixture()){
            state(f,enabled?"on":"mastery-off");EntityHelper.setSoulProtected(f.player,force);EntityHelper.setSoulDamageForce(f.player,.2F);
            float configured=force?addition*10F:addition;TrialMonolithConfig.smallBeamSoulDamage=configured;TrialMonolithConfig.hugeBeamSoulDamage=configured;
            var z=invader();clear(f.player);activate(beam(large,z,force));
            near(EntityHelper.getSoulDamage(f.player),enabled?.2F:.2F+addition,"threshold Soul");
            boolean replaced=server.getPlayerList().getPlayer(f.id)!=f.player;
            if(enabled){require(!replaced&&!f.player.isDeadOrDying()&&deaths.get(f.id).isEmpty(),"protected forced death/respawn");numeric(f.player,large?0:1,5F,z);}
            else{require(deaths.get(f.id).stream().anyMatch(d->d.startsWith("the_trial_monolith:soul_damage/cancel=false")),"native Soul death missing");require(replaced==(addition>=10F),"native respawn threshold changed");}
            record(Map.of("soul",EntityHelper.getSoulDamage(f.player),"addition",addition,"numeric",hits.get(f.id),"death",deaths.get(f.id),"playerReplaced",replaced,"externalProtection",force));
        }
    }
    void generated(boolean large)throws Exception{
        running="native-goal-generated/"+(large?"huge":"small");
        try(Fixture f=new Fixture()){
            var z=invader();z.tick();require(z.getTargets().contains(f.player),"native goal target missing");
            Set<UUID> before=new HashSet<>();level.getAllEntities().forEach(e->before.add(e.getUUID()));
            if(large){var goal=new OPShootHugeBeamGoal(z);var timer=OPShootHugeBeamGoal.class.getDeclaredField("attackTime");timer.setAccessible(true);timer.setInt(goal,1);goal.tick();}
            else new OPShootSmallBeamGoal(z).tick();
            var spawned=new ArrayList<AbstractDelayedTraceableEntity>();
            for(Entity e:level.getAllEntities())if(!before.contains(e.getUUID())&&(large?e instanceof HugeBeamEntity:e instanceof SmallBeamEntity)&&((AbstractDelayedTraceableEntity)e).getOwner()==z)spawned.add((AbstractDelayedTraceableEntity)e);
            require(spawned.size()==1,"native goal spawn count "+spawned.size());var b=spawned.get(0);
            require(EntityHelper.isSoulProtected(b),"generation-time attack-entity protection cancelled");
            // Aim only during fixture setup; owner, high-dimensional flag, predicate and activate are native.
            b.moveTo(0,100,0,0,0);configured(false);clear(f.player);float hp=f.player.getHealth();activate(b);
            near(EntityHelper.getSoulDamage(f.player),0,"generated beam Soul");numeric(f.player,large?0:1,5F,z);
            if(large)near(f.player.getHealth(),hp,"generated Huge numeric");else require(f.player.getHealth()<hp,"generated Small numeric");
            record(Map.of("beamUuid",b.getUUID().toString(),"nativeOwner",z.getUUID().toString(),"attackEntityProtected",true,"highDimensional",b.isHighDimensional(),"hpAfter",f.player.getHealth(),"numeric",hits.get(f.id)));
        }
    }
    void ownerSaveReload(){
        running="beam-native-owner-uuid-save-load";
        try(Fixture f=new Fixture()){
            var z=invader();var original=beam(true,z,false);CompoundTag tag=new CompoundTag();original.save(tag);
            var loaded=(HugeBeamEntity)TrialMonolithEntities.HUGE_BEAM.get().create(level);loaded.load(tag);
            require(tag.hasUUID("Owner")&&loaded.getOwner()==z,"native UUID owner resolution failed");
            configured(false);float hp=f.player.getHealth();clear(f.player);loaded.activate();numeric(f.player,0,0,z);near(f.player.getHealth(),hp,"resolved owner did not protect");
            record(Map.of("ownerUuid",tag.getUUID("Owner").toString(),"nativeResolution",true));
        }
    }
    void simultaneous(){
        running="one-ray-two-real-server-players";
        try(Fixture protectedPlayer=new Fixture();Fixture other=new Fixture()){
            state(other,"mastery-off");other.player.moveTo(.25,100,12,0,0);var z=invader();configured(false);clear(protectedPlayer.player);clear(other.player);activate(beam(true,z,false));
            numeric(protectedPlayer.player,0,0,z);numeric(other.player,1,Float.MAX_VALUE,z);
            near(EntityHelper.getSoulDamage(protectedPlayer.player),0,"other-player source leak");near(EntityHelper.getSoulDamage(other.player),.1F,"other-player protection leak");
            require(protectedPlayer.player.isAlive()&&other.player.isDeadOrDying(),"personal death separation");record(Map.of("protected",hits.get(protectedPlayer.id),"unprotected",hits.get(other.id)));
        }
    }
    void nonPlayer(){
        running="non-player-native-huge";Cow cow=new Cow(EntityType.COW,level);cow.moveTo(0,100,12,0,0);level.addFreshEntity(cow);clear(cow);configured(false);var z=invader();activate(beam(true,z,false));
        numeric(cow,1,Float.MAX_VALUE,z);near(EntityHelper.getSoulDamage(cow),.1F,"cow protected");require(cow.isDeadOrDying(),"cow death blocked");record(Map.of("numeric",hits.get(cow.getUUID()),"dead",true));cow.discard();
    }
    void highDamageNegative(){
        running="negative-direct-laser-hurt-not-beam-integration";
        try(Fixture f=new Fixture()){
            var z=invader();clear(f.player);f.player.hurt(TrialMonolithDamageTypes.laserAttack(level,z),Float.MAX_VALUE);
            numeric(f.player,1,Float.MAX_VALUE,z);require(f.player.isDeadOrDying(),"blanket high-damage filter");record(Map.of("classification","direct native hurt negative; not beam activate","numeric",hits.get(f.id)));
        }
    }
    void buffAndLegitimateRemoval(){
        running="native-tick-buff-and-legitimate-removal";
        try(Fixture f=new Fixture()){
            EntityHelper.setSoulProtected(f.player,true);f.player.addEffect(new MobEffectInstance(MobEffects.LUCK,100,0));var z=invader();z.tick();
            require(f.player.hasEffect(MobEffects.LUCK)&&f.player.getEffect(MobEffects.LUCK).getDuration()==100,"Invader removed ordinary beneficial effect");
            require(EntityHelper.isSoulProtected(f.player),"hostile flag clear");EntityHelper.setSoulProtected(f.player,false);
            require(!EntityHelper.isSoulProtected(f.player),"legitimate direct clear intercepted");require(f.player.removeEffect(MobEffects.LUCK),"ordinary effect removal blocked");
            f.player.addEffect(new MobEffectInstance(MobEffects.LUCK,2,0));for(int i=0;i<4;i++)f.player.doTick();
            require(!f.player.hasEffect(MobEffects.LUCK),"natural expiry frozen");f.toggle(P,true);
            require(!PurificationMasteryController.isEnabled(f.data)&&!f.data.isSkillDisabled(M),"parent OFF rewrote mastery");
            record(Map.of("invaderMobEffectRemoval","not found in scoped paths","legitimateFlagClear",true,"ordinaryEffectRemove",true,"naturalExpiry",true,"parentToggle",true));
        }
    }
    void purchaseAndSave(){
        running="purchase-rejection-and-normal-player-save-load";UUID id;CompoundTag expected;
        try(Fixture f=new Fixture()){
            id=f.id;f.data.setUnspentSkillPoints(1000);
            for(String skill:new String[]{M,FoodHealingSkillIds.TRUTH_MASTERY}){
                f.data.setSkillLevel(skill,0);
                require(FoodHealingSkills.purchaseStatus(f.data,skill,0)==FoodHealingSkills.PurchaseResult.IMPLEMENTATION_PENDING,"readiness reason changed");
                var before=f.data.serializeNBT();f.dispatch(new PurchaseSkillPacket(skill,0));require(before.equals(f.data.serializeNBT()),"purchase SP changed");
                if(skill.equals(M))f.data.setSkillLevel(M,1); // preparation for the next independent purchase case
            }
            f.toggle(P,true);expected=f.data.serializeNBT();EntityHelper.setSoulDamageForce(f.player,.2F);EntityHelper.setSoulProtected(f.player,true);
        }
        require(Files.isRegularFile(root.resolve("world/playerdata/"+id+".dat")),"normal save absent");
        try(Fixture f=new Fixture(id,false)){
            require(expected.equals(f.data.serializeNBT()),"saved canonical differs");near(EntityHelper.getSoulDamage(f.player),.2F,"saved Soul differs");require(EntityHelper.isSoulProtected(f.player),"saved protection differs");
            require(f.player.getPersistentData().getString("verification:foreign").equals("retained"),"foreign data lost");
            record(Map.of("normalSaveReload",true,"spUnchanged",true,"canonical",expected.toString(),"soul",EntityHelper.getSoulDamage(f.player),"protected",true));
        }
    }
    void numeric(Entity target,int count,float amount,Entity owner){var list=hits.get(target.getUUID());require(list.size()==count,"numeric count expected="+count+" actual="+list);
        for(var hit:list){require(((Number)hit.get("amount")).floatValue()==amount,"numeric amount "+hit);require(hit.get("source").equals("the_trial_monolith:laser_attack")&&hit.get("owner").equals(owner==null?"null":owner.getUUID().toString())&&hit.get("cancelled").equals(false),"source/owner/cancel changed "+hit);}}
    void clear(Entity e){hits.put(e.getUUID(),new ArrayList<>());deaths.put(e.getUUID(),new ArrayList<>());finalDamage.put(e.getUUID(),new ArrayList<>());}
    void record(Map<String,Object> details){results.add(Map.of("case",running,"status","PASS","observed",details));LogUtils.getLogger().info("FOODHEALING_INVADER_CASE_PASS {} {}",running,details);}
    static void require(boolean ok,String message){if(!ok)throw new AssertionError(message);}
    static void near(float a,float b,String label){require(Math.abs(a-b)<.0002F,label+": "+a+" != "+b);}
    final class Fixture implements AutoCloseable{
        final UUID id;final ServerPlayer player;final IShokugiData data;
        final Connection connection=new Connection(PacketFlow.SERVERBOUND);final EmbeddedChannel channel=new EmbeddedChannel(connection);
        Fixture(){this(UUID.randomUUID(),true);}
        Fixture(UUID uuid,boolean fresh){
            id=uuid;player=new ServerPlayer(server,level,new GameProfile(id,"fh-inv-"+id.toString().substring(0,8)));server.getPlayerList().placeNewPlayer(connection,player);
            data=player.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(AssertionError::new);player.moveTo(0,100,12,0,0);player.setNoGravity(true);
            // Native Invader DEFAULT_PREDICATE requires target.tickCount >= 100.
            for(int i=0;i<110;i++)level.tickNonPassenger(player);
            require(player.tickCount>=100,"native target minimum age not reached");
            if(fresh){data.deserializeNBT(new ShokugiData().serializeNBT());data.setSkillLevel(P,1);data.setSkillLevel(M,1);data.setUnspentSkillPoints(0);data.setSpentSkillPoints(103);
                player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(100);player.setHealth(100);player.getPersistentData().putString("verification:foreign","retained");}
            player.moveTo(0,100,12,0,0);clear(player);drain();
        }
        void dispatch(Object value){ICustomPacket<?> packet=(ICustomPacket<?>)PacketHandler.INSTANCE.toVanillaPacket(value,NetworkDirection.PLAY_TO_SERVER);try{require(NetworkHooks.onCustomPayload(packet,connection),"packet unhandled");}finally{packet.getInternalData().release();}}
        void toggle(String skill,boolean disabled){dispatch(new ToggleSkillPacket(skill,disabled));
            ICustomPacket<?> expected=(ICustomPacket<?>)PacketHandler.INSTANCE.toVanillaPacket(new ShokugiSyncPacket(data.serializeNBT()),NetworkDirection.PLAY_TO_CLIENT);boolean found=false;channel.runPendingTasks();Object m;
            try{while((m=channel.readOutbound())!=null){if(m instanceof ICustomPacket<?> actual)found|=ByteBufUtil.equals(expected.getInternalData(),actual.getInternalData());ReferenceCountUtil.release(m);}}finally{expected.getInternalData().release();}require(found,"product canonical sync missing");}
        void drain(){channel.runPendingTasks();Object m;while((m=channel.readOutbound())!=null)ReferenceCountUtil.release(m);}
        public void close(){ServerPlayer p=server.getPlayerList().getPlayer(id);if(p!=null)server.getPlayerList().remove(p);channel.finishAndReleaseAll();}
    }
}
