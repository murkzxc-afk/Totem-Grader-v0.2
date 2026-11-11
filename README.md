# Totem Grader

A Fabric mod for Minecraft 1.21.1 that adds a Totem Grader, a Semi-Grade Table, and progressive Graded Totems.

## Features

- Totem Graders have a 35% chance to appear in End City treasure chests.
- Craft a Semi-Grade Table with a Smithing Table in the center, a Diamond above it, and Blackstone in the other seven slots.
- Open the Semi-Grade Table and place a Totem of Undying or an existing Graded Totem, one Totem Grader, and one Netherite Ingot.
- A Totem of Undying becomes a Graded Totem with 2 uses.
- Each further upgrade increases the stored use level by one, up to 10.
- The table displays `You hit the limit!` when a 10-use Graded Totem is selected.
- The item stores its use level in `CUSTOM_DATA`, displays it in the name and tooltip, and exposes a `totemgrader:uses` model predicate with levels 2 through 10.
- The included level models are resource-pack-ready and currently use the vanilla Totem of Undying texture as their fallback artwork.

## Build and install

Install Java 21 and Fabric Loom's normal Gradle prerequisites. Run:

```text
./gradlew build
```

The mod jar is created in `build/libs`. Install Fabric Loader 0.16.9 for Minecraft 1.21.1 and Fabric API, then put the jar in the instance's `mods` folder.

## Texture artwork

To add the red eye shimmer artwork, provide PNG files under `src/main/resources/assets/totemgrader/textures/item/` and point `graded_totem_2.json` through `graded_totem_10.json` at those files. The predicate registration and all nine model levels are already included.
