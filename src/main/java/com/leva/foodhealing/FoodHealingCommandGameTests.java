package com.leva.foodhealing;

import com.leva.foodhealing.capability.FoodDiversityProvider;
import com.leva.foodhealing.capability.IFoodDiversityData;
import com.leva.foodhealing.capability.IShokugiData;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.leva.foodhealing.network.PacketHandler;
import com.leva.foodhealing.network.ShokugiSyncPacket;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.tree.CommandNode;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.ReferenceCountUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.network.ICustomPacket;
import net.minecraftforge.network.NetworkDirection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@GameTestHolder(FoodHealingMod.MODID)
@PrefixGameTestTemplate(false)
public final class FoodHealingCommandGameTests {
    private static final String EMPTY_TEMPLATE = "empty";

    private FoodHealingCommandGameTests() {
    }

    @GameTest(template = EMPTY_TEMPLATE, timeoutTicks = 200)
    public static void releaseCommandTreeRejectsLegacyAndVerificationOperations(GameTestHelper helper) {
        try (CommandFixture f = new CommandFixture(helper)) {
            var dispatcher = f.server.getCommands().getDispatcher();
            CommandNode<CommandSourceStack> root = dispatcher.getRoot().getChild("foodhealing");
            helper.assertTrue(root != null && root.getCommand() == null, "command root missing or executable");
            Set<String> paths = new HashSet<>();
            collectPaths(root, "", paths);
            Set<String> expected = Set.of("foodhealing syokugi level", "foodhealing syokugi count",
                    "foodhealing syokugi setlevel level", "foodhealing syokugi setcount count",
                    "foodhealing syokugi setskillpoint amount", "foodhealing syokugi addskillpoint amount");
            helper.assertTrue(paths.equals(expected), "unexpected executable release command paths: " + paths);
            var syokugi = root.getChild("syokugi");
            for (int permission : List.of(0, 1, 2)) {
                var source = f.source(permission);
                for (var child : syokugi.getChildren()) {
                    boolean query = child.getName().equals("level") || child.getName().equals("count");
                    helper.assertTrue(child.canUse(source) == (query || permission >= 2),
                            "permission boundary changed for " + child.getName());
                }
                var suggestions = dispatcher.getCompletionSuggestions(
                        dispatcher.parse("foodhealing syokugi ", source)).join();
                Set<String> offered = new HashSet<>();
                suggestions.getList().forEach(s -> offered.add(s.getText()));
                helper.assertTrue(offered.stream().noneMatch(Set.of("skill", "toggle", "prepare", "inspect", "hit", "seal", "arm")::contains),
                        "obsolete or verification command offered: " + offered);
                if (permission == 2) helper.assertTrue(offered.equals(Set.of("level", "count", "setlevel", "setcount", "setskillpoint", "addskillpoint")), "admin suggestions differ");
                for (String suffix : List.of("skill", "skill flight", "skill \"foodhealing:flight\"",
                        "toggle \"foodhealing:flight\"", "toggle \"飛翔の極意\"", "toggle \"豊穣\"",
                        "prepare", "inspect", "hit A", "seal", "arm")) {
                    String command = "foodhealing syokugi " + suffix;
                    var parsed = dispatcher.parse(command, source);
                    helper.assertTrue(parsed.getReader().canRead() || !parsed.getExceptions().isEmpty(),
                            "removed operation unexpectedly parses: " + command);
                    assertRejectedWithoutMutation(helper, f.server, source, f.channel, f.data, command,
                            "removed command must not change SP, toggles, reservation, or progression");
                }
                for (String suffix : List.of("prepare", "inspect", "hit A", "seal", "arm")) {
                    assertRejectedWithoutMutation(helper, f.server, source, f.channel, f.data,
                            "foodhealing " + suffix, "verification root command must be absent");
                }
            }
            System.out.println("RELEASE COMMAND TREE PASS: 6 executable paths; permissions 0/1/2; legacy/verification absent; canonical unchanged");
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE, timeoutTicks = 200)
    public static void retainedCommandsPreserveScopeAndValidateAllInputs(GameTestHelper helper) {
        try (CommandFixture f = new CommandFixture(helper); CommandFixture other = new CommandFixture(helper)) {
            CompoundTag otherBefore = other.data.serializeNBT();
            var admin = f.source(2);
            for (String name : List.of("setlevel", "setcount", "setskillpoint", "addskillpoint")) {
                String prefix = "foodhealing syokugi " + name;
                for (int permission : List.of(0, 1)) assertRejectedWithoutMutation(helper, f.server,
                        f.source(permission), f.channel, f.data, prefix + " 1", "mutation requires permission 2");
                for (String argument : List.of("", " -1", " invalid", " 1.5", " NaN", " Infinity", " 9223372036854775808", " 1 @a")) {
                    assertRejectedWithoutMutation(helper, f.server, admin, f.channel, f.data,
                            prefix + argument, "malformed/out-of-range/extra-target input must fail");
                }
                assertRejectedWithoutMutation(helper, f.server, f.server.createCommandSourceStack().withPermission(4).withSuppressedOutput(),
                        f.channel, f.data, prefix + " 1", "console without player cannot mutate player data");
            }
            for (String query : List.of("level", "count")) {
                var before = f.data.serializeNBT();
                drainAfterPending(f.channel);
                helper.assertTrue(run(f.server, f.source(0), "foodhealing syokugi " + query) == 1, "self diagnostic failed");
                helper.assertTrue(before.equals(f.data.serializeNBT()), "read-only diagnostic mutated canonical");
                assertQueryWithoutSync(helper, f.channel, query, f.data);
            }
            for (String name : List.of("setlevel", "setcount")) {
                String key = name.equals("setlevel") ? "ShokugiLevel" : "EatCount";
                for (long value : new long[]{0, 37, Long.MAX_VALUE}) {
                    var expected = f.data.serializeNBT().copy(); expected.putLong(key, value);
                    drainAfterPending(f.channel);
                    helper.assertTrue(run(f.server, admin, "foodhealing syokugi " + name + " " + value) == 1, "retained diagnostic setter rejected");
                    helper.assertTrue(expected.equals(f.data.serializeNBT()), "setter changed unrelated canonical fields: " + name);
                    assertExactSync(helper, f.channel, f.data);
                }
            }
            helper.assertTrue(otherBefore.equals(other.data.serializeNBT()), "command modified another player");
            System.out.println("RELEASE COMMAND SAFETY PASS: retained mutation permission/long bounds/current-player scope; read-only queries; exact canonical sync");
        }
        helper.succeed();
    }

    private static void collectPaths(CommandNode<CommandSourceStack> node, String prefix, Set<String> paths) {
        String path = prefix.isEmpty() ? node.getName() : prefix + " " + node.getName();
        if (node.getCommand() != null) paths.add(path);
        for (var child : node.getChildren()) collectPaths(child, path, paths);
    }

    private static void assertExactSync(GameTestHelper helper, EmbeddedChannel channel, IShokugiData data) {
        ICustomPacket<?> expected = (ICustomPacket<?>) PacketHandler.INSTANCE.toVanillaPacket(
                new ShokugiSyncPacket(data.serializeNBT()), NetworkDirection.PLAY_TO_CLIENT);
        int count = 0;
        channel.runPendingTasks();
        Object message;
        try {
            while ((message = channel.readOutbound()) != null) {
                if (message instanceof ICustomPacket<?> actual && ByteBufUtil.equals(expected.getInternalData(), actual.getInternalData())) count++;
                ReferenceCountUtil.release(message);
            }
        } finally { expected.getInternalData().release(); }
        helper.assertTrue(count == 1, "setter must sync exact canonical NBT once; count=" + count);
    }

    private static void assertQueryWithoutSync(GameTestHelper helper, EmbeddedChannel channel, String query, IShokugiData data) {
        channel.runPendingTasks();
        int messages = 0;
        Object message;
        while ((message = channel.readOutbound()) != null) {
            try {
                helper.assertTrue(!(message instanceof ICustomPacket<?>), "read-only query sent capability sync");
                if (message instanceof net.minecraft.network.protocol.game.ClientboundSystemChatPacket chat) {
                    var content = (net.minecraft.network.chat.contents.TranslatableContents) chat.content().getContents();
                    helper.assertTrue(content.getKey().equals("command.foodhealing." + query), "wrong diagnostic message");
                    Object[] args = content.getArgs();
                    helper.assertTrue(((Number) args[0]).longValue() == (query.equals("level") ? data.getLevel() : data.getEatCount()), "diagnostic not server canonical");
                    if (query.equals("count")) helper.assertTrue(((Number) args[1]).longValue() == FoodHealingConfig.nutritionThreshold(), "wrong diagnostic threshold");
                    messages++;
                }
            } finally { ReferenceCountUtil.release(message); }
        }
        helper.assertTrue(messages == 1, "diagnostic should report once");
    }

    private static final class CommandFixture implements AutoCloseable {
        final MinecraftServer server;
        final ServerPlayer player;
        final EmbeddedChannel channel;
        final IShokugiData data;
        CommandFixture(GameTestHelper helper) {
            server = helper.getLevel().getServer();
            UUID uuid = UUID.randomUUID();
            player = new ServerPlayer(server, helper.getLevel(), new GameProfile(uuid, "fh-cmd-" + uuid.toString().substring(0, 8)));
            Connection connection = new Connection(PacketFlow.SERVERBOUND);
            channel = new EmbeddedChannel(connection);
            server.getPlayerList().placeNewPlayer(connection, player);
            data = shokugi(player);
            data.setLevel(77); data.setEatCount(12); data.setUnspentSkillPoints(43); data.setSpentSkillPoints(25);
            data.setSkillLevel(FoodHealingSkillIds.FLIGHT, 1); data.setSkillDisabled(FoodHealingSkillIds.FLIGHT, true);
            data.setSkillLevel(FoodHealingSkillIds.GUTS, 5); data.setSkillLevel(FoodHealingSkillIds.TRUE_GUTS, 1);
            data.setRootReservedNutrition(18);
            drainAfterPending(channel);
        }
        CommandSourceStack source(int permission) { return player.createCommandSourceStack().withPermission(permission).withSuppressedOutput(); }
        @Override public void close() { server.getPlayerList().remove(player); channel.finishAndReleaseAll(); }
    }

    @GameTest(template = EMPTY_TEMPLATE, timeoutTicks = 200)
    public static void skillPointAdminCommandsAreAtomicScopedAndSynced(GameTestHelper helper) {
        MinecraftServer server = helper.getLevel().getServer();
        ServerPlayer player = new ServerPlayer(server, helper.getLevel(),
                new GameProfile(UUID.randomUUID(), "fh-sp-command"));
        Connection connection = new Connection(PacketFlow.SERVERBOUND);
        EmbeddedChannel channel = new EmbeddedChannel(connection);
        try {
            server.getPlayerList().placeNewPlayer(connection, player);
            IShokugiData data = shokugi(player);
            IFoodDiversityData diversity = diversity(player);
            data.setLevel(321L);
            data.setEatCount(45L);
            data.setUnspentSkillPoints(17L);
            data.setSpentSkillPoints(23L);
            data.setSkillLevel(FoodHealingSkillIds.GUTS, 2);
            data.setSkillDisabled(FoodHealingSkillIds.GUTS, true);
            data.setBaseStatPoints(FoodHealingBaseStatIds.BASE_DEFENSE, 3L);
            diversity.addEatenFood("minecraft:apple");
            diversity.addMaxHealthBonus(4);

            CompoundTag unchangedProgression = progressionExceptUnspent(data);
            Set<String> unchangedFoods = Set.copyOf(diversity.getAllEatenFoods());
            int unchangedDiversityHealth = diversity.getMaxHealthBonus();
            CommandSourceStack admin = player.createCommandSourceStack()
                    .withPermission(2).withSuppressedOutput();
            channel.runPendingTasks();
            drainOutbound(channel);

            helper.assertTrue(run(server, admin, "foodhealing syokugi setskillpoint 500") == 1,
                    "setskillpoint 500 was rejected");
            helper.assertTrue(data.getUnspentSkillPoints() == 500L,
                    "setskillpoint 500 did not set unspent SP to 500");
            assertUnrelatedDataUnchanged(helper, data, diversity, unchangedProgression,
                    unchangedFoods, unchangedDiversityHealth);
            assertSyncSent(helper, channel, "setskillpoint did not immediately sync the client");

            helper.assertTrue(run(server, admin, "foodhealing syokugi setskillpoint 0") == 1,
                    "setskillpoint 0 was rejected");
            helper.assertTrue(data.getUnspentSkillPoints() == 0L,
                    "setskillpoint 0 did not clear unspent SP");
            assertUnrelatedDataUnchanged(helper, data, diversity, unchangedProgression,
                    unchangedFoods, unchangedDiversityHealth);
            assertSyncSent(helper, channel, "setskillpoint 0 did not immediately sync the client");

            helper.assertTrue(run(server, admin, "foodhealing syokugi addskillpoint 500") == 1,
                    "addskillpoint 500 was rejected");
            helper.assertTrue(data.getUnspentSkillPoints() == 500L,
                    "addskillpoint 500 did not add to unspent SP");
            assertUnrelatedDataUnchanged(helper, data, diversity, unchangedProgression,
                    unchangedFoods, unchangedDiversityHealth);
            assertSyncSent(helper, channel, "addskillpoint did not immediately sync the client");

            data.setUnspentSkillPoints(88L);
            helper.assertTrue(run(server, admin, "foodhealing syokugi setlevel 500") == 1,
                    "existing setlevel command was rejected");
            helper.assertTrue(data.getLevel() == 500L && data.getUnspentSkillPoints() == 88L,
                    "setlevel changed unspent SP or failed to update only Shokugi level");
            helper.assertTrue(data.getEatCount() == 45L && data.getSpentSkillPoints() == 23L,
                    "setlevel changed Shokugi count or spent SP");
            drainAfterPending(channel);

            data.setUnspentSkillPoints(71L);
            assertRejectedWithoutMutation(helper, server,
                    player.createCommandSourceStack().withPermission(1).withSuppressedOutput(),
                    channel, data, "foodhealing syokugi setskillpoint 900",
                    "permission level below 2 executed setskillpoint");
            assertRejectedWithoutMutation(helper, server, admin, channel, data,
                    "foodhealing syokugi setskillpoint -1",
                    "negative setskillpoint input changed canonical data");
            assertRejectedWithoutMutation(helper, server, admin, channel, data,
                    "foodhealing syokugi addskillpoint -1",
                    "negative addskillpoint input changed canonical data");
            assertRejectedWithoutMutation(helper, server, admin, channel, data,
                    "foodhealing syokugi setskillpoint invalid",
                    "invalid setskillpoint input changed canonical data");
            assertRejectedWithoutMutation(helper, server, admin, channel, data,
                    "foodhealing syokugi setskillpoint 9223372036854775808",
                    "out-of-range setskillpoint input changed canonical data");

            helper.assertTrue(run(server, admin,
                            "foodhealing syokugi setskillpoint " + Long.MAX_VALUE) == 1,
                    "setskillpoint rejected Long.MAX_VALUE");
            helper.assertTrue(data.getUnspentSkillPoints() == Long.MAX_VALUE,
                    "setskillpoint did not preserve Long.MAX_VALUE");
            assertSyncSent(helper, channel, "Long.MAX_VALUE set did not sync the client");
            assertRejectedWithoutMutation(helper, server, admin, channel, data,
                    "foodhealing syokugi addskillpoint 1",
                    "overflowing addskillpoint partially changed canonical data");
        } finally {
            channel.finishAndReleaseAll();
        }
        helper.succeed();
    }

    private static int run(MinecraftServer server, CommandSourceStack source, String command) {
        return server.getCommands().performPrefixedCommand(source, command);
    }

    private static void assertRejectedWithoutMutation(GameTestHelper helper, MinecraftServer server,
                                                       CommandSourceStack source, EmbeddedChannel channel,
                                                       IShokugiData data, String command, String message) {
        CompoundTag before = data.serializeNBT();
        drainAfterPending(channel);
        helper.assertTrue(run(server, source, command) == 0, message + " (command unexpectedly succeeded)");
        helper.assertTrue(data.serializeNBT().equals(before), message);
        channel.runPendingTasks();
        helper.assertTrue(drainOutbound(channel) == 0,
                message + " (a capability sync was sent for a rejected command)");
    }

    private static void assertUnrelatedDataUnchanged(GameTestHelper helper, IShokugiData data,
                                                     IFoodDiversityData diversity,
                                                     CompoundTag expectedProgression,
                                                     Set<String> expectedFoods,
                                                     int expectedDiversityHealth) {
        helper.assertTrue(progressionExceptUnspent(data).equals(expectedProgression),
                "skill-point command changed level, count, spent SP, skills, toggles, or base stats");
        helper.assertTrue(diversity.getAllEatenFoods().equals(expectedFoods)
                        && diversity.getMaxHealthBonus() == expectedDiversityHealth,
                "skill-point command changed Food Diversity data");
    }

    private static CompoundTag progressionExceptUnspent(IShokugiData data) {
        CompoundTag tag = data.serializeNBT();
        tag.remove("UnspentSkillPoints");
        return tag;
    }

    private static void assertSyncSent(GameTestHelper helper, EmbeddedChannel channel, String message) {
        channel.runPendingTasks();
        helper.assertTrue(drainOutbound(channel) > 0, message);
    }

    private static void drainAfterPending(EmbeddedChannel channel) {
        channel.runPendingTasks();
        drainOutbound(channel);
    }

    private static int drainOutbound(EmbeddedChannel channel) {
        int count = 0;
        Object message;
        while ((message = channel.readOutbound()) != null) {
            count++;
            ReferenceCountUtil.release(message);
        }
        return count;
    }

    private static IShokugiData shokugi(ServerPlayer player) {
        return player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .orElseThrow(() -> new GameTestAssertException("Shokugi capability is missing"));
    }

    private static IFoodDiversityData diversity(ServerPlayer player) {
        return player.getCapability(FoodDiversityProvider.FOOD_DIVERSITY)
                .orElseThrow(() -> new GameTestAssertException("Food Diversity capability is missing"));
    }
}
