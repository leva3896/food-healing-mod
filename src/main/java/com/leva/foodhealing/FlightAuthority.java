package com.leva.foodhealing;

/** A read-only answer from the explicitly supported providers, not all Minecraft mods. */
public record FlightAuthority(boolean positive, boolean deny, boolean observed) {
    public static final FlightAuthority NEUTRAL = new FlightAuthority(false, false, true);
    public static final FlightAuthority UNKNOWN = new FlightAuthority(false, false, false);

    public FlightAuthority and(FlightAuthority other) {
        return new FlightAuthority(positive || other.positive, deny || other.deny,
                observed && other.observed);
    }
}
