package org.holoeasy.builder;

import org.holoeasy.config.HologramKey;
import org.bukkit.Location;
import org.holoeasy.hologram.IHologramLoader;
import org.holoeasy.hologram.TextBlockStandardLoader;
import org.holoeasy.line.ILine;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class HologramConfig {

    @NotNull
    final HologramKey key;
    @NotNull
    final Location location;
    final List<ILine<?>> lines = new ArrayList<>();
    public IHologramLoader loader = new TextBlockStandardLoader();

    HologramConfig(@NotNull HologramKey key, @NotNull Location location) {
        this.key = key;
        this.location = location;
    }


}
