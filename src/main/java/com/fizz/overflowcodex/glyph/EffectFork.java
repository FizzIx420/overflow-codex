package com.fizz.overflowcodex.glyph;

import com.fizz.overflowcodex.OverflowCodex;
import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Set;

/**
 * Fork glyph - duplicates the spell execution path.
 * Creates two parallel branches that execute independently.
 * Maximum 3 per spell to prevent server overload.
 *
 * IMPORTANT: This glyph extends AbstractEffect from Ars Nouveau.
 * If the AN API has changed in 5.10.6, adjust the method signatures
 * to match the current AbstractEffect interface.
 */
public class EffectFork extends AbstractEffect {
    public static final EffectFork INSTANCE = new EffectFork();

    public EffectFork() {
        super(new ResourceLocation(OverflowCodex.MOD_ID, "glyph_fork"), "Fork");
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (spellContext.getSpell().size() > spellContext.getCurrentIndex() + 1) {
            Spell spell = spellContext.getSpell();
            int currentIndex = spellContext.getCurrentIndex();

            // Branch 1: continue from next glyph
            SpellContext branch1 = spellContext.withIndex(currentIndex + 1);
            new SpellResolver(branch1).onResolveOnEntity(rayTraceResult);

            // Branch 2: skip ahead (half of remaining)
            int branchLength = Math.min(5, spell.size() - currentIndex - 1);
            SpellContext branch2 = spellContext.withIndex(currentIndex + branchLength);
            if (branch2.getCurrentIndex() < spell.size()) {
                new SpellResolver(branch2).onResolveOnEntity(rayTraceResult);
            }
        }
    }

    @Override
    public void onResolveBlock(net.minecraft.world.phys.BlockHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (spellContext.getSpell().size() > spellContext.getCurrentIndex() + 1) {
            Spell spell = spellContext.getSpell();
            int currentIndex = spellContext.getCurrentIndex();
            int branchLength = Math.min(5, spell.size() - currentIndex - 1);

            SpellContext branch1 = spellContext.withIndex(currentIndex + 1);
            new SpellResolver(branch1).onResolveOnBlock(rayTraceResult);

            SpellContext branch2 = spellContext.withIndex(currentIndex + branchLength);
            if (branch2.getCurrentIndex() < spell.size()) {
                new SpellResolver(branch2).onResolveOnBlock(rayTraceResult);
            }
        }
    }

    @Override
    public int getDefaultManaCost() {
        return 150;
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf();
    }

    @Override
    public String getBookDescription() {
        return "Duplicates the spell execution path, creating two parallel branches. Each branch executes independently. Extremely mana intensive. Maximum 3 per spell.";
    }

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(OverflowCodex.MOD_ID, "glyph_fork");
    }
}
