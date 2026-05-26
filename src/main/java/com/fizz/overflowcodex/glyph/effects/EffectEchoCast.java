package com.fizz.overflowcodex.glyph.effects;

import com.fizz.overflowcodex.OverflowCodex;
import net.minecraft.resources.ResourceLocation;

public class EffectEchoCast {
    public static final EffectEchoCast INSTANCE = new EffectEchoCast();
    public static final ResourceLocation GLYPH_ID = ResourceLocation.fromNamespaceAndPath(OverflowCodex.MOD_ID, "glyph_echo_cast");
    public final int manaCost = 120;
    private EffectEchoCast() {}
    public ResourceLocation getRegistryName() { return GLYPH_ID; }
}
