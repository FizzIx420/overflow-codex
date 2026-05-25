package com.fizz.overflowcodex;

import com.fizz.overflowcodex.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, OverflowCodex.MOD_ID);

    public static final Supplier<CreativeModeTab> OVERFLOW_TAB = CREATIVE_TABS.register("overflow_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.OVERFLOW_CODEX.get()))
                    .title(Component.translatable("itemGroup.overflow_codex"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.DORMANT_OVERFLOW_CODEX.get());
                        output.accept(ModItems.OVERFLOW_CODEX.get());
                    })
                    .build());
}
