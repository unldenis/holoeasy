package org.holoeasy.plugin;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.line.ITextLine;
import org.jetbrains.annotations.NotNull;

public class HelloWorldHologram extends Hologram  {

    ITextLine line = textLine("Hello World");

    public HelloWorldHologram(@NotNull Plugin plugin, @NotNull Location location) {
        super(plugin, location);
    }

}
