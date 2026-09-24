# Installation

This page adds HoloEasy and PacketEvents to your plugin.

You will:

1. Add the JitPack repository (for HoloEasy)
2. Add the CodeMC repository (for PacketEvents)
3. Declare both dependencies
4. Start PacketEvents and create a `HoloEasy` instance in your plugin

::: tip Version
Replace `5.0.0` with the [latest HoloEasy tag](https://github.com/denmeh/holoeasy/releases) if a newer one exists.
Replace PacketEvents `2.14.0` with the [latest PacketEvents release](https://docs.packetevents.com/introduction/development-setup/) if you prefer.
:::

## Step 1 — Repositories

### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.codemc.io/repository/maven-releases/")
}
```

### Gradle (Groovy)

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
    maven { url 'https://repo.codemc.io/repository/maven-releases/' }
}
```

### Maven

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
  <repository>
    <id>codemc-releases</id>
    <url>https://repo.codemc.io/repository/maven-releases/</url>
  </repository>
</repositories>
```

## Step 2 — Dependencies

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    compileOnly("com.github.denmeh.holoeasy:holoeasy-core:5.0.0")
    compileOnly("com.github.retrooper:packetevents-spigot:2.14.0")
}
```

### Gradle (Groovy)

```groovy
dependencies {
    compileOnly 'com.github.denmeh.holoeasy:holoeasy-core:5.0.0'
    compileOnly 'com.github.retrooper:packetevents-spigot:2.14.0'
}
```

### Maven

```xml
<dependencies>
  <dependency>
    <groupId>com.github.denmeh.holoeasy</groupId>
    <artifactId>holoeasy-core</artifactId>
    <version>5.0.0</version>
    <scope>provided</scope>
  </dependency>
  <dependency>
    <groupId>com.github.retrooper</groupId>
    <artifactId>packetevents-spigot</artifactId>
    <version>2.14.0</version>
    <scope>provided</scope>
  </dependency>
</dependencies>
```

::: warning compileOnly / provided vs shading
The snippets above assume PacketEvents (and optionally HoloEasy) are available at runtime — for example another plugin provides PacketEvents, or you shade them yourself.

If you want a single fat jar, use `implementation` and a shade/shadow plugin, and **relocate** PacketEvents packages so they do not clash with other plugins. See the `holoeasy-example-java` module in the repo for a working Shadow example.
:::

## Step 3 — Wire PacketEvents + HoloEasy

PacketEvents must be loaded in `onLoad`, initialized in `onEnable`, and terminated in `onDisable`. Create `HoloEasy` in `onLoad` as well.

### Java

```java
import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.holoeasy.HoloEasy;

public class MyPlugin extends JavaPlugin {

    private HoloEasy holoEasy;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();

        holoEasy = new HoloEasy(this);
    }

    @Override
    public void onEnable() {
        PacketEvents.getAPI().init();
    }

    @Override
    public void onDisable() {
        holoEasy.destroyPools();
        PacketEvents.getAPI().terminate();
    }

    public HoloEasy getHoloEasy() {
        return holoEasy;
    }
}
```

### Kotlin

```kotlin
import com.github.retrooper.packetevents.PacketEvents
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder
import org.bukkit.plugin.java.JavaPlugin
import org.holoeasy.HoloEasy

class MyPlugin : JavaPlugin() {

    lateinit var holoEasy: HoloEasy
        private set

    override fun onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this))
        PacketEvents.getAPI().load()

        holoEasy = HoloEasy(this)
    }

    override fun onEnable() {
        PacketEvents.getAPI().init()
    }

    override fun onDisable() {
        holoEasy.destroyPools()
        PacketEvents.getAPI().terminate()
    }
}
```

## What you should have now

- Project builds without missing-class errors for `org.holoeasy` and PacketEvents
- Plugin enables on the server with no PacketEvents crash
- A `HoloEasy` instance ready to create holograms

Next: [Your first hologram](./first-hologram)
