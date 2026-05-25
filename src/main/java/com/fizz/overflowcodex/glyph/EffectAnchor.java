package com.fizz.overflowcodex.glyph;

import com.fizz.overflowcodex.OverflowCodex;
import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Set;

/**
 * Anchor glyph - stores the current spell state for later glyph reference.
 * Essential for complex spell architectures where later glyphs need to
 * reference a prior execution point.
 */
public class EffectAnchor extends AbstractEffect {
    public static final EffectAnchor INSTANCE = new EffectAnchor();

    public EffectAnchor() {
        super(new ResourceLocation(OverflowCodex.MOD_ID, "glyph_anchor"), "Anchor");
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        // Store anchor point index in spell context stats
        spellContext.setStackShorthand(EffectAnchor.class, spellContext.getCurrentIndex());
        OverflowCodex.LOGGER.debug("Anchor set at index {}", spellContext.getCurrentIndex());
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        spellContext.setStackShorthand(EffectAnchor.class, spellContext.getCurrentIndex());
        OverflowCodex.LOGGER.debug("Anchor set at index {}", spellContext.getCurrentIndex());
    }

    @Override
    public int getDefaultManaCost() {
        return 80;
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf();
    }

    @Override
    public String getBookDescription() {
        return "Stores the current spell state as an anchor point. Other glyphs later in the chain can reference this point. Essential for complex spell architectures.";
    }

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(OverflowCodex.MOD_ID, "glyph_anchor");
    }
}
