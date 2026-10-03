package com.leva.foodhealing.compat.fantasyending;

/** Metadata-only gate, safe before gameplay class loading. */
public final class FantasyEndingVersions {
    private FantasyEndingVersions() { }
    public static boolean supports(String version) { return "2.7.20".equals(version); }
}
