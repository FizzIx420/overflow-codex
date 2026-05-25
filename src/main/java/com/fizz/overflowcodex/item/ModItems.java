package com.fizz.overflowcodex.item;

import com.fizz.overflowcodex.OverflowCodex;
import com.fizz.overflowcodex.ModCreativeTab;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(OverflowCodex.MOD_ID);

    public static final DeferredItem<DormantOverflowCodexItem> DORMANT_OVERFLOW_CODEX =
            ITEMS.register("dormant_overflow_codex",
                    () -> new DormantOverflowCodexItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<OverflowCodexItem> OVERFLOW_CODEX =
            ITEMS.register("overflow_codex",
                    () -> new OverflowCodexItem(new Item.Properties().stacksTo(1)));

    public static void register(net.neoforged.bus.api.IEventBus bus) {
        ITEMS.register(bus);
        ModCreativeTab.CREATIVE_TABS.register(bus);
    }
}
