> [!NOTE]
> New update: 3.4.0: [BlockLines & Animations](https://github.com/unldenis/holoeasy?tab=readme-ov-file#blockline--animations). Scroll down for more information.

HoloEasy is a simple, modern and high-performant Java and Kotlin Minecraft Hologram library for 1.8-1.20.4 servers.

Lightweight replacement for Holographic Display. HoloEasy only uses packets instead of registering the entity in the actual Minecraft server.
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
    <groupId>com.github.unldenis.holoeasy</groupId>
    <artifactId>holoeasy-core</artifactId>
    <version>3.4.3</version>
</dependency>

<!-- For java projects include also the kotlin stdlib -->
<dependency>
    <groupId>org.jetbrains.kotlin</groupId>
    <artifactId>kotlin-stdlib</artifactId>
    <version>1.9.21</version>
</dependency>
```

#### Gradle

```kotlin
implementation("com.github.unldenis.holoeasy:holoeasy-core:3.4.3")

// For java projects include also the kotlin stdlib
implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.21")

```

Make sure you include the <a href="https://jitpack.io/">repository</a> as well.

## Start programming

### Java

```java
import static org.holoeasy.builder.HologramBuilder.*;

// you can use a Pool or a org.bukkit.Plugin
IHologramPool pool = HoloEasy.startInteractivePool(plugin, 60, 0.5f, 5f);

public void addHologram(Location location) {
    pool.registerHolograms(() -> {
        
        hologram(location, () -> {
            textline("Hello");
            textline("Score {} - {}", 0, 1);
            clickable("Click me").onClick(p -> {
                p.sendTitle(ChatColor.AQUA + "Hi", ChatColor.BOLD + "by HoloEasy",
                        20, 20, 20);
            });
            item(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE));
        });
        // other holograms...
    });

}
```

### Kotlin
```kotlin
import org.holoeasy.builder.HologramBuilder.*

// you can use a Pool or a org.bukkit.Plugin 
val pool = startInteractivePool(plugin, 60.0, 0.5f, 5f)

fun addHologram(location: Location) {
    pool.registerHolograms {

        hologram(location) {
            textline("Hello")
            textline("Score {} - {}", 0, 1)
            clickable("Click me").onClick {
                it.sendTitle(ChatColor.AQUA + "Hi", ChatColor.BOLD + "by HoloEasy",
                    20, 20, 20)
            }
            item(ItemStack(Material.ENCHANTED_GOLDEN_APPLE))
        }
        // other holograms...
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
registerHolograms(plugin, () -> {
    var clickCount = mutableStateOf(0); // can be any type
    
    var holo = hologram(location, () -> {
        textline("{}!", "Static");
        textline("Count: {}", clickCount);
        clickable("Click me").onClick(player -> clickCount.update(it -> it + 1));
    });
    
    // It hasn't been added to a pool, so it's up to us to make it visible and hide it from players. It's better to use a pool because it's automatic and performs asynchronous operations.
    holo.show(player);
});
```

### Kotlin

```kotlin
plugin.registerHolograms {
    val clickCount = mutableStateOf(0) // can be any type

    val holo = hologram(location) {
        textline("{}!", "Static")
        textline("Count: {}", clickCount)
        clickable("Click me").onClick { clickCount.update { it + 1 } }
    }

    // It hasn't been added to a pool, so it's up to us to make it visible and hide it from players. It's better to use a pool because it's automatic and performs asynchronous operations.
    holo.show(player)
}
```

## BlockLine & Animations
From 3.4.0 version, library added support for:
- BlockLine: Instead of a dropping item it spawns an hologram with the block as helmet.
- Animations: You can apply animations to hologram lines.
<p align="center">
  <img src="preview/blockline-and-animations.gif"  alt="holoeasy state video"/>
</p>

### Java
```java
Iterator<ItemStack> blocks = Arrays.asList(
    new ItemStack(Material.DIRT),
    new ItemStack(Material.IRON_BLOCK),
    new ItemStack(Material.GOLD_BLOCK),
    new ItemStack(Material.DIAMOND_BLOCK)
).iterator();
                    
pool.registerHolograms(() -> {

    Hologram holo = hologram(location, () -> {
        MutableState<ItemStack> currBlock = mutableStateOf(blocks.next()); // can be any type
    
        textline("Hello");
        clickable("Update me").onClick(p -> currBlock.set(blocks.next()));
        block(currBlock);
    });
                
    holo.lineAt(2).setAnimation(Animations.CIRCLE);
});
```

### Kotlin

```kotlin
val blocks = arrayOf(
    ItemStack(Material.DIRT),
    ItemStack(Material.IRON_BLOCK),
    ItemStack(Material.GOLD_BLOCK),
    ItemStack(Material.DIAMOND_BLOCK),
).iterator()

pool.registerHolograms {

    val holo = hologram(location) {
        val currBlock = mutableStateOf(blocks.next()) 

        textline("Hello")
        clickable("Update me").onClick { currBlock.set(blocks.next()) }
        block(currBlock)
    }
    holo[2].setAnimation(Animations.CIRCLE)
}
```

## Ex (Hologram-Lib)
Are you using a version older than 3.0.0? You can find the documentation <a href="https://unldenis.github.io/hologramlib/">here</a>.

## Stargazers over time
[![Stargazers over time](https://starchart.cc/unldenis/holoeasy.svg?variant=adaptive)](https://starchart.cc/unldenis/holoeasy)
