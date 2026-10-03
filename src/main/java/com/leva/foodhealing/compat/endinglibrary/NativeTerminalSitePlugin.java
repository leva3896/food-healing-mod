package com.leva.foodhealing.compat.endinglibrary;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.extensibility.*;
import java.util.*;
import java.nio.file.*;
import java.security.*;
import java.util.jar.*;
/** Structural, version-pinned one-site transform. Generated method names are never selectors. */
public final class NativeTerminalSitePlugin implements IMixinConfigPlugin,Opcodes {
 static final String EL="com/mega/endinglib/util/time/", E="Lnet/minecraft/world/entity/LivingEntity;", B="com/leva/foodhealing/compat/endinglibrary/TerminalWitnessBridge";
 public static int applied;
 static void require(boolean b,String m){if(!b)throw new IllegalStateException("Food Healing TimeStop contract: "+m);}
 public void onLoad(String pkg){TimeStopGate.initialize();}
 public String getRefMapperConfig(){return null;}public boolean shouldApplyMixin(String t,String m){return TimeStopGate.supported();}public void acceptTargets(Set<String>a,Set<String>b){}public List<String> getMixins(){return null;}public void preApply(String t,ClassNode c,String m,IMixinInfo i){if(m.endsWith(".InitialGoalMixin"))EntrySiteContract.goal(c);}
 public void postApply(String t,ClassNode c,String m,IMixinInfo i){if(m.endsWith(".NativeTerminalMarker")){transform(c);require(++applied==1,"runtime transform count");System.out.println("FOODHEALING_TIMESTOP_NATIVE_SITE_1 "+c.name);}}
 static boolean origin(MethodNode m){List<AnnotationNode> a=new ArrayList<>();if(m.visibleAnnotations!=null)a.addAll(m.visibleAnnotations);if(m.invisibleAnnotations!=null)a.addAll(m.invisibleAnnotations);return a.stream().anyMatch(x->x.desc.equals("Lorg/spongepowered/asm/mixin/transformer/meta/MixinMerged;")&&x.values!=null&&x.values.contains("com.mega.endinglib.mixin.time.LivingEntityMixin"));}
 static List<AbstractInsnNode> code(MethodNode m){List<AbstractInsnNode> l=new ArrayList<>();for(var n:m.instructions)if(n.getOpcode()>=0)l.add(n);return l;}
 static boolean call(AbstractInsnNode n,String owner,String name,String desc){return n instanceof MethodInsnNode x&&x.getOpcode()==INVOKESTATIC&&x.owner.equals(owner)&&x.name.equals(name)&&x.desc.equals(desc);}
 static MethodInsnNode hook(String name,String desc){return new MethodInsnNode(INVOKESTATIC,B,name,desc,false);}
 static InsnList notice(String name){InsnList x=new InsnList();x.add(new VarInsnNode(ALOAD,0));x.add(hook(name,"("+E+")V"));return x;}
 static int realIndex(MethodNode m,LabelNode label){int index=0;for(var n:m.instructions){if(n==label)return index;if(n.getOpcode()>=0)index++;}return -1;}
 public static String transform(ClassNode c){
  List<MethodNode> candidates=c.methods.stream().filter(NativeTerminalSitePlugin::origin).filter(m->m.desc.equals("(Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V")).filter(m->code(m).stream().anyMatch(n->call(n,EL+"TimeStopEntityData","setTimeStopCount","("+E+"I)V"))).toList();
  require(candidates.size()==1,"exact merged callback count="+candidates.size());MethodNode m=candidates.get(0);var a=code(m);
  int[] ops={ALOAD,INVOKEVIRTUAL,INVOKEVIRTUAL,ASTORE,ALOAD,LDC,INVOKEINTERFACE,ALOAD,ASTORE,ALOAD,INVOKEVIRTUAL,GETFIELD,IFNE,ALOAD,INVOKESTATIC,IFLE,ALOAD,ALOAD,INVOKESTATIC,ICONST_1,ISUB,INVOKESTATIC,ALOAD,INVOKESTATIC,IFGT,ICONST_0,ALOAD,INVOKESTATIC,GOTO,ASTORE,ALOAD,INVOKEVIRTUAL,ALOAD,INVOKEINTERFACE,RETURN};
  require(a.size()==ops.length,"instruction count "+a.size());for(int k=0;k<ops.length;k++)require(a.get(k).getOpcode()==ops[k],"opcode order at "+k);
  require(((LdcInsnNode)a.get(5)).cst.equals("ending_library_entity_tickTimeStop"),"native profiler identity");
  for(int k:new int[]{14,18,23})require(call(a.get(k),EL+"TimeStopEntityData","getTimeStopCount","("+E+")I"),"getter "+k);
  require(call(a.get(21),EL+"TimeStopEntityData","setTimeStopCount","("+E+"I)V"),"setter");require(call(a.get(27),EL+"TimeStopUtils","use","(Z"+E+")V"),"use2");
  FieldInsnNode side=(FieldInsnNode)a.get(11);require(side.owner.equals("net/minecraft/world/level/Level")&&side.desc.equals("Z")&&(side.name.equals("f_46443_")||side.name.equals("isClientSide")),"server branch");
  for(int k:new int[]{12,15,24})require(realIndex(m,((JumpInsnNode)a.get(k)).label)==28,"native branch target");
  require(realIndex(m,((JumpInsnNode)a.get(28)).label)==32,"native catch bypass");
  require(m.tryCatchBlocks.size()==1,"catch count");TryCatchBlockNode tc=m.tryCatchBlocks.get(0);require("java/lang/Throwable".equals(tc.type)&&realIndex(m,tc.start)==7&&realIndex(m,tc.end)==28&&realIndex(m,tc.handler)==29,"catch structure");
  MethodInsnNode print=(MethodInsnNode)a.get(31);require(print.owner.equals("java/lang/Throwable")&&print.name.equals("printStackTrace")&&print.desc.equals("()V"),"catch delegates print");
  for(int k:new int[]{13,16,17,22,26})require(((VarInsnNode)a.get(k)).var==((VarInsnNode)a.get(8)).var,"source local");
  // Existing operations stay at the exact original branch locations, with the same stack arguments.
  m.instructions.set(a.get(21),hook("decrement","("+E+"I)V"));m.instructions.set(a.get(27),hook("exactUse","(Z"+E+")V"));
  m.instructions.insert(a.get(29),notice("caught"));
  LabelNode start=new LabelNode(),end=new LabelNode(),handler=new LabelNode();InsnList head=notice("enter");head.add(start);m.instructions.insert(head);
  m.instructions.insertBefore(a.get(34),notice("exit"));m.instructions.insertBefore(a.get(34),end);
  int local=m.maxLocals++;m.instructions.add(handler);m.instructions.add(new VarInsnNode(ASTORE,local));m.instructions.add(notice("outerFailure"));m.instructions.add(new VarInsnNode(ALOAD,local));m.instructions.add(new InsnNode(ATHROW));m.tryCatchBlocks.add(new TryCatchBlockNode(start,end,handler,"java/lang/Throwable"));m.maxStack=Math.max(m.maxStack,4);
  return m.name;
 }
}
