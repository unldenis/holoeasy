# Your first hologram

Goal: show the text **Hello World** floating at a player's location when they run a command.

We will:

1. Create a small hologram class
2. Spawn it for one player
3. (Optional) put it in a pool so nearby players see it automatically

## 1. Create a hologram class

A hologram is a class that **extends** `Hologram`. Inside it, you declare one or more **lines**.

### Java

```java
import org.bukkit.Location;
import org.holoeasy.HoloEasy;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.line.TextLine;
import org.jetbrains.annotations.NotNull;

public class HelloWorldHologram extends Hologram {

    // Declaring the field registers the line on the hologram
    private final TextLine line = textLine(player -> "Hello World");

    public HelloWorldHologram(@NotNull HoloEasy lib, @NotNull Location location) {
        super(lib, location);
    }
}
```

### Kotlin

```kotlin
import org.bukkit.Location
import org.holoeasy.HoloEasy
import org.holoeasy.hologram.Hologram

class HelloWorldHologram(
    lib: HoloEasy,
    location: Location
) : Hologram(lib, location) {

    private val line = textLine { "Hello World" }
}
```

::: tip Why `player -> "Hello World"`?
The lambda receives the `Player` who is looking at the hologram. You can return different text per player later. For a static message, ignore the player and return a constant string.
:::

## 2. Show it to one player

Somewhere in your plugin (for example a command):

```java
HelloWorldHologram hologram = new HelloWorldHologram(holoEasy, player.getLocation());
hologram.show(player);
```

Only that player sees it. To hide it later:

```java
hologram.hide(player);
```

## 3. Better: show it with a pool

Hard-coding `show(player)` is fine for a test. In real plugins you usually use a **pool**:

- Players who walk within range see the hologram
- Players who leave range stop seeing it
- You can clean everything up on disable

In `onEnable`:

```java
IHologramPool<HelloWorldHologram> pool = holoEasy.startPool(
    60,     // spawn distance (blocks)
    false,  // interactive? (clicks) — false for now
    false   // check line of sight?
);
```

When you want to place a hologram:

```java
HelloWorldHologram hologram = new HelloWorldHologram(holoEasy, player.getLocation());
hologram.show(pool);
```

To remove it from the pool:

```java
hologram.hide(pool);
```

On plugin disable (you already saw this in Installation):

```java
holoEasy.destroyPools();
```

## 4. Try it in-game

1. Build and install your plugin
2. Join the server
3. Run your command (or whatever code creates the hologram)
4. Look at the location — you should see **Hello World** floating there

If nothing appears:

- Confirm PacketEvents initialized without errors in the console
- Confirm you called `show(player)` or `show(pool)`
- Stand close enough if you used a pool (within the spawn distance)
- For `TextLine`, any 1.8+ server works; Display lines need 1.19.4+

## What next?

- Add more lines → [Lines](./lines)
- Understand packets vs entities → [How holograms work](./how-it-works)
- Auto show/hide for many holograms → [Pools](./pools)
- Make holograms clickable → [Click interactions](./interactions)
