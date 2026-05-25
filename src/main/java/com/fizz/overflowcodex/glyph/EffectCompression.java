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
 * Compression glyph - condenses repeated spell logic into a single node.
 * Reduces mana cost by 50% and lowers spell instability for following glyphs.
 * Essential for managing 30-glyph spell stability.
 */
public class EffectCompression extends AbstractEffect {
    public static final EffectCompression INSTANCE = new EffectCompression();

    public EffectCompression() {
        super(new ResourceLocation(OverflowCodex.MOD_ID, "glyph_compression"), "Compression");
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        applyCompression(spellStats, spellContext);
        // Continue executing remaining glyphs
        if (spellContext.getSpell().size() > spellContext.getCurrentIndex() + 1) {
            SpellContext compressedContext = spellContext.withIndex(spellContext.getCurrentIndex() + 1);
            new SpellResolver(compressedContext).onResolveOnEntity(rayTraceResult);
        }
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        applyCompression(spellStats, spellContext);
        if (spellContext.getSpell().size() > spellContext.getCurrentIndex() + 1) {
            SpellContext compressedContext = spellContext.withIndex(spellContext.getCurrentIndex() + 1);
            new SpellResolver(compressedContext).onResolveOnBlock(rayTraceResult);
        }
    }

    private void applyCompression(SpellStats spellStats, SpellContext spellContext) {
        spellStats.addBuff(EffectCompression.class, 50);
        OverflowCodex.LOGGER.debug("Compression applied - reducing mana cost and instability");
    }

    @Override
    public int getDefaultManaCost() {
        return 120;
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
        return "Condenses repeated spell logic into a single compressed node. Reduces mana cost by 50%% and lowers spell instability for the following glyphs. Essential for managing 30-glyph spell stability.";
    }

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(OverflowCodex.MOD_ID, "glyph_compression");
    }
}
