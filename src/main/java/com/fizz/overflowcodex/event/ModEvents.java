package com.fizz.overflowcodex.event;

import com.fizz.overflowcodex.OverflowCodex;
import com.fizz.overflowcodex.item.OverflowCodexItem;
import com.fizz.overflowcodex.integration.OverflowSpellResolver;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * Game event handlers for the Overflow Codex mod.
 * Handles sneaking right-click casting and passive instability aura.
 */
@Mod.EventBusSubscriber(modid = OverflowCodex.MOD_ID, bus = Mod.EventBusSubscriber.Bus.GAME)
public class ModEvents {

    /**
     * When player is sneaking + right-clicks with OverflowCodexItem, cast the spell.
     */
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!player.isShiftKeyDown()) return;

        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof OverflowCodexItem)) return;

        // Prevent normal item use; handle spell casting instead
        event.setCanceled(true);

        OverflowCodex.LOGGER.info("Player {} shift-right-clicked Overflow Codex, casting spell",
                player.getName().getString());

        OverflowSpellResolver.castOverflowSpell(player, stack);
    }

    /**
     * Passive instability aura particles every 2 seconds for codex carriers with high instability.
     */
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.level().isClientSide()) return;

        // Check every 40 ticks (~2 seconds)
        if (player.tickCount % 40 != 0) return;

        // Check inventory for OverflowCodexItem
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof OverflowCodexItem) {
                OverflowCodexItem.SpellData data = OverflowCodexItem.loadSpellFromNBT(stack);
                if (data != null && data.instability() > 0.3f) {
                    ServerLevel level = (ServerLevel) player.level();
                    double intensity = data.instability();
                    int particleCount = (int) (intensity * 10);

                    // Aura particles around the player
                    level.sendParticles(ParticleTypes.DRAGON_BREATH,
                            player.getX() + (player.getRandom().nextDouble() - 0.5) * 2.0,
                            player.getY() + player.getRandom().nextDouble() * 2.0,
                            player.getZ() + (player.getRandom().nextDouble() - 0.5) * 2.0,
                            particleCount, 0.2, 0.2, 0.2, 0.01);

                    // Occasional warning sound for very high instability
                    if (intensity > 0.7f && player.getRandom().nextFloat() < 0.3f) {
                        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                                player.getX(), player.getY() + 2.0, player.getZ(),
                                5, 0.3, 0.3, 0.3, 0.02);
                    }
                }
                break; // Only check first found codex
            }
        }
    }
}
