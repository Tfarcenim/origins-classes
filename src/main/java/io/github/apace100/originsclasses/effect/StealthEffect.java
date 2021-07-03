package io.github.apace100.originsclasses.effect;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class StealthEffect extends Effect {

    public static final Effect INSTANCE = new StealthEffect(EffectType.BENEFICIAL, 0x242424);

    protected StealthEffect(EffectType type, int color) {
        super(type, color);
    }

}
