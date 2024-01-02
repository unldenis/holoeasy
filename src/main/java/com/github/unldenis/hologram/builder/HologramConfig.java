package com.github.unldenis.hologram.builder;

import com.github.unldenis.hologram.config.HologramKey;
import com.github.unldenis.hologram.hologram.IHologramLoader;
import com.github.unldenis.hologram.hologram.TextBlockStandardLoader;
import com.github.unldenis.hologram.line.ILine;
import org.bukkit.Location;
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
