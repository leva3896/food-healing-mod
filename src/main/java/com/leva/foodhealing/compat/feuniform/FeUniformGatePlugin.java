package com.leva.foodhealing.compat.feuniform;

import java.util.List;
import java.util.Set;
import net.minecraftforge.fml.loading.FMLLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class FeUniformGatePlugin implements IMixinConfigPlugin {
    private Boolean active;
    /** Reads early loader metadata without linking optional FE classes. */
    protected PatchContract.Profile readProfile() throws Exception {
        var version=FMLLoader.versionInfo(); var mods=FMLLoader.getLoadingModList();
        if(version==null || mods==null || FMLLoader.getDist()==null)return null;
        if(!"CLIENT".equals(FMLLoader.getDist().name()) || !"1.20.1".equals(version.mcVersion()) || !"47.4.0".equals(version.forgeVersion()))
            return new PatchContract.Profile(FMLLoader.getDist().name(),version.mcVersion(),version.forgeVersion(),null,null);
        var fe=mods.getMods().stream().filter(m->"fantasy_ending".equals(m.getModId())).findFirst();
        if(fe.isEmpty()) return new PatchContract.Profile(FMLLoader.getDist().name(),version.mcVersion(),version.forgeVersion(),null,null);
        if(!"2.7.20".equals(fe.get().getVersion().toString()))return new PatchContract.Profile("CLIENT",version.mcVersion(),version.forgeVersion(),fe.get().getVersion().toString(),null);
        var file=fe.get().getOwningFile().getFile();
        var profile=new PatchContract.Profile(FMLLoader.getDist().name(),version.mcVersion(),version.forgeVersion(),fe.get().getVersion().toString(),PatchContract.hash(file.getFilePath()));
        if(PatchContract.accepts(profile)) {
            ClassNode n=new ClassNode();new ClassReader(java.nio.file.Files.readAllBytes(file.findResource(PatchContract.FE+".class"))).accept(n,0);
            PatchContract.checkBridgeBefore(n);
        }
        return profile;
    }
    public void onLoad(String pkg) { }
    public String getRefMapperConfig(){return null;}
    public synchronized boolean shouldApplyMixin(String target,String mixin) {
        if(active==null) {
            try { var p=readProfile(); active=PatchContract.accepts(p); System.getLogger("foodhealing.feuniform").log(System.Logger.Level.DEBUG,"FECP profile="+p+" active="+active); }
            catch(Exception e) { throw new IllegalStateException("FECP profile read/structure failure",e); }
        }
        return active;
    }
    public void acceptTargets(Set<String>a,Set<String>b) { }
    public List<String> getMixins(){return null;}
    public void preApply(String target,ClassNode n,String mixin,IMixinInfo info) {
        if(n.name.equals(PatchContract.PARENT))PatchContract.checkParentBefore(n);
        else if(n.name.equals(PatchContract.FE))PatchContract.checkBridgeBefore(n);
        else throw new IllegalStateException("FECP unexpected target "+n.name);
    }
    public void postApply(String target,ClassNode n,String mixin,IMixinInfo info) {
        if(n.name.equals(PatchContract.PARENT))PatchContract.checkParentAfter(n);
        else if(n.name.equals(PatchContract.FE))PatchContract.checkBridgeAfter(n);
    }
}
