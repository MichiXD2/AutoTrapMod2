# AutoTrap Mod 2

AutoTrap Mod 2 is a Fabric-based singleplayer utility mod that automates repetitive trap-building routines. The mod ships with an in-game HUD, configurable action profiles, and per-world presets so you can adapt the automation to every world you play.

> **Disclaimer:** AutoTrap Mod 2 only supports singleplayer worlds. Using it on multiplayer servers may violate server rules and is not supported.

## Building the mod

1. Install the required tooling:
   - [Java 17 JDK](https://adoptium.net/) or newer that is compatible with Fabric.
   - [Gradle](https://gradle.org/) 8.x (or use the Gradle wrapper once the project is opened in your IDE).
2. Clone the repository and run the client in development mode:

   ```bash
   git clone https://github.com/example/AutoTrapMod2.git
   cd AutoTrapMod2
   ./gradlew runClient
   ```

3. Produce a distributable JAR once you are ready to ship:

   ```bash
   ./gradlew build
   ```

   The compiled JAR will be located in `build/libs/`.

## Controls

| Action | Default Key |
| ------ | ------------ |
| Toggle Autoplayer HUD | `H` |
| Pause/Resume sequence | `P` |
| Next profile | `.` |
| Previous profile | `,` |

All keybinds can be adjusted in the standard Minecraft controls menu under the "AutoTrap" category.

## HUD overview

The HUD displays the currently active profile, automation status, and the number of remaining steps in the current sequence. You can move or hide the HUD via the configuration settings described below.

## Configuration

AutoTrap Mod 2 reads its defaults from `config/defaults/autoplayer.json`. When a world is opened, the mod copies the defaults into `config/autoplayer/<world-name>.json` (creating the folder if necessary) and applies any overrides defined under the `worldOverrides` section of the defaults file. You can edit the per-world files to tweak timings or select different profiles without affecting other saves.

### Notable options

- `showHud` – Enables or disables the HUD overlay.
- `activeProfile` – The profile selected when the mod is first loaded.
- `delayTicks` – Base delay between automated actions in game ticks.
- `autoCollect`, `autoBuild`, `autoReset` – Toggles for the key automation behaviors.
- `worldOverrides` – Map of world names to setting overrides that are applied when the matching world loads.
- `profiles` – Named sequences that define the ordered actions the mod should run.
- `keybinds` – Default key bindings that can be overridden in Minecraft's controls menu.

After editing configuration files, reload them with the in-game `/autoplayer reload` command or restart the client.

## Assets

- `assets/autoplayer/icon.png` – Mod icon displayed in the Fabric mod list.
- `assets/autoplayer/textures/gui/hud_overlay.png` – HUD background texture used by the in-game overlay.
- `assets/autoplayer/lang/*.json` – Language files that provide localised strings for the HUD and configuration screens.

## License

This project is licensed under the [MIT License](./LICENSE).
