package com.leva.foodhealing;

import com.google.gson.GsonBuilder;
import com.leva.foodhealing.capability.*;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.nbt.*;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.*;

/** Opt-in, non-distributed verification. Imports only actual Food Healing capability payloads. */
public final class RealLegacyCapabilityVerification {
    private static final String PROGRESS = "foodhealing:shokugi_data";
    private static final String DIVERSITY = "foodhealing:food_diversity";

    private RealLegacyCapabilityVerification() { }

    public static void main(String[] args) throws Exception {
        Path root = checkedRoot(Path.of(args[0]));
        Inputs inputs = readInputs(root);
        if (args.length == 2 && args[1].equals("dependencies")) {
            Map<String, Set<String>> references = new TreeMap<>();
            collectReferences(inputs.level, "level.dat", references);
            collectReferences(inputs.player.get("Inventory"), "player.Inventory", references);
            collectReferences(inputs.player.get("EnderItems"), "player.EnderItems", references);
            Map<String, Object> metadata = new LinkedHashMap<>();
            metadata.put("scope", "STATIC NBT REFERENCES ONLY / NO CHUNK BOOT / NOT INTEGRATION TESTED");
            metadata.put("rootKeys", new TreeSet<>(inputs.level.getAllKeys()));
            metadata.put("playerCapabilityKeys", new TreeSet<>(inputs.player.getCompound("ForgeCaps").getAllKeys()));
            metadata.put("resourceReferences", references);
            try (var walk = Files.walk(inputs.levelFile.getParent())) {
                metadata.put("savedDataFiles", walk.filter(Files::isRegularFile)
                        .map(inputs.levelFile.getParent()::relativize).map(Path::toString).sorted().toList());
            }
            writeJson(root.resolve("audit/world-dependencies.json"), metadata);
            System.out.println("REAL_V225_DEPENDENCIES_AUDITED groups=" + references.size() + " fullWorldBoot=NOT_RUN");
            return;
        }
        Map<String, Object> report = new LinkedHashMap<>();
        CompoundTag data = inputs.level.getCompound("Data");
        report.put("levelName", data.getString("LevelName"));
        report.put("minecraftVersion", data.getCompound("Version").toString());
        report.put("uuid", inputs.player.getUUID("UUID").toString());
        report.put("lastPlayed", data.getLong("LastPlayed"));
        report.put("levelFile", inputs.levelFile.toString());
        report.put("levelSHA256", hash(Files.readAllBytes(inputs.levelFile)));
        report.put("playerFile", inputs.playerFile.toString());
        report.put("playerSHA256", hash(Files.readAllBytes(inputs.playerFile)));
        report.put("levelNBT", inputs.level.toString());
        report.put("playerNBT", inputs.player.toString());
        Map<String, String> mods = new TreeMap<>();
        collectMods(inputs.level, mods);
        report.put("loadingMods", mods);
        writeJson(root.resolve("audit/provenance.json"), report);
        require("1.20.1".equals(data.getCompound("Version").getString("Name")), "unexpected Minecraft version");
        require("47.4.0".equals(mods.get("forge")) && "2.2.5".equals(mods.get("foodhealing")),
                "source Forge/Food Healing provenance not confirmed; inspect provenance.json");
        require(data.getCompound("Player").getUUID("UUID").equals(inputs.player.getUUID("UUID")),
                "level/player UUID disagreement");
        CompoundTag levelCaps = foodCaps(data.getCompound("Player"));
        CompoundTag playerCaps = foodCaps(inputs.player);
        // Do not choose one side as canonical if the two stored snapshots disagree.
        require(levelCaps.equals(playerCaps), "level.dat and playerdata Food Healing disagreement");
        for (String kind : List.of("level", "player")) {
            CompoundTag caps = kind.equals("level") ? levelCaps : playerCaps;
            verifyMemory(caps);
            Path fixture = root.resolve("fixture/caps-" + kind + ".dat");
            require(!Files.exists(fixture), "refusing to overwrite payload fixture");
            NbtIo.writeCompressed(caps, fixture.toFile());
            writeJson(root.resolve("audit/payload-" + kind + ".json"), Map.of(
                    "scope", "REAL V2.2.5 CAPABILITY FIXTURE", "payloadSHA256", hash(Files.readAllBytes(fixture)),
                    "payload", caps.toString(), "source", kind, "roundTrips", 100));
        }
        System.out.println("REAL_V225_PROVENANCE_PASS mods=" + mods.size() + " uuid=" + inputs.player.getUUID("UUID"));
        System.out.println("REAL_V225_NBT_PASS sources=2 roundTrips=100 raw=true defensiveCopy=true SP=2 fullWorldBoot=NOT_RUN");
    }

    static void verify(MinecraftServer server, Path serverRoot, boolean write, List<EmbeddedChannel> channels) throws Exception {
        Path root = checkedRoot(Path.of(System.getProperty("foodhealing.test.realLegacyRoot")));
        require(serverRoot.equals(root.resolve("migrated-test").toRealPath()), "wrong real legacy server directory");
        Inputs inputs = readInputs(root);
        for (String kind : List.of("level", "player")) {
            CompoundTag source = foodCaps(kind.equals("level") ? inputs.level.getCompound("Data").getCompound("Player") : inputs.player);
            Path payloadFile = root.resolve("fixture/caps-" + kind + ".dat");
            byte[] bytes = Files.readAllBytes(payloadFile);
            require(source.equals(NbtIo.readCompressed(payloadFile.toFile())), "fixture differs from actual extracted payload");
            verifyMemory(source);
            UUID id = UUID.nameUUIDFromBytes(("foodhealing-real-legacy-" + kind + hash(bytes)).getBytes(StandardCharsets.UTF_8));
            GameProfile profile = new GameProfile(id, "FHRealV2" + kind);
            Path playerFile = server.getWorldPath(LevelResource.PLAYER_DATA_DIR).resolve(id + ".dat");
            Path seedFile = root.resolve("fixture/player-seed-" + kind + ".dat");
            if (write) {
                require(!Files.exists(seedFile) && !Files.exists(playerFile), "refusing to overwrite player fixture");
                ServerPlayer template = new ServerPlayer(server, server.overworld(), profile);
                CompoundTag seed = template.saveWithoutId(new CompoundTag());
                // Vanilla-only template: do not load/remove the real world's other mods or inventory.
                for (String key : List.of(PROGRESS, DIVERSITY)) seed.getCompound("ForgeCaps").put(key, source.get(key).copy());
                NbtIo.writeCompressed(seed, seedFile.toFile());
                Files.createDirectories(playerFile.getParent());
                NbtIo.writeCompressed(seed, playerFile.toFile());
            }
            require(Files.isRegularFile(playerFile), "missing persisted real capability fixture");
            CompoundTag beforeLogin = NbtIo.readCompressed(playerFile.toFile());
            if (write) require(foodCaps(beforeLogin).equals(source), "seed altered actual payload");
            else verifyCanonical(beforeLogin.getCompound("ForgeCaps"), source);
            Connection connection = new Connection(PacketFlow.SERVERBOUND);
            EmbeddedChannel channel = new EmbeddedChannel(connection);
            channels.add(channel);
            ServerPlayer player = new ServerPlayer(server, server.overworld(), profile);
            server.getPlayerList().placeNewPlayer(connection, player);
            IShokugiData progress = player.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(IllegalStateException::new);
            IFoodDiversityData diversity = player.getCapability(FoodDiversityProvider.FOOD_DIVERSITY).orElseThrow(IllegalStateException::new);
            CompoundTag canonical = player.saveWithoutId(new CompoundTag());
            verifyCanonical(canonical.getCompound("ForgeCaps"), source);
            for (int i = 0; i < 20; i++) {
                CapabilityEvents.rebuildOwnedModifiers(player);
                FoodDiversityHandler.applyHealthBonus(player, diversity.getMaxHealthBonus());
            }
            require(progress.serializeNBT().equals(canonical.getCompound("ForgeCaps").getCompound(PROGRESS)),
                    "rebuild changed progression");
            require(player.getAttribute(Attributes.MAX_HEALTH).getModifiers().stream().filter(m ->
                    m.getId().equals(FoodDiversityHandler.HEALTH_BONUS_UUID)).count() == 1, "diversity modifier not idempotent");
            server.getPlayerList().saveAll();
            CompoundTag saved = NbtIo.readCompressed(playerFile.toFile());
            verifyCanonical(saved.getCompound("ForgeCaps"), source);
            require(Arrays.equals(bytes, Files.readAllBytes(payloadFile)), "payload source mutated");
            writeJson(root.resolve("audit/disk-" + kind + "-" + (write ? "write" : "read") + ".json"), Map.of(
                    "pid", ProcessHandle.current().pid(), "uuid", id.toString(), "payloadSHA256", hash(bytes),
                    "savedSHA256", hash(Files.readAllBytes(playerFile)), "savedCaps", foodCaps(saved).toString(),
                    "scope", "REAL LEGACY CAPABILITY FIXTURE / NOT FULL WORLD BOOT", "status", "PASS"));
            LogUtils.getLogger().info("FOODHEALING_REAL_LEGACY_PASS source={} phase={} pid={} payloadSHA256={}",
                    kind, write ? "write" : "read", ProcessHandle.current().pid(), hash(bytes));
            server.getPlayerList().remove(player);
        }
    }

    private static void verifyMemory(CompoundTag source) {
        CompoundTag original = source.copy();
        ShokugiData progress = new ShokugiData();
        FoodDiversityData diversity = new FoodDiversityData();
        CompoundTag mutable = source.copy();
        progress.deserializeNBT(mutable.getCompound(PROGRESS));
        diversity.deserializeNBT(mutable.getCompound(DIVERSITY));
        mutable.getCompound(PROGRESS).putInt("EatCount", -99);
        mutable.getCompound(PROGRESS).getList("DisabledSkills", Tag.TAG_STRING).add(StringTag.valueOf("test:external"));
        mutable.getCompound(DIVERSITY).getList("AllEatenFoods", Tag.TAG_STRING).clear();
        for (int i = 0; i < 100; i++) {
            CompoundTag output = new CompoundTag();
            output.put(PROGRESS, progress.serializeNBT());
            output.put(DIVERSITY, diversity.serializeNBT());
            verifyCanonical(output, original);
            progress.deserializeNBT(output.getCompound(PROGRESS));
            diversity.deserializeNBT(output.getCompound(DIVERSITY));
            output.getCompound(PROGRESS).getCompound("LegacyV2Backup").putInt("EatCount", -1);
            output.getCompound(PROGRESS).getCompound("LegacyV2Backup")
                    .getList("DisabledSkills", Tag.TAG_STRING).add(StringTag.valueOf("test:outside"));
            output.getCompound(DIVERSITY).getList("AllEatenFoods", Tag.TAG_STRING).clear();
        }
        require(source.equals(original), "audit mutated source payload");
    }

    private static void verifyCanonical(CompoundTag actual, CompoundTag source) {
        CompoundTag raw = source.getCompound(PROGRESS);
        CompoundTag progress = actual.getCompound(PROGRESS);
        require(progress.getInt("FoodHealingDataVersion") == 5 && progress.getCompound("LegacyV2Backup").equals(raw),
                "raw backup/schema mismatch");
        for (String key : List.of("ShokugiLevel", "EatCount")) {
            require(progress.getLong(key) == raw.getLong(key) * (key.equals("EatCount") ? 10L : 1L), "legacy value changed: " + key);
        }
        require(!progress.getBoolean("LegacyMigrationPending")
                && progress.getLong("LegacyShokugiLevel") == raw.getLong("ShokugiLevel")
                && progress.getLong("LegacyEatCount") == raw.getLong("EatCount"), "legacy pending metadata changed");
        require(progress.getLong("UnspentSkillPoints") == raw.getLong("ShokugiLevel") && progress.getLong("SpentSkillPoints") == 0
                && progress.getList("AcquiredSkills", Tag.TAG_COMPOUND).isEmpty() && progress.getCompound("BaseStats").isEmpty(),
                "legacy respec refund/ownership mismatch");
        require(progress.getList("DisabledSkills", Tag.TAG_STRING).isEmpty(), "respec toggle state not fresh");
        for (String key : List.of("RootAccumulatedNutrition", "RootAccumulationDeadline", "RootActiveUntil", "RootCooldownUntil", "RootReservedNutrition")) {
            require(progress.getLong(key) == 0, "invented Root state");
        }
        CompoundTag inputDiversity = source.getCompound(DIVERSITY);
        CompoundTag outputDiversity = actual.getCompound(DIVERSITY);
        for (String key : List.of("AllEatenFoods", "CurrentEatenFoods")) {
            require(strings(inputDiversity.getList(key, Tag.TAG_STRING)).equals(strings(outputDiversity.getList(key, Tag.TAG_STRING))),
                    "diversity history lost: " + key);
        }
        require(outputDiversity.getInt("MaxHealthBonus") == inputDiversity.getInt("MaxHealthBonus"), "diversity HP bonus lost");
    }

    private static Inputs readInputs(Path root) throws Exception {
        Path extracted = root.resolve("extracted-readonly").toRealPath();
        List<Path> levels;
        try (var walk = Files.walk(extracted, 2)) {
            levels = walk.filter(p -> p.getFileName().toString().equals("level.dat")).toList();
        }
        require(levels.size() == 1, "expected one extracted world, do not guess among several");
        Path levelFile = levels.get(0).toRealPath();
        require(levelFile.startsWith(extracted), "source escaped extracted root");
        CompoundTag level = NbtIo.readCompressed(levelFile.toFile());
        UUID id = level.getCompound("Data").getCompound("Player").getUUID("UUID");
        Path playerFile = levelFile.getParent().resolve("playerdata/" + id + ".dat").toRealPath();
        require(playerFile.startsWith(extracted), "player source escaped extracted root");
        return new Inputs(levelFile, playerFile, level, NbtIo.readCompressed(playerFile.toFile()));
    }

    private static CompoundTag foodCaps(CompoundTag player) {
        CompoundTag selected = new CompoundTag();
        for (String key : List.of(PROGRESS, DIVERSITY)) {
            require(player.getCompound("ForgeCaps").contains(key, Tag.TAG_COMPOUND), "missing actual capability: " + key);
            selected.put(key, player.getCompound("ForgeCaps").get(key).copy());
        }
        return selected;
    }

    private static void collectMods(CompoundTag tag, Map<String, String> mods) {
        for (String key : tag.getAllKeys()) {
            if (key.equals("LoadingModList")) {
                for (Tag entry : tag.getList(key, Tag.TAG_COMPOUND)) {
                    CompoundTag mod = (CompoundTag) entry;
                    mods.put(mod.getString("ModId"), mod.getString("ModVersion"));
                }
            } else if (tag.get(key) instanceof CompoundTag child) collectMods(child, mods);
        }
    }

    private static void collectReferences(Tag tag, String scope, Map<String, Set<String>> references) {
        if (tag instanceof CompoundTag compound) {
            for (String key : compound.getAllKeys()) collectReferences(compound.get(key), scope, references);
        } else if (tag instanceof ListTag list) {
            for (Tag entry : list) collectReferences(entry, scope, references);
        } else if (tag instanceof StringTag string) {
            String value = string.getAsString();
            if (value.matches("[a-z0-9_.-]+:[a-z0-9/._-]+")) {
                references.computeIfAbsent(scope + "/" + value.substring(0, value.indexOf(':')), k -> new TreeSet<>()).add(value);
            }
        }
    }

    private static Set<String> strings(ListTag list) {
        Set<String> result = new TreeSet<>();
        for (int i = 0; i < list.size(); i++) result.add(list.getString(i));
        require(result.size() == list.size(), "unexpected duplicate food identity in source/output");
        return result;
    }

    private static Path checkedRoot(Path path) throws Exception {
        Path root = path.toRealPath();
        require(root.getFileName().toString().matches("real-v225-migration-[0-9]{8}-[0-9]{6}")
                && root.getParent().getFileName().toString().equals("verification")
                && root.getParent().getParent().getFileName().toString().equals("build"), "not an isolated real legacy root");
        return root;
    }

    private static void writeJson(Path path, Object value) throws Exception {
        Files.writeString(path, new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(value),
                StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
    }

    private static String hash(byte[] bytes) throws Exception {
        return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
    }

    private static void require(boolean value, String message) {
        if (!value) throw new AssertionError(message);
    }

    private record Inputs(Path levelFile, Path playerFile, CompoundTag level, CompoundTag player) { }
}
