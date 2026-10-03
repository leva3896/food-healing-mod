package com.leva.foodhealing;

import com.leva.foodhealing.capability.ShokugiData;
import net.minecraft.nbt.CompoundTag;
import com.electronwill.nightconfig.toml.TomlParser;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.StringReader;

final class NutritionCountRegression {
    private static int checks;
    static void run() {
        arithmetic();
        migrations();
        malformedSchema4();
        config();
        System.out.println("NUTRITION_COUNT_UNIT_PASS assertions="+checks);
    }
    private static void arithmetic() {
        for (long[] x:new long[][]{{0,1,0,1},{1,4,0,5},{1999,1,1,0},{1995,8,1,3},{1990,4010,3,0},{0,4000,2,0}}) {
            var u=NutritionProgress.add(x[0],7,9,x[1],2000);
            check(u.gained()==x[2]&&u.count()==x[3]&&u.level()==7+x[2]&&u.unspent()==9+x[2],"crossing");
        }
        var large=NutritionProgress.add(0,0,0,Long.MAX_VALUE,2000);
        check(large.gained()==Long.MAX_VALUE/2000&&large.count()==Long.MAX_VALUE%2000,"long");
        for (long[] x:new long[][]{{-1,0,0,1,2000},{2000,0,0,1,2000},{0,0,0,0,2000},{0,0,0,-1,2000},{0,0,0,1,0},
                {1999,Long.MAX_VALUE,0,1,2000},{1999,0,Long.MAX_VALUE,1,2000},{1,0,0,Long.MAX_VALUE,2000}}) {
            boolean rejected=false;
            try { NutritionProgress.add(x[0],x[1],x[2],x[3],x[4]); } catch (IllegalArgumentException|ArithmeticException expected) { rejected=true; }
            check(rejected,"reject invalid arithmetic");
        }
        for (boolean levelOverflow:new boolean[]{true,false}) {
            ShokugiData d=new ShokugiData();d.setEatCount(1999);d.setLevel(levelOverflow?Long.MAX_VALUE:1);d.setUnspentSkillPoints(levelOverflow?1:Long.MAX_VALUE);d.setSpentSkillPoints(50);
            CompoundTag before=d.serializeNBT();
            check(!d.addNutritionUnits(1,2000)&&before.equals(d.serializeNBT()),"atomic progression rejection");
        }
    }
    private static void migrations() {
        for(long[] x:new long[][]{{0,200,2000,0},{1,200,2000,10},{50,200,2000,500},{100,200,2000,1000},{199,200,2000,1990},{125,250,2500,1250},{300,400,4000,3000},{1,200,1001,5}})
            check(NutritionProgress.convert(x[0],x[1],x[2])==x[3],"proportional math");
        for(long count:new long[]{0,1,50,100,199}) {
            ShokugiData d=new ShokugiData();d.setLevel(17);d.setEatCount(count);d.setUnspentSkillPoints(3);d.setSpentSkillPoints(14);
            d.setSkillLevel(FoodHealingSkillIds.GUTS,5);d.setSkillDisabled(FoodHealingSkillIds.GUTS,true);d.setRootReservedNutrition(18);d.setRootActiveUntil(555);
            CompoundTag raw=d.serializeNBT();raw.putInt("FoodHealingDataVersion",4);
            d.deserializeNBT(raw);CompoundTag expected=raw.copy();expected.putInt("FoodHealingDataVersion",5);expected.putLong("EatCount",count*10);
            check(d.serializeNBT().equals(expected),"schema4 preserves every field except count/schema");
            for(int i=0;i<3;i++){d.deserializeNBT(d.serializeNBT());check(d.serializeNBT().equals(expected),"no double conversion");}
        }
        CompoundTag v2=new CompoundTag();v2.putInt("ShokugiLevel",2);v2.putInt("EatCount",35);
        ShokugiData d=new ShokugiData();d.deserializeNBT(v2);
        check(d.getLevel()==2&&d.getEatCount()==350&&d.getUnspentSkillPoints()==2&&d.getSpentSkillPoints()==0,"real v2 shape 35->350");
        check(d.serializeNBT().getCompound("LegacyV2Backup").equals(v2),"raw35 retained");
        for (long count:new long[]{-1,200,Long.MAX_VALUE}) {
            CompoundTag raw=new ShokugiData().serializeNBT();raw.putInt("FoodHealingDataVersion",4);raw.putLong("EatCount",count);raw.putLong("UnspentSkillPoints",27);
            d=new ShokugiData();d.deserializeNBT(raw);check(d.isLegacyMigrationPending()&&d.getUnspentSkillPoints()==27,"bad old partial pending");
            check(d.serializeNBT().getCompound("CountMigrationInput").equals(raw),"bad raw exact preservation");
            CompoundTag saved=d.serializeNBT();d.deserializeNBT(saved);check(d.serializeNBT().equals(saved),"pending reload stable");
        }
    }
    private static void malformedSchema4() {
        for (int bad=0;bad<3;bad++) {
            CompoundTag raw=new ShokugiData().serializeNBT();raw.putInt("FoodHealingDataVersion",4);
            raw.putLong("UnspentSkillPoints",27);
            if(bad==0)raw.putString("EatCount","35");
            if(bad==1)raw.putDouble("EatCount",35.5);
            if(bad==2){raw.putLong("EatCount",35);raw.putBoolean("LegacyMigrationPending",true);}
            ShokugiData d=new ShokugiData();d.deserializeNBT(raw);
            check(d.isLegacyMigrationPending()&&d.getUnspentSkillPoints()==27,"type/provenance pending without SP change");
            check(d.serializeNBT().getCompound("CountMigrationInput").equals(raw),"typed raw preservation");
            CompoundTag saved=d.serializeNBT();check(!d.addNutritionUnits(2000,2000)&&saved.equals(d.serializeNBT()),"pending progression atomic reject");
        }
    }
    private static void config() {
        try {
            Path root=Files.createTempDirectory("foodhealing-count-config-");
            for(long old:new long[]{100,200,250,400}) {
                Path file=root.resolve("old"+old+".toml");Files.writeString(file,"[general]\nshokugiLevelUpRequirement="+old+"\n");
                NutritionConfigMigration.prepare(file);
                var parsed=new TomlParser().parse(new StringReader(Files.readString(file)));
                check(((Number)parsed.get(NutritionConfigMigration.THRESHOLD)).longValue()==old*10,"old custom x10");
                check(((Number)parsed.get(NutritionConfigMigration.LEGACY_INPUT)).longValue()==old,"legacy provenance");
                String text=Files.readString(file);NutritionConfigMigration.prepare(file);NutritionConfigMigration.prepare(file);check(text.equals(Files.readString(file)),"config restart idempotence");
                // Actual Forge correction follows migration, so explicit-vs-default is decided earlier.
                FoodHealingConfig.COMMON_SPEC.setConfig(parsed);
                check(FoodHealingConfig.nutritionThreshold()==old*10,"Forge loaded migrated value");
                ShokugiData d=new ShokugiData();CompoundTag raw=new CompoundTag();raw.putLong("ShokugiLevel",2);raw.putLong("EatCount",old/2);d.deserializeNBT(raw);
                check(d.getEatCount()==old*5&&d.getUnspentSkillPoints()==2,"custom config/player migration");
            }
            Path explicit=root.resolve("explicit.toml");Files.writeString(explicit,"[general]\nshokugiLevelUpRequirement=250\nshokugiNutritionLevelUpRequirement=4000\n");NutritionConfigMigration.prepare(explicit);
            var cfg=new TomlParser().parse(new StringReader(Files.readString(explicit)));FoodHealingConfig.COMMON_SPEC.setConfig(cfg);
            check(FoodHealingConfig.nutritionThreshold()==4000,"explicit new takes precedence");
            CompoundTag raw=new CompoundTag();raw.putInt("ShokugiLevel",2);raw.putInt("EatCount",125);ShokugiData d=new ShokugiData();d.deserializeNBT(raw);check(d.getEatCount()==2000&&d.getUnspentSkillPoints()==2,"explicit ratio no extra SP");
            for (String bad:new String[]{"-1","0","\"bad\"","1.5","9223372036854775807"}) {
                Path file=root.resolve("bad"+checks+".toml");String original="[general]\nshokugiLevelUpRequirement="+bad+"\n";Files.writeString(file,original);boolean rejected=false;
                try{NutritionConfigMigration.prepare(file);}catch(IllegalArgumentException expected){rejected=true;}
                check(rejected&&Files.readString(file).equals(original),"invalid config no correction");
            }
            Path fresh=root.resolve("fresh.toml");NutritionConfigMigration.prepare(fresh);
            FoodHealingConfig.COMMON_SPEC.setConfig(new TomlParser().parse(new StringReader(Files.readString(fresh))));
            check(FoodHealingConfig.nutritionThreshold()==2000&&FoodHealingConfig.legacyCountThreshold()==200,"fresh defaults restored");
        } catch(java.io.IOException e){throw new AssertionError(e);}
    }
    private static void check(boolean yes,String what){checks++;if(!yes)throw new AssertionError(what);}
}
