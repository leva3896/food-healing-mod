package com.leva.foodhealing.compat;
import com.leva.foodhealing.compat.endinglibrary.TimeStopGate;
public final class EndingLibraryCompatibility {
    private EndingLibraryCompatibility() { }
    public static void initialize() {
        if(TimeStopGate.supported()) com.leva.foodhealing.compat.endinglibrary.TimeStopRuntime.initialize();
    }
}
