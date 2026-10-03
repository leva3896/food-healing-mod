package com.leva.foodhealing.compat.fantasyending;

import com.leva.foodhealing.PurificationMasteryController;
import com.leva.foodhealing.capability.ShokugiProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.registries.ForgeRegistries;

/** Only reached from the six version-gated call sites. No external class linkage. */
public final class FantasyEndingCompatibility {
    private static final ResourceLocation UOM = new ResourceLocation("fantasy_ending", "ultimate_order_manager");
    private FantasyEndingCompatibility() { }
    public static boolean blocksDirect(Entity source, Entity recipient) {
        return source != null && recipient instanceof ServerPlayer player
                && !player.level().isClientSide() && source.level() == player.level()
                && source.getClass().getName().equals("com.mega.uom.common.entity.boss.uom.UomWither")
                && source.getType() == ForgeRegistries.ENTITY_TYPES.getValue(UOM)
                && player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .map(PurificationMasteryController::isEnabled).orElse(false);
    }
    public static boolean blocksProjectile(Projectile projectile, Entity recipient) {
        return projectile.level() == recipient.level() && blocksDirect(projectile.getOwner(), recipient);
    }
}
