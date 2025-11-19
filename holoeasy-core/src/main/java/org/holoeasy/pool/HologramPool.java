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
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.holoeasy.HoloEasy;
import org.holoeasy.hologram.Hologram;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HologramPool<T extends Hologram> implements Listener, IHologramPool<T> {

    private final HoloEasy lib;
    private final double spawnDistance;

    private final boolean isInteractive;
    protected final boolean checkLineOfSight;

    private final Set<T> holograms = ConcurrentHashMap.newKeySet();
    private final BukkitTask tickTask;

    public HologramPool(@NotNull HoloEasy lib, double spawnDistance, boolean isInteractive, boolean checkLineOfSight) {
        this.lib = lib;
        this.spawnDistance = spawnDistance;
        this.isInteractive = isInteractive;
        this.checkLineOfSight = checkLineOfSight;

        Bukkit.getPluginManager().registerEvents(this, lib.getPlugin());
        this.tickTask = hologramTick();
    }

    @Override
    public boolean isInteractive() {
        return isInteractive;
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
                    boolean inRange = holoLoc.distanceSquared(playerLoc) <= this.spawnDistance * this.spawnDistance;

                    boolean hasLineOfSight = true;
                    if (checkLineOfSight && inRange) {
                        hasLineOfSight = checkLineOfSight(player, holoLoc);
                    }

                    boolean shouldShow = inRange && hasLineOfSight;

                    if (!shouldShow && isShown) {
                        hologram.hide(player);
                    } else if (shouldShow && !isShown) {
                        hologram.show(player);
                    }
                }
            }
        }, 20L, 2L);
    }

    /**
     * Check if player has direct line of sight to the hologram location
     */
    private boolean checkLineOfSight(Player player, Location holoLoc) {
        Location eyeLoc = player.getEyeLocation();
        Vector direction = holoLoc.toVector().subtract(eyeLoc.toVector());
        double distance = direction.length();

        RayTraceResult result = player.getWorld().rayTraceBlocks(
                eyeLoc,
                direction.normalize(),
                distance,
                org.bukkit.FluidCollisionMode.NEVER,
                true
        );

        return result == null || result.getHitBlock() == null;
    }
}