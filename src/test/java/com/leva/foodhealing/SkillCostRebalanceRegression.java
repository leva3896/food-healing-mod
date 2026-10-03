package com.leva.foodhealing;

import com.leva.foodhealing.capability.ShokugiData;
import net.minecraft.nbt.CompoundTag;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

final class SkillCostRebalanceRegression {
    private static int assertions;
    static void run() {
        String[] ids = {"fire_resistance","water_night_vision","fast_eating","acrobatics","flame_blessing",
                "explosion_resistance","purification","food_production_mastery","slaughter","satisfaction",
                "quarrying","gathering","unbreaking","armor_mastery","flight","kongo","immovable_mastery","pursuit",
                "guts","true_guts","heroics","true_heroics","break_realm_mastery","purification_mastery","truth_mastery","tacz_ammo_conservation"};
        long[][] costs = {{1},{1},{10},{1},{2},{10},{3},{4},{5},{5,5,5},{1},{5,5,5},{10,20,30},{20},{2},{50},{50},
                {100,100,100,100,100,100,100,100,100},{10,10,10,10,10},{20},{30,30,30,30,30},{100},{20},{100},{500},
                {50,50,50,50,50,50,50,50,50,50}};
        long[] groups = new long[5];
        for (int k=0;k<ids.length;k++) {
            String id=FoodHealingSkillIds.id(ids[k]);var def=FoodHealingSkills.definitions().get(id);
            check(def!=null && def.maxLevel()==costs[k].length && Arrays.equals(def.levelCosts(), costs[k]), "exact definition "+id);
            groups[k<18?0:k<20?1:k<22?2:k<25?3:4]+=Arrays.stream(def.levelCosts()).sum();
            if (id.equals(FoodHealingSkillIds.BREAK_REALM_MASTERY) || id.equals(FoodHealingSkillIds.TACZ_AMMO_CONSERVATION)) continue;
            ShokugiData data=new ShokugiData();
            for(var req:def.requirements())data.setSkillLevel(req.skillId(), req.level());
            long spent=0;
            for(int level=0;level<costs[k].length;level++) {
                long cost=costs[k][level];data.setUnspentSkillPoints(cost-1);
                rejected(data,id,level,FoodHealingSkills.PurchaseResult.INSUFFICIENT_SP);
                data.setUnspentSkillPoints(cost);rejected(data,id,level+1,FoodHealingSkills.PurchaseResult.STALE_REQUEST);
                check(FoodHealingSkills.tryPurchase(data,id,level)==FoodHealingSkills.PurchaseResult.SUCCESS,"sequential price");
                spent+=cost;check(data.getSpentSkillPoints()==spent && data.getUnspentSkillPoints()==0,"exact accounting");
                check(data.isSkillDisabled(id)==(level>0),"default ON / OFF upgrade");
                rejected(data,id,level,FoodHealingSkills.PurchaseResult.STALE_REQUEST);data.setSkillDisabled(id,true);
                CompoundTag saved=data.serializeNBT();data=load(saved);check(data.serializeNBT().equals(saved),"normal reload");
            }
        }
        check(Arrays.equals(groups,new long[]{1150,70,250,620,500}),"group totals");
        check(Arrays.stream(groups).sum()==2590,"finite total excludes repeatable");
        for(String id:new String[]{FoodHealingSkillIds.QUARRYING,FoodHealingSkillIds.IMMOVABLE_MASTERY}) {
            long cost=FoodHealingSkills.definitions().get(id).levelCosts()[0];
            ShokugiData data=new ShokugiData();data.setUnspentSkillPoints(Long.MAX_VALUE);
            data.setSpentSkillPoints(Long.MAX_VALUE-cost+1);rejected(data,id,0,FoodHealingSkills.PurchaseResult.INSUFFICIENT_SP);
            data.setSpentSkillPoints(0);check(FoodHealingSkills.tryPurchase(data,id,0)==FoodHealingSkills.PurchaseResult.SUCCESS
                    && data.getUnspentSkillPoints()==Long.MAX_VALUE-cost,"large SP no narrowing");
            check(FoodHealingSkills.isCanonicalSkillEnabled(data,id),"fresh enabled");
            for(int schema:new int[]{-1,6,Integer.MAX_VALUE}) {
                CompoundTag raw=data.serializeNBT();raw.putInt("FoodHealingDataVersion",schema);ShokugiData bad=load(raw);
                rejected(bad,id,0,FoodHealingSkills.PurchaseResult.LEGACY_MIGRATION_PENDING);
                check(!FoodHealingSkills.isCanonicalSkillEnabled(bad,id),"future has no effect");
            }
            CompoundTag raw=data.serializeNBT();raw.putBoolean("LegacyMigrationPending",true);
            check(!FoodHealingSkills.isCanonicalSkillEnabled(load(raw),id),"pending has no effect");
            ShokugiData malformed=new ShokugiData();malformed.setUnspentSkillPoints(1000);malformed.setSkillLevel("foodhealing:unknown",1);
            rejected(malformed,id,0,FoodHealingSkills.PurchaseResult.STALE_REQUEST);
            rejected(new ShokugiData(),"%%%",0,FoodHealingSkills.PurchaseResult.UNKNOWN_SKILL);
        }
        Map<String,Long> stats=new LinkedHashMap<>();
        stats.put(FoodHealingBaseStatIds.BASE_DEFENSE,5L);stats.put(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR,10L);
        stats.put(FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_LINEAR,10L);stats.put(FoodHealingBaseStatIds.BASE_MAX_HEALTH,5L);
        stats.put(FoodHealingBaseStatIds.RECOVERY_MULTIPLIER,20L);stats.put(FoodHealingBaseStatIds.BASE_OUTGOING_DAMAGE,20L);
        stats.put(FoodHealingBaseStatIds.TACZ_BASE_OUTGOING_DAMAGE,2L);
        for(var e:stats.entrySet()) {
            String id=e.getKey();long cost=e.getValue();check(FoodHealingBaseStats.cost(id)==cost,"repeatable definition");
            if(id.equals(FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_LINEAR))continue; // native mod gate in dedicated focused run
            ShokugiData data=new ShokugiData();
            for(int n=0;n<3;n++) {
                data.setUnspentSkillPoints(cost-1);CompoundTag old=data.serializeNBT();
                check(FoodHealingBaseStats.tryPurchase(data,id,n, modId -> modId.equals("tacz"))==FoodHealingBaseStats.PurchaseResult.INSUFFICIENT_SP && data.serializeNBT().equals(old),"insufficient base unchanged");
                data.setUnspentSkillPoints(cost);check(FoodHealingBaseStats.tryPurchase(data,id,n, modId -> modId.equals("tacz"))==FoodHealingBaseStats.PurchaseResult.SUCCESS,"base repeated");
                check(data.getSpentSkillPoints()==(n+1)*cost && data.getUnspentSkillPoints()==0 && FoodHealingBaseStats.purchaseCount(data,id)==n+1,"base count/accounting");
            }
            data.setUnspentSkillPoints(Long.MAX_VALUE);data.setSpentSkillPoints(Long.MAX_VALUE-cost+1);CompoundTag old=data.serializeNBT();
            check(FoodHealingBaseStats.tryPurchase(data,id,3, modId -> modId.equals("tacz"))==FoodHealingBaseStats.PurchaseResult.INSUFFICIENT_SP && old.equals(data.serializeNBT()),"base overflow unchanged");
        }
        ShokugiData old=new ShokugiData();old.setSpentSkillPoints(8);old.setUnspentSkillPoints(123);
        old.setSkillLevel(FoodHealingSkillIds.FAST_EATING,1);old.setSkillDisabled(FoodHealingSkillIds.FAST_EATING,true);
        old.setBaseStatPoints(FoodHealingBaseStatIds.BASE_DEFENSE,7);CompoundTag saved=old.serializeNBT();
        ShokugiData read=load(saved);check(read.serializeNBT().equals(saved),"existing owner not repriced");
        check(FoodHealingBaseStats.tryPurchase(read,FoodHealingBaseStatIds.BASE_DEFENSE,7)==FoodHealingBaseStats.PurchaseResult.SUCCESS
                && read.getSpentSkillPoints()==13 && read.getUnspentSkillPoints()==118 && read.getBaseStatPoints(FoodHealingBaseStatIds.BASE_DEFENSE)==8,"prospective base price only");
        double[] rolls={0,Math.nextDown(.005D),.005D,Math.nextDown(.01D),.01D,.5D,Double.NaN,-1,Double.POSITIVE_INFINITY};
        int[] expected={1,1,2,2,0,0,0,0,0};
        for(int i=0;i<rolls.length;i++)check(QuarryingController.selectBonus(rolls[i])==expected[i],"exclusive double RNG boundary");
        System.out.println("SkillCostRebalanceRegression PASS assertions="+assertions+" groups=1150/70/250/620/500 total=2590");
    }
    private static ShokugiData load(CompoundTag n) {ShokugiData d=new ShokugiData();d.deserializeNBT(n);return d;}
    private static void rejected(ShokugiData d,String id,int lv,FoodHealingSkills.PurchaseResult result) {
        CompoundTag before=d.serializeNBT();check(FoodHealingSkills.tryPurchase(d,id,lv)==result,"refusal "+id+" "+result);
        check(before.equals(d.serializeNBT()),"refusal mutated data");
    }
    private static void check(boolean value,String message) {assertions++;if(!value)throw new AssertionError(message);}
}
