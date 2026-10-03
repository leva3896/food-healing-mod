package com.leva.foodhealing.compat.endinglibrary;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import java.util.*;
public final class EntrySiteContract implements Opcodes {
 public static void goal(ClassNode c){
  var targets=c.methods.stream().filter(m->m.name.equals("timeStop")&&m.desc.equals("(Lcom/mega/uom/common/entity/boss/uom/UomWither;)V")).toList();
  if(targets.size()!=1)throw new IllegalStateException("initial goal descriptor count");
  var m=targets.get(0);List<AbstractInsnNode> a=new ArrayList<>();for(var n:m.instructions)if(n.getOpcode()>=0)a.add(n);
  int[] ops={ALOAD,INVOKEVIRTUAL,GETFIELD,IFNE,ALOAD,SIPUSH,INVOKESTATIC,ICONST_1,ALOAD,INVOKESTATIC,RETURN};
  if(a.size()!=ops.length||!m.tryCatchBlocks.isEmpty())throw new IllegalStateException("initial goal structural count/catch");
  for(int i=0;i<ops.length;i++)if(a.get(i).getOpcode()!=ops[i])throw new IllegalStateException("initial goal opcode "+i);
  if(((IntInsnNode)a.get(5)).operand!=180)throw new IllegalStateException("initial goal duration");
  var setter=(MethodInsnNode)a.get(6);var use=(MethodInsnNode)a.get(9);
  if(!setter.owner.equals("com/mega/endinglib/util/time/TimeStopEntityData")||!setter.name.equals("setTimeStopCount")||!setter.desc.equals("(Lnet/minecraft/world/entity/LivingEntity;I)V")||!use.owner.equals("com/mega/endinglib/util/time/TimeStopUtils")||!use.name.equals("use")||!use.desc.equals("(ZLnet/minecraft/world/entity/LivingEntity;)V"))throw new IllegalStateException("initial goal exact callsite");
  AbstractInsnNode target=((JumpInsnNode)a.get(3)).label;while(target!=null&&target.getOpcode()<0)target=target.getNext();
  if(target!=a.get(10))throw new IllegalStateException("initial goal client branch");
  System.out.println("FOODHEALING_TIMESTOP_GOAL_EXACT_1 setter180-before-use; no catch");
 }
}
