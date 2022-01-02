package com.github.unldenis.hologram;


import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;


public class HologramPool implements Listener {

    private final Plugin plugin;
    private final double spawnDistance;

    private final Collection<Hologram> holograms = new CopyOnWriteArraySet<>();

    public HologramPool(@NotNull Plugin plugin, double spawnDistance) {
        Validate.notNull(plugin, "Plugin cannot be null");
        this.plugin = plugin;
        this.spawnDistance = spawnDistance*spawnDistance;

        Bukkit.getPluginManager().registerEvents(this, plugin);

        hologramTick();
    }

    @EventHandler
    public void handleRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        holograms.stream()
                .filter(h->h.isShownFor(player))
                .forEach(h->h.hide(player));
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        holograms.stream()
                .filter(h->h.isShownFor(player))
                .forEach(h->h.hide(player));
    }

    protected Plugin getPlugin() {
        return plugin;
    }

    /**
     * Adds the given {@code hologram} to the list of handled Holograms of this pool.
     *
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

                    if (!holoLoc.getWorld().equals(playerLoc.getWorld())) {
                        if (isShown) {
                            hologram.hide(player);
                        }
                        continue;
                    } else if (!holoLoc.getWorld()
                            .isChunkLoaded(holoLoc.getBlockX() >> 4, holoLoc.getBlockZ() >> 4)) {
                        if (isShown) {
                            hologram.hide(player);
                        }
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
        }, 20, 2);
    }


}
