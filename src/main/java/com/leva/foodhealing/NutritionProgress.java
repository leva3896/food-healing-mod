package com.leva.foodhealing;

/** Checked, side-effect-free arithmetic for the Nutrition count model. */
public final class NutritionProgress {
    private NutritionProgress() { }

    public record Update(long count, long level, long unspent, long gained) { }

    public static Update add(long count, long level, long unspent, long units, long threshold) {
        if (threshold <= 0 || count < 0 || count >= threshold || level < 0 || unspent < 0 || units <= 0)
            throw new IllegalArgumentException("Invalid Nutrition progression input");
        long total = Math.addExact(count, units);
        long gained = total / threshold;
        return new Update(total % threshold, Math.addExact(level, gained),
                Math.addExact(unspent, gained), gained);
    }

    public static long convert(long count, long oldThreshold, long newThreshold) {
        if (oldThreshold <= 0 || newThreshold <= 0 || count < 0 || count >= oldThreshold)
            throw new IllegalArgumentException("Invalid legacy partial progress");
        return Math.multiplyExact(count, newThreshold) / oldThreshold;
    }
}
