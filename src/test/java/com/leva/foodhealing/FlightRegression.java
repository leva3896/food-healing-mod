package com.leva.foodhealing;

import com.leva.foodhealing.capability.ShokugiData;
import net.minecraft.nbt.*;

final class FlightRegression {
    private static int assertions;
    private static final FlightAuthority ALLOW = new FlightAuthority(true, false, true);
    private static final FlightAuthority DENY = new FlightAuthority(false, true, true);
    static void run() {
        var avaritiaHash = com.leva.foodhealing.compat.flight.AvaritiaFlightGate.SHA256;
        require(com.leva.foodhealing.compat.flight.AvaritiaFlightGate.matches("avaritia", "4.0.3", avaritiaHash), "exact Avaritia identity");
        require(!com.leva.foodhealing.compat.flight.AvaritiaFlightGate.matches("avaritia", "4.0.4", avaritiaHash), "other Avaritia version inactive");
        require(!com.leva.foodhealing.compat.flight.AvaritiaFlightGate.matches("avaritia", "4.0.3", "00"), "same-version Avaritia other artifact inactive");
        require(!com.leva.foodhealing.compat.flight.AvaritiaFlightGate.matches("other", "4.0.3", avaritiaHash), "other Avaritia id inactive");
        require(!com.leva.foodhealing.compat.flight.AvaritiaFlightGate.matches("avaritia", "4.0.3", null), "unavailable identity not supported");
        var hash = com.leva.foodhealing.compat.flight.MekanismFlightGate.SHA256;
        require(com.leva.foodhealing.compat.flight.MekanismFlightGate.matches("mekanism", "10.4.16", hash), "exact Mekanism archive identity");
        require(!com.leva.foodhealing.compat.flight.MekanismFlightGate.matches("mekanism", "10.4.16.80", hash), "filename is not loader version");
        require(!com.leva.foodhealing.compat.flight.MekanismFlightGate.matches("mekanism", "10.4.17", hash), "other version unsupported");
        require(!com.leva.foodhealing.compat.flight.MekanismFlightGate.matches("mekanism", "10.4.16", "00"), "same-version different build unsupported");
        require(!com.leva.foodhealing.compat.flight.MekanismFlightGate.matches("other", "10.4.16", hash), "other mod unsupported");
        var self = step(false, false, false, false, true, FlightAuthority.NEUTRAL);
        require(self.mayfly() && self.owns() && !self.flying(), "FH grants permission, not active flight");
        var off = step(true, true, self.owns(), false, false, FlightAuthority.NEUTRAL);
        require(!off.mayfly() && !off.flying() && !off.owns(), "self-only revoke");
        var before = step(true, true, false, false, true, ALLOW);
        require(before.mayfly() && before.flying() && !before.owns(), "foreign-before no takeover");
        require(step(true, true, false, false, false, ALLOW).flying(), "foreign-before OFF retains");
        var after = step(true, true, true, false, false, ALLOW);
        require(after.mayfly() && after.flying() && !after.owns(), "foreign-after OFF only drops token");
        var regrant = step(false, false, false, false, true, FlightAuthority.NEUTRAL);
        require(regrant.owns(), "provider-first OFF regrant");
        require(!step(true, true, regrant.owns(), false, false, FlightAuthority.NEUTRAL).mayfly(), "subsequent self OFF");
        require(!step(false, false, false, false, false, FlightAuthority.NEUTRAL).mayfly(), "FH-first OFF leaves native removal alone");
        for (boolean eligible : new boolean[] {false, true}) {
            for (boolean owns : new boolean[] {false, true}) {
                var vanilla = step(true, true, owns, true, eligible, DENY);
                require(vanilla.mayfly() && vanilla.flying() && !vanilla.owns(), "native modes protected even under DENY");
                var unknown = step(true, true, owns, false, eligible, FlightAuthority.UNKNOWN);
                require(unknown.mayfly() && unknown.flying() && unknown.owns() == owns, "query failure is non-destructive");
            }
        }
        require(!step(false, false, false, false, true, DENY).mayfly(), "DENY blocks grant");
        require(!step(true, true, true, false, true, DENY).mayfly(), "DENY ends self-only grant");
        require(step(true, true, false, false, true, DENY).mayfly(), "DENY does not let FH revoke foreign-before");
        var mixed = step(true, true, true, false, true, ALLOW.and(DENY));
        require(mixed.mayfly() && mixed.flying() && !mixed.owns(), "DENY stops FH but does not override another positive provider");
        require(step(false, false, false, false, true, FlightAuthority.NEUTRAL).owns(), "DENY release reevaluates");
        ShokugiData valid = new ShokugiData(); valid.setSkillLevel(FoodHealingSkillIds.FLIGHT, 1);
        require(FlightGrantPolicy.eligible(valid), "valid fresh acquisition");
        valid.setSkillDisabled(FoodHealingSkillIds.FLIGHT, true);
        require(!FlightGrantPolicy.eligible(valid), "OFF");
        for (int level : new int[]{0, -1, 2, Integer.MAX_VALUE}) {
            ShokugiData data = new ShokugiData(); data.setSkillLevel(FoodHealingSkillIds.FLIGHT, level);
            require(!FlightGrantPolicy.eligible(data), "invalid/unowned level " + level);
        }
        for (Tag malformed : new Tag[]{FloatTag.valueOf(1.5F), DoubleTag.valueOf(1D), LongTag.valueOf(1),
                StringTag.valueOf("1"), IntTag.valueOf(0), IntTag.valueOf(-1), IntTag.valueOf(2), new CompoundTag()}) {
            CompoundTag nbt = new ShokugiData().serializeNBT();
            CompoundTag entry = new CompoundTag(); entry.putString("Id", FoodHealingSkillIds.FLIGHT); entry.put("Level", malformed);
            ListTag list = new ListTag(); list.add(entry); nbt.put("AcquiredSkills", list);
            ShokugiData data = new ShokugiData(); data.deserializeNBT(nbt);
            for (int round = 0; round < 2; round++) {
                require(!FlightGrantPolicy.eligible(data), "malformed must not become Lv1 after save/clone");
                require(data.serializeNBT().getList("AcquiredSkills", Tag.TAG_COMPOUND).equals(list), "retain malformed raw entry");
                require(FoodHealingSkills.purchaseStatus(data, FoodHealingSkillIds.FLIGHT, data.getSkillLevel(FoodHealingSkillIds.FLIGHT)) == FoodHealingSkills.PurchaseResult.STALE_REQUEST, "no purchase repair");
                ShokugiData copy = new ShokugiData(); copy.copyFrom(data); data = copy;
            }
        }
        CompoundTag duplicate = new ShokugiData().serializeNBT();
        CompoundTag entry = new CompoundTag(); entry.putString("Id", FoodHealingSkillIds.FLIGHT); entry.putInt("Level", 1);
        ListTag twice = new ListTag(); twice.add(entry); twice.add(entry.copy()); duplicate.put("AcquiredSkills", twice);
        ShokugiData data = new ShokugiData(); data.deserializeNBT(duplicate);
        require(!FlightGrantPolicy.eligible(data) && data.serializeNBT().getList("AcquiredSkills", Tag.TAG_COMPOUND).size() == 2, "duplicate retained and denied");
        for (int schema : new int[]{5, 6}) {
            CompoundTag nbt = new ShokugiData().serializeNBT(); nbt.putInt("FoodHealingDataVersion", schema);
            nbt.putBoolean("LegacyMigrationPending", true); nbt.put("AcquiredSkills", twice);
            data.deserializeNBT(nbt); require(!FlightGrantPolicy.eligible(data), "pending/future denied");
        }
        System.out.println("FlightRegression PASS assertions=" + assertions);
    }
    private static FlightGrantPolicy.Decision step(boolean mayfly, boolean flying, boolean owns, boolean vanilla, boolean eligible, FlightAuthority provider) {
        return FlightGrantPolicy.evaluate(mayfly, flying, owns, vanilla, eligible, provider);
    }
    private static void require(boolean value, String label) { assertions++; if (!value) throw new AssertionError(label); }
}
