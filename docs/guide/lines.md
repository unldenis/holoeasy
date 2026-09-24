# Lines

A hologram is a stack of **lines**. Each line is one piece of content (or an invisible click area).

You declare lines as fields when you subclass `Hologram`. Creating the field registers it.

## Vertical spacing

Use `.yOffset(...)` to move a line up or down relative to the hologram location:

```java
private final TextLine title = textLine(player -> "Shop")
        .yOffset(0.5f);

private final TextLine subtitle = textLine(player -> "Click to open")
        .yOffset(0.2f);
```

Positive offset = higher. Negative = lower.

## Line types

### TextLine (1.8+)

Classic floating text via a fake armor stand.

```java
private final TextLine line = textLine(player ->
        "Clicked " + getClicks(player) + " times by " + player.getName());
```

### BlockLine (1.8+)

Shows a block as an armor-stand helmet / equipment style display.

```java
private final BlockLine blockLine = blockLine(new ItemStack(Material.DIAMOND_BLOCK));
```

### ItemLine (1.9+)

Shows a floating dropped-item style stack.

```java
private final ItemLine itemLine = itemLine(new ItemStack(Material.APPLE));
```

### DisplayTextLine (1.19.4+)

Modern text using a Display entity. Supports background color, opacity, billboard, etc.

```java
private final DisplayTextLine globalCounter =
        displayTextLine(player -> "Clicked " + clickCount + " times")
                .backgroundColor(Color.GREEN)
                .yOffset(1.55f);
```

You can also pass an ARGB int, for example `.backgroundColor(0x80FF0000)`.

### DisplayBlockLine (1.19.4+)

Modern block display entity.

```java
private final DisplayBlockLine displayBlockLine =
        displayBlockLine(Material.DIAMOND_BLOCK);
```

### InteractionLine (1.19.4+)

Invisible hitbox used for clicks. Players do not see text; they can click the area.

```java
private final Line<?> interaction = interactionLine()
        .height(1.0f)
        .yOffset(1f);
```

You need a **pool started with `isInteractive = true`**, and a listener for `AsyncHologramInteractEvent`. See [Click interactions](./interactions).

## Updating text after it changed

If your supplier’s data changes (for example a counter), tell the line to refresh:

```java
// One player
playerCounter.update(player);

// Everyone currently seeing this hologram
globalCounter.updateAll();
```

## Mixing line types

You can combine armor-stand lines and Display lines in the same hologram. Pick what fits your minimum server version.

![Mixed lines and animation example](/preview/blockline-and-animations.gif)

Next: [Pools](./pools) · [Click interactions](./interactions)
