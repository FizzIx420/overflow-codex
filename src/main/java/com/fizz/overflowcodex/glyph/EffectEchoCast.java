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
 * Echo Cast glyph - repeats the previous spell segment.
 * Dangerous when combined with recursive or fork chains.
 * Hard cap: maximum 2 Echo Cast glyphs per spell.
 */
public class EffectEchoCast extends AbstractEffect {
    public static final EffectEchoCast INSTANCE = new EffectEchoCast();

    public EffectEchoCast() {
        super(new ResourceLocation(OverflowCodex.MOD_ID, "glyph_echo_cast"), "Echo Cast");
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        int echoCount = 1 + (int) spellStats.getAmpMultiplier();
        echoCount = Math.min(echoCount, 2); // Hard cap

        int currentIndex = spellContext.getCurrentIndex();
        int lookback = Math.min(5, currentIndex);

        if (world instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            for (int i = 0; i < echoCount; i++) {
                final int iteration = i;
                serverLevel.getServer().tell(new net.minecraft.server.TickTask(
                        serverLevel.getServer().getTickCount() + 5 * (i + 1),
                        () -> {
                            try {
                                SpellContext echoContext = spellContext.withIndex(currentIndex - lookback);
                                SpellResolver echoResolver = new SpellResolver(echoContext);
                                echoResolver.onResolveOnEntity(rayTraceResult);
                            } catch (Exception e) {
                                OverflowCodex.LOGGER.warn("Echo cast iteration {} failed: {}", iteration, e.getMessage());
                            }
                        }
                ));
            }
        }
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        int echoCount = 1 + (int) spellStats.getAmpMultiplier();
        echoCount = Math.min(echoCount, 2);

        int currentIndex = spellContext.getCurrentIndex();
        int lookback = Math.min(5, currentIndex);

        if (world instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            for (int i = 0; i < echoCount; i++) {
                final int iteration = i;
                serverLevel.getServer().tell(new net.minecraft.server.TickTask(
                        serverLevel.getServer().getTickCount() + 5 * (i + 1),
                        () -> {
                            try {
                                SpellContext echoContext = spellContext.withIndex(currentIndex - lookback);
                                SpellResolver echoResolver = new SpellResolver(echoContext);
                                echoResolver.onResolveOnBlock(rayTraceResult);
                            } catch (Exception e) {
                                OverflowCodex.LOGGER.warn("Echo cast iteration {} failed: {}", iteration, e.getMessage());
                            }
                        }
                ));
            }
        }
    }

    @Override
    public int getDefaultManaCost() {
        return 200;
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentAmplify.INSTANCE);
    }

    @Override
    public String getBookDescription() {
        return "Repeats the previous segment of the spell. Dangerous when combined with recursive or fork chains. Maximum 2 echo glyphs per spell. Each echo amplification increases the repeat count.";
    }

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(OverflowCodex.MOD_ID, "glyph_echo_cast");
    }
}
