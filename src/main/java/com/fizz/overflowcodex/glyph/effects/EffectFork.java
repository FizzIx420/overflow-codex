package com.fizz.overflowcodex.glyph.effects;

import com.fizz.overflowcodex.OverflowCodex;
import net.minecraft.resources.ResourceLocation;

public class EffectFork {
    public static final EffectFork INSTANCE = new EffectFork();
    public static final ResourceLocation GLYPH_ID = ResourceLocation.fromNamespaceAndPath(OverflowCodex.MOD_ID, "glyph_fork");
    public final int manaCost = 150;
    private EffectFork() {}
    public ResourceLocation getRegistryName() { return GLYPH_ID; }
}
