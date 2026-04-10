# Food Healing Mod v2.2.0 Full Specification Document

This document defines the complete game systems, mechanics, and internal behaviors present in the Food Healing Mod entirely updated for version 2.2.0.

---

## 1. Core Mechanics
- **Nutrition-based Healing**: When a player eats food, their Health (HP) restores instantly relative to the food's **Nutrition Value ("Hunger" restored)** multiplied by the configured heal multiplier (Default: 2.0x).
- **Overeating Enabled**: Players can continue to eat and heal even when their hunger bar is completely full, provided their underlying health is not maxed out.
- **Max Health Expansion**: The theoretical maximum health limit for a player is profoundly extended, capable of reaching up to 1,000,000,000,000 (1 trillion) depending on config settings.

### 1-1. High Nutrition Bonus (19+ Nutrition Foods)
By default, eating highly nutritious food (or powerful food added by other mods) that restores **19 or more Nutrition** at once automatically grants the player extremely powerful defensive buffs and blessings.
- **Ironclad Defense (20 minutes)**: Grants "Resistance IV" and "Fire Resistance," acting as an ultra-thick defensive armor against virtually all incoming threats.
- **Guts / Instant Death Evasion (5 seconds)**: Provides the ultimate death evasion effect. Upon taking lethal damage, falling into the void, being hit by Mekanism Antimatter, or even being targeted by a forced `/kill` command, the player **will absolutely survive with exactly 1 HP securely remaining**.

---

## 2. Food Diversity System
The game continually memorizes every unique type of food you eat.
- **Max HP Bonus**: For every **5 new distinct types** of food you consume, your base Maximum HP permanently increases by **2**.
- This directly incentivizes global culinary exploration, turning the act of discovering and cooking diverse modded foods into a tangible RPG progression and permanent character growth cycle.

---

## 3. Custom UI (User Interface) Settings
A custom Heads-Up Display (HUD) exists on the screen to indicate various mechanics. (Coordinates are purely customizable via the config file).
- **Shokugi Level Display**: Showcases the currently unlocked Shokugi (Food Skill) level and eat counts incrementally.
- **Heroics Indicator**: When an allied player takes enough damage to fall into the "Heroics" zone (Default: under 40% HP), an active buff alert brightly lights up on-screen ensuring players recognize their massive damage boost.
- **【NEW】Eaten Tooltip Indicator**: When hovering over a food item inside your inventory, a text tooltip proactively informs you whether you have already added it to your Food Diversity collection, dynamically displaying **"🍽 既食 (Eaten)"** in green or **"🍽 未食 (Not Eaten)"** in gray.

---

## 4. Shokugi (Secret Skills) System
Every time you eat, your internal "Eat Count" accumulates. Reaching the threshold (Default: 1,000 foods eaten) increases your Shokugi Level by 1 up to level 1,000.

- **Data Persistence**: Backed effectively by Forge Capabilities guaranteeing data never resets or glitches upon deaths or dimensional travel.
- **Base Attack Increase**: Raw damage done to any mob inherently mounts by `1.0 + (Level * 0.1)` times.
- **Base Defense Increase**: Total incoming damage evaluates a pure resistance filter dropping incoming hits continuously by `Level * 1%` reductions.
- **Smart UI and Live Toggles**: Send `/foodhealing syokugi skill` to read all currently acquired traits directly via chat. Click your acquired skill's name, read its description, and selectively **toggle it ON or OFF** with a single click inside the UI without risking OP requirements or cheat usage configurations.

---

## 5. Unlockable Skills Table Summary

| Unlock Lv | Skill Name | Specification Details |
|---|---|---|
| **Lv 1** | **Secret of Fire Resistance** | Perfectly nullifies magma and flame damage. |
| **Lv 2** | **Secret of Water & Night** | Grants permanent Water Breathing and Night Vision. |
| **Lv 3** | **Fast Eater I** | Eating food animations resolve twice as fast. |
| **Lv 4** | **Secret of Acrobatics** | Fall damage is 100% neutralized at all times. |
| **Lv 5** | **Blessing of Flames** | Defensively trims 30% off any incoming hit dynamically so long as your player's body persists on-fire. |
| **Lv 6** | **Secret of Blast Resistance** | Explosion damage natively dampens uniformly by exactly 90%. |
| **Lv 7** | **Secret of Purification** | Negatively typed debuffs (Poisons/Weakness) are auto-detected & wiped every tick. |
| **Lv 8** | **Secret of Abundance** | Food stack creation numbers passively double inside the crafting grid. |
| **Lv 9** | **Secret of Slaughter** | Passive Mobs (Cows, Villagers, Squids, etc.) drop item quantities securely forced to triple their initial payout. Can cleanly stack with Looting. |
| **Lv 10** | **Unleashed Heroics** | Under the HP threshold (Default 40%), all damage dealt is multiplicatively escalated (Default: 2x final damage). |
| **Lv 11〜13** | **Secret of Satisfaction** | TaCZ synergy: Grants probability rates (10%, 20%, 30%) skipping your ammo costs locally, and forces Minigun overheating directly to zero. *Note that the ammo saving probability continuously scales by +10% per level organically past Level 11.* |
| **Lv 14〜16** | **Secret of Gathering** | Crop and vanilla-adjacent blocks (Ore, Glowstones, Clay) harvest payouts forcefully explode multiplicatively post-fortune calculations (Lv14=2x, Lv15=4x, Lv16=6x). |
| **Lv 17〜19** | **Secret of the Unbreakable** | Re-routes armor damage calculations canceling durability losses safely up to near ~96.7% coverage on Lv 19 natively preventing Meta-destroying attributes originating from standard L2 Hostility attacks. |
| **Lv 20** | **Armor Mastery** | Capably routes any unmitigated mega-damage capping durability shred to an immovable max ceiling hit limit exactly equating to "1". At this exact level, your **TaCZ ammo saving probability organically reaches 100%**, yielding permanent infinite ammo. |
| **Lv 30** | **Secret of Flight** | Seamlessly gifts the standard Creative Mayfly privilege. |
| **Lv 100** | **Secret of Adamantine** | Passive "Resistance IV" buff strictly deployed locally on the client. |
| **Lv 991〜** | **Ultimate Pursuit Extreme** | True endgame. Ensures pure damage-bonus ticks completely circumventing native game i-frames smoothly stacking consecutive additional hit guarantees up towards 9 additional strikes globally at Lv999. |

---

## 6. Commands
*(All commands dynamically available for standard players entirely unhindered securely in non-cheat servers)*
*   `/foodhealing syokugi level` - Check current Shokugi Level
*   `/foodhealing syokugi count` - Check food count towards next level
*   `/foodhealing syokugi skill` - View unlocked skill list (Interactive clickable Chat GUI)
*   `/foodhealing syokugi skill [Name]` - Expose the mechanical exact text definitions pertaining to specific acquired skills.
*   `/foodhealing syokugi toggle [Name]` - Toggle execution sequence activating a skill's ON/OFF phase.
