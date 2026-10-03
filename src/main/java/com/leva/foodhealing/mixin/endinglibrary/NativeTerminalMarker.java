package com.leva.foodhealing.mixin.endinglibrary;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.entity.LivingEntity;
@Mixin(value=LivingEntity.class,priority=500)
public abstract class NativeTerminalMarker {}
