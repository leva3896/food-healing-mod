package com.leva.foodhealing;

import com.leva.foodhealing.capability.ShokugiData;
import com.leva.foodhealing.compat.l2hostility.L2HostilityVersions;

final class TruthMasteryRegression {
    private static int assertions;
    private static final String[] IDS = {FoodHealingSkillIds.PURIFICATION,
            FoodHealingSkillIds.PURIFICATION_MASTERY, FoodHealingSkillIds.TRUTH_MASTERY};
    static void run() {
        for (int mask = 0; mask < 8; mask++) {
            var data = fresh();
            for (int i=0;i<3;i++) data.setSkillDisabled(IDS[i], (mask & (1<<i)) != 0);
            var before = data.serializeNBT();
            check(TruthMasteryController.isEnabled(data) == (mask == 0), "toggle combination " + mask);
            check(before.equals(data.serializeNBT()), "eligibility must be read only");
        }
        for (String id : IDS) for (int rank : new int[]{0,2}) {
            var data=fresh(); data.setSkillLevel(id, rank);
            check(!TruthMasteryController.isEnabled(data), "missing/invalid acquired level " + id);
        }
        for (String boundary : new String[]{"pending","future-schema"}) {
            var data=fresh();var nbt=data.serializeNBT();
            if (boundary.equals("pending")) nbt.putBoolean("LegacyMigrationPending",true);
            else nbt.putInt("FoodHealingDataVersion",99);
            data.deserializeNBT(nbt);
            check(!TruthMasteryController.isEnabled(data), boundary);
        }
        check(!TruthMasteryController.isEnabled(null), "null ownership");
        for (int axis=0;axis<3;axis++) for (double sign : new double[]{-1,1}) {
            double[] pos={100,-20,300};pos[axis]+=sign*75;
            check(TruthMasteryController.contains(100,-20,300,pos[0],pos[1],pos[2]), "inclusive boundary");
            pos[axis]+=sign*0.00001;
            check(!TruthMasteryController.contains(100,-20,300,pos[0],pos[1],pos[2]), "outside boundary");
        }
        check(!TruthMasteryController.contains(0,0,0,Double.NaN,0,0), "nonfinite position");
        check(!TruthMasteryController.contains(0,0,0,0,Double.POSITIVE_INFINITY,0), "infinite position");
        check(L2HostilityVersions.supports("2.5.19","2.5.3","2.6.1","0.4.4"), "reviewed combination");
        String[] valid={"2.5.19","2.5.3","2.6.1","0.4.4"};
        for (int i=0;i<4;i++) for (String unsupported:new String[]{"",null,"unreviewed"}) {
            String[] values=valid.clone();values[i]=unsupported;
            check(!L2HostilityVersions.supports(values[0],values[1],values[2],values[3]), "unsupported version");
        }
        check(!L2HostilityVersions.supports("2.5.19","2.5.3","2.6.1","0.4.3"), "old Tracker candidate");
        System.out.println("Truth mastery eligibility/range/version assertions=" + assertions);
    }
    private static ShokugiData fresh() {
        var data=new ShokugiData();for(String id:IDS)data.setSkillLevel(id,1);return data;
    }
    private static void check(boolean condition,String message) {
        assertions++;if(!condition)throw new AssertionError(message);
    }
}
