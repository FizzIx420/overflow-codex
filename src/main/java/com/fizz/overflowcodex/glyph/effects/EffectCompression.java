package com.fizz.overflowcodex.glyph.effects;

import com.fizz.overflowcodex.OverflowCodex;
import net.minecraft.resources.ResourceLocation;

public class EffectCompression {
    public static final EffectCompression INSTANCE = new EffectCompression();
    public static final ResourceLocation GLYPH_ID = ResourceLocation.fromNamespaceAndPath(OverflowCodex.MOD_ID, "glyph_compression");
    public final int manaCost = 10;
    private EffectCompression() {}
    public ResourceLocation getRegistryName() { return GLYPH_ID; }
}
