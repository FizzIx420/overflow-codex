package com.fizz.overflowcodex.recipe;

import com.fizz.overflowcodex.OverflowCodex;
import net.neoforged.bus.api.IEventBus;

public class ModRecipes {
    public static void register(IEventBus bus) {
        // Recipe types are statically registered via their serializers
        OverflowCodex.LOGGER.info("Overflow Codex recipe types registered");
    }
}
