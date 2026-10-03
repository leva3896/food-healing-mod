package com.leva.foodhealing.mixin.endinglibrary;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.leva.foodhealing.compat.endinglibrary.MovementGuard;
@Mixin(value=ServerGamePacketListenerImpl.class)
public class VehicleBoundary {
 @Inject(method="handleMoveVehicle",at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/Entity;getBoundingBox()Lnet/minecraft/world/phys/AABB;",ordinal=0),cancellable=true,require=1,expect=1,allow=1)
 private void vehicleBoundary(ServerboundMoveVehiclePacket packet,CallbackInfo ci){if(MovementGuard.deny((ServerGamePacketListenerImpl)(Object)this))ci.cancel();}
}
