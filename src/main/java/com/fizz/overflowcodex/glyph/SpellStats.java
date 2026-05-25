package com.fizz.overflowcodex.glyph;

/**
 * Constants for Overflow Codex-specific spell stat keys.
 * These are used with Ars Nouveau's SpellStats system via addBuff/setStackShorthand.
 */
public final class OverflowCodexStats {
    private OverflowCodexStats() {}

    /** Key for storing anchor point indices */
    public static final String ANCHOR_POINT = "overflow_anchor_point";

    /** Key for echo repeat count tracking */
    public static final String ECHO_COUNT = "overflow_echo_count";

    /** Key for compression mana reduction factor */
    public static final String COMPRESSION_FACTOR = "overflow_compression_factor";

    /** Key for instability reduction from Compression glyphs */
    public static final String INSTABILITY_REDUCTION = "overflow_instability_reduction";

    /** Key for fork branch count tracking */
    public static final String FORK_BRANCH_COUNT = "overflow_fork_branch";

    /** Key for sequence delay tick count */
    public static final String DELAY_TICKS = "overflow_delay_ticks";
}
