package com.github.unldenis.hologram.builder;

import com.github.unldenis.hologram.hologram.Hologram;
import com.github.unldenis.hologram.hologram.IHologramLoader;
import com.github.unldenis.hologram.line.ILine;
import com.github.unldenis.hologram.pool.IHologramPool;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static com.github.unldenis.hologram.builder.HologramBuilder.getInstance;

public class HologramConfig {

    @NotNull
    final Location location;
    final List<ILine<?>> lines = new ArrayList<>();
    final Set<Consumer<Hologram>> onLoad = new HashSet<>();
    public String name;
    public IHologramLoader loader;
    public IHologramPool pool = getInstance().getLastPool().get();
    public Plugin plugin;
    HologramConfig(@NotNull Location location) {
        this.location = location;
    }


}
