
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


## First step

### Kotlin
```kotlin
// you can use a Pool or a org.bukkit.Plugin
val pool = HoloEasy.startInteractivePool(this, 60.0,
    minHitDistance = 0.5f, /* optional */
    maxHitDistance = 5f, /* optional */
    clickAction = ClickAction.LEFT_CLICK /* optional */
)
```

### Java
```java
// you can use a Pool or a org.bukkit.Plugin
IHologramPool pool = HoloEasy.startInteractivePool(plugin, 60, 0.5f, 5f);
```


## Hello World

### Kotlin
```kotlin
class HelloWorldHologram(plugin: Plugin, location: Location) : Hologram(plugin, location) {
    var line = textLine("Hello World")
}

// When you need
val hologram = HelloWorldHologram(this, location)
hologram.show(pool)
```

### Java
```java
public class HelloWorldHologram extends Hologram  {

    ITextLine line = textLine("Hello World");

    public HelloWorldHologram(@NotNull Plugin plugin, @NotNull Location location) {
        super(plugin, location);
    }

}

// When you need
HelloWorldHologram hologram = new HelloWorldHologram(this, location);
hologram.show(pool);
```

## Deep into holoeasy

### Kotlin
```kotlin
class MyHolo(plugin: Plugin, location: Location) : Hologram(plugin, location) {

    private val clickCount = MutableState(0) // can be any type

    val counter = textLine("Clicked {} times", TextLineModifiers()
        .args(clickCount)
        .clickable { _ -> clickCount.update { it + 1 } })

    var status= blockLine(ItemStack(Material.RED_DYE))

}

// Create new hologram
val myHolo = MyHolo(this, location)
myHolo.show(pool)

Bukkit.getScheduler().runTaskLaterAsynchronously(this, Runnable {
    // access lines
    myHolo.status.update(ItemStack(Material.GREEN_DYE))
}, 20L * 5)
```

### Java
```java
class MyHolo extends Hologram {

    private final MutableState<Integer> clickCount = new MutableState<>(0); // can be any type

    public ITextLine counter = textLine("Clicked {} times", new TextLineModifiers()
            .args(clickCount)
            .clickable(player -> clickCount.update(it -> it + 1)));
    public ILine<ItemStack> status = blockLine(new ItemStack(Material.RED_DYE));

    public MyHolo(@NotNull Plugin plugin, @NotNull Location location) {
        super(plugin, location);
    }
}

// Create new hologram
MyHolo myHolo = new MyHolo(this, location);
myHolo.show(pool);

Bukkit.getScheduler().runTaskLaterAsynchronously(this, () -> {
    // access lines
    myHolo.status.update(new ItemStack(Material.GREEN_DYE));
}, 20L * 5);
```



## History
Are you using a version older than 4.0.0? You can find the documentation <a href="https://github.com/unldenis/holoeasy/tree/3.4.4">here</a>. 

Are you using a version older than 3.0.0? You can find the documentation <a href="https://unldenis.github.io/hologramlib/">here</a>.

## Stargazers over time
[![Stargazers over time](https://starchart.cc/unldenis/holoeasy.svg?variant=adaptive)](https://starchart.cc/unldenis/holoeasy)
