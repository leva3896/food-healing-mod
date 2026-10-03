package com.leva.foodhealing;

import com.leva.foodhealing.capability.IShokugiData;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.leva.foodhealing.compat.flight.FlightProviders;
import com.mojang.authlib.GameProfile;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.ReferenceCountUtil;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.util.UUID;
import java.util.function.Function;

@GameTestHolder(FoodHealingMod.MODID)
@PrefixGameTestTemplate(false)
public final class FoodHealingFlightGameTests {
    @GameTest(template = "empty")
    public static void flightNormalPurchasePacketToggleCommand(GameTestHelper h) {
        try (Fixture f=new Fixture(h)) {
            f.data().setUnspentSkillPoints(2);f.packets();
            h.assertTrue(FoodHealingSkills.purchaseStatus(f.data(),FoodHealingSkillIds.FLIGHT,0)==FoodHealingSkills.PurchaseResult.SUCCESS,"preview READY without provider or prerequisites");
            f.dispatch(new com.leva.foodhealing.network.PurchaseSkillPacket(FoodHealingSkillIds.FLIGHT,0));
            h.assertTrue(f.data().getUnspentSkillPoints()==0&&f.data().getSpentSkillPoints()==2&&f.data().getSkillLevel(FoodHealingSkillIds.FLIGHT)==1&&!f.data().isSkillDisabled(FoodHealingSkillIds.FLIGHT),"normal purchase cost/default");
            h.assertTrue(!f.player.getAbilities().mayfly&&f.packets()==0,"purchase leaves ability to tick");
            f.tick();h.assertTrue(f.player.getAbilities().mayfly&&f.packets()==1,"next END grant");
            var acquired=f.data().serializeNBT();f.dispatch(new com.leva.foodhealing.network.PurchaseSkillPacket(FoodHealingSkillIds.FLIGHT,0));
            h.assertTrue(f.data().serializeNBT().equals(acquired),"replayed purchase no SP or ownership change");
            f.dispatch(new com.leva.foodhealing.network.ToggleSkillPacket(FoodHealingSkillIds.FLIGHT,true));
            f.tick();h.assertTrue(!f.player.getAbilities().mayfly&&f.packets()==1,"registered GUI OFF packet revokes once");
            var disabled=f.data().serializeNBT();
            int result=f.player.server.getCommands().performPrefixedCommand(f.player.createCommandSourceStack().withPermission(2).withSuppressedOutput(),"foodhealing syokugi toggle \"foodhealing:flight\"");
            h.assertTrue(result==0&&f.data().serializeNBT().equals(disabled),"obsolete command cannot toggle Flight");
            f.tick();h.assertTrue(!f.player.getAbilities().mayfly&&f.packets()==0,"rejected command sends no grant");
            f.dispatch(new com.leva.foodhealing.network.ToggleSkillPacket(FoodHealingSkillIds.FLIGHT,false));
            f.tick();h.assertTrue(f.player.getAbilities().mayfly&&f.packets()==1,"registered GUI ON packet grants at next END");
        }h.succeed();
    }
    @GameTest(template = "empty")
    public static void flightOwnedToggleAndPacketBound(GameTestHelper h) {
        try (Fixture f = new Fixture(h)) {
            f.acquire(); int baseline = f.packets();
            h.assertTrue(FlightProviders.query(f.player).equals(FlightAuthority.NEUTRAL), "optional absent path");
            f.tick(); h.assertTrue(f.player.getAbilities().mayfly && FlightController.owns(f.player), "grant owns");
            h.assertTrue(f.packets() == 1, "grant packet exactly once");
            for (int i=0;i<100;i++) f.tick();
            h.assertTrue(f.packets() == 0, "steady ON spam");
            f.player.getAbilities().flying = true;
            RootController.setSkillDisabled(f.data(), FoodHealingSkillIds.FLIGHT, true, f.player.level().getGameTime());
            f.tick(); h.assertTrue(!f.player.getAbilities().mayfly && !f.player.getAbilities().flying && !FlightController.owns(f.player), "owned OFF revoke");
            h.assertTrue(f.packets() == 1, "revoke packet exactly once");
            for (int i=0;i<100;i++) f.tick();
            h.assertTrue(f.packets() == 0, "steady OFF spam");
            f.data().setSkillDisabled(FoodHealingSkillIds.FLIGHT, false); f.tick();
            h.assertTrue(f.player.getAbilities().mayfly && f.packets() == 1, "re-ON");
        } h.succeed();
    }

    @GameTest(template = "empty")
    public static void flightForeignBeforeAndInvalidData(GameTestHelper h) {
        try (Fixture f = new Fixture(h)) {
            f.player.getAbilities().mayfly = true; f.player.getAbilities().flying = true;
            f.acquire(); f.packets(); f.tick(); f.data().setSkillDisabled(FoodHealingSkillIds.FLIGHT, true); f.tick();
            h.assertTrue(f.player.getAbilities().mayfly && f.player.getAbilities().flying && !FlightController.owns(f.player) && f.packets()==0, "foreign-before preserved");
            for (int schema : new int[]{5,6}) {
                CompoundTag invalid = f.data().serializeNBT(); invalid.putInt("FoodHealingDataVersion",schema); invalid.putBoolean("LegacyMigrationPending",true);
                f.data().deserializeNBT(invalid); f.tick();
                h.assertTrue(f.player.getAbilities().mayfly && !FlightController.owns(f.player), "invalid must not revoke foreign");
                f.player.getAbilities().mayfly=false; f.player.getAbilities().flying=false; f.tick();
                h.assertTrue(!f.player.getAbilities().mayfly, "pending/future no grant");
                f.player.getAbilities().mayfly=true;
            }
        } h.succeed();
    }

    @GameTest(template = "empty")
    public static void flightNativeGameModes(GameTestHelper h) {
        try (Fixture f = new Fixture(h)) {
            for (GameType mode : new GameType[]{GameType.CREATIVE,GameType.SPECTATOR}) {
                f.acquire(); f.tick(); h.assertTrue(FlightController.owns(f.player), "initial own");
                f.player.setGameMode(mode); f.tick();
                h.assertTrue(!FlightController.owns(f.player) && f.player.getAbilities().mayfly, "native authority drops stale token");
                boolean flying=f.player.getAbilities().flying;
                f.data().setSkillDisabled(FoodHealingSkillIds.FLIGHT,true); f.tick();
                h.assertTrue(f.player.getAbilities().mayfly && f.player.getAbilities().flying==flying, "mode OFF retains");
                f.player.setGameMode(GameType.SURVIVAL); f.tick();
                h.assertTrue(!f.player.getAbilities().mayfly, "native survival OFF");
                f.data().setSkillDisabled(FoodHealingSkillIds.FLIGHT,false); f.tick();
                h.assertTrue(FlightController.owns(f.player), "survival new grant");
                f.player.setGameMode(mode); f.tick(); f.player.setGameMode(GameType.ADVENTURE); f.tick();
                h.assertTrue(FlightController.owns(f.player) && f.player.getAbilities().mayfly, "Adventure reevaluation");
                f.data().setSkillDisabled(FoodHealingSkillIds.FLIGHT,true); f.tick();
            }
        } h.succeed();
    }

    @GameTest(template = "empty", timeoutTicks = 200)
    public static void flightNativeDimensionSameInstance(GameTestHelper h) {
        try (Fixture f = new Fixture(h)) {
            f.acquire(); f.tick(); ServerPlayer original=f.player;
            for (var dimension : java.util.List.of(Level.NETHER,Level.OVERWORLD)) {
                Entity moved=f.player.changeDimension(h.getLevel().getServer().getLevel(dimension),new Teleporter());
                h.assertTrue(moved==original && FlightController.owns(f.player), "same-instance dimension retains ownership");
                f.tick();
            }
            f.data().setSkillDisabled(FoodHealingSkillIds.FLIGHT,true);f.tick();
            h.assertTrue(!f.player.getAbilities().mayfly, "dimension token still revocable");
        } h.succeed();
    }

    @GameTest(template = "empty", timeoutTicks = 200)
    public static void flightNativeDeathRespawn(GameTestHelper h) { replacement(h,false); }

    @GameTest(template = "empty", timeoutTicks = 200)
    public static void flightNativeEndReturnReplacement(GameTestHelper h) { replacement(h,true); }

    private static void replacement(GameTestHelper h, boolean end) {
        try (Fixture f = new Fixture(h)) {
            f.acquire(); f.tick(); ServerPlayer old=f.player;
            if(end) old.changeDimension(h.getLevel().getServer().getLevel(Level.END),new Teleporter());
            f.player=h.getLevel().getServer().getPlayerList().respawn(old,end);
            h.assertTrue(f.player!=old && !FlightController.owns(old) && !FlightController.owns(f.player), "native replacement must not copy token");
            h.assertTrue(f.data().getSkillLevel(FoodHealingSkillIds.FLIGHT)==1, "canonical copy");
            f.tick(); h.assertTrue(f.player.getAbilities().mayfly && FlightController.owns(f.player), "replacement evaluates fresh");
            f.data().setSkillDisabled(FoodHealingSkillIds.FLIGHT,true); f.tick();
            h.assertTrue(!f.player.getAbilities().mayfly,"replacement OFF");
        } h.succeed();
    }

    @GameTest(template = "empty", timeoutTicks = 200)
    public static void flightNativeLogoutLoginSavedAbilities(GameTestHelper h) {
        UUID id=UUID.randomUUID(); ServerPlayer old;
        try (Fixture f=new Fixture(h,id)) { f.acquire(); f.tick(); old=f.player; }
        h.assertTrue(!FlightController.owns(old),"native logout cleans token");
        try (Fixture f=new Fixture(h,id)) {
            h.assertTrue(f.player!=old && !FlightController.owns(f.player),"new same-UUID login no old token");
            h.assertTrue(f.data().getSkillLevel(FoodHealingSkillIds.FLIGHT)==1,"saved canonical login");
            f.tick(); h.assertTrue(f.player.getAbilities().mayfly && FlightController.owns(f.player),"native GameMode false then fresh grant");
            CompoundTag saved=new CompoundTag(); f.player.saveWithoutId(saved);
            h.assertTrue(!saved.toString().contains("FH_OWNS"),"no persistent ownership field");
        } h.succeed();
    }

    @GameTest(template = "empty")
    public static void flightOtherPlayerAndUnrelatedAbilities(GameTestHelper h) {
        try (Fixture a=new Fixture(h); Fixture b=new Fixture(h)) {
            a.acquire();b.acquire();a.tick();b.tick();
            var before=new CompoundTag();b.player.getAbilities().addSaveData(before);
            float fly=a.player.getAbilities().getFlyingSpeed(),walk=a.player.getAbilities().getWalkingSpeed();
            boolean inv=a.player.getAbilities().invulnerable,gravity=a.player.isNoGravity();
            a.data().setSkillDisabled(FoodHealingSkillIds.FLIGHT,true);a.tick();
            var after=new CompoundTag();b.player.getAbilities().addSaveData(after);
            h.assertTrue(before.equals(after) && FlightController.owns(b.player),"other player unchanged");
            h.assertTrue(a.player.getAbilities().getFlyingSpeed()==fly && a.player.getAbilities().getWalkingSpeed()==walk
                    && a.player.getAbilities().invulnerable==inv && a.player.isNoGravity()==gravity,"unrelated abilities unchanged");
        } h.succeed();
    }

    private static final class Fixture implements AutoCloseable {
        ServerPlayer player; final EmbeddedChannel channel; final Connection connection;
        Fixture(GameTestHelper h) {this(h,UUID.randomUUID());}
        Fixture(GameTestHelper h,UUID id) {
            player=new ServerPlayer(h.getLevel().getServer(),h.getLevel(),new GameProfile(id,"FlightTest"));
            connection=new Connection(PacketFlow.SERVERBOUND);channel=new EmbeddedChannel(connection);
            h.getLevel().getServer().getPlayerList().placeNewPlayer(connection,player);
            player.setGameMode(GameType.SURVIVAL);
        }
        IShokugiData data(){return player.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(()->new AssertionError("missing data"));}
        void acquire(){data().setSkillLevel(FoodHealingSkillIds.FLIGHT,1);data().setSkillDisabled(FoodHealingSkillIds.FLIGHT,false);}
        void tick(){FlightController.tick(new net.minecraftforge.event.TickEvent.PlayerTickEvent(net.minecraftforge.event.TickEvent.Phase.END,player));}
        void dispatch(Object message){
            var packet=(net.minecraftforge.network.ICustomPacket<?>)com.leva.foodhealing.network.PacketHandler.INSTANCE.toVanillaPacket(message,net.minecraftforge.network.NetworkDirection.PLAY_TO_SERVER);
            try {if(!net.minecraftforge.network.NetworkHooks.onCustomPayload(packet,connection))throw new AssertionError("registered Flight packet not handled");}
            finally {packet.getInternalData().release();}
        }
        int packets(){int n=0;Object packet;while((packet=channel.readOutbound())!=null){if(packet instanceof ClientboundPlayerAbilitiesPacket)n++;ReferenceCountUtil.release(packet);}return n;}
        public void close(){player.server.getPlayerList().remove(player);channel.finishAndReleaseAll();}
    }
    private static final class Teleporter implements ITeleporter {
        @Override public PortalInfo getPortalInfo(Entity entity,ServerLevel level,Function<ServerLevel,PortalInfo> fallback){return new PortalInfo(new Vec3(.5,80,.5),Vec3.ZERO,0,0);}
    }
}
