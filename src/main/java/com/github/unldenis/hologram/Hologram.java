package com.github.unldenis.hologram;

import com.github.unldenis.hologram.event.PlayerHologramHideEvent;
import com.github.unldenis.hologram.event.PlayerHologramShowEvent;
import com.github.unldenis.hologram.line.ILine;
import com.github.unldenis.hologram.line.hologram.IHologramLoader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class Hologram {

  private final Plugin plugin;
  private final List<ILine<?>> hLines = new CopyOnWriteArrayList<>();       // writes are slow and Iterators are fast and consistent.
  private final Set<Player> seeingPlayers = ConcurrentHashMap.newKeySet();  // faster writes

  private String name;
  private Location location;
  private IHologramLoader loader;
  private Integer hashCode;

  public Hologram(Plugin plugin, Location location, IHologramLoader loader) {
    this.plugin = plugin;

    this.location = location;
    this.loader = loader;
    this.name = UUID.randomUUID().toString();
  }

  public void load(ILine<?>... lines) {
    this.hLines.clear();
    this.loader.load(this, lines);
    this.hashCode = Arrays.hashCode(
        hLines.stream().map(ILine::getEntityId).toArray(Integer[]::new));
  }

  public void teleport(Location to) {
    this.location = to.clone();
    this.loader.teleport(this);
  }

  public boolean isShownFor(Player player) {
    return this.seeingPlayers.contains(player);
  }

  public void show(Player player) {
    this.seeingPlayers.add(player);
    for (ILine<?> line : this.hLines) {
      line.show(player);
    }
    Bukkit.getScheduler().runTask(
        plugin,
        () -> Bukkit.getPluginManager().callEvent(new PlayerHologramShowEvent(player, this)));
  }

  public void hide(Player player) {
    for (ILine<?> line : this.hLines) {
      line.hide(player);
    }
    this.seeingPlayers.remove(player);

    Bukkit.getScheduler().runTask(
        plugin,
        () -> Bukkit.getPluginManager().callEvent(new PlayerHologramHideEvent(player, this)));
  }

  public Location getLocation() {
    return location;
  }

  public List<ILine<?>> getLines() {
    return hLines;
  }

  public Set<Player> getSeeingPlayers() {
    return seeingPlayers;
  }

  public static HologramBuilder builder(Plugin plugin, Location location) {
    return new HologramBuilder(plugin, location);
  }

  public String getName() {
    return name;
  }

  public void setName(@NotNull String name) {
    this.name = name;
  }

  public void setLoader(@NotNull IHologramLoader loader) {
    this.loader = loader;
  }

  @Override
  public int hashCode() {
    return hashCode == null ? super.hashCode() : hashCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Hologram hologram = (Hologram) o;

    return Objects.equals(name, hologram.name) && Objects.equals(location, hologram.location) && Objects.equals(hashCode(), o.hashCode());
  }

  @Override
  public String toString() {
    return "Hologram{" +
        ", name='" + name + '\'' +
        ", location=" + location +
        '}';
  }
}
