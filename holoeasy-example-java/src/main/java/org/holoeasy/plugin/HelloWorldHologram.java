package org.holoeasy.plugin;

import org.bukkit.Location;
import org.holoeasy.HoloEasy;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.line.TextLine;
import org.jetbrains.annotations.NotNull;

public class HelloWorldHologram extends Hologram {

    private final TextLine line = textLine(player -> "Hello World");

    public HelloWorldHologram(@NotNull HoloEasy lib, @NotNull Location location) {
        super(lib, location);
    }
}
