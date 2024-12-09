# Pool

The structure of the hologram has been defined. But before proceeding, you need to understand what are your requirements.

Do you need a hologram visible to all players within a certain radius? Or do you need a hologram to be shown to one or more specific players?

In the first case, you may want to use a `Pool`. A Pool is a container for holograms and manages showing them asynchronously 
to players within a certain radius of the holograms, while hiding them from players who are too far away or leave the server. 

A Pool can be **interactive**, allowing you to click on lines (if defined that way in the class), or not. 
If you don't have any specific needs, you can show the hologram with the show method.

```java
public void create() {
    MyHolo hologram = new MyHolo(location);
    hologram.show(/* pool */);
}
```

In this case, the hologram is added and displayed using the `Standard Pool`. This pool is accessible from the HoloEasy class, from which you can obtain all the holograms in this pool.

It is always possible to create one or more Pools for greater flexibility and customization with `HoloEasy.startPool` and `HoloEasy.startInteractivePool`.

## Custom Logic

As mentioned earlier, if you're not using a Pool, it is **your** responsibility to manage the hologram's visibility. In this case, the Hologram class provides two methods: `show(Player)` and `hide(Player)`.