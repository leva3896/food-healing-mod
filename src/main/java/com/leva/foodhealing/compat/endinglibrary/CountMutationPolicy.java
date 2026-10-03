package com.leva.foodhealing.compat.endinglibrary;
/** An unbound zero-to-zero write cannot introduce, prolong, or end a source. */
public final class CountMutationPolicy {
    private CountMutationPolicy() { }
    public static boolean inertZero(int previous,int next,boolean hasOrigin,boolean hasBinding,boolean hasFrame) {
        return previous==0&&next==0&&!hasOrigin&&!hasBinding&&!hasFrame;
    }
}
