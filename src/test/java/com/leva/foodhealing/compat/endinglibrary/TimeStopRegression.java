package com.leva.foodhealing.compat.endinglibrary;
import java.util.UUID;
/** Focused tests of the shipped witness matcher, not a replay of the verification matrix. */
public final class TimeStopRegression {
    private static int cases;
    public static void run() {
        normal(); wrongIdentity(); missingReturn(); unknown(); faultStaysLatched(); inertZero();
        System.out.println("TimeStop production focused: "+cases+" cases PASS");
    }
    private static TerminalWitness.Identity identity() {
        return new TerminalWitness.Identity(new Object(),UUID.randomUUID(),Ownership.UOM,new Object(),"minecraft:overworld",new Object(),1,Thread.currentThread());
    }
    private static void check(boolean ok) { if(!ok)throw new AssertionError("TimeStop production witness"); }
    private static TerminalWitness.CallbackFrame decrement(TerminalWitness w,TerminalWitness.Identity id) {
        w.bind(id);var f=w.enter(id,true,false,0);Object cap=new Object();
        w.arm(f,id,1,0);w.head(f,id,cap,1,0);w.returned(f,id,cap,0);w.minted(f,id,0);return f;
    }
    private static void normal() {
        var w=new TerminalWitness();var id=identity();var f=decrement(w,id);
        w.beginUse(f,id);w.consume(f,id,true,0,false,0);w.commonReturn(f,id);w.delegateReturn(f,id);
        check(w.exit(f,id,true)&&w.bindings.isEmpty()&&w.created==1&&w.committed==1&&!w.terminalFault);cases++;
    }
    private static void wrongIdentity() {
        var w=new TerminalWitness();var id=identity();w.bind(id);
        var other=new TerminalWitness.Identity(id.source(),id.uuid(),id.type(),id.level(),id.dimension(),new Object(),id.epoch(),id.thread());
        check(w.enter(other,true,false,0)==null&&w.terminalFault&&w.committed==0);cases++;
    }
    private static void missingReturn() {
        var w=new TerminalWitness();var id=identity();var f=decrement(w,id);
        w.beginUse(f,id);w.consume(f,id,true,0,false,0);
        check(!w.exit(f,id,true)&&w.terminalFault&&w.committed==0);cases++;
    }
    private static void unknown() {
        var w=new TerminalWitness();var id=identity();w.bind(id);
        check(w.enter(id,true,true,0)==null&&w.terminalFault);cases++;
    }
    private static void faultStaysLatched() {
        var w=new TerminalWitness();var id=identity();w.fault("foreign");w.bind(id);
        check(w.bindings.isEmpty()&&w.terminalFault&&w.enter(id,true,false,0)==null);cases++;
    }
    private static void inertZero() {
        check(CountMutationPolicy.inertZero(0,0,false,false,false));cases++;
        check(!CountMutationPolicy.inertZero(0,180,false,false,false));cases++;
        check(!CountMutationPolicy.inertZero(1,0,false,false,false));cases++;
        check(!CountMutationPolicy.inertZero(0,0,true,false,false)
                &&!CountMutationPolicy.inertZero(0,0,false,true,false)
                &&!CountMutationPolicy.inertZero(0,0,false,false,true));cases++;
    }
}
