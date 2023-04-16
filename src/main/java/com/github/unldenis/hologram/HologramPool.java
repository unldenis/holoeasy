/*
 * Hologram-Lib - Asynchronous, high-performance Minecraft Hologram
 * library for 1.8-1.18 servers.
 * Copyright (C) unldenis <https://github.com/unldenis>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.unldenis.hologram;


import com.github.unldenis.hologram.util.Validate;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;


public class HologramPool implements Listener {

  protected static AtomicInteger IDs_COUNTER = new AtomicInteger(new Random().nextInt());

  private final Plugin plugin;
  private final double spawnDistance;

  private final Collection<Hologram> holograms = new CopyOnWriteArraySet<>();

  public HologramPool(@NotNull Plugin plugin, double spawnDistance) {
    Validate.notNull(plugin, "Plugin cannot be null");
    this.plugin = plugin;
    this.spawnDistance = spawnDistance * spawnDistance;

    Bukkit.getPluginManager().registerEvents(this, plugin);

    hologramTick();
  }

  @EventHandler
  public void handleRespawn(PlayerRespawnEvent event) {
    Player player = event.getPlayer();
    holograms.stream()
        .filter(h -> h.isShownFor(player))
        .forEach(h -> h.hide(player));
  }

  @EventHandler
  public void handleQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    holograms.stream()
        .filter(h -> h.isShownFor(player) || h.isExcluded(player))
        .forEach(h -> {
          h.removeSeeingPlayer(player);
          h.removeExcludedPlayer(player);
        });
  }

  protected Plugin getPlugin() {
    return plugin;
  }

  /**
   * Adds the given {@code hologram} to the list of handled Holograms of this pool.
   */
  protected void takeCareOf(@NotNull Hologram hologram) {
    this.holograms.add(hologram);
  }

  /**
   * Starts the hologram tick.
   */
  protected void hologramTick() {
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

          if ((hologram.isExcluded(player) || !inRange) && isShown) {
            hologram.hide(player);
          } else if (!hologram.isExcluded(player) && inRange && !isShown) {
            hologram.show(player);
          }
        }
      }
    }, 20L, 2L);
  }

  /**
   * Removes the given hologram by from the handled Holograms of this pool.
   *
   * @param hologram the hologram of the pool to remove.
   */
  public void remove(@NotNull Hologram hologram) {
    Validate.notNull(hologram, "Hologram to remove cannot be null");
    if (this.holograms.contains(hologram)) {
      this.holograms.remove(hologram);
      hologram.getSeeingPlayers()
          .forEach(hologram::hide);
    }
  }
}
