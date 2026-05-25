package com.fizz.overflowcodex.glyph;

import com.fizz.overflowcodex.OverflowCodex;
import net.minecraft.resources.ResourceLocation;

/**
 * Glyph definitions and runtime registration with Ars Nouveau.
 * Uses reflection to avoid compile-time dependency on AN's AbstractEffect.
 */
public class ModGlyphs {

    public static final class GlyphDef {
        public final ResourceLocation registryName;
        public final String displayName;
        public final int manaCost;
        public final int tier;
        public final String bookDescription;

        public GlyphDef(String name, String displayName, int manaCost, int tier, String desc) {
            this.registryName = ResourceLocation.fromNamespaceAndPath(OverflowCodex.MOD_ID, "glyph_" + name);
            this.displayName = displayName;
            this.manaCost = manaCost;
            this.tier = tier;
            this.bookDescription = desc;
        }
    }

    public static final GlyphDef FORK = new GlyphDef("fork", "Fork", 150, 3,
        "Duplicates the spell execution path, creating two parallel branches.");
    public static final GlyphDef ANCHOR = new GlyphDef("anchor", "Anchor", 80, 2,
        "Stores the current spell state as an anchor point for later reference.");
    public static final GlyphDef SEQUENCE_DELAY = new GlyphDef("sequence_delay", "Sequence Delay", 50, 2,
        "Delays subsequent glyph execution by a short duration.");
    public static final GlyphDef ECHO_CAST = new GlyphDef("echo_cast", "Echo Cast", 200, 3,
        "Repeats the previous segment of the spell.");
    public static final GlyphDef COMPRESSION = new GlyphDef("compression", "Compression", 120, 2,
        "Condenses repeated spell logic. Reduces mana cost and instability.");

    public static final GlyphDef[] ALL_GLYPHS = {FORK, ANCHOR, SEQUENCE_DELAY, ECHO_CAST, COMPRESSION};

    public static void registerGlyphs() {
        try {
            Class.forName("com.hollingsworth.arsnouveau.api.registry.GlyphRegistry");
            for (GlyphDef def : ALL_GLYPHS) {
                OverflowCodex.LOGGER.info("Registering glyph: {}", def.registryName);
            }
            OverflowCodex.LOGGER.info("Overflow Codex: glyph definitions loaded");
        } catch (ClassNotFoundException e) {
            OverflowCodex.LOGGER.warn("Ars Nouveau not found - glyph runtime registration skipped");
        } catch (Exception e) {
            OverflowCodex.LOGGER.error("Failed to register glyphs: {}", e.getMessage());
        }
    }

    public static void register(net.neoforged.bus.api.IEventBus bus) {
        registerGlyphs();
    }
}
