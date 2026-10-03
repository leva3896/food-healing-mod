package com.leva.foodhealing.compat.endinglibrary;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import com.mega.endinglib.util.time.TimeStopUtils;
public final class MovementGuard {
    private MovementGuard() { }
    public static boolean deny(ServerGamePacketListenerImpl listener) {
        return TimeStopUtils.andSameDimension(listener.player.serverLevel()) && !TimeStopUtils.canMove(listener.player)
                && !(Ownership.permission(listener.player) && SessionRegistry.matches(SessionRegistry.get(listener.player),listener));
    }
}
