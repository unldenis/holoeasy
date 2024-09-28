
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
    <version>4.0.0</version>
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
implementation("com.github.unldenis.holoeasy:holoeasy-core:4.0.0")

// For java projects include also the kotlin stdlib
implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.21")

```

Make sure you include the <a href="https://jitpack.io/">repository</a> as well.

## Start programming

### Java

```java
// you can use a Pool or a org.bukkit.Plugin
IHologramPool pool = HoloEasy.startInteractivePool(plugin, 60, 0.5f, 5f);

public void addHologram(Plugin plugin, Location location) {
    MutableState<Integer> clickCount = new MutableState<>(0); // can be any type
    Hologram.create(plugin, location)
            .name("my-holo")
            .textLine("Hello")
            .textLine("Count {}", TextLineModifiers
                    .create()
                    .args(clickCount))
            .textLine("Click me", TextLineModifiers
                    .create()
                    .clickable()
                    .onClick(player -> clickCount.update(it -> it + 1)))
            .blockLine(new ItemStack(Material.APPLE))
            .onShow(player -> player.sendMessage("Hi :)"))
            .buildAndLoad(pool);
}
```

### Kotlin
```kotlin
import org.holoeasy.builder.HologramBuilder.*

// you can use a Pool or a org.bukkit.Plugin 
val pool = startInteractivePool(plugin, 60.0, 0.5f, 5f)

fun addHologram(plugin : Plugin, location: Location) {
    val clickCount = MutableState(0) // can be any type
    Hologram.create(plugin, location)
        .name("my-holo")
        .textLine("Hello")
        .textLine("Count {}", TextLineModifiers
            .create()
            .args(clickCount)
        )
        .textLine("Click me", TextLineModifiers
            .create()
            .clickable()
            .onClick { clickCount.update { it + 1 } }
        )
        .blockLine(ItemStack(Material.APPLE))
        .onShow { it.sendMessage("Hi :)") }
        .buildAndLoad(pool)

}
```

## History
Are you using a version older than 4.0.0? You can find the documentation <a href="https://github.com/unldenis/holoeasy/tree/3.4.4">here</a>. 

Are you using a version older than 3.0.0? You can find the documentation <a href="https://unldenis.github.io/hologramlib/">here</a>.

## Stargazers over time
[![Stargazers over time](https://starchart.cc/unldenis/holoeasy.svg?variant=adaptive)](https://starchart.cc/unldenis/holoeasy)
