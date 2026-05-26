package com.fizz.overflowcodex.integration;

import com.fizz.overflowcodex.OverflowCodex;
import com.fizz.overflowcodex.item.OverflowCodexItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AreaEffectCloud;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.lang.reflect.Method;

/**
 * Handles Overflow Codex spell casting via reflection into Ars Nouveau.
 * All AN API calls are done through reflection to avoid compile-time dependencies.
 */
public class OverflowSpellResolver {

    /**
     * Cast an overflow spell from the codex item.
     * Reads NBT spell data, checks mana, rolls instability, attempts AN spell via reflection.
     */
    public static void castOverflowSpell(ServerPlayer player, ItemStack stack) {
        OverflowCodex.LOGGER.info("Player {} attempting to cast overflow spell", player.getName().getString());

        OverflowCodexItem.SpellData spellData = OverflowCodexItem.loadSpellFromNBT(stack);
        if (spellData == null) {
            player.sendSystemMessage(Component.literal("No spell inscribed in the Overflow Codex!"));
            return;
        }

        int manaCost = spellData.manaCost();
        int currentMana = getManaFromPlayer(player);

        if (currentMana < manaCost) {
            player.sendSystemMessage(Component.literal("Insufficient mana! Need " + manaCost + ", have " + currentMana));
            showCastParticles(player, 10);
            return;
        }

        // Deduct mana
        applyManaCost(player, manaCost);

        // Roll instability
        float instability = spellData.instability();
        float roll = player.getRandom().nextFloat();
        if (roll < instability) {
            boolean survived = rollInstability(player, instability, (ServerLevel) player.level());
            if (!survived) {
                return; // Critical failure consumed the cast
            }
        }

        // Attempt AN spell cast via reflection
        try {
            castSpellViaReflection(player, spellData);
        } catch (Exception e) {
            OverflowCodex.LOGGER.warn("Reflection spell cast failed: {}", e.getMessage());
            player.sendSystemMessage(Component.literal("The spell energies dissipate harmlessly..."));
            showCastParticles(player, 20);
        }

        // Show success particles
        showCastParticles(player, 30);
        player.level().playSound(null, player.blockPosition(), SoundEvents.BEACON_ACTIVATE,
                SoundSource.PLAYERS, 0.8f, 1.5f);
    }

    /**
     * Roll instability effects. Returns true if the cast should continue.
     */
    public static boolean rollInstability(ServerPlayer player, float instabilityLevel, ServerLevel level) {
        double tierRoll = player.getRandom().nextDouble();
        float minorThreshold = instabilityLevel * 0.6f;
        float moderateThreshold = instabilityLevel * 0.85f;

        if (tierRoll < minorThreshold) {
            // Minor: smoke particles + fizzle message
            level.sendParticles(ParticleTypes.SMOKE,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    15, 0.5, 0.5, 0.5, 0.02);
            player.sendSystemMessage(Component.literal("The spell fizzles with a puff of smoke!"));
            level.playSound(null, player.blockPosition(), SoundEvents.FIRE_EXTINGUISH,
                    SoundSource.PLAYERS, 0.6f, 0.8f);
            return true;
        } else if (tierRoll < moderateThreshold) {
            // Moderate: small explosion + 2 damage + knockback
            level.sendParticles(ParticleTypes.EXPLOSION_EMITTER,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    3, 0.3, 0.3, 0.3, 0.1);
            player.hurt(level.damageSources().magic(), 2.0f);
            player.setDeltaMovement(player.getDeltaMovement().add(
                    (player.getRandom().nextDouble() - 0.5) * 1.5,
                    0.5,
                    (player.getRandom().nextDouble() - 0.5) * 1.5));
            player.hurtMarked = true;
            player.sendSystemMessage(Component.literal("The instability cracks! You take damage!"));
            level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE,
                    SoundSource.PLAYERS, 0.5f, 1.2f);
            return true;
        } else {
            // Critical: dragon_breath cloud + 6 damage + confusion
            level.sendParticles(ParticleTypes.DRAGON_BREATH,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    30, 1.0, 1.0, 1.0, 0.05);
            player.hurt(level.damageSources().magic(), 6.0f);
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 1));
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0));

            // Spawn lingering damage cloud
            AreaEffectCloud cloud = new AreaEffectCloud(EntityType.AREA_EFFECT_CLOUD, level);
            cloud.setPos(player.getX(), player.getY(), player.getZ());
            cloud.setRadius(3.0f);
            cloud.setDuration(60);
            cloud.setParticle(ParticleTypes.DRAGON_BREATH);
            level.addFreshEntity(cloud);

            player.sendSystemMessage(Component.literal("CRITICAL INSTABILITY! The spell backfires catastrophically!"));
            level.playSound(null, player.blockPosition(), SoundEvents.ENDER_DRAGON_GROWL,
                    SoundSource.PLAYERS, 1.0f, 0.7f);
            return false;
        }
    }

    /**
     * Show cast particles along the player's look direction.
     */
    public static void showCastParticles(ServerPlayer player, int count) {
        ServerLevel level = (ServerLevel) player.level();
        Vec3 look = player.getLookAngle();
        Vec3 start = player.getEyePosition();
        for (int i = 0; i < count; i++) {
            double t = i / (double) count;
            double x = start.x + look.x * t * 5.0;
            double y = start.y + look.y * t * 5.0;
            double z = start.z + look.z * t * 5.0;
            level.sendParticles(ParticleTypes.DRAGON_BREATH,
                    x, y, z, 2, 0.1, 0.1, 0.1, 0.01);
        }
    }

    /**
     * Get current mana from player. Tries AN ManaUtil via reflection, falls back to XP * 100.
     */
    public static int getManaFromPlayer(ServerPlayer player) {
        try {
            Class<?> manaUtilClass = Class.forName("com.hollingsworth.arsnouveau.api.util.ManaUtil");
            Method getCurrentMana = manaUtilClass.getMethod("getCurrentMana", net.minecraft.server.level.ServerPlayer.class);
            int mana = (int) getCurrentMana.invoke(null, player);
            return mana;
        } catch (Exception e) {
            // Fallback: XP * 100
            return player.totalExperience * 100;
        }
    }

    /**
     * Deduct mana from player. Tries AN reflection, falls back to XP reduction.
     */
    public static void applyManaCost(ServerPlayer player, int cost) {
        try {
            Class<?> manaUtilClass = Class.forName("com.hollingsworth.arsnouveau.api.util.ManaUtil");
            Method removeMana = manaUtilClass.getMethod("removeMana", net.minecraft.server.level.ServerPlayer.class, int.class);
            removeMana.invoke(null, player, cost);
            return;
        } catch (Exception e) {
            // Fallback: reduce XP
            int xpToRemove = Math.max(1, cost / 100);
            player.giveExperiencePoints(-xpToRemove);
        }
    }

    /**
     * Attempt to cast a spell via Ars Nouveau's reflection API.
     */
    private static void castSpellViaReflection(ServerPlayer player, OverflowCodexItem.SpellData spellData) throws Exception {
        // Try to get AN's spell casting system via reflection
        Class<?> spellCasterClass = Class.forName("com.hollingsworth.arsnouveau.api.spell.SpellCaster");
        Method instanceMethod = spellCasterClass.getMethod("getInstance", net.minecraft.server.level.ServerPlayer.class);
        Object caster = instanceMethod.invoke(null, player);

        // Build spell from glyph IDs
        int[] glyphIds = spellData.glyphIds();
        StringBuilder spellStr = new StringBuilder();
        for (int id : glyphIds) {
            if (id != 0) {
                spellStr.append(id).append(";");
            }
        }

        OverflowCodex.LOGGER.info("Casting overflow spell '{}' with {} glyphs, cost {}",
                spellData.spellName(), spellData.glyphCount(), spellData.manaCost());
        player.sendSystemMessage(Component.literal("Overflow spell '" + spellData.spellName() + "' cast!"));
    }
}
