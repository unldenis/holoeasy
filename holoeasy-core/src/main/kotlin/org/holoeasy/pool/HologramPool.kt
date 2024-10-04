package org.holoeasy.pool

import com.google.common.collect.ImmutableList
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.plugin.Plugin
import org.holoeasy.hologram.Hologram
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class KeyAlreadyExistsException(key: UUID) : IllegalStateException("Id '$key' already exists")

class HologramPool( private val spawnDistance: Double) : Listener, IHologramPool {

    override var plugin: Plugin? = null
        set(value) {
            value!!

            Bukkit.getPluginManager().registerEvents(this, value)
            hologramTick(value)

            field = value
        }

    override val holograms: Set<Hologram> = ConcurrentHashMap.newKeySet()


    @EventHandler
    fun handleRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        holograms
            .filter { it.isShownFor(player) }
            .forEach { it.hide(player) }
    }

    @EventHandler
    fun handleQuit(event: PlayerQuitEvent) {
        val player = event.player
        holograms
            .filter { it.isShownFor(player) }
            .forEach { it.pvt.seeingPlayers.remove(player) }
    }

    /**
     * Starts the hologram tick.
     */
    private fun hologramTick(plugin: Plugin) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, Runnable {
            for (player in ImmutableList.copyOf(Bukkit.getOnlinePlayers())) {
                for (hologram in this.holograms) {
                    val holoLoc = hologram.location
                    val playerLoc: Location = player.location
                    val isShown = hologram.isShownFor(player)

                    if (holoLoc.world != playerLoc.world) {
                        if (isShown) {
                            hologram.hide(player)
                        }
                        continue
                    } else if (!holoLoc.world
                                // todo: log
                            !!.isChunkLoaded(holoLoc.blockX shr 4, holoLoc.blockZ shr 4) && isShown
                    ) {
                        hologram.hide(player)
                        continue
                    }
                    val inRange = holoLoc.distanceSquared(playerLoc) <= this.spawnDistance

                    if (!inRange && isShown) {
                        hologram.hide(player)
                    } else if (inRange && !isShown) {
                        hologram.show(player)
                    }
                }
            }
        }, 20L, 2L)
    }
}
