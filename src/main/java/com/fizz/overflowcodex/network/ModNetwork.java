package com.fizz.overflowcodex.network;

import com.fizz.overflowcodex.OverflowCodex;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModNetwork {
    private static final String PROTOCOL_VERSION = "1";

    /**
     * No-op for backward compatibility.
     */
    public static void register() {
        OverflowCodex.LOGGER.info("Registering network payloads for Overflow Codex");
    }

    /**
     * Register network payloads using NeoForge's payload registration event.
     */
    public static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");
        // Just log - full payloads will be in a future update
        OverflowCodex.LOGGER.info("Network payloads registered");
    }
}
