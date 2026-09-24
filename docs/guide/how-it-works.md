# How holograms work

You do not need this page to get started — but it helps when something looks wrong, or when you choose between line types.

## Normal entities vs packets

In vanilla Bukkit, if you spawn an Armor Stand, the **server** tracks it like any other entity. That costs memory and tick time, especially with many holograms.

HoloEasy instead:

1. Keeps hologram data in memory (location, lines, who should see what)
2. Uses **PacketEvents** to send “spawn / update / destroy entity” packets **only to the players who should see them**

From the player’s point of view it looks like a real floating entity. From the server’s point of view it is mostly packets.

## The main objects

| Object | Role |
|---|---|
| `HoloEasy` | Library entry point. Holds pools and registers click packet listening. |
| `Hologram` | One hologram at one location. You subclass it. |
| `Line` | One row of content (text, item, block, click hitbox, …). |
| `IHologramPool` | Manages many holograms and who can see them. |

## Lifecycle in plain English

1. Plugin loads → create PacketEvents + `new HoloEasy(plugin)`
2. Plugin enables → `PacketEvents.getAPI().init()`, optionally `startPool(...)`
3. You create a hologram subclass and call `show(player)` or `show(pool)`
4. HoloEasy sends spawn packets for each line
5. When values change, you call `update` / `updateAll` on lines
6. Plugin disables → `destroyPools()` + PacketEvents `terminate()`

## Per-player content

Many line helpers take a `Function<Player, …>` (or a Kotlin lambda). That function runs when HoloEasy needs the value **for that player**.

Example: `"Welcome, " + player.getName()` shows a different name for each viewer.

## Version notes

HoloEasy picks behavior from the server version. Broadly:

- **Armor-stand style** lines (`TextLine`, `BlockLine`, …) work on older versions
- **Display entity** lines (`DisplayTextLine`, `DisplayBlockLine`) need **1.19.4+**
- **Interaction** click hitboxes need **1.19.4+**

Details for each type are on the [Lines](./lines) page.
