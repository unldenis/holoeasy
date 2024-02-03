> [!NOTE]
> New update: 3.1.0: Reactive holograms. Scroll down for more information.

HoloEasy is a simple, modern and high-performant Java and Kotlin Minecraft Hologram library for 1.8-1.20.4 servers.

<p align="center">
  <img src="preview/gif.gif"  alt="holoeasy video"/>
</p>

## Quickstart

### Requirements 
* ProtocolLib installed on your server

### Add dependency
#### Maven

```xml
<dependency>
    <groupId>com.github.unldenis</groupId>
    <artifactId>holoeasy</artifactId>
    <version>3.1.1</version>
</dependency>
```

#### Gradle

```kotlin
implementation("com.github.unldenis:holoeasy:3.1.1")
```

Make sure you include the <a href="https://jitpack.io/">repository</a> as well.

## Start programming

### Java

```java
import static org.holoeasy.builder.HologramBuilder.*;

// you can use a Pool or a org.bukkit.Plugin for HologramKey
IHologramPool pool = HoloEasy.startInteractivePool(plugin, 60, 0.5f, 5f);

public void addHologram(Location location) {
    hologram(new HologramKey(pool, "unique-id-holo"), location, () -> {
        textline("Hello");
        textline("Score {} - {}", 0, 1);
        clickable("Click me").onClick(p -> {
            p.sendTitle(ChatColor.AQUA + "Hi", ChatColor.BOLD + "by HoloEasy",
                    20, 20, 20);
        });
        item(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE));
    });
}
```

### Kotlin
```kotlin
import org.holoeasy.builder.HologramBuilder.*

// you can use a Pool or a org.bukkit.Plugin for HologramKey
val pool = startInteractivePool(plugin, 60.0, 0.5f, 5f)

fun addHologram(location: Location) {
    hologram(HologramKey(pool, "unique-id-holo"), location) {
        textline("Hello")
        textline("Score {} - {}", 0, 1)
        clickable("Click me").onClick {
            it.sendTitle(ChatColor.AQUA + "Hi", ChatColor.BOLD + "by HoloEasy",
                20, 20, 20)
        }
        item(ItemStack(Material.ENCHANTED_GOLDEN_APPLE))
    }
}
```

## Reactive Holograms
From 3.1.0 version, the parameters of text lines and item lines **_can also be reactive_**. This means that you can update the line by simply calling the 'set' method to these.

<p align="center">
  <img src="preview/state.gif"  alt="holoeasy state video"/>
</p>

> [!WARNING]
> Mutable states have no player information at this time. If you need to create a hologram for a specific player, it is recommended that you do **_not_** add it to a Pool.
### Java
```java
var clickCount = mutableStateOf(0); // can be any type

var holo = hologram(new HologramKey(plugin, "unique-id-holo"), location, () -> {
    textline("{}!", "Static");
    textline("Count: {}", clickCount);
    clickable("Click me", 0.5f, 5f).onClick($ -> clickCount.set(clickCount.get() + 1));
});

// It hasn't been added to a pool, so it's up to us to make it visible and hide it from players. It's better to use a pool because it's automatic and performs asynchronous operations.
// HologramKey decides whether to add it to a pool or not.
holo.show(player);
```

### Kotlin
```kotlin
val clickCount = mutableStateOf(0) // can be any type

val holo = hologram(HologramKey(plugin, "unique-id-holo"), location) {
    textline("{}!", "Static")
    textline("Count: {}", clickCount)
    clickable("Click me", 0.5f, 5f).onClick { clickCount.set(clickCount.get() + 1)}
}

// It hasn't been added to a pool, so it's up to us to make it visible and hide it from players. It's better to use a pool because it's automatic and performs asynchronous operations.
// HologramKey decides whether to add it to a pool or not.
holo.show(player)
```


## Ex (Hologram-Lib)
Are you using a version older than 3.0.0? You can find the documentation <a href="https://unldenis.github.io/hologramlib/">here</a>.

## Contributors

<a href="https://github.com/unldenis/holoeasy/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=unldenis/holoeasy" />
</a>

Made with [contrib.rocks](https://contrib.rocks).
