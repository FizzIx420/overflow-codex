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
 * Sequence Delay glyph - delays subsequent glyph execution by ticks.
 * Enables spell choreography and timed sequences.
 * Amplify increases delay duration. Duration extends chain length.
 */
public class EffectSequenceDelay extends AbstractEffect {
    public static final EffectSequenceDelay INSTANCE = new EffectSequenceDelay();

    private static final int BASE_DELAY_TICKS = 10;

    public EffectSequenceDelay() {
        super(new ResourceLocation(OverflowCodex.MOD_ID, "glyph_sequence_delay"), "Sequence Delay");
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        int delay = BASE_DELAY_TICKS + (int)(spellStats.getAmpMultiplier() * 10);
        scheduleDelayedExecution(world, rayTraceResult, spellContext, delay, false);
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        int delay = BASE_DELAY_TICKS + (int)(spellStats.getAmpMultiplier() * 10);
        scheduleDelayedExecution(world, rayTraceResult, spellContext, delay, true);
    }

    private void scheduleDelayedExecution(Level world, Object hitResult, SpellContext spellContext, int delay, boolean isBlock) {
        if (world instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            serverLevel.getServer().tell(new net.minecraft.server.TickTask(
                    serverLevel.getServer().getTickCount() + delay,
                    () -> {
                        try {
                            SpellContext delayedContext = spellContext.withIndex(spellContext.getCurrentIndex() + 1);
                            SpellResolver delayedResolver = new SpellResolver(delayedContext);
                            if (isBlock && hitResult instanceof BlockHitResult blockHit) {
                                delayedResolver.onResolveOnBlock(blockHit);
                            } else if (!isBlock && hitResult instanceof EntityHitResult entityHit) {
                                delayedResolver.onResolveOnEntity(entityHit);
                            }
                        } catch (Exception e) {
                            OverflowCodex.LOGGER.warn("Delayed execution failed: {}", e.getMessage());
                        }
                    }
            ));
        }
    }

    @Override
    public int getDefaultManaCost() {
        return 50;
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentDuration.INSTANCE, AugmentAmplify.INSTANCE);
    }

    @Override
    public String getBookDescription() {
        return "Delays subsequent glyph execution by a short duration. Amplify increases the delay. Duration extends the delayed chain length. Enables spell choreography and timed sequences.";
    }

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(OverflowCodex.MOD_ID, "glyph_sequence_delay");
    }
}
