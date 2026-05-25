package com.fizz.overflowcodex.recipe;

import com.fizz.overflowcodex.OverflowCodex;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    public static final DeferredRegister<?> SERIALIZERS =
            DeferredRegister.create(net.minecraft.core.registries.BuiltInRegistries.RECIPE_SERIALIZER, OverflowCodex.MOD_ID);

    public static final DeferredRegister<?> TYPES =
            DeferredRegister.create(net.minecraft.core.registries.BuiltInRegistries.RECIPE_TYPE, OverflowCodex.MOD_ID);

    public static void register(IEventBus bus) {
        SERIALIZERS.register(bus);
        TYPES.register(bus);
    }
}
