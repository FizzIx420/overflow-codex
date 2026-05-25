package com.fizz.overflowcodex.network;

import com.fizz.overflowcodex.OverflowCodex;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModNetwork {
    private static final String PROTOCOL_VERSION = "1";

    public static void register() {
        OverflowCodex.LOGGER.info("Registering network payloads for Overflow Codex");
    }
}
