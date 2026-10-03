package com.leva.foodhealing.mixin.endinglibrary;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.leva.foodhealing.compat.endinglibrary.MovementGuard;
/** After native thread/invalid/teleport/passenger/sleep/speed checks; before jump/move/fall mutation. */
@Mixin(value=ServerGamePacketListenerImpl.class)
public class MovementBoundary {
 @Inject(method="handleMovePlayer",at=@At(value="INVOKE",target="Lnet/minecraft/server/level/ServerPlayer;getBoundingBox()Lnet/minecraft/world/phys/AABB;",ordinal=0),cancellable=true,require=1,expect=1,allow=1)
 private void boundary(ServerboundMovePlayerPacket packet,CallbackInfo ci){if(MovementGuard.deny((ServerGamePacketListenerImpl)(Object)this))ci.cancel();}
}
