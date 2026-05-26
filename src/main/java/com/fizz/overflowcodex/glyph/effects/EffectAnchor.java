package com.fizz.overflowcodex.glyph.effects;

import com.fizz.overflowcodex.OverflowCodex;
import net.minecraft.resources.ResourceLocation;

public class EffectAnchor {
    public static final EffectAnchor INSTANCE = new EffectAnchor();
    public static final ResourceLocation GLYPH_ID = ResourceLocation.fromNamespaceAndPath(OverflowCodex.MOD_ID, "glyph_anchor");
    public final int manaCost = 40;
    private EffectAnchor() {}
    public ResourceLocation getRegistryName() { return GLYPH_ID; }
}
