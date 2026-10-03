package com.leva.foodhealing.compat.endinglibrary;
import java.util.*;
/** Memory-only matcher shared verbatim by native bridge and finite contracts. */
public final class TerminalWitness {
 public record Identity(Object source,UUID uuid,String type,Object level,String dimension,Object server,long epoch,Object thread){}
 public record OriginBinding(Identity identity){}
 public static final class CallbackFrame {
  final Identity identity; final OriginBinding binding; final long token;
  Object cap; int old,value,heads,returns; boolean setter,delegating,consumed,commonReturn,delegateReturn,caught;
  CallbackFrame(Identity i,OriginBinding b,long t){identity=i;binding=b;token=t;}
 }
 public final IdentityHashMap<Object,OriginBinding> bindings=new IdentityHashMap<>();
 public final IdentityHashMap<Object,CallbackFrame> frames=new IdentityHashMap<>(), witnesses=new IdentityHashMap<>();
 final Deque<CallbackFrame> stack=new ArrayDeque<>();
 public boolean terminalFault; public String reason=""; long tokens;
 public int created,consumed,committed,faultEvents;
 public void fault(String why){terminalFault=true;reason=reason.isEmpty()?why:reason;faultEvents++;witnesses.clear();}
 static boolean same(Identity a,Identity b){return a.source()==b.source()&&a.uuid().equals(b.uuid())&&a.type().equals(b.type())&&a.level()==b.level()&&a.dimension().equals(b.dimension())&&a.server()==b.server()&&a.epoch()==b.epoch()&&a.thread()==b.thread();}
 public void bind(Identity i){if(terminalFault)return;OriginBinding old=bindings.get(i.source());if(old!=null&&!same(old.identity(),i)){fault("binding-conflict");return;}bindings.put(i.source(),new OriginBinding(i));}
 public CallbackFrame enter(Identity i,boolean known,boolean unknown,int foreign){
  OriginBinding b=bindings.get(i.source());
  if(terminalFault||unknown){fault("pre-existing-unknown");return null;}
  if(!known||b==null||!same(b.identity(),i)){fault("binding-identity");return null;}
  if(foreign!=0){fault("foreign");return null;}
  if(!stack.isEmpty()||frames.containsKey(i.source())){fault("nested-callback");return null;}
  CallbackFrame f=new CallbackFrame(i,b,++tokens);frames.put(i.source(),f);stack.push(f);return f;
 }
 public boolean context(CallbackFrame f,Identity i){boolean ok=f!=null&&frames.get(i.source())==f&&stack.peek()==f&&bindings.get(i.source())==f.binding&&same(f.identity,i)&&!terminalFault;if(!ok)fault("callback-identity");return ok;}
 public void arm(CallbackFrame f,Identity i,int old,int value){if(!context(f,i))return;if(f.setter||f.heads!=0||old<=0||value!=old-1){fault("decrement-shape");return;}f.setter=true;f.old=old;f.value=value;}
 public void head(CallbackFrame f,Identity i,Object cap,int old,int value){if(!context(f,i))return;if(!f.setter||f.heads++!=0||old!=f.old||value!=f.value){fault("setter-head");return;}f.cap=cap;}
 public void returned(CallbackFrame f,Identity i,Object cap,int actual){if(!context(f,i))return;if(!f.setter||f.heads!=1||f.cap!=cap||f.returns++!=0||actual!=f.value){fault("setter-return");}}
 public void minted(CallbackFrame f,Identity i,int actual){if(!context(f,i))return;f.setter=false;if(f.heads!=1||f.returns!=1||actual!=f.value){fault("missing-setter-ack");return;}if(f.old==1){if(witnesses.containsKey(i.source())){fault("duplicate-witness");return;}witnesses.put(i.source(),f);created++;}}
 public boolean pending(Object source){CallbackFrame f=witnesses.get(source);return !terminalFault&&f!=null&&frames.get(source)==f&&stack.peek()==f;}
 public void beginUse(CallbackFrame f,Identity i){if(!context(f,i))return;if(!pending(i.source())||f.delegating){fault("unexpected-use");return;}f.delegating=true;}
 public void consume(CallbackFrame f,Identity i,boolean shape,int depth,boolean unknown,int foreign){
  if(f==null){fault("unexpected-use");return;}if(!context(f,i))return;
  if(unknown||foreign!=0){fault("use-unknown-foreign");return;}
  if(!pending(i.source())||!f.delegating||!shape||depth!=0||f.consumed){fault("consume-shape-or-duplicate");return;}
  f.consumed=true;consumed++;
 }
 public void commonReturn(CallbackFrame f,Identity i){if(!context(f,i))return;if(!f.consumed||f.commonReturn){fault("common-return");return;}f.commonReturn=true;}
 public void delegateReturn(CallbackFrame f,Identity i){if(!context(f,i))return;if(!f.commonReturn||f.delegateReturn){fault("delegate-return");return;}f.delegateReturn=true;}
 public boolean exit(CallbackFrame f,Identity i,boolean post){
  if(f==null)return false;boolean valid=context(f,i);boolean terminal=f.old==1&&f.heads>0;
  boolean commit=valid&&terminal&&pending(i.source())&&f.consumed&&f.commonReturn&&f.delegateReturn&&!f.caught&&post;
  if(terminal&&!commit)fault("incomplete-terminal-exit");
  if(!terminal&&valid&&(f.setter||f.heads!=1||f.returns!=1))fault("incomplete-decrement-exit");
  frames.remove(f.identity.source());witnesses.remove(f.identity.source());stack.remove(f);
  if(commit&&!terminalFault){bindings.remove(i.source());committed++;return true;}return false;
 }
 public void caught(CallbackFrame f){if(f!=null){f.caught=true;fault("native-catch-or-outer-exception");}}
 public void serverEnd(){if(!frames.isEmpty()||!witnesses.isEmpty()||!stack.isEmpty())fault("callback-not-ended");}
 public Map<String,Object> snapshot(){var m=new LinkedHashMap<String,Object>();m.put("terminalFault",terminalFault);m.put("faultReason",reason);m.put("bindings",bindings.size());m.put("callbackFrames",frames.size());m.put("witnesses",witnesses.size());m.put("created",created);m.put("consumed",consumed);m.put("committed",committed);m.put("faultEvents",faultEvents);return m;}
}
