package com.fizz.overflowcodex.item;

import com.fizz.overflowcodex.OverflowCodex;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipContext;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OverflowCodexItem extends Item {
    public static final int MAX_GLYPHS = 30;
    public static final int MANA_COST_MULTIPLIER = 3;
    public static final int COMPLEXITY_CAP = 500;

    public OverflowCodexItem(Properties properties) {
        super(properties.rarity(Rarity.EPIC));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            com.fizz.overflowcodex.client.screen.ArcaneWeaveScreen.open();
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.overflow_codex.awakened.line1")
                .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
        tooltip.add(Component.translatable("tooltip.overflow_codex.awakened.line2")
                .withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.translatable("tooltip.overflow_codex.awakened.line3")
                .withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.translatable("tooltip.overflow_codex.awakened.line4")
                .withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.translatable("tooltip.overflow_codex.awakened.line5")
                .withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.translatable("tooltip.overflow_codex.awakened.warning")
                .withStyle(ChatFormatting.RED, ChatFormatting.ITALIC));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    /**
     * Calculates mana cost scaling for spells beyond 10 glyphs.
     * Spells with 1-10 glyphs use normal cost, beyond that cost scales exponentially.
     */
    public static int calculateManaScaling(int glyphCount) {
        if (glyphCount <= 10) return 1;
        int overflow = glyphCount - 10;
        return 1 + (overflow * overflow) / 10;
    }

    /**
     * Calculates instability risk for complex spells.
     * Returns a value from 0.0 to 1.0 representing misfire chance.
     */
    public static float calculateInstability(int glyphCount, int complexity) {
        if (glyphCount <= 15 && complexity <= 200) return 0.0f;
        float glyphFactor = Math.max(0, (glyphCount - 15)) / 15.0f;
        float complexFactor = Math.max(0, (complexity - 200)) / 300.0f;
        return Math.min(1.0f, (glyphFactor + complexFactor) * 0.5f);
    }

    /**
     * Validates a spell doesn't exceed hard limits.
     */
    public static boolean validateSpellLimits(int forkCount, int echoCount, int complexity) {
        return forkCount <= 3 && echoCount <= 2 && complexity <= COMPLEXITY_CAP;
    }
}
