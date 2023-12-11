package com.github.unldenis.hologram.dsl;

import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.line.ILine;
import com.github.unldenis.hologram.line.hologram.IHologramLoader;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.function.Consumer;

public final class HologramExt {

    private final Plugin plugin;

    private Location location;
    private final List<ILine<?>> lines;
    private PlaceholdersJava placeholdersJava;
    private IHologramLoader loader;

    private String name;

    private final Set<Consumer<Hologram>> tasks = new HashSet<>();


    public HologramExt(Plugin plugin) {
        this.plugin = plugin;
        this.lines = new LinkedList<>();
        this.placeholdersJava = new PlaceholdersJava(0);
    }

    public List<ILine<?>> lines() {
        return lines;
    }

    public PlaceholdersJava placeholders() {
        return placeholdersJava;
    }

    public void setPlaceholders(PlaceholdersJava placeholdersJava) {
        this.placeholdersJava = placeholdersJava;
    }

    public IHologramLoader loader() {
        return loader;
    }

    public void setLoader(IHologramLoader loader) {
        this.loader = loader;
    }

    public Location location() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Plugin plugin() {
        return plugin;
    }

    public Set<Consumer<Hologram>> tasks() {
        return tasks;
    }
}
