package com.leva.foodhealing.verification;

import com.google.gson.GsonBuilder;
import com.leva.foodhealing.*;
import com.leva.foodhealing.capability.*;
import com.leva.foodhealing.compat.l2hostility.*;
import com.leva.foodhealing.network.*;
import dev.xkmc.l2hostility.content.capability.mob.*;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import top.theillusivec4.curios.api.CuriosApi;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.ReferenceCountUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.effect.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/** Separate real-MOD fixture. Artificial acquisition is not product purchase evidence. */
@Mod("foodhealing_l2_verification")
public final class L2HostilityVerification {
    private static final String P=FoodHealingSkillIds.PURIFICATION, M=FoodHealingSkillIds.PURIFICATION_MASTERY,
            T=FoodHealingSkillIds.TRUTH_MASTERY;
    private static final UUID FOREIGN=UUID.fromString("ad36768f-9b7a-44c3-9e6c-f30b7f538c88");
    private final List<Map<String,Object>> results=new ArrayList<>();
    private final Map<UUID,List<Float>> numeric=new HashMap<>();
    private final Map<UUID,Map<MobEffect,Integer>> additions=new HashMap<>();
    private final Map<UUID,List<String>> sources=new HashMap<>();
    private final Map<String,Object> receipt=new LinkedHashMap<>();
    private MinecraftServer server; private ServerLevel level; private Path root;
    private String running="startup"; private boolean ended; private int waitTicks; private int stage;
    private UUID savedMob; private CompoundTag savedMarker, savedCap; private float savedHealth;
    private Entity beforeUnload;
    public L2HostilityVerification() { MinecraftForge.EVENT_BUS.register(this); }

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void numeric(LivingHurtEvent e) {
        var list=numeric.get(e.getEntity().getUUID());if(list!=null){list.add(e.getAmount());sources.get(e.getEntity().getUUID()).add(e.getSource().getMsgId());}
    }
    @SubscribeEvent public void added(MobEffectEvent.Added e) {
        additions.computeIfAbsent(e.getEntity().getUUID(),id->new HashMap<>()).merge(e.getEffectInstance().getEffect(),1,Integer::sum);
    }
    private int addedCount(LivingEntity target,MobEffect effect){return additions.getOrDefault(target.getUUID(),Map.of()).getOrDefault(effect,0);}
    @SubscribeEvent public void start(ServerStartedEvent event) throws Exception {
        server=event.getServer();level=server.overworld();root=server.getServerDirectory().toPath().toRealPath();
        require(root.equals(Path.of(System.getProperty("foodhealing.l2.verificationRoot")).toRealPath())
                &&server.isDedicatedServer(),"wrong verification root/server");
        receipt.put("pid",ProcessHandle.current().pid());receipt.put("mode",System.getProperty("foodhealing.l2.mode","startup"));
        receipt.put("loadedMods",ModList.get().getMods().stream().map(m->Map.of("id",m.getModId(),"version",
                m.getVersion().toString(),"file",m.getOwningFile().getFile().getFilePath().toString())).toList());
        try {
            require(L2HostilityCompatibility.available(),"unreviewed loaded versions");
            var librarySources=new LinkedHashMap<String,String>();
            for(String name:new String[]{"com/tterrag/registrate/Registrate.class","dev/xkmc/l2serial/serialization/codec/TagCodec.class",
                    "dev/xkmc/l2modularblock/impl/PowerBlockMethodImpl.class"})
                librarySources.put(name,String.valueOf(getClass().getClassLoader().getResource(name)));
            receipt.put("runtimeLibraryResources",librarySources);
            level.getGameRules().getRule(GameRules.RULE_DOMOBSPAWNING).set(false,server);
            level.getGameRules().getRule(GameRules.RULE_NATURAL_REGENERATION).set(false,server);
            level.setDayTime(18000);level.getChunk(0,0);
            if(System.getProperty("foodhealing.l2.mode","startup").equals("startup")) {
                Zombie z=new Zombie(EntityType.ZOMBIE,level);require(cap(z) instanceof L2MobTraitAccess,"Mixin absent");
                record("startup",Map.of("scope","actual loader and class linkage"));finish(null);return;
            }
            if(System.getProperty("foodhealing.l2.mode").equals("reflection")){reflection();finish(null);return;}
            if(!System.getProperty("foodhealing.l2.mode").equals("truth")){purification(); equipmentVariants(); existingAndOtherEffects(); addedPotionPreservation(); ordinaryPurificationControl(); reflection();}
            purchaseGate(); truthToggles(); truthInvalid(); boundaries(); spawnEntry(); levelScaling();
            reapplication(); iterationSafety(); syncAndPlayerPersistence(); prepareChunkPersistence();
        } catch(Throwable error) {finish(error);}
    }
    private MobTrait[] traits(){return new MobTrait[]{LHTraits.POISON.get(),LHTraits.SLOWNESS.get(),LHTraits.CORROSION.get(),LHTraits.EROSION.get(),LHTraits.WEAKNESS.get(),LHTraits.WITHER.get()};}
    private MobTraitCap cap(Mob mob){return mob.getCapability(MobTraitCap.CAPABILITY).orElseThrow(AssertionError::new);}
    private CompoundTag payload(Mob mob){return TagCodec.toTag(new CompoundTag(),cap(mob));}
    private CompoundTag stable(Mob mob){CompoundTag n=payload(mob);n.remove("traits");return n;}
    private Zombie mob(double x,double y,double z) {
        level.getChunk((int)Math.floor(x/16),(int)Math.floor(z/16));
        Zombie mob=new Zombie(EntityType.ZOMBIE,level);mob.setNoAi(true);mob.setNoGravity(true);
        mob.setPersistenceRequired();mob.moveTo(x,y,z,0,0);level.addFreshEntity(mob);
        cap(mob).reinit(mob,1,false);cap(mob).tick(mob);
        require(cap(mob).isInitialized(),"native initialization incomplete");
        require(cap(mob).traits.isEmpty(),"native level1 baseline has unexpected traits "+cap(mob).traits);
        mob.getPersistentData().putString("verification:foreign","retained");
        mob.getAttribute(Attributes.FOLLOW_RANGE).addPermanentModifier(new AttributeModifier(FOREIGN,"verification foreign",3,AttributeModifier.Operation.ADDITION));
        return mob;
    }
    private void assign(Mob mob,MobTrait trait,int rank) {
        cap(mob).setTrait(trait,rank);cap(mob).tick(mob);
    }
    private void supported(Mob mob,int rank) {for(MobTrait t:traits())cap(mob).setTrait(t,rank);cap(mob).tick(mob);}
    private ItemStack sword(int damage){ItemStack s=new ItemStack(Items.DIAMOND_SWORD);s.setDamageValue(damage);return s;}
    private float attack(Zombie mob,Fixture target) {
        target.player.invulnerableTime=0;mob.getRandom().setSeed(9417L);
        numeric.put(target.id,new ArrayList<>());sources.put(target.id,new ArrayList<>());additions.remove(target.id);float before=target.player.getHealth();
        require(!target.player.isInvulnerableTo(mob.damageSources().mobAttack(mob)),"fixture player still has native login immunity");
        require(mob.doHurtTarget(target.player),"native Zombie.doHurtTarget failed: damage="+mob.getAttributeValue(Attributes.ATTACK_DAMAGE)+" targetHP="+before);
        require(numeric.get(target.id).size()==1,"numeric event count "+numeric.get(target.id));
        require(sources.get(target.id).equals(List.of("mob")),"unexpected native damage source "+sources.get(target.id));
        require(target.player.getHealth()<before &&target.player.isAlive(),"ordinary numeric damage did not pass safely");
        return before-target.player.getHealth();
    }
    private boolean isPotion(MobTrait trait){return trait==LHTraits.POISON.get()||trait==LHTraits.SLOWNESS.get()||trait==LHTraits.WEAKNESS.get()||trait==LHTraits.WITHER.get();}
    private MobEffect effect(MobTrait trait){if(trait==LHTraits.POISON.get())return MobEffects.POISON;
        if(trait==LHTraits.SLOWNESS.get())return MobEffects.MOVEMENT_SLOWDOWN;
        if(trait==LHTraits.WEAKNESS.get())return MobEffects.WEAKNESS;
        if(trait==LHTraits.WITHER.get())return MobEffects.WITHER;
        throw new AssertionError("not a potion trait");}
    private void purification() {
        for(MobTrait trait:traits()) {
            Float protectedDamage=null;
            for(String state:new String[]{"on","parent-off","mastery-off","both-off","unowned-parent","unowned-mastery","overlevel","pending","future-schema"}) {
                running="purification/"+L2HostilityAdapter.id(trait)+"/"+state;
                try(Fixture f=new Fixture()) {
                    if(state.equals("parent-off")||state.equals("both-off"))f.data.setSkillDisabled(P,true);
                    if(state.equals("mastery-off")||state.equals("both-off"))f.data.setSkillDisabled(M,true);
                    if(state.equals("unowned-parent"))f.data.setSkillLevel(P,0);
                    if(state.equals("unowned-mastery"))f.data.setSkillLevel(M,0);
                    if(state.equals("overlevel"))f.data.setSkillLevel(M,2);
                    if(state.equals("pending")||state.equals("future-schema")) {
                        var n=f.data.serializeNBT();if(state.equals("pending"))n.putBoolean("LegacyMigrationPending",true);else n.putInt("FoodHealingDataVersion",99);f.data.deserializeNBT(n);
                    }
                    f.player.setItemSlot(EquipmentSlot.MAINHAND,sword(100));
                    Zombie z=mob(2,100,0);assign(z,trait,2);require(cap(z).hasTrait(trait),"native assignment missing");
                    var source=payload(z);var canonical=f.data.serializeNBT();int initial=f.player.getMainHandItem().getDamageValue();
                    float loss=attack(z,f);boolean protectedTarget=state.equals("on");
                    if(isPotion(trait)) {
                        require(f.player.hasEffect(effect(trait))!=protectedTarget,"immediate potion addition "+running);
                        require(addedCount(f.player,effect(trait))==(protectedTarget?0:1),"native effect addition count");
                    }
                    else require(protectedTarget ? f.player.getMainHandItem().getDamageValue()==initial : f.player.getMainHandItem().getDamageValue()>initial,"equipment wear "+running);
                    require(source.equals(payload(z)),"Purification changed source traits/data");
                    require(canonical.equals(f.data.serializeNBT()),"attack changed canonical/SP/toggle");
                    if(state.equals("on"))protectedDamage=loss;
                    if(state.equals("parent-off")||state.equals("mastery-off")||state.equals("both-off"))near(loss,protectedDamage,"numeric damage changed with protection");
                    record(running,Map.of("hpLoss",loss,"numeric",numeric.get(f.id),"immediateEffect",isPotion(trait)&&f.player.hasEffect(effect(trait)),
                            "wear",f.player.getMainHandItem().getDamageValue()-initial,"sourceUnchanged",true,"canonicalUnchanged",true,"source",sources.get(f.id),"effectAddedEvents",isPotion(trait)?addedCount(f.player,effect(trait)):0));z.discard();
                }
            }
        }
    }
    private void equipmentVariants() {
        for(MobTrait trait:new MobTrait[]{LHTraits.CORROSION.get(),LHTraits.EROSION.get()}) {
            for(String setup:new String[]{"none","fresh","damaged","two-slots","unbreakable"}) {
                float[] losses=new float[2];long[] nextRandom=new long[2];
                for(int protectedIndex=0;protectedIndex<2;protectedIndex++)try(Fixture f=new Fixture()) {
                    boolean enabled=protectedIndex==1;f.data.setSkillDisabled(M,!enabled);
                    if(!setup.equals("none"))f.player.setItemSlot(EquipmentSlot.MAINHAND,sword(setup.equals("fresh")?0:50));
                    if(setup.equals("two-slots"))f.player.setItemSlot(EquipmentSlot.OFFHAND,sword(100));
                    if(setup.equals("unbreakable"))f.player.getMainHandItem().getOrCreateTag().putBoolean("Unbreakable",true);
                    int before=f.player.getMainHandItem().getDamageValue()+f.player.getOffhandItem().getDamageValue();
                    Zombie z=mob(2,100,0);assign(z,trait,2);running="equipment/"+L2HostilityAdapter.id(trait)+"/"+setup+"/"+enabled;
                    losses[protectedIndex]=attack(z,f);nextRandom[protectedIndex]=z.getRandom().nextLong();
                    int wear=f.player.getMainHandItem().getDamageValue()+f.player.getOffhandItem().getDamageValue()-before;
                    if(enabled)require(wear==0,"protected wear");
                    record(running,Map.of("hpLoss",losses[protectedIndex],"wear",wear,"nextRandom",nextRandom[protectedIndex]));z.discard();
                }
                near(losses[0],losses[1],"selection/count numeric branch differs");require(nextRandom[0]==nextRandom[1],"selection RNG advanced differently");
            }
        }
    }
    private void existingAndOtherEffects() {
        try(Fixture protectedPlayer=new Fixture();Fixture other=new Fixture()) {
            other.data.setSkillLevel(P,0);other.data.setSkillLevel(M,0);
            protectedPlayer.player.addEffect(new MobEffectInstance(MobEffects.POISON,400,4));
            protectedPlayer.player.addEffect(new MobEffectInstance(MobEffects.REGENERATION,400,0));
            Zombie z=mob(2,100,0);assign(z,LHTraits.POISON.get(),1);
            attack(z,protectedPlayer);require(protectedPlayer.player.getEffect(MobEffects.POISON).getDuration()==400,"existing poison treated");
            require(protectedPlayer.player.hasEffect(MobEffects.REGENERATION),"beneficial effect lost");
            attack(z,other);require(other.player.hasEffect(MobEffects.POISON),"other player wrongly protected");
            assign(z,LHTraits.LEVITATION.get(),1);attack(z,protectedPlayer);
            require(protectedPlayer.player.hasEffect(MobEffects.LEVITATION),"out-of-scope trait canceled");
            record("existing-effects-and-other-player",Map.of("priorPoisonDuration",400,"otherPlayerPoison",true,"levitationPreserved",true));z.discard();
        }
    }
    private void addedPotionPreservation() {
        for(MobTrait trait:new MobTrait[]{LHTraits.WEAKNESS.get(),LHTraits.WITHER.get()})
            for(boolean stronger:new boolean[]{false,true})try(Fixture protectedPlayer=new Fixture();Fixture other=new Fixture()) {
                running="new-potion/preservation/"+L2HostilityAdapter.id(trait)+"/"+stronger;
                unprotected(other);
                protectedPlayer.player.addEffect(new MobEffectInstance(effect(trait),stronger?400:40,stronger?4:0));
                protectedPlayer.player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS,333,0));
                protectedPlayer.player.addEffect(new MobEffectInstance(MobEffects.REGENERATION,444,0));
                Map<MobEffect,CompoundTag> before=new HashMap<>();
                for(MobEffectInstance e:protectedPlayer.player.getActiveEffects())before.put(e.getEffect(),e.save(new CompoundTag()));
                Zombie z=mob(2,100,0);assign(z,trait,2);var source=payload(z);
                var attributes=z.getAttributes().save().copy();var canonical=protectedPlayer.data.serializeNBT();
                float guarded=attack(z,protectedPlayer);
                require(addedCount(protectedPlayer.player,effect(trait))==0,"new or hidden effect added over existing effect");
                require(protectedPlayer.player.getActiveEffects().size()==before.size(),"existing effect set changed");
                before.forEach((effect,nbt)->require(nbt.equals(protectedPlayer.player.getEffect(effect).save(new CompoundTag())),"existing effect changed"));
                float nativeLoss=attack(z,other);
                require(other.player.hasEffect(effect(trait))&&addedCount(other.player,effect(trait))==1,"other player native effect count");
                near(guarded,nativeLoss,"ordinary numeric damage differs");
                require(canonical.equals(protectedPlayer.data.serializeNBT())&&source.equals(payload(z))&&attributes.equals(z.getAttributes().save()),"source or canonical changed");
                record(running,Map.of("hpLoss",guarded,"otherPlayerHpLoss",nativeLoss,"otherPlayerAddCount",1,"protectedAddCount",0,
                        "existingHarmfulAndBeneficialUnchanged",true,"sourceAttributesUnchanged",true));z.discard();
            }
    }
    private void ordinaryPurificationControl() {
        for(MobTrait trait:new MobTrait[]{LHTraits.WEAKNESS.get(),LHTraits.WITHER.get()})try(Fixture f=new Fixture()) {
            running="new-potion/ordinary-purification-after-add/"+L2HostilityAdapter.id(trait);
            f.data.setSkillDisabled(M,true);Zombie z=mob(2,100,0);assign(z,trait,1);
            float loss=attack(z,f);
            require(f.player.hasEffect(effect(trait))&&addedCount(f.player,effect(trait))==1,"native addition was not observed before ordinary tick");
            MinecraftForge.EVENT_BUS.post(new TickEvent.PlayerTickEvent(TickEvent.Phase.END,f.player));
            require(!f.player.hasEffect(effect(trait)),"ordinary Purification did not remove effect after addition");
            require(addedCount(f.player,effect(trait))==1,"ordinary removal fabricated an addition");
            record(running,Map.of("hpLoss",loss,"beforeOrdinaryTick",true,"addCount",1,"afterOrdinaryTick",false));z.discard();
        }
    }
    private void reflection() {
        for(MobTrait trait:new MobTrait[]{LHTraits.POISON.get(),LHTraits.WEAKNESS.get(),LHTraits.WITHER.get()}) {
        running="native-reflection-recipient/"+L2HostilityAdapter.id(trait);
        try(Fixture f=new Fixture()) {
            var inventory=CuriosApi.getCuriosInventory(f.player).orElseThrow(AssertionError::new);
            ItemStack ring=new ItemStack(LHItems.RING_REFLECTION.get());
            var slot=inventory.getCurios().values().stream().map(h->h.getStacks())
                    .filter(h->h.getSlots()>0&&h.isItemValid(0,ring)).findFirst().orElseThrow(()->new AssertionError("no native ring slot "+inventory.getCurios().keySet()));
            slot.setStackInSlot(0,ring);require(LHItems.RING_REFLECTION.get().isOn(f.player),"native reflection ring inactive");
            Zombie attacker=mob(2,100,0);
            var recipient=new net.minecraft.world.entity.monster.Pillager(EntityType.PILLAGER,level);
            recipient.setNoAi(true);recipient.setNoGravity(true);recipient.moveTo(3,100,0,0,0);level.addFreshEntity(recipient);
            cap(recipient).reinit(recipient,1,false);cap(recipient).tick(recipient);
            require(recipient.canBeAffected(new MobEffectInstance(effect(trait),100,0)),"reflection recipient immune to poison");
            require(level.getEntities(f.player,f.player.getBoundingBox().inflate(16)).contains(recipient),"reflection recipient not query-visible");
            assign(attacker,trait,1);
            attack(attacker,f);require(recipient.hasEffect(effect(trait)),"Purification swallowed native reflected recipient");
            require(!f.player.hasEffect(effect(trait)),"reflection returned poison to player");
            require(addedCount(recipient,effect(trait))==1,"reflected effect not added exactly once");
            record(running,Map.of("nativeRingEquipped",true,"nativeZombieAttack",true,"reflectedEffect",effect(trait).getDescriptionId(),"addedEvents",addedCount(recipient,effect(trait))));attacker.discard();recipient.discard();
        }
    }
    }
    private void purchaseGate() {
        for(String id:new String[]{M,T})try(Fixture f=new Fixture()) {
            f.data.setSkillLevel(id,0);f.data.setUnspentSkillPoints(1000);
            var before=f.data.serializeNBT();
            require(FoodHealingSkills.purchaseStatus(f.data,id,0)==FoodHealingSkills.PurchaseResult.IMPLEMENTATION_PENDING,"readiness gate changed with L2 present");
            ICustomPacket<?> packet=(ICustomPacket<?>)PacketHandler.INSTANCE.toVanillaPacket(new PurchaseSkillPacket(id,0),NetworkDirection.PLAY_TO_SERVER);
            try{require(NetworkHooks.onCustomPayload(packet,f.connection),"purchase packet unhandled");}finally{packet.getInternalData().release();}
            require(before.equals(f.data.serializeNBT()),"denied purchase spent SP or changed ownership");
            record("purchase-block/"+id,Map.of("sp",1000,"result","IMPLEMENTATION_PENDING","canonicalUnchanged",true));
        }
    }
    private void owner(Fixture f){f.data.setSkillLevel(T,1);f.data.setSpentSkillPoints(603);}
    private void unprotected(Fixture f){f.data.setSkillLevel(P,0);f.data.setSkillLevel(M,0);f.data.setSkillLevel(T,0);}
    private void settle(Mob mob){for(int i=0;i<20;i++){mob.tickCount++;cap(mob).tick(mob);}}
    private void truthToggles() {
        for(int mask=0;mask<8;mask++)try(Fixture owner=new Fixture();Fixture target=new Fixture()) {
            owner(owner);unprotected(target);
            for(int i=0;i<3;i++)owner.data.setSkillDisabled(new String[]{P,M,T}[i],(mask&(1<<i))!=0);
            Zombie z=mob(200,100,0);supported(z,2);var preserved=stable(z);float hp=z.getHealth();
            z.setPos(2,100,0);running="truth/toggles/"+mask;
            // Attack before a periodic refresh: execution guards must work without map mutation in traitEvent.
            float loss=attack(z,target);
            for(MobTrait trait:traits())require(L2HostilityAdapter.marked(z,L2HostilityAdapter.id(trait))==(mask==0),"Truth mask eligibility");
            for(MobTrait trait:traits())if(isPotion(trait)) {
                require(target.player.hasEffect(effect(trait))==(mask!=0),"Truth action leaked or overblocked");
                require(addedCount(target.player,effect(trait))==(mask==0?0:1),"Truth native addition count");
            }
            settle(z);for(MobTrait trait:traits())require(cap(z).hasTrait(trait)==(mask!=0),"trait pruning mismatch");
            require(preserved.equals(stable(z)),"unrelated native fields changed");near(z.getHealth(),hp,"source HP rewritten");
            if(mask==0){Zombie plain=mob(200,100,0);try(Fixture plainTarget=new Fixture()){unprotected(plainTarget);near(loss,attack(plain,plainTarget),"Truth retained trait numeric bonus");}plain.discard();}
            record(running,Map.of("hpLoss",loss,"unprotectedRecipient",true,"pruned",mask==0,"otherDataUnchanged",true));z.discard();
        }
    }
    private void truthInvalid() {
        for(MobTrait trait:new MobTrait[]{LHTraits.POISON.get(),LHTraits.WEAKNESS.get(),LHTraits.WITHER.get()})
        for(String state:new String[]{"unowned-parent","unowned-mastery","unowned-truth","overlevel","pending","future-schema"})try(Fixture owner=new Fixture();Fixture target=new Fixture()) {
            owner(owner);unprotected(target);
            if(state.startsWith("unowned"))owner.data.setSkillLevel(state.equals("unowned-parent")?P:state.equals("unowned-mastery")?M:T,0);
            if(state.equals("overlevel"))owner.data.setSkillLevel(T,2);
            if(state.equals("pending")||state.equals("future-schema")){var n=owner.data.serializeNBT();if(state.equals("pending"))n.putBoolean("LegacyMigrationPending",true);else n.putInt("FoodHealingDataVersion",99);owner.data.deserializeNBT(n);}
            Zombie z=mob(2,100,0);assign(z,trait,1);settle(z);
            require(cap(z).hasTrait(trait)&&!L2HostilityAdapter.marked(z,L2HostilityAdapter.id(trait)),"invalid Truth qualified");
            float loss=attack(z,target);
            require(target.player.hasEffect(effect(trait))&&addedCount(target.player,effect(trait))==1,"invalid Truth swallowed native effect");
            record("truth/"+L2HostilityAdapter.id(trait)+"/"+state,Map.of("traitPreserved",true,"nativeEffectAddCount",1,"hpLoss",loss));z.discard();
        }
    }
    private void boundaries() {
        try(Fixture owner=new Fixture()) {
            owner(owner);
            for(int axis=0;axis<3;axis++)for(int sign:new int[]{-1,1})for(double distance:new double[]{75,75.001}) {
                double[] p={0,100,0};p[axis]+=sign*distance;Zombie z=mob(200,100,0);supported(z,1);
                running="truth/boundary/"+axis+"/"+sign+"/"+distance;
                z.teleportTo(p[0],p[1],p[2]);settle(z);boolean inside=distance==75;
                for(MobTrait trait:traits())require(!cap(z).hasTrait(trait)==inside,"AABB boundary");
                record("truth/boundary/"+axis+"/"+sign+"/"+distance,Map.of("inside",inside,"position",List.of(p[0],p[1],p[2])));z.discard();
            }
            running="truth/player-movement-overlap";
            owner.player.setPos(-100,100,0);Zombie z=mob(2,100,0);supported(z,1);owner.player.setPos(2,100,0);
            require(level.getEntitiesOfClass(Mob.class,owner.player.getBoundingBox().inflate(75)).contains(z),"movement fixture mob not query-visible");
            MinecraftForge.EVENT_BUS.post(new TickEvent.PlayerTickEvent(TickEvent.Phase.END,owner.player));
            for(MobTrait trait:traits())require(!cap(z).hasTrait(trait),"player movement scan");
            try(Fixture second=new Fixture()){owner(second);second.player.setPos(2,100,0);var before=z.getPersistentData().copy();
                MinecraftForge.EVENT_BUS.post(new TickEvent.PlayerTickEvent(TickEvent.Phase.END,second.player));require(before.equals(z.getPersistentData()),"overlapping player idempotence");}
            record("truth/player-movement-overlap",Map.of("registeredForgeEvent",true,"markerUnchangedOnOverlap",true));z.discard();
        }
    }
    private void spawnEntry() {
        running="truth/native-entity-load-and-spawn";
        try(Fixture owner=new Fixture()) {
            owner(owner);Zombie source=mob(200,100,0);supported(source,1);
            CompoundTag saved=source.saveWithoutId(new CompoundTag());source.discard();
            Zombie entering=new Zombie(EntityType.ZOMBIE,level);entering.load(saved);entering.setPos(2,100,0);
            require(cap(entering).traits.size()==traits().length,"native entity load lost source traits");
            require(level.addFreshEntity(entering),"spawn fixture registration");
            require(cap(entering).traits.isEmpty(),"spawn join did not prune supported traits");
            for(MobTrait t:traits())require(L2HostilityAdapter.marked(entering,L2HostilityAdapter.id(t)),"spawn marker missing");
            record(running,Map.of("nativeFullEntityLoad",true,"registeredJoinEvent",true,"supportedPruned",traits().length));entering.discard();
        }
    }
    private void levelScaling() {
        running="truth/native-level-scaling-preserved";
        try(Fixture owner=new Fixture();Fixture target=new Fixture();Fixture baseline=new Fixture()) {
            owner(owner);unprotected(target);unprotected(baseline);
            Zombie z=mob(200,100,0),plain=mob(210,100,0);
            cap(z).setLevel(z,5);cap(plain).setLevel(plain,5);
            require(cap(z).getLevel()==5&&cap(plain).getLevel()==5,"native nonzero level preparation");
            supported(z,2);var attributes=z.getAttributes().save().copy();var fields=stable(z);
            z.setHealth(16);float hp=z.getHealth();z.teleportTo(2,100,0);
            float loss=attack(z,target),ordinary=attack(plain,baseline);settle(z);
            require(cap(z).traits.isEmpty(),"scaled mob traits retained");near(loss,ordinary,"native nonzero scaling changed");
            require(attributes.equals(z.getAttributes().save())&&fields.equals(stable(z))&&z.isNoAi(),"native attributes/fields/AI changed");
            near(z.getHealth(),hp,"hurt source repaired");
            record(running,Map.of("level",cap(z).getLevel(),"hpLoss",loss,"plainSameLevelLoss",ordinary,"sourceHp",hp,"attributesUnchanged",true));
            z.discard();plain.discard();
        }
    }
    private void reapplication() {
        running="truth/regrant-reinit-copy-off-exit";
        try(Fixture owner=new Fixture();Fixture target=new Fixture()) {
            owner(owner);unprotected(target);Zombie z=mob(200,100,0);supported(z,1);z.setPos(2,100,0);settle(z);
            var marker=z.getPersistentData().getCompound(L2HostilityAdapter.MARKER).copy();owner.data.setSkillDisabled(T,true);z.setPos(200,100,0);
            for(MobTrait trait:traits())assign(z,trait,2);
            attack(z,target);for(MobTrait trait:traits())if(isPotion(trait))require(!target.player.hasEffect(effect(trait)),"regrant resumed effect");
            for(MobTrait trait:traits())require(!cap(z).hasTrait(trait),"native pending regrant survived");
            cap(z).reinit(z,1,false);cap(z).tick(z);supported(z,2);
            require(marker.equals(z.getPersistentData().getCompound(L2HostilityAdapter.MARKER)),"reinit lost marker");
            Zombie source=mob(210,100,0);supported(source,1);cap(z).copyFrom(source,z,cap(source));settle(z);
            for(MobTrait trait:traits())require(!cap(z).hasTrait(trait),"copyFrom regrant survived");
            Zombie separate=mob(220,100,0);cap(separate).copyFrom(z,separate,cap(z));supported(separate,1);
            for(MobTrait trait:traits())require(cap(separate).hasTrait(trait),"new mob inherited nullification");
            separate.getPersistentData().put(L2HostilityAdapter.MARKER,marker.copy());settle(separate);
            for(MobTrait trait:traits())require(cap(separate).hasTrait(trait),"foreign UUID marker applied");
            record("truth/regrant-reinit-copy-off-exit",Map.of("sameMobBlocked",true,"newMobUnaffected",true,"foreignUuidRejected",true));
            z.discard();source.discard();separate.discard();
        }
    }
    private void iterationSafety() {
        running="truth/iteration-safety";
        try(Fixture owner=new Fixture()) {
            owner(owner);Zombie z=mob(200,100,0);supported(z,1);z.setPos(2,100,0);int[] visited={0};
            cap(z).traitEvent((trait,rank)->{L2HostilityAdapter.consider(z,true);visited[0]++;});
            require(visited[0]==traits().length&&cap(z).traits.size()==traits().length,"pruned live iterator");
            settle(z);require(cap(z).traits.isEmpty(),"deferred prune missing");
            record("truth/iteration-safety",Map.of("visited",visited[0],"eventMapRetainedUntilSafeBoundary",true));z.discard();
        }
    }
    private void syncAndPlayerPersistence() {
        running="sync-and-player-persistence";
        UUID playerId;CompoundTag expected;
        try(Fixture owner=new Fixture()) {
            owner(owner);playerId=owner.id;expected=owner.data.serializeNBT();
            for(String id:new String[]{P,M,T}){owner.toggle(id,true);require(!TruthMasteryController.isEnabled(owner.data),"OFF packet not applied");owner.toggle(id,false);require(expected.equals(owner.data.serializeNBT()),"packet changed other data");}
            Zombie z=mob(200,100,0);supported(z,1);
            var mirror=new MobTraitCap();var before=new MobCapSyncToClient(z,cap(z));TagCodec.fromTag(before.tag,MobTraitCap.class,mirror,a->a.toClient());
            for(MobTrait trait:traits())require(mirror.hasTrait(trait),"initial native sync decode");
            z.setPos(2,100,0);settle(z);var after=new MobCapSyncToClient(z,cap(z));
            require(after.tag.contains("traits",10)&&after.tag.getCompound("traits").isEmpty(),"empty map omitted");
            TagCodec.fromTag(after.tag,MobTraitCap.class,mirror,a->a.toClient());require(mirror.traits.isEmpty(),"empty map did not clear mirror");
            owner.drain();cap(z).syncToPlayer(z,owner.player);require(owner.outboundCount()>0,"native syncToPlayer did not send");
            record("native-sync-empty-map",Map.of("before",before.tag.toString(),"after",after.tag.toString(),"mirrorCleared",true,"transport","server EmbeddedChannel; no real client"));z.discard();
            server.getPlayerList().saveAll();
        }
        try(Fixture loaded=new Fixture(playerId,false)){require(expected.equals(loaded.data.serializeNBT()),"normal player disk reload");record("normal-player-save-reload",Map.of("canonical",expected.toString(),"uuid",playerId.toString()));}
    }
    private void prepareChunkPersistence() {
        running="chunk-unload/save-reload";level.setChunkForced(64,0,true);
        try(Fixture owner=new Fixture()) {
            owner(owner);owner.player.setPos(1024,100,0);Zombie z=mob(1026,100,0);
            // Give traits outside the aura, then enter; preserve a real out-of-scope trait as well.
            owner.player.setPos(0,100,0);supported(z,1);assign(z,LHTraits.LEVITATION.get(),1);owner.player.setPos(1024,100,0);settle(z);
            require(cap(z).traits.size()==1&&cap(z).hasTrait(LHTraits.LEVITATION.get()),"scope preservation before save");
            savedMob=z.getUUID();savedMarker=z.getPersistentData().getCompound(L2HostilityAdapter.MARKER).copy();savedCap=payload(z);savedHealth=z.getHealth();beforeUnload=z;
        }
        server.saveEverything(true,true,true);level.setChunkForced(64,0,false);stage=1;waitTicks=0;
    }
    @SubscribeEvent public void tick(TickEvent.ServerTickEvent event) {
        if(event.phase!=TickEvent.Phase.END||ended||stage==0)return;
        try {
            waitTicks++;
            if(stage==1) {
                if(level.getEntity(savedMob)==null&&!level.getChunkSource().hasChunk(64,0)) {
                    record("chunk-unload",Map.of("uuid",savedMob.toString(),"waitTicks",waitTicks,"entityAbsent",true));
                    level.setChunkForced(64,0,true);level.getChunk(64,0);stage=2;waitTicks=0;
                } else require(waitTicks<700,"normal chunk did not unload; entity="+level.getEntity(savedMob));
            } else {
                Entity found=level.getEntity(savedMob);
                if(found instanceof Zombie z) {
                    require(found!=beforeUnload,"chunk reload reused in-memory entity");
                    require(savedMarker.equals(z.getPersistentData().getCompound(L2HostilityAdapter.MARKER)),"marker disk reload");
                    require(savedCap.equals(payload(z)),"native cap disk reload");near(z.getHealth(),savedHealth,"mob health reload");
                    require(z.getAttribute(Attributes.FOLLOW_RANGE).getModifier(FOREIGN)!=null&&z.getPersistentData().getString("verification:foreign").equals("retained"),"foreign state lost");
                    try(Fixture target=new Fixture()) {
                        unprotected(target);target.player.setPos(1028,100,0);supported(z,2);attack(z,target);
                        for(MobTrait trait:traits())if(isPotion(trait))require(!target.player.hasEffect(effect(trait)),"reloaded action mismatch");
                        require(target.player.hasEffect(MobEffects.LEVITATION),"reloaded out-of-scope trait lost");
                    }
                    record("normal-chunk-save-reload-and-regrant",Map.of("uuid",savedMob.toString(),"nativeCap",savedCap.toString(),"marker",savedMarker.toString(),"hp",savedHealth,"newEntityInstance",true));
                    level.setChunkForced(64,0,false);finish(null);
                } else require(waitTicks<150,"normal chunk reload did not restore entity");
            }
        } catch(Throwable error){finish(error);}
    }
    private void finish(Throwable error) {
        if(ended)return;ended=true;
        receipt.put("status",error==null?"PASS":"FAIL");receipt.put("results",results);receipt.put("completedCases",results.size());
        if(error!=null){receipt.put("failedCase",running);receipt.put("failure",error.toString());LogUtils.getLogger().error("FOODHEALING_L2_FAIL case="+running,error);}
        try {server.saveEverything(true,true,true);receipt.put("saveRequested",true);Files.writeString(root.resolve("l2-result.json"),new GsonBuilder().setPrettyPrinting().create().toJson(receipt));}
        catch(Exception e){throw new RuntimeException(e);}finally{server.halt(false);}
    }
    private void record(String name,Map<String,Object> data){results.add(Map.of("case",name,"status","PASS","observed",data));LogUtils.getLogger().info("FOODHEALING_L2_CASE_PASS {} {}",name,data);}
    private static void require(boolean value,String message){if(!value)throw new AssertionError(message);}
    private static void near(float a,float b,String label){require(Math.abs(a-b)<.0002F,label+": "+a+" != "+b);}
    private final class Fixture implements AutoCloseable {
        final UUID id;final ServerPlayer player;final IShokugiData data;
        final Connection connection=new Connection(PacketFlow.SERVERBOUND);final EmbeddedChannel channel=new EmbeddedChannel(connection);
        Fixture(){this(UUID.randomUUID(),true);}
        Fixture(UUID uuid,boolean fresh){
            id=uuid;player=new ServerPlayer(server,level,new GameProfile(id,"fh-l2-"+id.toString().substring(0,8)));
            server.getPlayerList().placeNewPlayer(connection,player);data=player.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(AssertionError::new);
            player.setPos(0,100,0);player.setNoGravity(true);
            // Advance native login grace before fixture measurements; never override the damage gate.
            for(int i=0;i<65;i++)player.tick();
            if(fresh){data.deserializeNBT(new ShokugiData().serializeNBT());data.setSkillLevel(P,1);data.setSkillLevel(M,1);data.setSpentSkillPoints(103);data.setUnspentSkillPoints(0);
                player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(100);player.setHealth(100);}
            player.setPos(0,100,0);player.setNoGravity(true);drain();
        }
        void toggle(String id,boolean disabled){
            ICustomPacket<?> packet=(ICustomPacket<?>)PacketHandler.INSTANCE.toVanillaPacket(new ToggleSkillPacket(id,disabled),NetworkDirection.PLAY_TO_SERVER);
            try{require(NetworkHooks.onCustomPayload(packet,connection),"product toggle packet unhandled");}finally{packet.getInternalData().release();}
            ICustomPacket<?> expected=(ICustomPacket<?>)PacketHandler.INSTANCE.toVanillaPacket(new ShokugiSyncPacket(data.serializeNBT()),NetworkDirection.PLAY_TO_CLIENT);
            boolean found=false;channel.runPendingTasks();Object message;
            try{while((message=channel.readOutbound())!=null){if(message instanceof ICustomPacket<?> actual)found|=ByteBufUtil.equals(expected.getInternalData(),actual.getInternalData());ReferenceCountUtil.release(message);}}
            finally{expected.getInternalData().release();}require(found&&data.isSkillDisabled(id)==disabled,"canonical sync/toggle mismatch");
        }
        int outboundCount(){channel.runPendingTasks();int count=0;Object message;while((message=channel.readOutbound())!=null){if(message instanceof ICustomPacket<?>)count++;ReferenceCountUtil.release(message);}return count;}
        void drain(){channel.runPendingTasks();Object message;while((message=channel.readOutbound())!=null)ReferenceCountUtil.release(message);}
        public void close(){if(server.getPlayerList().getPlayer(id)!=null)server.getPlayerList().remove(player);channel.finishAndReleaseAll();numeric.remove(id);sources.remove(id);additions.remove(id);}
    }
}
