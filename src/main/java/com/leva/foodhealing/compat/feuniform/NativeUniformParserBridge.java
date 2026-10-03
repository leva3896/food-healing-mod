package com.leva.foodhealing.compat.feuniform;

import com.google.gson.JsonElement;
import net.minecraft.server.ChainedJsonException;

public interface NativeUniformParserBridge {
    void fhfe$parseUniform(JsonElement node) throws ChainedJsonException;
}
