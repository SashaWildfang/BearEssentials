# BearEssentials

The everyday staff and player utility commands for the **Bear Nation** network, in one small plugin.

![Paper](https://img.shields.io/badge/Paper-1.21-blue) ![Java](https://img.shields.io/badge/Java-21-orange) ![Build](https://img.shields.io/badge/build-Gradle-02303A)

## Features

- Game mode shortcuts for yourself or other players
- Inventory inspection (`/invsee`) and a disposal bin (`/trash`)
- Heal, feed, extinguish and hat commands
- `/back` to return to where you died or last teleported from
- Colour codes on signs
- Cooldowns on selected commands

## Commands

| Command | Description | Permission |
|---|---|---|
| `/gamemode <mode> [player]` | Change game mode | `bearessentials.cmd.gamemode` |
| `/gmc`, `/gms`, `/gma`, `/gmsp` `[player]` | Creative / Survival / Adventure / Spectator | `bearessentials.cmd.<gmc\|gms\|gma\|gmsp>` |
| `/invsee <player>` | Open another player's inventory | `bearessentials.cmd.invsee` |
| `/trash` | Open a disposal bin | `bearessentials.cmd.trash` |
| `/heal [player]` | Restore health | `bearessentials.cmd.heal` |
| `/feed [player]` | Restore hunger | `bearessentials.cmd.feed` |
| `/extinguish` | Put out fire | `bearessentials.cmd.extinguish` |
| `/hat` | Wear the held item as a hat | `bearessentials.cmd.hat` |
| `/back` | Return to your previous location | `bearessentials.cmd.back` |
| `/item` | Spawn an item | `bearessentials.cmd.item` |

## Requirements

- Paper 1.21+
- Java 21

## Building

```bash
gradle build
```

The plugin jar goes to `build/libs/`.

## Project layout

```
src/main/java/com/bearnation/bearessentials/
├── BearEssentials.java  # Plugin entry point
├── commands/            # One class per command
├── listeners/           # Invsee, trash, death and sign listeners
└── utils/               # Back locations, cooldowns, game mode helpers
```

---

Part of the Bear Nation plugin suite.
