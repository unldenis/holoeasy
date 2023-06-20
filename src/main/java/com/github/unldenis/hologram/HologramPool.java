package com.github.unldenis.hologram;

import com.github.unldenis.hologram.util.Validate;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSets;
import java.util.Objects;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class HologramPool implements Listener, IHologramPool {

  private final Plugin plugin;
  private final double spawnDistance;
  private final Set<Hologram> holograms = ReferenceSets.synchronize(new ReferenceOpenHashSet<>());

  public HologramPool(Plugin plugin, double spawnDistance) {
    this.plugin = plugin;
    this.spawnDistance = spawnDistance;

    Bukkit.getPluginManager().registerEvents(this, plugin);

    hologramTick();
  }

  @Override
  public void takeCareOf(Hologram hologram) {
    this.holograms.add(hologram);
  }

  @Override
  public Plugin getPlugin() {
    return plugin;
  }

  /**
   * Removes the given hologram by from the handled Holograms of this pool.
   *
   * @param hologram the hologram of the pool to remove.
   */
  @Override
  public void remove(@NotNull Hologram hologram) {
    Validate.notNull(hologram, "Hologram to remove cannot be null");
    if (this.holograms.contains(hologram)) {
      this.holograms.remove(hologram);
//      hologram.getSeeingPlayers()
//          .forEach(hologram::hide);
      for(Player player: hologram.getSeeingPlayers()) {
        hologram.hide(player);
      }
    }
  }

  @Override
  public Set<Hologram> getHolograms() {
    return holograms;
  }

  @EventHandler
  public void handleRespawn(PlayerRespawnEvent event) {
    Player player = event.getPlayer();
//    holograms.stream()
//        .filter(h -> h.isShownFor(player))
//        .forEach(h -> h.hide(player));
    for (Hologram h : holograms) {
      if (h.isShownFor(player)) {
        h.hide(player);
      }
    }
  }

  @EventHandler
  public void handleQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    for (Hologram h : holograms) {
      if (h.isShownFor(player)) {
        h.removeSeeingPlayer(player);
      }
    }
  }

  /**
   * Starts the hologram tick.
   */
  private void hologramTick() {
    Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
      for (Player player : ImmutableList.copyOf(Bukkit.getOnlinePlayers())) {
        for (Hologram hologram : this.holograms) {
          Location holoLoc = hologram.getLocation();
          Location playerLoc = player.getLocation();
          boolean isShown = hologram.isShownFor(player);

          if (!Objects.equals(holoLoc.getWorld(), playerLoc.getWorld())) {
            if (isShown) {
              hologram.hide(player);
            }
            continue;
          } else if (!holoLoc.getWorld()
              .isChunkLoaded(holoLoc.getBlockX() >> 4, holoLoc.getBlockZ() >> 4) && isShown) {
            hologram.hide(player);
            continue;
          }
          boolean inRange = holoLoc.distanceSquared(playerLoc) <= this.spawnDistance;

          if (!inRange && isShown) {
            hologram.hide(player);
          } else if (inRange && !isShown) {
            hologram.show(player);
          }
        }
      }
    }, 20L, 2L);
  }


}
