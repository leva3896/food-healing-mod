package com.leva.foodhealing.compat.feuniform;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/** Exact artifact and bytecode contracts for the supported native FE shader parser. */
public final class PatchContract {
    private PatchContract() { }
    public static final String FE_HASH = "E32FD4BA2E48FCF2C84F71AA07155C63D6BFA2178BB840899307DA0DA5E5D141";
    public static final String PARENT = "net/minecraft/client/renderer/ShaderInstance";
    public static final String FE = "com/mega/uom/client/render/shader/core/MShaderInstance";
    public static final String CTOR = "(Lnet/minecraft/server/packs/resources/ResourceProvider;Lnet/minecraft/resources/ResourceLocation;Lcom/mojang/blaze3d/vertex/VertexFormat;)V";
    public static final String PARSER = "(Lcom/google/gson/JsonElement;)V";
    public record Profile(String side, String mc, String forge, String fe, String hash) { }
    public static boolean accepts(Profile p) {
        return p != null && "CLIENT".equals(p.side()) && "1.20.1".equals(p.mc())
                && "47.4.0".equals(p.forge()) && "2.7.20".equals(p.fe()) && FE_HASH.equals(p.hash());
    }
    public static boolean eligible(String type, String resource) {
        return ("com.mega.uom.client.render.shader.cosmic.CosmicShaderInstance".equals(type)
                && ("fantasy_ending:cosmic".equals(resource) || "fantasy_ending:cosmic_2".equals(resource)))
            || ("com.mega.uom.client.render.shader.core.MShaderInstance".equals(type)
                && ("fantasy_ending:hash".equals(resource)
                    || "fantasy_ending:rendertype_light_beacon_beam".equals(resource)
                    || "fantasy_ending:rendertype_cil_particle".equals(resource)));
    }
    public static String hash(Path path) throws Exception {
        MessageDigest d = MessageDigest.getInstance("SHA-256");
        try (var in = Files.newInputStream(path)) {
            byte[] b = new byte[65536]; int n;
            while ((n = in.read(b)) >= 0) if (n != 0) d.update(b, 0, n);
        }
        return HexFormat.of().withUpperCase().formatHex(d.digest());
    }
    public static MethodNode method(ClassNode n, String name, String descriptor) {
        return n.methods.stream().filter(m -> m.name.equals(name) && m.desc.equals(descriptor)).findFirst()
                .orElseThrow(() -> new IllegalStateException("FECP missing method " + n.name + "." + name + descriptor));
    }
    public static void require(boolean ok, String reason) {
        if (!ok) throw new IllegalStateException("FECP contract: " + reason);
    }
    public static void checkParentBefore(ClassNode n) {
        MethodNode parser = method(n, "m_173354_", PARSER);
        require((parser.access & Opcodes.ACC_PRIVATE) != 0, "parent parser must remain private");
        MethodNode ctor = method(n, "<init>", CTOR);
        int call = -1, count = 0, delegate = -1, list = -1, map = -1, getter = -1;
        for (int i = 0; i < ctor.instructions.size(); i++) {
            var x = ctor.instructions.get(i);
            if (x instanceof MethodInsnNode m) {
                if (m.owner.equals("java/lang/Object") && m.name.equals("<init>")) delegate = i;
                if (m.owner.equals(PARENT) && m.name.equals("m_173354_") && m.desc.equals(PARSER)) {count++;call=i;}
                if (m.name.equals("m_173348_") && getter < 0) getter=i;
            }
            if (x instanceof FieldInsnNode f && x.getOpcode() == Opcodes.PUTFIELD && f.owner.equals(PARENT)) {
                if (f.name.equals("f_173331_")) list=i;
                if (f.name.equals("f_173333_")) map=i;
            }
        }
        require(count == 1, "exact constructor parser call count=" + count);
        require(delegate >= 0 && delegate < call && list >= 0 && list < call && map >= 0 && map < call,
                "delegate/list/map must initialize before parser: " + delegate + "/" + list + "/" + map + "/" + call);
        require(getter > call, "getter must follow parsing");
    }
    public static void checkBridgeBefore(ClassNode n) {
        MethodNode m = method(n, "m_173354_", PARSER);
        require((m.access & Opcodes.ACC_PUBLIC) != 0, "FE parser public");
        for (var x : m.instructions) if (x instanceof FieldInsnNode f)
            require(!f.owner.equals(FE), "FE parser must not access subclass fields");
        require(n.interfaces.stream().noneMatch(s -> s.equals("com/leva/foodhealing/compat/feuniform/NativeUniformParserBridge")), "bridge already present");
    }
    public static void checkParentAfter(ClassNode n) {
        MethodNode ctor = method(n, "<init>", CTOR); int original=0, routed=0;
        for (var x : ctor.instructions) if (x instanceof MethodInsnNode m) {
            if (m.owner.equals(PARENT) && m.name.equals("m_173354_") && m.desc.equals(PARSER)) original++;
            if (m.owner.equals(PARENT) && m.name.contains("fhfe$route")) routed++;
        }
        require(original == 0 && routed == 1, "constructor redirect count="+routed+", original="+original);
        var handler = n.methods.stream().filter(m -> m.name.contains("fhfe$route")).findFirst().orElseThrow();
        int fallback=0, bridge=0;
        for (var x : handler.instructions) if (x instanceof MethodInsnNode m) {
            if (m.owner.equals(PARENT) && m.name.equals("m_173354_") && m.desc.equals(PARSER)) fallback++;
            if (m.owner.equals("com/leva/foodhealing/compat/feuniform/NativeUniformParserBridge") && m.name.equals("fhfe$parseUniform") && m.desc.equals(PARSER)) bridge++;
        }
        require(fallback==1 && bridge==1, "handler fallback/bridge="+fallback+"/"+bridge);
        require((method(n,"m_173354_",PARSER).access & Opcodes.ACC_PRIVATE)!=0,"fallback private");
    }
    public static void checkBridgeAfter(ClassNode n) {
        require(n.interfaces.contains("com/leva/foodhealing/compat/feuniform/NativeUniformParserBridge"),"bridge interface missing");
        MethodNode m=method(n,"fhfe$parseUniform",PARSER); int calls=0;
        for(var x:m.instructions) if(x instanceof MethodInsnNode i && i.owner.equals(FE) && i.name.equals("m_173354_") && i.desc.equals(PARSER))calls++;
        require(calls==1,"FE public delegate call count="+calls);
    }
}
