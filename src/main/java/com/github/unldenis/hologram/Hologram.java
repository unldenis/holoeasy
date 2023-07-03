package com.github.unldenis.hologram;


import com.github.unldenis.hologram.collection.ReferenceArrayList;
import com.github.unldenis.hologram.collection.ReferenceHashSet;
import com.github.unldenis.hologram.event.PlayerHologramHideEvent;
import com.github.unldenis.hologram.event.PlayerHologramShowEvent;
import com.github.unldenis.hologram.line.ILine;
import com.github.unldenis.hologram.line.hologram.IHologramLoader;
import com.github.unldenis.hologram.placeholder.Placeholders;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class Hologram {

  private final Plugin plugin;
  private final IHologramLoader loader;
  private final List<ILine<?>> hLines = Collections.synchronizedList(new ReferenceArrayList<>());
  private final Set<Player> seeingPlayers = Collections.synchronizedSet(new ReferenceHashSet<>());

  private Location location;

  private Integer hashCode;

  public Hologram(Plugin plugin, Location location, IHologramLoader loader) {
    this.plugin = plugin;
    this.loader = loader;

    this.location = location;
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

  void removeSeeingPlayer(Player player) {
    this.seeingPlayers.remove(player);
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

  public static HologramBuilder builder(Plugin plugin, Location location, Placeholders placeholders) {
    return new HologramBuilder(plugin, location, placeholders);
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

    return Objects.equals(location, hologram.location) && Objects.equals(hashCode(), o.hashCode());
  }
}
