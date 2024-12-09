# Hello World

*Your first hologram.*

## Start programming

**1.** Register HoloEasy api.

```java
@Override
public void onEnable() {
    HoloEasy.bind(plugin, PacketImpl.ProtocolLib /* PacketImpl.PacketEvents */ );
}
```

<br />


**2.** Define your first Hologram class.

```java
public class HelloWorldHologram extends Hologram  {

    Line<String> line = textLine("Hello World");

    public HelloWorldHologram(@NotNull Location location) {
        super(location);
    }
}
```

<br />

**3.** Now spawn a hologram.

```java
public void create(Location location) {
    HelloWorldHologram hologram = new HelloWorldHologram(location);
    hologram.show();
}
```

<br />

**4.** Congratulations! You have created your first Hologram.
