# HoloEasy - Documentation

## Introduction

HoloEasy is an open-source library for creating holograms in Minecraft that uses PacketEvents to handle packets. This allows for greater performance as the server does not have to manage these entities.

---

## Installation

1. Add the Jitpack repository to your `build.gradle`:
   ```groovy
   repositories {
       maven { url 'https://jitpack.io' }
   }
   ```
   
    Or if you are using Maven, add the following to your `pom.xml`:
    ```xml
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
    ```
   
2. Add the dependency to your `build.gradle`:
   ```groovy
   dependencies {
      implementation 'com.github.unldenis.holoeasy:holoeasy-core:5.0.0-rc.1'
   }
   ```
   
    Or if you are using Maven, add the following to your `pom.xml`:
    ```xml
    <dependency>
        <groupId>com.github.unldenis.holoeasy</groupId>
        <artifactId>holoeasy-core</artifactId>
        <version>5.0.0-rc.1</version>
    </dependency>
    ```
   
3. The `PacketEvents` dependency is not included by HoloEasy and neither is the instance registration. You need to add it manually to your project. 
   You can find the latest version of PacketEvents [here](https://docs.packetevents.com/getting-started). 

4. Once you’re done, you may now proceed creating your first hologram.


---

## Basic Usage

### Initialization

In your plugin, create an instance of `HoloEasy` by passing the plugin reference in the Plugin `onLoad` method:

```java
HoloEasy holoEasy = new HoloEasy(this);
```

### Creating a Hologram

**Java:**
```java
public class HelloWorldHologram extends Hologram {

  private final TextLine line = textLine(player -> "Hello World");

  public HelloWorldHologram(@NotNull HoloEasy lib, @NotNull Location location) {
    super(lib, location);
  }
}
```

**Kotlin:**
```kotlin
class HelloWorldHologram(lib : HoloEasy, location: Location) : Hologram(lib, location) {
  val line = textLine { "Hello World" }
}
```

### Displaying the Hologram

**Java:**
```java
HelloWorldHologram hologram = new HelloWorldHologram(holoEasy, player.getLocation());
hologram.show(player);
```

### Destroying the Hologram

**Java:**
```java
hologram.hide(player);
```

---

## Hologram Pools

To efficiently manage multiple holograms, you can use hologram pools. 
This allows you to create and manage groups of holograms that can be shown or hidden together and cleaned up automatically.

```java
IHologramPool<MyHologram> pool = holoEasy.startPool(60, true);
```

### Adding Holograms to a Pool

```java
MyHologram hologram = new MyHologram(holoEasy, player.getLocation());
hologram.show(pool);
```

### Removing Holograms from a Pool

```java
hologram.hide(pool);
```

---

## Interaction

You can handle interaction events with holograms using `AsyncHologramInteractEvent`.
This event is triggered when a player interacts with a `InteractionLine` in a hologram and is fired asynchronously.

```java
@EventHandler
public void onClick(AsyncHologramInteractEvent event) {
    // Custom logic
}
```

---

## Example Plugins

- See `holoeasy-example-java` for a Java implementation.
- See `holoeasy-example-kotlin` for a Kotlin implementation.

---

## Line Types Explained

A hologram is made up of one or more **Lines**. Each line can display text or items, react to player actions, or show dynamic content.
Lines `yOffset` method can be used to adjust the vertical position of each line in the hologram, allowing for flexible layouts.

The main types are:

- **TextLine**: Displays a line of text, it's based on a fake armor stand, and it is supported from the Minecraft version 1.8 and above.
  
  *Java Example:*
  ```java
  TextLine playerCounter = textLine(player -> "Clicked " + getClicks(player) + " times by " + player.getName());
  ```

- **BlockLine**: Displays a block item, it's based on a fake armor stand, and it is supported from the Minecraft version 1.8 and above. 
  
  *Java Example:*
  ```java
  BlockLine blockLine = blockLine(new ItemStack(Material.DIAMOND_BLOCK));
  ```

- **ItemLine**: Displays a general item, it's based on a dropped item, and it is supported from the Minecraft version 1.9 and above.
  
  *Java Example:*
  ```java
  ItemLine itemLine = itemLine(new ItemStack(Material.APPLE));
  ```

- **DisplayTextLine**: Displays a line of text, it's based on a Display entity, and it is supported from the Minecraft version 1.19.4 and above. 

  *Java Example:*
  ```java
  DisplayTextLine globalCounter = displayTextLine(player -> "Clicked " + (++clickCount) + " times")
      .backgroundColor(Color.GREEN);
  ```

- **DisplayBlockLine**: Displays a block item, it's based on a Display entity, and it is supported from the Minecraft version 1.19.4 and above.
  
  *Java Example:*
  ```java
  DisplayBlockLine displayBlockLine = displayBlockLine(Material.DIAMOND_BLOCK);
  ```

- **InteractionLine**: An invisible line used to detect player interactions (like clicks) without displaying text. It is supported from the Minecraft version 1.19.4 and above.   
  
  *Java Example:*
  ```java
  InteractionLine interactionLine = interactionLine();
  ```

You can combine these lines in a single hologram to create rich and interactive displays.

---

## Main API

- `HoloEasy` - Main library manager
- `Hologram` - Represents a hologram

---

## Support

For questions or issues, see the official documentation or open an issue on the project's GitHub repository.