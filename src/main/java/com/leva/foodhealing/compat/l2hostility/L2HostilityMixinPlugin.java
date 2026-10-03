package com.leva.foodhealing.compat.l2hostility;

import net.minecraftforge.fml.loading.FMLLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import java.util.List;
import java.util.Set;

/** No external target class loads for absent or unreviewed dependency combinations. */
public final class L2HostilityMixinPlugin implements IMixinConfigPlugin {
    private static String version(String id) {
        return FMLLoader.getLoadingModList().getMods().stream().filter(m -> m.getModId().equals(id))
                .map(m -> m.getVersion().toString()).findFirst().orElse("");
    }
    @Override public boolean shouldApplyMixin(String target, String mixin) {
        return L2HostilityVersions.supports(version("l2hostility"), version("l2library"),
                version("l2complements"), version("l2damagetracker"));
    }
    @Override public void onLoad(String name) { }
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> mine, Set<String> others) { }
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String target, ClassNode node, String mixin, IMixinInfo info) { }
    @Override public void postApply(String target, ClassNode node, String mixin, IMixinInfo info) { }
}
