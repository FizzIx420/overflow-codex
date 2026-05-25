package com.fizz.overflowcodex.client.screen;

import com.fizz.overflowcodex.OverflowCodex;
import com.fizz.overflowcodex.item.OverflowCodexItem;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ArcaneWeaveScreen extends Screen {
    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath("overflow_codex", "textures/gui/arcane_weave_background.png");

    private static final int GLYPH_SLOTS = OverflowCodexItem.MAX_GLYPHS;
    private static final int SLOTS_PER_ROW = 10;
    private static final int TOTAL_ROWS = 3;
    private static final int SLOT_SIZE = 20;
    private static final int SLOT_SPACING = 4;

    private int scrollOffset = 0;
    private int[] spellGlyphs = new int[GLYPH_SLOTS];
    private int selectedSlot = -1;
    private int manaCostDisplay = 0;
    private float instabilityDisplay = 0.0f;
    private EditBox spellNameField;

    private int canvasX, canvasY;
    private int scrollBarX, scrollBarY, scrollBarHeight;

    public ArcaneWeaveScreen() {
        super(Component.translatable("screen.overflow_codex.arcane_weave"));
    }

    public static void open() {
        Minecraft.getInstance().setScreen(new ArcaneWeaveScreen());
    }

    @Override
    protected void init() {
        super.init();
        int centerX = this.width / 2;
        canvasX = centerX - 120;
        canvasY = 30;
        scrollBarX = canvasX + 265;
        scrollBarY = canvasY;
        scrollBarHeight = (SLOT_SIZE + SLOT_SPACING) * TOTAL_ROWS + 20;

        spellNameField = new EditBox(this.font, centerX - 60, this.height - 35, 120, 15,
                Component.translatable("screen.overflow_codex.spell_name"));
        spellNameField.setMaxLength(32);
        this.addRenderableWidget(spellNameField);

        this.addRenderableWidget(Button.builder(Component.translatable("screen.overflow_codex.save"),
                button -> saveSpell()).pos(centerX - 55, this.height - 55).size(50, 16).build());
        this.addRenderableWidget(Button.builder(Component.translatable("screen.overflow_codex.clear"),
                button -> clearSpell()).pos(centerX + 5, this.height - 55).size(50, 16).build());
        this.addRenderableWidget(Button.builder(Component.translatable("screen.overflow_codex.test"),
                button -> testCastSpell()).pos(centerX + 65, this.height - 55).size(50, 16).build());
        this.addRenderableWidget(Button.builder(Component.literal("<"),
                button -> scrollGlyphPalette(-1)).pos(canvasX, canvasY + scrollBarHeight + 10).size(20, 16).build());
        this.addRenderableWidget(Button.builder(Component.literal(">"),
                button -> scrollGlyphPalette(1)).pos(canvasX + 250, canvasY + scrollBarHeight + 10).size(20, 16).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        guiGraphics.blit(BACKGROUND, canvasX - 10, canvasY - 10, 0, 0, 280, this.height - 80, 280, this.height - 80);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 8, 0xFFD700);

        int color = manaCostDisplay > 500 ? 0xFF4444 : manaCostDisplay > 200 ? 0xFFAA00 : 0x44FF44;
        guiGraphics.drawString(this.font,
                Component.translatable("screen.overflow_codex.mana_cost", manaCostDisplay),
                canvasX, canvasY + scrollBarHeight + 30, color);

        String instabilityText = String.format("Instability: %.0f%%", instabilityDisplay * 100);
        int instColor = instabilityDisplay > 0.5f ? 0xFF4444 : instabilityDisplay > 0.2f ? 0xFFAA00 : 0x44FF44;
        guiGraphics.drawString(this.font, instabilityText,
                canvasX + 140, canvasY + scrollBarHeight + 30, instColor);

        renderGlyphSlots(guiGraphics, mouseX, mouseY);
        renderGlyphPalette(guiGraphics, mouseX, mouseY);
        renderMinimap(guiGraphics);
        renderScrollBar(guiGraphics);
        renderComplexityMeter(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void renderGlyphSlots(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        for (int row = 0; row < TOTAL_ROWS; row++) {
            for (int col = 0; col < SLOTS_PER_ROW; col++) {
                int slotIndex = row * SLOTS_PER_ROW + col;
                int x = canvasX + col * (SLOT_SIZE + SLOT_SPACING);
                int y = canvasY + row * (SLOT_SIZE + SLOT_SPACING) + 20;
                int slotColor = (slotIndex == selectedSlot) ? 0x806030 : 0x2A2A4A;
                guiGraphics.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, slotColor);
                int borderColor = isMouseInSlot(mouseX, mouseY, x, y) ? 0xFFD700 : 0x4A4A6A;
                guiGraphics.fill(x - 1, y - 1, x + SLOT_SIZE + 1, y + SLOT_SIZE + 1, borderColor);
                if (spellGlyphs[slotIndex] != 0) {
                    guiGraphics.drawString(this.font, String.valueOf(spellGlyphs[slotIndex]),
                            x + 6, y + 6, 0x00FFFF);
                }
            }
        }
    }

    private void renderGlyphPalette(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int paletteY = canvasY + scrollBarHeight + 50;
        guiGraphics.drawString(this.font, "Glyph Palette:", canvasX, paletteY, 0xFFD700);
        for (int i = 0; i < 20; i++) {
            int x = canvasX + (i % 10) * (SLOT_SIZE + SLOT_SPACING);
            int y = paletteY + 18 + (i / 10) * (SLOT_SIZE + SLOT_SPACING);
            int glyphId = scrollOffset * 20 + i + 1;
            int c = isMouseInSlot(mouseX, mouseY, x, y) ? 0x4A3A5A : 0x1A1A3A;
            guiGraphics.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, c);
            guiGraphics.drawString(this.font, String.valueOf(glyphId), x + 5, y + 6, 0xAA88FF);
        }
    }

    private void renderMinimap(GuiGraphics guiGraphics) {
        int minimapX = scrollBarX + 5;
        int minimapY = canvasY;
        int minimapSize = 60;
        guiGraphics.fill(minimapX, minimapY, minimapX + minimapSize, minimapY + minimapSize, 0x1A0A2A);
        guiGraphics.drawString(this.font, "Spell Map", minimapX + 5, minimapY + 2, 0x8866AA);
        for (int i = 0; i < GLYPH_SLOTS; i++) {
            if (spellGlyphs[i] != 0) {
                int dotX = minimapX + 3 + (i % SLOTS_PER_ROW) * (minimapSize / SLOTS_PER_ROW);
                int dotY = minimapY + 15 + (i / SLOTS_PER_ROW) * 15;
                guiGraphics.fill(dotX, dotY, dotX + 4, dotY + 4, 0x00FFFF);
            }
        }
    }

    private void renderScrollBar(GuiGraphics guiGraphics) {
        guiGraphics.fill(scrollBarX, scrollBarY, scrollBarX + 8, scrollBarY + scrollBarHeight, 0x2A2A4A);
        int handleHeight = 20;
        int handleY = scrollBarY + (int)((float)scrollOffset / 10f * (scrollBarHeight - handleHeight));
        guiGraphics.fill(scrollBarX, handleY, scrollBarX + 8, handleY + handleHeight, 0x8B00FF);
    }

    private void renderComplexityMeter(GuiGraphics guiGraphics) {
        int meterX = scrollBarX + 5;
        int meterY = canvasY + 70;
        int meterHeight = 80;
        guiGraphics.drawString(this.font, "Complexity", meterX - 5, meterY - 12, 0x8866AA);
        guiGraphics.fill(meterX, meterY, meterX + 15, meterY + meterHeight, 0x1A0A2A);
        int fill = (int)(instabilityDisplay * meterHeight);
        int c = fill > meterHeight * 0.5 ? 0xFF4444 : fill > meterHeight * 0.2 ? 0xFFAA00 : 0x00FF88;
        guiGraphics.fill(meterX + 1, meterY + meterHeight - fill, meterX + 14, meterY + meterHeight, c);
    }

    private boolean isMouseInSlot(int mouseX, int mouseY, int slotX, int slotY) {
        return mouseX >= slotX && mouseX <= slotX + SLOT_SIZE &&
               mouseY >= slotY && mouseY <= slotY + SLOT_SIZE;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (int row = 0; row < TOTAL_ROWS; row++) {
            for (int col = 0; col < SLOTS_PER_ROW; col++) {
                int x = canvasX + col * (SLOT_SIZE + SLOT_SPACING);
                int y = canvasY + row * (SLOT_SIZE + SLOT_SPACING) + 20;
                if (isMouseInSlot((int)mouseX, (int)mouseY, x, y)) {
                    selectedSlot = row * SLOTS_PER_ROW + col;
                    return true;
                }
            }
        }
        int paletteY = canvasY + scrollBarHeight + 50;
        for (int i = 0; i < 20; i++) {
            int x = canvasX + (i % 10) * (SLOT_SIZE + SLOT_SPACING);
            int y = paletteY + 18 + (i / 10) * (SLOT_SIZE + SLOT_SPACING);
            if (isMouseInSlot((int)mouseX, (int)mouseY, x, y)) {
                int glyphId = scrollOffset * 20 + i + 1;
                if (selectedSlot >= 0 && selectedSlot < GLYPH_SLOTS) {
                    spellGlyphs[selectedSlot] = glyphId;
                    recalculateStats();
                }
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta, double scrollAmount) {
        if (mouseX >= scrollBarX - 20 && mouseX <= scrollBarX + 30) {
            scrollGlyphPalette(delta > 0 ? -1 : 1);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta, scrollAmount);
    }

    private void scrollGlyphPalette(int direction) {
        scrollOffset = Math.max(0, Math.min(9, scrollOffset + direction));
    }

    private void recalculateStats() {
        int glyphCount = 0;
        for (int g : spellGlyphs) {
            if (g != 0) glyphCount++;
        }
        manaCostDisplay = glyphCount * glyphCount;
        instabilityDisplay = OverflowCodexItem.calculateInstability(glyphCount, manaCostDisplay);
    }

    private void saveSpell() {
        OverflowCodex.LOGGER.info("Saving spell: {}", spellNameField.getValue());
        Minecraft.getInstance().setScreen(null);
    }

    private void clearSpell() {
        spellGlyphs = new int[GLYPH_SLOTS];
        selectedSlot = -1;
        recalculateStats();
    }

    private void testCastSpell() {
        OverflowCodex.LOGGER.info("Testing spell cast with {} glyphs",
                java.util.Arrays.stream(spellGlyphs).filter(g -> g != 0).count());
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }
}
