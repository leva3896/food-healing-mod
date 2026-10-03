package com.leva.foodhealing.mixin.endinglibrary;
import com.leva.foodhealing.compat.endinglibrary.*;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
@Mixin(MinecraftServer.class)
public abstract class ServerContextHolder implements TimeStopContextHolder {
    @Unique private AuthorityContext foodhealing$authority;
    @Override public synchronized AuthorityContext foodhealing$timeStopContext() {
        if (foodhealing$authority == null) foodhealing$authority = new AuthorityContext((MinecraftServer)(Object)this);
        return foodhealing$authority;
    }
}
