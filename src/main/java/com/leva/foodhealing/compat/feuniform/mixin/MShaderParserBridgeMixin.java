package com.leva.foodhealing.compat.feuniform.mixin;

import com.google.gson.JsonElement;
import com.leva.foodhealing.compat.feuniform.NativeUniformParserBridge;
import net.minecraft.server.ChainedJsonException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

@Pseudo
@Mixin(targets="com.mega.uom.client.render.shader.core.MShaderInstance",remap=false,priority=1000)
public abstract class MShaderParserBridgeMixin implements NativeUniformParserBridge {
    @Shadow(remap=false) public abstract void m_173354_(JsonElement node) throws ChainedJsonException;
    @Override public void fhfe$parseUniform(JsonElement node) throws ChainedJsonException { m_173354_(node); }
}
