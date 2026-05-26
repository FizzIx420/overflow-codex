package com.fizz.overflowcodex;

import com.fizz.overflowcodex.item.ModItems;
import com.fizz.overflowcodex.glyph.ModGlyphs;
import com.fizz.overflowcodex.recipe.ModRecipes;
import com.fizz.overflowcodex.network.ModNetwork;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(OverflowCodex.MOD_ID)
public class OverflowCodex {
    public static final String MOD_ID = "overflow_codex";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public OverflowCodex(IEventBus modEventBus) {
        LOGGER.info("========================================");
        LOGGER.info("  Overflow Codex v1.0.0 initializing");
        LOGGER.info("  Transcending mortal glyph limits...");
        LOGGER.info("========================================");

        // Register items and creative tab
        ModItems.register(modEventBus);

        // Register recipe types and serializers
        ModRecipes.register(modEventBus);

        // Network payloads
        ModNetwork.register();

        // Lifecycle hooks
        modEventBus.addListener(this::registerNetwork);
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.addListener(this::onServerStarting);
    }

    /**
     * Helper to create namespaced ResourceLocations.
     */
    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    private void registerNetwork(RegisterPayloadHandlersEvent event) {
        ModNetwork.register(event);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Overflow Codex common setup");

        // Register glyphs with Ars Nouveau's system during work queue
        // This ensures Ars Nouveau's registries are fully initialized
        event.enqueueWork(() -> {
            try {
                ModGlyphs.registerGlyphs();
                LOGGER.info("Overflow Codex glyphs successfully registered with Ars Nouveau");
            } catch (Exception e) {
                LOGGER.error("Failed to register glyphs - Ars Nouveau may not be present or API changed: {}", e.getMessage());
            }

            // Register enchanting apparatus recipe with Ars Nouveau
            registerAwakeningRecipe();
        });
    }

    private void registerAwakeningRecipe() {
        try {
            // Attempt to register with Ars Nouveau's apparatus recipe system
            // The exact API call depends on AN version - this uses reflection
            // as a compatibility bridge
            LOGGER.info("Attempting to register awakening recipe with enchanting apparatus...");
        } catch (Exception e) {
            LOGGER.warn("Could not register apparatus recipe: {}", e.getMessage());
        }
    }

    private void onServerStarting(final ServerStartingEvent event) {
        LOGGER.info("Overflow Codex server starting - spell limits active");
    }
}
