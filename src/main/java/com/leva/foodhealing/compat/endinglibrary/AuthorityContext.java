package com.leva.foodhealing.compat.endinglibrary;

import com.mega.endinglib.common.capability.EndingLibraryLivingCapability;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import java.util.*;

/** Owned by the actual server; no static server history and no persisted permission. */
public final class AuthorityContext {
    final MinecraftServer server;
    final IdentityHashMap<ServerLevel, Ownership.State> states = new IdentityHashMap<>();
    final TerminalWitness engine = new TerminalWitness();
    final Deque<Ownership.Frame> calls = new ArrayDeque<>();
    final IdentityHashMap<EndingLibraryLivingCapability, EntryDeserialize.Frame> deserializing = new IdentityHashMap<>();
    final IdentityHashMap<ServerPlayer, SessionRegistry.Session> sessions = new IdentityHashMap<>();
    final IdentityHashMap<SessionRegistry.Session, SessionRegistry.Grant> grants = new IdentityHashMap<>();
    InitialStart.Frame initialFrame;
    long nextEpoch;
    boolean closed;
    public AuthorityContext(MinecraftServer server) { this.server = Objects.requireNonNull(server); }
    static AuthorityContext of(MinecraftServer server) { return ((TimeStopContextHolder)server).foodhealing$timeStopContext(); }
    static AuthorityContext of(ServerLevel level) { return of(level.getServer()); }
    static void close(MinecraftServer server) {
        var c = of(server);
        for (var session : new ArrayList<>(c.sessions.values())) SessionRegistry.revoke(session.player, "server-close");
        c.closed = true;
        c.states.clear(); c.calls.clear(); c.deserializing.clear(); c.initialFrame = null;
        c.engine.bindings.clear(); c.engine.frames.clear(); c.engine.witnesses.clear(); c.engine.stack.clear();
    }
    Ownership.State state(ServerLevel level) {
        if (level.getServer() != server) throw new IllegalArgumentException("Authority server mismatch");
        return states.computeIfAbsent(level, l -> new Ownership.State(this, l));
    }
}
