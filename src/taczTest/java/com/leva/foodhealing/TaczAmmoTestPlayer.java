package com.leva.foodhealing;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.LegacyRandomSource;

/** Counts actual conservation RNG calls, excluding TaCZ's separate sound/spread RNG. */
final class TaczAmmoTestPlayer extends ServerPlayer {
    private final ProbeRandom probe = new ProbeRandom();
    TaczAmmoTestPlayer(MinecraftServer server, ServerLevel level, GameProfile profile) { super(server, level, profile); }
    @Override public RandomSource getRandom() { return probe == null ? super.getRandom() : probe; }
    long rolls() { return probe.rolls; }

    private static final class ProbeRandom extends LegacyRandomSource {
        private long rolls;
        private ProbeRandom() { super(20260909L); }
        @Override public float nextFloat() {
            if (StackWalker.getInstance().walk(frames -> frames.anyMatch(frame ->
                    frame.getClassName().equals("com.leva.foodhealing.compat.tacz.TaczAmmoAdapter")
                            && frame.getMethodName().equals("consumeShot")))) rolls++;
            return super.nextFloat();
        }
    }
}
