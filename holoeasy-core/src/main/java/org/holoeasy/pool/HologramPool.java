package org.holoeasy.pool;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitTask;
import org.holoeasy.HoloEasy;
import org.holoeasy.hologram.Hologram;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HologramPool<T extends Hologram> implements Listener, IHologramPool<T> {

    private final HoloEasy lib;
    private final double spawnDistance;
    private final Set<T> holograms = ConcurrentHashMap.newKeySet();
    private final BukkitTask tickTask;

    public HologramPool(@NotNull HoloEasy lib, double spawnDistance) {
        this.lib = lib;
        this.spawnDistance = spawnDistance;
        Bukkit.getPluginManager().registerEvents(this, lib.getPlugin());
        this.tickTask = hologramTick();
    }

    @Override
    public @NotNull HoloEasy getLib() {
        return lib;
    }

    @Override
    public @NotNull Set<T> getHolograms() {
        return holograms;
    }

    @Override
    public void destroy() {
        if (!tickTask.isCancelled()) {
            tickTask.cancel();
        }
        for (Hologram hologram : ImmutableList.copyOf(holograms)) {
            hologram.hide(this);
        }
        holograms.clear();
    }

    @EventHandler
    public void handleRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        for (T hologram : holograms) {
            if (hologram.isShownFor(player)) {
                hologram.hide(player);
            }
        }
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        for (T hologram : holograms) {
            if (hologram.isShownFor(player)) {
                hologram.getPvt().getSeeingPlayers().remove(player);
            }
        }
    }

    private BukkitTask hologramTick() {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(lib.getPlugin(), () -> {
            for (Player player : ImmutableList.copyOf(Bukkit.getOnlinePlayers())) {
                for (T hologram : holograms) {
                    Location holoLoc = hologram.getLocation();
                    Location playerLoc = player.getLocation();
                    boolean isShown = hologram.isShownFor(player);

                    if (holoLoc.getWorld() != playerLoc.getWorld()) {
                        if (isShown) {
                            hologram.hide(player);
                        }
                        continue;
                    } else if (holoLoc.getWorld() != null &&
                            !holoLoc.getWorld().isChunkLoaded(holoLoc.getBlockX() >> 4, holoLoc.getBlockZ() >> 4) && isShown) {
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