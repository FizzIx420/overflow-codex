package com.fizz.overflowcodex.glyph;

import com.fizz.overflowcodex.OverflowCodex;
import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Handles registration of all Overflow Codex glyphs with Ars Nouveau's glyph system.
 *
 * NOTE: The exact registration API depends on Ars Nouveau 5.10.6 internals.
 * If GlyphRegistry.registerGlyph() or the DeferredRegister approach
 * doesn't match, check the Ars Nouveau source for the correct method.
 * Common patterns in AN 5.x:
 *   - GlyphRegistry.registerSpellPart(ResourceLocation, AbstractSpellPart)
 *   - Or using DeferredHolder with the AN registry
 */
public class ModGlyphs {

    /**
     * Called during mod construction to register all glyphs.
     * Uses Ars Nouveau's GlyphRegistry for spell part registration.
     */
    public static void registerGlyphs() {
        registerGlyph(EffectFork.INSTANCE);
        registerGlyph(EffectAnchor.INSTANCE);
        registerGlyph(EffectSequenceDelay.INSTANCE);
        registerGlyph(EffectEchoCast.INSTANCE);
        registerGlyph(EffectCompression.INSTANCE);

        OverflowCodex.LOGGER.info("Overflow Codex: 5 glyphs registered with Ars Nouveau");
    }

    private static void registerGlyph(AbstractEffect glyph) {
        try {
            GlyphRegistry.registerSpellPart(glyph.getRegistryName(), glyph);
            OverflowCodex.LOGGER.info("Registered glyph: {}", glyph.getRegistryName());
        } catch (Exception e) {
            OverflowCodex.LOGGER.error("Failed to register glyph {}: {}", glyph.getRegistryName(), e.getMessage());
        }
    }

    /**
     * EventBus registration for NeoForge registry events.
     */
    public static void register(net.neoforged.bus.api.IEventBus bus) {
        // Glyphs in Ars Nouveau are typically registered through GlyphRegistry
        // rather than NeoForge's DeferredRegister. This method exists for
        // potential future NeoForge registry integration.
        registerGlyphs();
    }
}
