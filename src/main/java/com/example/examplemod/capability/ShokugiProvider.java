package com.example.examplemod.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShokugiProvider implements ICapabilitySerializable<CompoundTag> {
    public static final Capability<IShokugiData> SHOKUGI_CAPA = CapabilityManager.get(new CapabilityToken<IShokugiData>() {});

    private IShokugiData shokugiData = null;
    private final LazyOptional<IShokugiData> optional = LazyOptional.of(this::createShokugiData);

    private IShokugiData createShokugiData() {
        if (this.shokugiData == null) {
            this.shokugiData = new ShokugiData();
        }
        return this.shokugiData;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == SHOKUGI_CAPA) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return createShokugiData().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createShokugiData().deserializeNBT(nbt);
    }
    
    public void invalidate() {
        optional.invalidate();
    }
}
