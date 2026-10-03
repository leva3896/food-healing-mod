package com.leva.foodhealing.compat.endinglibrary;

import com.mega.endinglib.common.capability.EndingLibraryLivingCapability;
import com.mega.endinglib.util.time.TimeStopEntityData;
import com.mega.endinglib.util.time.TimeStopUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import java.util.*;

/** Exact goal transaction. No source is committed from count or common-use alone. */
public final class InitialStart {

 static long tokens; public static int committed;
 static final class Frame {
  final AuthorityContext context; final ServerLevel level; final LivingEntity source;
  final UUID uuid; final Thread thread; final long token,epoch; final boolean quiet;
  final boolean global,dimension; int stage,firstHeads,firstReturns,useHeads,useReturns; Object cap;
  Frame(LivingEntity e,ServerLevel l){context=AuthorityContext.of(l);level=l;source=e;uuid=e.getUUID();thread=Thread.currentThread();token=++tokens;epoch=Ownership.state(l).epoch;quiet=Ownership.state(l).quiet;global=Ownership.global();dimension=Ownership.stopped(l);}
 }
 static Frame frame(LivingEntity e){return e.level() instanceof ServerLevel l?AuthorityContext.of(l).initialFrame:null;}
 static boolean same(Frame f,LivingEntity e){return f!=null&&f.source==e&&e.level()==f.level&&e.getUUID().equals(f.uuid)&&f.context==AuthorityContext.of(f.level)&&f.context.server==e.getServer()&&!f.context.closed&&f.thread==Thread.currentThread()&&f.context.server.isSameThread()&&f.context.initialFrame==f&&Ownership.state(f.level).epoch==f.epoch;}
 static void fail(Frame f,ServerLevel l,String why){TerminalWitnessBridge.fault(l,"initial:"+why);if(f!=null)f.context.initialFrame=null;}
 static void require(boolean ok,Frame f,ServerLevel l,String why){if(!ok){fail(f,l,why);throw new IllegalStateException("initial observer: "+why);}}
 public static boolean processing(ServerLevel l){return AuthorityContext.of(l).initialFrame!=null;}
 public static void enter(LivingEntity e){
  if(!(e.level() instanceof ServerLevel l))return;
  var c=AuthorityContext.of(l);Frame old=c.initialFrame;
  if(old!=null){fail(old,l,"nested-invocation");return;}
  var s=Ownership.state(l);
  // Read before quiescent reconcile: refusal here does not create a permanent fault.
  if(s.unknown){return;}
  Ownership.reconcile(l);
  boolean exact=e.getClass().getName().equals("com.mega.uom.common.entity.boss.uom.UomWither")&&Ownership.UOM.equals(Ownership.type(e))&&l.getEntity(e.getUUID())==e;
  boolean clean=!c.closed&&l.getServer().isSameThread()&&exact&&e.isAlive()&&!e.isRemoved()&&TimeStopEntityData.getTimeStopCount(e)==0&&!s.origins.containsKey(e.getUUID())&&!c.engine.terminalFault&&!s.unknown&&c.calls.isEmpty()&&c.engine.frames.isEmpty()&&!EntryDeserialize.processing(l);
  // A fresh source can join an already completely attributed UOM episode; it cannot rebind an active source.
  clean &= (s.quiet&&!Ownership.global()&&!Ownership.stopped(l)) || (Ownership.global()&&Ownership.stopped(l)&&!s.origins.isEmpty()&&TerminalWitnessBridge.foreign(l)==0);
  if(!clean){fail(null,l,"entry-not-fresh-or-attributed");return;}
  Frame f=new Frame(e,l);c.initialFrame=f;
 }
 public static void setter(LivingEntity e,int value){
  if(!(e.level() instanceof ServerLevel l)){TimeStopEntityData.setTimeStopCount(e,value);return;}
  Frame f=frame(e);
  if(f==null){TimeStopEntityData.setTimeStopCount(e,value);return;}
  require(same(f,e)&&f.stage==0&&value==180&&TimeStopEntityData.getTimeStopCount(e)==0,f,l,"setter-order-or-value");f.stage=1;
  try{TimeStopEntityData.setTimeStopCount(e,value);}catch(Throwable t){fail(f,l,"setter-throw");throw t;}
  require(same(f,e)&&f.firstHeads==1&&f.firstReturns==1&&TimeStopEntityData.getTimeStopCount(e)==180,f,l,"setter-missing-ack");f.stage=2;
 }
 public static boolean countHead(EndingLibraryLivingCapability cap,int value){
  if(!(cap.getEntity() instanceof LivingEntity e)||!(e.level() instanceof ServerLevel l))return false;
  Frame f=frame(e);if(f==null)return false;
  require(same(f,e)&&value==180,f,l,"count-ref-or-value");
  if(f.stage==1){require(f.firstHeads++==0&&cap.getTimeStopCount()==0,f,l,"first-count-head");f.cap=cap;}
  else if(f.stage==4){require(f.useHeads++==0&&cap==f.cap&&cap.getTimeStopCount()==180,f,l,"use-count-head");}
  else require(false,f,l,"unexpected-count-with-frame");
  return true;
 }
 public static void countReturn(EndingLibraryLivingCapability cap){
  if(!(cap.getEntity() instanceof LivingEntity e)||!(e.level() instanceof ServerLevel l))return;
  Frame f=frame(e);if(f==null)return;
  require(same(f,e)&&f.cap==cap&&cap.getTimeStopCount()==180,f,l,"count-return-ref-value");
  if(f.stage==1)require(f.firstHeads==1&&f.firstReturns++==0,f,l,"first-count-return");
  else if(f.stage==4)require(f.useHeads==1&&f.useReturns++==0,f,l,"use-count-return");
  else require(false,f,l,"count-return-order");
 }
 public static void use(boolean enabled,LivingEntity e){
  if(!(e.level() instanceof ServerLevel l)){TimeStopUtils.use(enabled,e);return;}
  Frame f=frame(e);if(f==null){TimeStopUtils.use(enabled,e);return;}
  require(enabled&&same(f,e)&&f.stage==2,f,l,"use-callsite-order");f.stage=3;
  try{TimeStopUtils.use(enabled,e);}catch(Throwable t){fail(f,l,"use-throw");throw t;}
  require(same(f,e)&&f.stage==5,f,l,"missing-common-return");f.stage=6;
 }
 public static boolean commonHead(boolean enable,LivingEntity e,boolean reset,int duration,boolean sound){
  Frame f=frame(e);if(f==null)return false;ServerLevel l=(ServerLevel)e.level();
  require(same(f,e)&&f.stage==3&&enable&&reset&&duration==180&&sound&&Ownership.global()==f.global&&Ownership.stopped(l)==f.dimension&&TimeStopEntityData.getTimeStopCount(e)==180&&!f.context.engine.terminalFault&&!Ownership.state(l).unknown,f,l,"common-head-match");
  f.stage=4;return true;
 }
 public static boolean commonReturn(boolean enable,LivingEntity e){
  Frame f=frame(e);if(f==null)return false;ServerLevel l=(ServerLevel)e.level();
  require(same(f,e)&&enable&&f.stage==4&&f.useHeads==1&&f.useReturns==1&&Ownership.global()&&Ownership.stopped(l)&&TimeStopEntityData.getTimeStopCount(e)==180,f,l,"common-return-native-post");f.stage=5;return true;
 }
 public static void returned(LivingEntity e){
  if(!(e.level() instanceof ServerLevel l))return;Frame f=frame(e);if(f==null)return;
  var s=Ownership.state(l);var live=Ownership.loaded(l);
  boolean others=live.values().stream().filter(x->x!=e).allMatch(x->Ownership.UOM.equals(Ownership.type(x))&&s.origins.containsKey(x.getUUID())&&f.context.engine.bindings.containsKey(x));
  require(same(f,e)&&f.stage==6&&others&&live.get(e.getUUID())==e&&Ownership.global()&&Ownership.stopped(l)&&TimeStopEntityData.getTimeStopCount(e)>0&&!f.context.engine.terminalFault&&!s.unknown&&f.context.calls.isEmpty()&&f.context.engine.frames.isEmpty()&&!EntryDeserialize.processing(l),f,l,"goal-return-contract");
  if(f.quiet)s.epoch=++f.context.nextEpoch;
  s.quiet=false;s.reason="exact-initial-goal-return";s.origins.put(e.getUUID(),Ownership.type(e));TerminalWitnessBridge.bind(e);f.context.initialFrame=null;committed++;Ownership.reconcile(l);
 }
 public static void serverEnd(ServerLevel l){Frame f=AuthorityContext.of(l).initialFrame;if(f!=null)fail(f,l,"callback-return-missing");}
}
