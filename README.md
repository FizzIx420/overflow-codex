# Overflow Codex - Ars Nouveau Expansion Mod

## Overview
**A transcendent endgame spellbook expansion for Ars Nouveau.** The Overflow Codex breaks the 10-glyph barrier, allowing up to **30 glyphs per spell** with a redesigned arcane circuitry-based editor UI.

> *"The Archmage Spellbook was never intended to contain recursive spell architecture. The Overflow Codex is what happens when a spellbook stops being a book and starts becoming an arcane operating system."*

## Specifications
| Property | Value |
|---|---|
| Mod ID | `overflow_codex` |
| Minecraft | 1.21.1 |
| NeoForge | 21.0.109 |
| Ars Nouveau | 5.10.6 |
| AllTheModium | 3.0.1 |

## Features

### Overflow Codex (Spellbook)
- **30 glyph slots** per spell (up from 10)
- **Scrollable spell editor** with 3 rows of 10 glyphs
- **Spell node minimap** for visual overview
- **Mana cost scaling**: Spells beyond 10 glyphs gain exponentially increasing mana costs
- **Instability system**: Complex spells above 15 glyphs risk misfires, silence, and mana drain
- **Hard limits**: Max 3 Fork, 2 Echo Cast glyphs, complexity cap at 500

### Arcane Weave Interface (Custom UI)
Right-click the Overflow Codex to open:
- Zoomable glyph editing canvas (3 rows x 10 columns)
- Scrollable glyph palette with all Ars Nouveau + Overflow Codex glyphs
- Real-time mana cost and instability meter
- Complexity visualization bar
- Spell minimap showing filled slots
- Save, Clear, and Test Cast buttons
- Spell naming field
- Scrollable palette navigation

### New Glyphs

#### Fork (Tier 3)
Duplicates the spell execution path, creating two parallel branches.
- Mana: 150 base
- Max 3 per spell

#### Anchor (Tier 2)
Stores the current spell state for later glyph reference.
- Mana: 80 base

#### Sequence Delay (Tier 2)
Delays subsequent glyph execution by ticks.
- Mana: 50 base
- Augments: Amplify, Duration

#### Echo Cast (Tier 3)
Repeats the previous spell segment.
- Mana: 200 base
- Max 2 per spell
- Augments: Amplify

#### Compression (Tier 2)
Condenses repeated logic, reducing mana cost by 50% and lowering instability.
- Mana: 120 base

### Crafting Progression

#### Stage 1: Dormant Overflow Codex (Crafting Table)
```
[AllTheModium Ingot] [Elytra]           [AllTheModium Ingot]
[Nether Star]         [Archmage Spellbook] [Sculk Shrieker]
[AllTheModium Ingot]  [Dragon Head]       [AllTheModium Ingot]
```

#### Stage 2: Overflow Codex (Enchanting Apparatus)
**Center:** Dormant Overflow Codex
**Source Gem** on one pedestal
**Pedestal Items:**
- Fire Essence
- Water Essence
- Air Essence
- Earth Essence
- Manipulation Essence
- Conjuration Essence
- Abjuration Essence
- AllTheModium Smithing Template
- Bucket of Soul Lava (from the other dimension)
**Mana Cost:** 50,000

## Installation
1. Install NeoForge 21.0.109
2. Install Ars Nouveau 5.10.6
3. Install AllTheModium 3.0.1 (optional, needed for recipes)
4. Place `overflow_codex-1.0.0.jar` in `.minecraft/mods/`

## Building from Source

### Prerequisites
- Java 21 JDK
- Internet connection (for dependency download)

### Build Steps
```bash
# Option 1: Using build script
./build.sh

# Option 2: Direct Gradle
chmod +x gradlew
./gradlew build
```

Output JAR: `build/libs/overflow_codex-1.0.0.jar`

### Troubleshooting Build
- **Timeout**: Re-run; first build downloads ~500MB of dependencies
- **Ars Nouveau API errors**: The glyph registration may need adjustment based on exact AN 5.10.6 API
- **AllTheModium not found**: The ATM dependency is optional; recipes use it but won't prevent compilation

## Project Structure
```
src/main/
  java/com/fizz/overflowcodex/
    OverflowCodex.java          # Main mod class
    ModCreativeTab.java         # Creative tab
    item/
      ModItems.java             # Item registration
      DormantOverflowCodexItem.java
      OverflowCodexItem.java    # 30-glyph spellbook with balancing logic
    glyph/
      ModGlyphs.java            # Glyph registration with AN
      EffectFork.java           # Branches spell execution
      EffectAnchor.java         # Stores spell state
      EffectSequenceDelay.java  # Timed execution
      EffectEchoCast.java       # Repeats spell segment
      EffectCompression.java    # Reduces mana/instability
      OverflowCodexStats.java   # Custom stat keys
    recipe/
      ModRecipes.java           # Recipe type registration
      AwakeningRecipe.java      # Enchanting apparatus recipe
    network/
      ModNetwork.java           # Network payload registration
    client/
      ClientSetup.java          # Client-side init
      screen/
        ArcaneWeaveScreen.java  # Custom 30-glyph spell editor UI
  resources/
    META-INF/neoforge.mods.toml
    assets/overflow_codex/
      lang/en_us.json           # All text/translations
      models/item/              # Item JSON models
      textures/item/            # Item textures (16x16)
      textures/glyph/           # Glyph textures (16x16)
      textures/gui/             # UI background (256x256)
    data/overflow_codex/
      recipes/                  # Crafting + awakening recipes
      tags/items/               # Item tags
```

## Balance Philosophy
This mod should feel **dangerous, endgame, and arcane** - like bending the Ars Nouveau system past its intended limits. Large spells are magical infrastructure, not just bigger spellbooks. Instability, mana scaling, and hard limits prevent power creep.

## License
MIT

## Credits
- Ars Nouveau by baileyholl
- AllTheModium by AllTheMods Team
- NeoForge Team
