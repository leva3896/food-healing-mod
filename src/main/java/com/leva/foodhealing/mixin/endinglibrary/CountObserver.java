package com.leva.foodhealing.mixin.endinglibrary;
import com.mega.endinglib.common.capability.EndingLibraryLivingCapability;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.leva.foodhealing.compat.endinglibrary.*;
@Mixin(targets="com.mega.endinglib.common.capability.EndingLibraryLivingCapability",remap=false)
public class CountObserver {
 @Inject(method="setTimeStopCount(I)V",at=@At("HEAD"),remap=false)
 private void head(int value,CallbackInfo ci){var c=(EndingLibraryLivingCapability)(Object)this;TerminalWitnessBridge.countHead(c,value);Ownership.count(c,value);}
 @Inject(method="setTimeStopCount(I)V",at=@At("RETURN"),remap=false)
 private void returned(int value,CallbackInfo ci){var c=(EndingLibraryLivingCapability)(Object)this;TerminalWitnessBridge.countReturn(c);InitialStart.countReturn(c);}
 @Inject(method="customDeserializeNBT(Lnet/minecraft/nbt/CompoundTag;)V",at=@At("HEAD"),remap=false)
 private void deserialize(CompoundTag tag,CallbackInfo ci){EntryDeserialize.head((EndingLibraryLivingCapability)(Object)this,tag);}
 @Inject(method="customDeserializeNBT(Lnet/minecraft/nbt/CompoundTag;)V",at=@At("RETURN"),remap=false)
 private void deserialized(CompoundTag tag,CallbackInfo ci){
  var cap=(EndingLibraryLivingCapability)(Object)this;
  EntryDeserialize.returned(cap);
 }
}
