package com.leva.foodhealing.compat.endinglibrary;

import com.mega.endinglib.common.capability.EndingLibraryLivingCapability;
import com.mega.endinglib.util.time.TimeStopEntityData;
import com.mega.endinglib.util.time.TimeStopUtils;
import com.leva.foodhealing.PurificationMasteryController;
import com.leva.foodhealing.capability.ShokugiProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.*;

/** Candidate only. Observes native state; NEVER writes library flags/count/NBT/entity fields. */
public final class Ownership {
 public static final boolean enabled=true;
 public static final String UOM="fantasy_ending:ultimate_order_manager";
 static Deque<Frame> calls(ServerLevel l){return AuthorityContext.of(l).calls;}


 static class State {final AuthorityContext context;final ServerLevel level;State(AuthorityContext c,ServerLevel l){context=c;level=l;}long epoch;boolean unknown=true,quiet=false;String reason="initial-unknown";Map<UUID,String> origins=new HashMap<>();}
 record Frame(boolean enable,LivingEntity entity,boolean reset,int duration,boolean global,boolean dimension,int count,boolean hadOther,Map<String,Object> before){}
 static String dim(ServerLevel l){return l.dimension().location().toString();}
 static State state(ServerLevel l){State observed=AuthorityContext.of(l).state(l);return observed;}
 static String type(Entity e){return String.valueOf(ForgeRegistries.ENTITY_TYPES.getKey(e.getType()));}
 static Map<UUID,LivingEntity> loaded(ServerLevel l){
  Map<UUID,LivingEntity> out=new LinkedHashMap<>();
  for(Entity e:l.getAllEntities()) if(e instanceof LivingEntity x && !x.isRemoved() && x.isAlive() && TimeStopEntityData.getTimeStopCount(x)>0)out.put(e.getUUID(),x);
  return out;
 }
 static boolean global(){return TimeStopUtils.isTimeStop;}
 static boolean stopped(ServerLevel l){return TimeStopUtils.andSameDimension(l);}
 static void unknown(ServerLevel l,String reason){State s=state(l);s.unknown=true;s.quiet=false;s.reason=reason;}
 public static Map<String,Object> snapshot(ServerLevel l){
  List<Map<String,Object>> entities=new ArrayList<>();
  for(Entity e:l.getAllEntities())if(e instanceof LivingEntity x){
   entities.add(Map.of("uuid",e.getUUID().toString(),"type",type(e),"count",TimeStopEntityData.getTimeStopCount(x),"alive",x.isAlive(),"removed",e.isRemoved(),"canMove",TimeStopUtils.canMove(e)));
  }
  return Map.of("global",global(),"dimensionStopped",stopped(l),"dimension",dim(l),"sources",entities);
 }
 public static void reconcile(ServerLevel l){
  State s=state(l);if(s.context.closed){unknown(l,"closed-authority-context");return;}var live=loaded(l);
  Set<UUID> identities=new HashSet<>();for(Entity entity:l.getAllEntities())if(entity instanceof LivingEntity x&&!x.isRemoved()&&x.isAlive()&&TimeStopEntityData.getTimeStopCount(x)>0&&!identities.add(x.getUUID()))TerminalWitnessBridge.fault(l,"duplicate-UUID-different-ref");
  var witness=TerminalWitnessBridge.engine(l);
  if(witness.terminalFault){unknown(l,"terminalFault:"+witness.reason);return;}
  boolean processing=witness.frames.size()>0;
  if(!global() && !stopped(l)){
   if(!live.isEmpty()){unknown(l,"positive-count-without-native-dimension-stop");return;}
   // Quiescent is witnessed, not restored from a persisted owner ledger.
   if(processing){if(s.origins.keySet().stream().allMatch(id->witness.witnesses.keySet().stream().anyMatch(o->o instanceof LivingEntity e&&e.getUUID().equals(id)&&witness.pending(e))))return;unknown(l,"unexplained-quiet-with-pending");return;}
   s.unknown=false;s.quiet=true;s.reason="observed-quiescence";s.origins.clear();witness.bindings.clear();return;
  }
  if(global()!=stopped(l)){unknown(l,"global-dimension-mismatch");return;}
  if(s.quiet){unknown(l,"unobserved-start");return;}
  for(var e:live.entrySet())if(!s.origins.containsKey(e.getKey())){unknown(l,"unattributed-positive-count:"+e.getKey());}
  for(UUID id:new HashSet<>(s.origins.keySet()))if(!live.containsKey(id)&&witness.witnesses.keySet().stream().noneMatch(o->o instanceof LivingEntity e&&e.getUUID().equals(id)&&witness.pending(e)))unknown(l,"source-disappeared-without-observed-end:"+id);
  for(var e:live.values()){var b=witness.bindings.get(e);if(b==null||!TerminalWitness.same(b.identity(),TerminalWitnessBridge.identity(e)))unknown(l,"positive-source-binding-mismatch");}
  if(live.isEmpty()&&!(processing&&!s.origins.isEmpty()&&s.origins.keySet().stream().allMatch(id->witness.witnesses.keySet().stream().anyMatch(o->o instanceof LivingEntity e&&e.getUUID().equals(id)&&witness.pending(e)))))unknown(l,"stopped-without-source");
 }
 public static void beforeUse(boolean enable,LivingEntity e,boolean reset,int duration){
  if(!enabled||!(e.level() instanceof ServerLevel l))return;
  if(AuthorityContext.of(l).closed){unknown(l,"closed-authority-context");return;}reconcile(l);var live=loaded(l);State s=state(l);
  var f=new Frame(enable,e,reset,duration,global(),stopped(l),TimeStopEntityData.getTimeStopCount(e),live.keySet().stream().anyMatch(x->!x.equals(e.getUUID())),snapshot(l));
  calls(l).push(f);
 }
 public static void afterUse(boolean enable,LivingEntity e,boolean reset,int duration){
  if(!enabled||!(e.level() instanceof ServerLevel l))return;
  if(AuthorityContext.of(l).closed){unknown(l,"closed-authority-context");return;}
  if(calls(l).isEmpty()||calls(l).peek().entity()!=e){unknown(l,"use-stack-mismatch");return;}
  Frame f=calls(l).pop();State s=state(l);
  if(enable){unknown(l,"untrusted-start-without-initial-frame");if(!UOM.equals(type(e)))TerminalWitnessBridge.fault(l,"FOREIGN-participation");}
  // Terminal removal commits ONLY after exact callback completion. Cleanup cannot repair a fault ledger.
  reconcile(l);

 }
 public static void count(EndingLibraryLivingCapability cap,int value){
  if(!enabled)return;
  Entity entity=cap.getEntity();
  if(!(entity instanceof LivingEntity e)||!(e.level() instanceof ServerLevel l))return;
  int old=cap.getTimeStopCount();State s=state(l);
  if(InitialStart.countHead(cap,value))return;
  // EndingLibrary's native logout callback writes zero even for a non-source player.
  // This no-op changes neither UNKNOWN nor any authority; tracked/positive writes stay strict.
  var witness=TerminalWitnessBridge.engine(l);
  if(CountMutationPolicy.inertZero(old,value,s.origins.containsKey(e.getUUID()),witness.bindings.containsKey(e),witness.frames.containsKey(e)))return;
  if(calls(l).stream().noneMatch(f->f.entity()==e) && global() && stopped(l)){
   // A known source monotonically counting down creates no new owner; disappearance is still reconciled.
   if(!TerminalWitnessBridge.nativeDecrement(e))TerminalWitnessBridge.fault(l,"direct-count-change");
  }

 }
 public static void lifecycle(Entity e,String reason){
  if(!enabled || !(e.level() instanceof ServerLevel l))return;
  if(e instanceof ServerPlayer p) SessionRegistry.revoke(p,reason);
  if(e instanceof LivingEntity x&&(TimeStopEntityData.getTimeStopCount(x)>0||state(l).origins.containsKey(e.getUUID())))unknown(l,reason);

 }
 public static boolean permission(ServerPlayer p){
  ServerLevel l=p.serverLevel();reconcile(l);State s=state(l);
  boolean eligible=p.isAlive()&&!p.isRemoved()&&SessionRegistry.eligible(p)&&p.getCapability(ShokugiProvider.SHOKUGI_CAPA).map(PurificationMasteryController::isEnabled).orElse(false);
  var live=loaded(l);
  return !EntryDeserialize.processing(l)&&!InitialStart.processing(l)&&!s.context.closed && eligible && !TerminalWitnessBridge.engine(l).terminalFault&&TerminalWitnessBridge.engine(l).frames.isEmpty()&&calls(l).isEmpty()&&global()&&stopped(l)&&!s.unknown&&!live.isEmpty()&&live.values().stream().allMatch(e->UOM.equals(type(e))&&UOM.equals(s.origins.get(e.getUUID())));
 }
 public static Map<String,Object> decision(ServerPlayer p){
  boolean allow=permission(p);State s=state(p.serverLevel());
  return Map.of("allow",allow,"epoch",s.epoch,"unknown",s.unknown,"reason",s.reason,"sourceCount",s.origins.size(),"native",snapshot(p.serverLevel()));
 }
}
