# Hologram

The main class is `Hologram`. This is an abstract class, so to define your holograms you must extend this class.

A hologram is composed of lines, defined by the `Line<*>` interface. The lines can be either text or an item/block/dropped item. 

As you saw in the Hello World example, the Hologram class provides these three methods, `textLine`, `itemLine` and `blockLine`, 
which should preferably be assigned to a class field; however, you can also call them in the constructor. 

> The order of the fields will correspond to the order of the lines in the hologram.

Having them as fields or accessible through a getter allows you to retrieve information or perform actions on a specific line.

```java
public class MyHolo extends Hologram {

    public Line<String> counter = textLine("Hello World");
    public Line<ItemStack> status = itemLine(new ItemStack(Material.RED_DYE));

    public MyHolo(@NotNull Location location) {
        super(location);
    }
}
```

In this example, the hologram will display the text "Hello World" with a RED_DYE dropped below it.

For more complex lines, take a look at TextLine, ItemLine or BlockLine. Once we've created our hologram class, we can instantiate it. This **does not** mean the hologram is visible yet.