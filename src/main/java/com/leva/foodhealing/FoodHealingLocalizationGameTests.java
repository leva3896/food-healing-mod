package com.leva.foodhealing;

import com.leva.foodhealing.capability.IShokugiData;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.leva.foodhealing.network.*;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.ReferenceCountUtil;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.network.*;
import java.util.UUID;

@GameTestHolder(FoodHealingMod.MODID)
@PrefixGameTestTemplate(false)
public final class FoodHealingLocalizationGameTests {
    @GameTest(template="empty")
    public static void skillSuccessMessagesExactlyOnce(GameTestHelper h) {
        try (Fixture f=new Fixture(h)) {
            f.data.setUnspentSkillPoints(11);
            f.send(new PurchaseSkillPacket(FoodHealingSkillIds.FIRE_RESISTANCE,0),"message.foodhealing.skill.learned","skill.foodhealing.fire_resistance.name",0);
            f.send(new PurchaseSkillPacket(FoodHealingSkillIds.FIRE_RESISTANCE,0),"message.foodhealing.stale_purchase",null,0);
            f.send(new PurchaseSkillPacket(FoodHealingSkillIds.FIRE_RESISTANCE,1),"message.foodhealing.max_level",null,0);
            f.send(new PurchaseSkillPacket(FoodHealingSkillIds.SATISFACTION,0),"message.foodhealing.skill.learned","skill.foodhealing.satisfaction.name",0);
            f.send(new PurchaseSkillPacket(FoodHealingSkillIds.SATISFACTION,1),"message.foodhealing.skill.level_up","skill.foodhealing.satisfaction.name",2);
            var before=f.data.serializeNBT();
            f.send(new PurchaseSkillPacket(FoodHealingSkillIds.SATISFACTION,1),"message.foodhealing.stale_purchase",null,0);
            f.send(new PurchaseSkillPacket(FoodHealingSkillIds.SATISFACTION,2),"message.foodhealing.insufficient_sp",null,0);
            h.assertTrue(before.equals(f.data.serializeNBT()) && f.data.getSpentSkillPoints()==11, "rejection changed state/SP");
        }
        h.succeed();
    }
    @GameTest(template="empty")
    public static void baseStatSuccessMessagesExactlyOnce(GameTestHelper h) {
        try(Fixture f=new Fixture(h)) {
            f.data.setUnspentSkillPoints(15);
            f.send(new PurchaseBaseStatPacket(FoodHealingBaseStatIds.BASE_DEFENSE,0),"message.foodhealing.stat.increased","stat.foodhealing.base_defense",0);
            f.send(new PurchaseBaseStatPacket(FoodHealingBaseStatIds.BASE_DEFENSE,0),"message.foodhealing.stat.stale",null,0);
            f.send(new PurchaseBaseStatPacket(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR,0),"message.foodhealing.stat.increased","stat.foodhealing.base_damage_reduction",0);
            var before=f.data.serializeNBT();
            f.send(new PurchaseBaseStatPacket(FoodHealingBaseStatIds.BASE_DEFENSE,1),"message.foodhealing.stat.insufficient_sp",null,0);
            f.send(new PurchaseBaseStatPacket("invalid:unknown",0),"message.foodhealing.stat.unknown",null,0);
            h.assertTrue(before.equals(f.data.serializeNBT()) && f.data.getSpentSkillPoints()==15,"base failure changed state/SP");
        }
        h.succeed();
    }
    @GameTest(template="empty")
    public static void taczBasePurchaseOptionalBoundaryAndRegisteredSync(GameTestHelper h) {
        String id = FoodHealingBaseStatIds.TACZ_BASE_OUTGOING_DAMAGE;
        boolean present = net.minecraftforge.fml.ModList.get().isLoaded("tacz");
        h.assertTrue(FoodHealingBaseStats.isAvailable(id) == present, "GUI/server availability disagrees with loader");
        try (Fixture f = new Fixture(h); Fixture other = new Fixture(h)) {
            // The second player's native login broadcasts a join message to the first.
            // Drain preparation traffic before measuring purchase responses.
            f.channel.runPendingTasks();
            Object preparationPacket;
            while ((preparationPacket = f.channel.readOutbound()) != null) ReferenceCountUtil.release(preparationPacket);
            f.data.setUnspentSkillPoints(6);
            var otherBefore = other.data.serializeNBT();
            if (!present) {
                var before = f.data.serializeNBT();
                for (long generation : new long[] {0, 0, -1, Long.MAX_VALUE}) {
                    f.send(new PurchaseBaseStatPacket(id, generation), "message.foodhealing.tacz_base_required", null, 0);
                    h.assertTrue(before.equals(f.data.serializeNBT()), "absent TaCZ changed SP/points/canonical");
                }
            } else {
                for (int n = 0; n < 3; n++) {
                    f.send(new PurchaseBaseStatPacket(id, n), "message.foodhealing.stat.increased", "stat.foodhealing.tacz_base_outgoing_damage", 0);
                    h.assertTrue(f.data.getUnspentSkillPoints() == 6 - 2 * (n + 1)
                            && f.data.getSpentSkillPoints() == 2 * (n + 1)
                            && FoodHealingBaseStats.purchaseCount(f.data, id) == n + 1,
                            "native TaCZ purchase accounting changed");
                    h.assertTrue(Math.abs(FoodHealingBaseStats.taczDamageMultiplier(f.data) - (1 + .01 * (n + 1))) < 1e-9,
                            "TaCZ multiplier changed");
                    var before = f.data.serializeNBT();
                    f.send(new PurchaseBaseStatPacket(id, n), "message.foodhealing.stat.stale", null, 0);
                    h.assertTrue(before.equals(f.data.serializeNBT()), "duplicate spent SP");
                }
                var before = f.data.serializeNBT();
                f.send(new PurchaseBaseStatPacket(id, 3), "message.foodhealing.stat.insufficient_sp", null, 0);
                h.assertTrue(before.equals(f.data.serializeNBT()), "insufficient SP mutated state");
                f.data.setUnspentSkillPoints(2);
                f.data.setSpentSkillPoints(Long.MAX_VALUE - 1);
                before = f.data.serializeNBT();
                f.send(new PurchaseBaseStatPacket(id, 3), "message.foodhealing.stat.insufficient_sp", null, 0);
                h.assertTrue(before.equals(f.data.serializeNBT()), "overflow mutated state");
            }
            h.assertTrue(otherBefore.equals(other.data.serializeNBT()), "another player changed");
        }
        System.out.println("TACZ BASE OPTIONAL GATE PASS present=" + present);
        h.succeed();
    }

    private static final class Fixture implements AutoCloseable {
        final GameTestHelper h; final ServerPlayer player; final IShokugiData data;
        final Connection connection=new Connection(PacketFlow.SERVERBOUND);
        final EmbeddedChannel channel=new EmbeddedChannel(connection);
        Fixture(GameTestHelper h) {
            this.h=h; var server=h.getLevel().getServer();
            player=new ServerPlayer(server,h.getLevel(),new GameProfile(UUID.randomUUID(),"fh-language"));
            server.getPlayerList().placeNewPlayer(connection,player);
            data=player.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(IllegalStateException::new);
            channel.runPendingTasks(); Object m;while((m=channel.readOutbound())!=null)ReferenceCountUtil.release(m);
        }
        void send(Object message,String expectedKey,String nameKey,int level) {
            ICustomPacket<?> packet=(ICustomPacket<?>)PacketHandler.INSTANCE.toVanillaPacket(message,NetworkDirection.PLAY_TO_SERVER);
            try {h.assertTrue(NetworkHooks.onCustomPayload(packet,connection),"registered dispatch");}
            finally {packet.getInternalData().release();}
            ICustomPacket<?> expected=(ICustomPacket<?>)PacketHandler.INSTANCE.toVanillaPacket(new ShokugiSyncPacket(data.serializeNBT()),NetworkDirection.PLAY_TO_CLIENT);
            int chat=0;boolean sync=false;channel.runPendingTasks();Object m;
            try {while((m=channel.readOutbound())!=null) {
                try {
                    if(m instanceof ClientboundSystemChatPacket p) {
                        chat++; var t=(TranslatableContents)p.content().getContents();
                        h.assertTrue(t.getKey().equals(expectedKey),"wrong success/failure key: "+t.getKey());
                        if(nameKey!=null) h.assertTrue(((TranslatableContents)((Component)t.getArgs()[0]).getContents()).getKey().equals(nameKey),"wrong localized name");
                        if(level>0) h.assertTrue(t.getArgs()[1].equals(level),"not authoritative level");
                    }
                    if(m instanceof ICustomPacket<?> p) sync|=ByteBufUtil.equals(expected.getInternalData(),p.getInternalData());
                } finally {ReferenceCountUtil.release(m);}
            }} finally {expected.getInternalData().release();}
            h.assertTrue(chat==1 && sync,"exactly one result and canonical sync required; chat="+chat+" sync="+sync);
        }
        public void close() {
            h.getLevel().getServer().getPlayerList().remove(player);channel.finishAndReleaseAll();
        }
    }
}
