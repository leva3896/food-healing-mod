package com.leva.foodhealing.compat.endinglibrary;
import com.mega.endinglib.util.time.*;
import com.mega.endinglib.common.capability.EndingLibraryLivingCapability;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import java.util.*;
/** Original operations delegated once. Never synthesizes native state or permission. */
public final class TerminalWitnessBridge {
 public static TerminalWitness engine(ServerLevel l){return AuthorityContext.of(l).engine;}
 static TerminalWitness engine(LivingEntity e){return engine((ServerLevel)e.level());}

 static TerminalWitness.Identity identity(LivingEntity e){ServerLevel l=(ServerLevel)e.level();return new TerminalWitness.Identity(e,e.getUUID(),Ownership.type(e),l,Ownership.dim(l),l.getServer(),Ownership.state(l).epoch,Thread.currentThread());}
 static TerminalWitness.CallbackFrame frame(LivingEntity e){return e.level() instanceof ServerLevel l&&!AuthorityContext.of(l).closed?engine(l).frames.get(e):null;}
 static int foreign(ServerLevel l){return (int)Ownership.loaded(l).values().stream().filter(e->!Ownership.UOM.equals(Ownership.type(e))||!Ownership.UOM.equals(Ownership.state(l).origins.get(e.getUUID()))||!engine(l).bindings.containsKey(e)).count();}

 public static void fault(ServerLevel l,String why){engine(l).fault(why);Ownership.unknown(l,"terminalFault:"+engine(l).reason);}
 public static void bind(LivingEntity e){if(e.level() instanceof ServerLevel)engine(e).bind(identity(e));}
 public static void enter(LivingEntity e){
  if(!Ownership.enabled||!(e.level() instanceof ServerLevel l)||TimeStopEntityData.getTimeStopCount(e)<=0||!Ownership.state(l).origins.containsKey(e.getUUID()))return;
  if(AuthorityContext.of(l).closed){fault(l,"closed-authority-context");return;}
  if(!l.getServer().isSameThread()){fault(l,"wrong-server-thread");return;}
  engine(e).enter(identity(e),Ownership.UOM.equals(Ownership.state(l).origins.get(e.getUUID())),Ownership.state(l).unknown,foreign(l));
  if(engine(e).terminalFault)Ownership.unknown(l,"terminalFault:"+engine(e).reason);
 }
 public static void decrement(LivingEntity e,int value){
  var f=frame(e);if(f!=null)engine(e).arm(f,identity(e),TimeStopEntityData.getTimeStopCount(e),value);
  try{TimeStopEntityData.setTimeStopCount(e,value);}catch(Throwable t){engine(e).caught(f);throw t;}
  if(f!=null)engine(e).minted(f,identity(e),TimeStopEntityData.getTimeStopCount(e));
 }
 public static void exactUse(boolean enable,LivingEntity e){
  var f=frame(e);if(f!=null)engine(e).beginUse(f,identity(e));
  try{TimeStopUtils.use(enable,e);}catch(Throwable t){engine(e).caught(f);throw t;}
  if(f!=null)engine(e).delegateReturn(f,identity(e));
 }
 public static void commonHead(boolean enable,LivingEntity e,boolean reset,int duration,boolean sound){
  if(!Ownership.enabled||!(e.level() instanceof ServerLevel l)||enable)return;
  var f=frame(e);engine(e).consume(f,identity(e),!enable&&reset&&duration==180&&sound,Ownership.calls(l).size(),Ownership.state(l).unknown,foreign(l));
  if(engine(e).terminalFault)Ownership.unknown(l,"terminalFault:"+engine(e).reason);
 }
 public static void commonReturn(boolean enable,LivingEntity e){if(!enable&&frame(e)!=null)engine(e).commonReturn(frame(e),identity(e));}
 public static void countHead(EndingLibraryLivingCapability cap,int value){if(cap.getEntity() instanceof LivingEntity e&&e.level() instanceof ServerLevel&&frame(e)!=null&&frame(e).setter)engine(e).head(frame(e),identity(e),cap,cap.getTimeStopCount(),value);}
 public static void countReturn(EndingLibraryLivingCapability cap){if(cap.getEntity() instanceof LivingEntity e&&e.level() instanceof ServerLevel&&frame(e)!=null&&frame(e).setter)engine(e).returned(frame(e),identity(e),cap,cap.getTimeStopCount());}
 public static boolean nativeDecrement(LivingEntity e){var f=frame(e);return f!=null&&f.setter&&!engine(e).terminalFault;}
 public static void caught(LivingEntity e){var f=frame(e);if(f!=null){engine(e).caught(f);Ownership.unknown((ServerLevel)e.level(),"terminalFault:"+engine(e).reason);}}
 public static void exit(LivingEntity e){
  var f=frame(e);if(f==null)return;ServerLevel l=(ServerLevel)e.level();var live=Ownership.loaded(l);
  boolean others=live.values().stream().anyMatch(x->x!=e);
  boolean post=TimeStopEntityData.getTimeStopCount(e)==0&&!e.isRemoved()&&e.isAlive()&&foreign(l)==0&&Ownership.calls(l).isEmpty()&&!Ownership.state(l).unknown;
  post &= others ? Ownership.global()&&Ownership.stopped(l) : !Ownership.global()&&!Ownership.stopped(l);
  if(engine(e).exit(f,identity(e),post)){Ownership.state(l).origins.remove(e.getUUID());Ownership.reconcile(l);}
  else if(engine(e).terminalFault)Ownership.unknown(l,"terminalFault:"+engine(e).reason);
 }
 public static void outerFailure(LivingEntity e){caught(e);exit(e);}
}
