# Pools

A **pool** manages a group of holograms for you: who sees them, when they appear, and cleanup.

If you only ever call `hologram.show(player)`, you are doing visibility by hand. Pools are the usual approach for real plugins.

## Starting a pool

```java
IHologramPool<MyHologram> pool = holoEasy.startPool(
    60,     // spawnDistance — how close a player must be (blocks)
    true,   // isInteractive — listen for clicks on InteractionLines
    false   // checkLineOfSight — require a clear line of sight
);
```

| Argument | Meaning |
|---|---|
| `spawnDistance` | Players farther than this do not receive the hologram packets |
| `isInteractive` | Set `true` if holograms in this pool should receive click events |
| `checkLineOfSight` | When `true`, players behind walls may not see the hologram |

Use a type parameter matching your hologram class (`MyHologram`, `HelloWorldHologram`, …).

## Adding / removing holograms

```java
MyHologram hologram = new MyHologram(holoEasy, location);
hologram.show(pool);

// later
hologram.hide(pool);
```

## Cleanup on disable

```java
@Override
public void onDisable() {
    holoEasy.destroyPools();
    PacketEvents.getAPI().terminate();
}
```

`destroyPools()` destroys every pool you started and clears them from `HoloEasy`.

## When should you use a pool?

| Situation | Recommendation |
|---|---|
| Quick test for one player | `show(player)` is enough |
| Holograms in the world for everyone nearby | Use a pool |
| Clickable holograms | Pool with `isInteractive = true` |
| Many holograms | Prefer pools — they handle join/quit and distance |

Next: [Click interactions](./interactions)
