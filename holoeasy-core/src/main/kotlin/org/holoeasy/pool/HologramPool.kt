package org.holoeasy.pool

import com.google.common.collect.ImmutableList
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.holoeasy.HoloEasy
import org.holoeasy.hologram.Hologram
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class KeyAlreadyExistsException(key: UUID) : IllegalStateException("Id '$key' already exists")

class HologramPool<T : Hologram>(private val spawnDistance: Double) : Listener, IHologramPool<T> {

    override val holograms: Set<T> = ConcurrentHashMap.newKeySet()

    init {
        Bukkit.getPluginManager().registerEvents(this, HoloEasy.plugin())
        hologramTick()
    }

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
    private fun hologramTick() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(HoloEasy.plugin(), Runnable {
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
