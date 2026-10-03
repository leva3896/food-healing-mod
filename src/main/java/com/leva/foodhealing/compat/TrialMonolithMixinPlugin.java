package com.leva.foodhealing.compat;

import net.minecraftforge.fml.loading.FMLLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import java.util.List;
import java.util.Set;

/** Optional when absent/unsupported, mandatory injections for the selected 1.4.9 artifact. */
public final class TrialMonolithMixinPlugin implements IMixinConfigPlugin {
    @Override public boolean shouldApplyMixin(String target, String mixin) {
        return FMLLoader.getLoadingModList().getMods().stream().anyMatch(mod ->
                mod.getModId().equals(TrialMonolithCompatibility.MOD_ID)
                        && mod.getVersion().toString().equals(TrialMonolithCompatibility.SUPPORTED_VERSION));
    }
    @Override public void onLoad(String name) { }
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> mine, Set<String> others) { }
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String target, ClassNode node, String mixin, IMixinInfo info) { }
    @Override public void postApply(String target, ClassNode node, String mixin, IMixinInfo info) { }
}
