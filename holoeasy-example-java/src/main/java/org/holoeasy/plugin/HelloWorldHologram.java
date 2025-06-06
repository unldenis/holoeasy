package org.holoeasy.plugin;

import org.bukkit.Location;
import org.holoeasy.HoloEasy;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.line.Line;
import org.jetbrains.annotations.NotNull;

public class HelloWorldHologram extends Hologram  {

    Line<String> line = textLine("Hello World");

    public HelloWorldHologram(@NotNull HoloEasy lib, @NotNull Location location) {
        super(lib, location);
    }

}
