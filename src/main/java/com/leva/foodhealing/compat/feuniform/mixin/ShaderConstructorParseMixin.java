package com.leva.foodhealing.compat.feuniform.mixin;

import com.google.gson.JsonElement;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.leva.foodhealing.compat.feuniform.NativeUniformParserBridge;
import com.leva.foodhealing.compat.feuniform.PatchContract;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ChainedJsonException;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value=ShaderInstance.class,priority=1000)
public abstract class ShaderConstructorParseMixin {
    @Shadow private void parseUniformNode(JsonElement node) throws ChainedJsonException { throw new AssertionError("unmerged shadow"); }

    @Redirect(method="<init>(Lnet/minecraft/server/packs/resources/ResourceProvider;Lnet/minecraft/resources/ResourceLocation;Lcom/mojang/blaze3d/vertex/VertexFormat;)V",
        at=@At(value="INVOKE",target="Lnet/minecraft/client/renderer/ShaderInstance;parseUniformNode(Lcom/google/gson/JsonElement;)V",ordinal=0),
        require=1,expect=1,allow=1)
    private void fhfe$route(ShaderInstance receiver,JsonElement node,ResourceProvider provider,ResourceLocation location,VertexFormat format) throws ChainedJsonException {
        if(receiver != (Object)this)throw new IllegalStateException("FECP receiver mismatch");
        if(PatchContract.eligible(receiver.getClass().getName(),location.toString())) {
            if(!(receiver instanceof NativeUniformParserBridge bridge))throw new IllegalStateException("FECP eligible receiver missing bridge");
            bridge.fhfe$parseUniform(node);
        } else parseUniformNode(node);
    }
}
