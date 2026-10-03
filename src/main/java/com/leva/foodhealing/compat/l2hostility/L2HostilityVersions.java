package com.leva.foodhealing.compat.l2hostility;

/** Safe during Mixin preparation: deliberately has no Minecraft or external MOD linkage. */
public final class L2HostilityVersions {
    private L2HostilityVersions() { }

    public static boolean supports(String hostility, String library, String complements, String tracker) {
        return "2.5.19".equals(hostility) && "2.5.3".equals(library)
                && "2.6.1".equals(complements) && "0.4.4".equals(tracker);
    }

}
