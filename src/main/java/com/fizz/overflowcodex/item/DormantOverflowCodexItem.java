package com.fizz.overflowcodex.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipContext;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DormantOverflowCodexItem extends Item {
    public DormantOverflowCodexItem(Properties properties) {
        super(properties.rarity(Rarity.EPIC));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.overflow_codex.dormant.line1")
                .withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
        tooltip.add(Component.translatable("tooltip.overflow_codex.dormant.line2")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.overflow_codex.dormant.line3")
                .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }
}
