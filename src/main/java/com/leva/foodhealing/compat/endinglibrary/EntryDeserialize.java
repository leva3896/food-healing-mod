package com.leva.foodhealing.compat.endinglibrary;
import com.mega.endinglib.common.capability.EndingLibraryLivingCapability;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import java.util.*;
public final class EntryDeserialize {
 public enum Kind { ABSENT,ZERO,POSITIVE,MALFORMED }
 record Frame(LivingEntity entity,ServerLevel level,AuthorityContext context,Thread thread,Kind kind,int value,int pre,boolean inertBefore){}

 public static int inert,loss;
 public static Kind classify(CompoundTag tag){if(tag==null)return Kind.MALFORMED;if(!tag.contains("TimeStopCount"))return Kind.ABSENT;if(!tag.contains("TimeStopCount",Tag.TAG_INT))return Kind.MALFORMED;int n=tag.getInt("TimeStopCount");return n<0?Kind.MALFORMED:n==0?Kind.ZERO:Kind.POSITIVE;}
 static boolean inertContext(LivingEntity e,ServerLevel l){var c=AuthorityContext.of(l);return !c.closed&&l.getServer().isSameThread()&&c.calls.isEmpty()&&c.engine.frames.isEmpty()&&c.engine.witnesses.isEmpty()&&!c.engine.bindings.containsKey(e)&&!Ownership.state(l).origins.containsKey(e.getUUID())&&!InitialStart.processing(l);}
 public static boolean inertDecision(Kind k,int pre,int post,boolean before,boolean after,boolean identity){return (k==Kind.ABSENT||k==Kind.ZERO)&&pre==0&&post==0&&before&&after&&identity;}
 public static boolean processing(ServerLevel l){return AuthorityContext.of(l).deserializing.values().stream().anyMatch(f->f.context()==AuthorityContext.of(l));}
 public static void head(EndingLibraryLivingCapability cap,CompoundTag tag){
  if(!(cap.getEntity() instanceof LivingEntity e)||!(e.level() instanceof ServerLevel l))return;
  Kind k=classify(tag);boolean before=inertContext(e,l)&&!processing(l);Frame f=new Frame(e,l,AuthorityContext.of(l),Thread.currentThread(),k,k==Kind.ZERO||k==Kind.POSITIVE?tag.getInt("TimeStopCount"):0,cap.getTimeStopCount(),before);
  if(f.context().deserializing.put(cap,f)!=null)TerminalWitnessBridge.fault(l,"deserialize-nesting");
  if(!(k==Kind.ABSENT||k==Kind.ZERO)||f.pre()!=0||!before){loss++;TerminalWitnessBridge.fault(l,"deserialize-provenance-loss:"+k);}

 }
 public static void returned(EndingLibraryLivingCapability cap){
  if(!(cap.getEntity() instanceof LivingEntity owner)||!(owner.level() instanceof ServerLevel ownerLevel))return;
  Frame f=AuthorityContext.of(ownerLevel).deserializing.remove(cap);if(f==null)return;
  var e=f.entity();boolean identity=e==cap.getEntity()&&e.level()==f.level()&&e.getServer()==f.context().server&&Thread.currentThread()==f.thread()&&AuthorityContext.of(f.level())==f.context();int post=cap.getTimeStopCount();
  boolean safe=inertDecision(f.kind(),f.pre(),post,f.inertBefore(),inertContext(e,f.level()),identity);
  if(safe)inert++;else if((f.kind()==Kind.ABSENT||f.kind()==Kind.ZERO)||!identity||(f.kind()==Kind.POSITIVE&&post!=f.value())){loss++;TerminalWitnessBridge.fault(f.level(),"deserialize-post-inconsistent");}

 }
 public static void serverEnd(ServerLevel l){for(var it=AuthorityContext.of(l).deserializing.entrySet().iterator();it.hasNext();){var x=it.next();if(x.getValue().context()==AuthorityContext.of(l)){TerminalWitnessBridge.fault(l,"deserialize-return-missing");it.remove();}}}
}
