package org.holoeasy.plugin;

import org.bukkit.Location;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.line.ITextLine;
import org.jetbrains.annotations.NotNull;

public class HelloWorldHologram extends Hologram  {

    ITextLine line = textLine("Hello World");

    public HelloWorldHologram(@NotNull Location location) {
        super(location);
    }

}
