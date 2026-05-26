package com.fizz.overflowcodex.glyph.effects;

import com.fizz.overflowcodex.OverflowCodex;
import net.minecraft.resources.ResourceLocation;

public class EffectSequenceDelay {
    public static final EffectSequenceDelay INSTANCE = new EffectSequenceDelay();
    public static final ResourceLocation GLYPH_ID = ResourceLocation.fromNamespaceAndPath(OverflowCodex.MOD_ID, "glyph_sequence_delay");
    public final int manaCost = 25;
    private EffectSequenceDelay() {}
    public ResourceLocation getRegistryName() { return GLYPH_ID; }
}
