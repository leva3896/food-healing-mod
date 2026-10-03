package com.leva.foodhealing.mixin;

import com.leva.foodhealing.ImmovableMasteryController;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.util.Map;

@Mixin(Explosion.class)
public abstract class ImmovableExplosionMixin {
    @Redirect(method = "explode", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"))
    private void foodhealing$explosionImpulse(Entity target, Vec3 velocity) {
        if (!ImmovableMasteryController.protects(target)) target.setDeltaMovement(velocity);
    }
    @Redirect(method = "explode", at = @At(value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private Object foodhealing$explosionPacketImpulse(Map<Player, Vec3> hits, Object key, Object value) {
        Player player = (Player) key;
        return hits.put(player, ImmovableMasteryController.protects(player) ? Vec3.ZERO : (Vec3) value);
    }
}
