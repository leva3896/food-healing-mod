package com.leva.foodhealing.compat.l2hostility;

import com.leva.foodhealing.PurificationMasteryController;
import com.leva.foodhealing.TruthMasteryController;
import com.leva.foodhealing.capability.ShokugiProvider;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

/** Exact-version external linkage. Only six registered IDs and Food Healing-owned data are written. */
public final class L2HostilityAdapter {
    public static final String MARKER = "foodhealing:truth_l2";
    private L2HostilityAdapter() { }

    public static String id(MobTrait trait) {
        if (trait == LHTraits.POISON.get()) return "l2hostility:poison";
        if (trait == LHTraits.SLOWNESS.get()) return "l2hostility:slowness";
        if (trait == LHTraits.WEAKNESS.get()) return "l2hostility:weakness";
        if (trait == LHTraits.WITHER.get()) return "l2hostility:wither";
        if (trait == LHTraits.CORROSION.get()) return "l2hostility:corrosion";
        if (trait == LHTraits.EROSION.get()) return "l2hostility:erosion";
        return null;
    }

    private static MobTraitCap cap(Mob mob) {
        return mob.getCapability(MobTraitCap.CAPABILITY).resolve().orElse(null);
    }

    private static CompoundTag existingMarker(Mob mob) {
        CompoundTag root = mob.getPersistentData();
        if (!root.contains(MARKER, Tag.TAG_COMPOUND)) return null;
        CompoundTag tag = root.getCompound(MARKER);
        return tag.getInt("Version") == 1 && tag.hasUUID("Mob") && tag.getUUID("Mob").equals(mob.getUUID()) ? tag : null;
    }

    public static boolean marked(Mob mob, String id) {
        CompoundTag tag = existingMarker(mob);
        return id != null && tag != null && tag.getCompound("Traits").getBoolean(id);
    }

    private static boolean inActiveArea(Mob mob) {
        if (!(mob.level() instanceof ServerLevel level)) return false;
        for (ServerPlayer player : level.players()) {
            if (player.isAlive() && TruthMasteryController.contains(player.getX(), player.getY(), player.getZ(),
                    mob.getX(), mob.getY(), mob.getZ()) && player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                    .map(TruthMasteryController::isEnabled).orElse(false)) return true;
        }
        return false;
    }

    /** Called at safe lifecycle boundaries, and read-before-effect without pruning an iterated map. */
    public static void consider(Mob mob, boolean prune) {
        if (!(mob.level() instanceof ServerLevel) || !mob.isAlive()) return;
        MobTraitCap cap = cap(mob);
        if (cap == null) return;
        boolean hasNew = cap.traits.entrySet().stream().anyMatch(e -> e.getValue() != null && e.getValue() > 0
                && id(e.getKey()) != null && !marked(mob, id(e.getKey())));
        if (hasNew && inActiveArea(mob)) {
            CompoundTag previous = existingMarker(mob);
            CompoundTag tag = previous == null ? new CompoundTag() : previous.copy();
            CompoundTag ids = tag.getCompound("Traits");
            cap.traits.forEach((trait, rank) -> {
                String id = id(trait);
                if (id != null && rank != null && rank > 0) ids.putBoolean(id, true);
            });
            tag.putInt("Version", 1); tag.putUUID("Mob", mob.getUUID()); tag.put("Traits", ids);
            mob.getPersistentData().put(MARKER, tag);
        }
        if (prune) prune(mob, cap);
    }

    public static void prune(Mob mob, MobTraitCap cap) {
        if (mob.level().isClientSide() || existingMarker(mob) == null || !(cap instanceof L2MobTraitAccess access) || !access.foodhealing$canPrune()) return;
        boolean changed = false;
        // A bounded snapshot avoids mutating a live iterator, and never touches unrelated traits/pending/data.
        for (MobTrait trait : new MobTrait[]{LHTraits.POISON.get(), LHTraits.SLOWNESS.get(),
                LHTraits.CORROSION.get(), LHTraits.EROSION.get(), LHTraits.WEAKNESS.get(), LHTraits.WITHER.get()}) {
            if (marked(mob, id(trait)) && cap.traits.containsKey(trait)) {
                cap.removeTrait(trait);
                changed = true;
            }
        }
        if (changed) cap.syncToClient(mob);
    }

    public static boolean nullified(MobTrait trait, Entity attacker) {
        String id = id(trait);
        if (id == null || !(attacker instanceof Mob mob) || mob.level().isClientSide()) return false;
        // Catch entry/teleport and same-tick reapplication before the next bounded spatial refresh.
        consider(mob, false);
        return marked(mob, id);
    }

    public static boolean blocksPersonalEffect(MobTrait trait, Entity attacker, LivingEntity target) {
        if (id(trait) == null || !(attacker instanceof Mob mob) || mob.level().isClientSide()
                || !(target instanceof ServerPlayer player) || mob.level() != player.level()) return false;
        MobTraitCap source = cap(mob);
        return source != null && source.getTraitLevel(trait) > 0
                && player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .map(PurificationMasteryController::isEnabled).orElse(false);
    }
}
