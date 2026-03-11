package com.example.examplemod;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class GutsEffect extends MobEffect {
    public GutsEffect() {
        // BENEFICIAL: 有益なエフェクト
        // 0xF82424: 「即時回復」に近い赤色
        super(MobEffectCategory.BENEFICIAL, 0xF82424);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        // 定期的な処理（tickごとの動作）は行わないのでfalse
        return false;
    }
}
