package com.leva.foodhealing.compat.endinglibrary;

import com.leva.foodhealing.network.PacketHandler;
import com.leva.foodhealing.network.TimeStopLeasePacket;
import io.netty.channel.Channel;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.network.PacketDistributor;
import java.util.concurrent.atomic.AtomicLong;

/** Transient server-owned identities. UUID equality never restores an old grant. */
public final class SessionRegistry {
    private static final AtomicLong SERIAL = new AtomicLong(), SEQUENCE = new AtomicLong();
    public static final class Session {
        public final long number;
        public final ServerPlayer player;
        public final ServerGamePacketListenerImpl listener;
        public final Connection connection;
        public final Channel channel;
        final AuthorityContext context;
        final net.minecraft.server.level.ServerLevel level;
        final long epochAtLogin;
        boolean revoked, freshEpochRequired;
        long revision;
        int leaseTicks;
        Session(ServerPlayer p) {
            number = SERIAL.incrementAndGet(); player = p; listener = p.connection;
            connection = listener.connection; channel = channel(connection); level = p.serverLevel();
            context = AuthorityContext.of(level); epochAtLogin = Ownership.state(level).epoch;
            freshEpochRequired = Ownership.global() || Ownership.stopped(level);
        }
    }
    public static final class Grant {
        public final Session session;
        public final long epoch, sequence;
        public boolean revoked;
        Grant(Session s, long e) { session = s; epoch = e; sequence = SEQUENCE.incrementAndGet(); }
    }
    public static Channel channel(Connection c) {
        if (c == null) return null;
        try { return (Channel)ObfuscationReflectionHelper.findField(Connection.class, "f_129468_").get(c); }
        catch (ReflectiveOperationException failure) { return null; }
    }
    public static Session current(ServerPlayer p) { return AuthorityContext.of(p.serverLevel()).sessions.get(p); }
    public static Session login(ServerPlayer p) {
        var c = AuthorityContext.of(p.serverLevel());
        if (c.closed || !p.getServer().isSameThread() || p.connection == null || channel(p.connection.connection) == null) return null;
        revoke(p, "new-session"); var s = new Session(p); c.sessions.put(p, s); return s;
    }
    public static boolean identity(Session s, ServerGamePacketListenerImpl l) {
        return s != null && !s.revoked && !s.context.closed && s.context.server.isSameThread()
                && s.context.server == l.player.getServer() && s.context.sessions.get(l.player) == s
                && s.player == l.player && s.level == l.player.serverLevel() && s.listener == l
                && l.player.connection == l && s.connection == l.connection && channel(l.connection) == s.channel
                && s.channel != null && s.channel.isOpen() && s.connection.isConnected()
                && !s.player.hasDisconnected() && !s.player.isRemoved();
    }
    public static boolean eligible(ServerPlayer p) { var s = current(p); return identity(s,p.connection) && !s.freshEpochRequired; }
    public static Grant get(ServerPlayer p) { var s = current(p); return s == null ? null : s.context.grants.get(s); }
    public static boolean matches(Grant g, ServerGamePacketListenerImpl l) {
        return g != null && !g.revoked && identity(g.session,l) && get(l.player) == g
                && g.epoch == Ownership.state(l.player.serverLevel()).epoch;
    }
    public static void revokeGrant(ServerPlayer p, String reason) {
        var s = current(p); if (s == null) return;
        var g = s.context.grants.remove(s);
        if (g != null) { g.revoked = true; send(s,false); }
    }
    public static void revoke(ServerPlayer p, String reason) {
        var s = current(p); if (s == null) return;
        revokeGrant(p,reason); s.revoked = true; s.context.sessions.remove(p);
    }
    static void tick(ServerPlayer p) {
        var s = current(p);
        if (s == null) s = login(p);
        if (!identity(s,p.connection)) { revoke(p,"identity-lost"); return; }
        var state = Ownership.state(p.serverLevel());
        if (state.quiet && !state.unknown && !s.context.engine.terminalFault && !Ownership.global() && !Ownership.stopped(p.serverLevel()))
            s.freshEpochRequired = false;
        boolean allowed = Ownership.permission(p);
        Grant g = get(p);
        if (!allowed || (g != null && !matches(g,p.connection))) { revokeGrant(p,"provenance-lost"); return; }
        if (g == null) {
            if (state.epoch <= s.epochAtLogin) return;
            g = new Grant(s,state.epoch); s.context.grants.put(s,g); send(s,true);
        // World gameTime is frozen by EndingLibrary. Count authority ticks instead.
        } else if (++s.leaseTicks >= 5) send(s,true);
    }
    private static void send(Session s, boolean allowed) {
        if (!s.connection.isConnected()) return;
        var g = s.context.grants.get(s); var state = Ownership.state(s.level);
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> s.player),
                new TimeStopLeasePacket(s.player.getUUID(),s.level.dimension().location(),s.number,state.epoch,
                        g == null ? 0 : g.sequence,++s.revision,allowed && g != null && !state.unknown,1000));
        s.leaseTicks = 0;
    }
}
