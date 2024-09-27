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
class NoValueForKeyException(key: UUID) : IllegalStateException("No value for id '$key'")

class HologramPool(internal val plugin: Plugin, private val spawnDistance: Double) : Listener, IHologramPool {

    val holograms: MutableMap<UUID, Hologram> = ConcurrentHashMap()

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)

        hologramTick()
    }

    override fun get(id: UUID): Hologram {
        return holograms[id] ?: throw NoValueForKeyException(id)
    }

    override fun takeCareOf(value: Hologram) {
        if (holograms.containsKey(value.id)) {
            throw KeyAlreadyExistsException(value.id)
        }
        holograms[value.id] = value
    }

    /**
     * Removes the given hologram by from the handled Holograms of this pool.
     *
     * @param hologram the hologram of the pool to remove.
     * @return true if any elements were removed
     */
    override fun remove(id : UUID): Hologram? {
        // if removed
        val removed = holograms.remove(id)
        removed?.let {
            for (player in it.seeingPlayers) {
                it.hide(player)
            }
            return it
        }
        return null
    }

    @EventHandler
    fun handleRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        holograms
            .values
            .filter { it.isShownFor(player) }
            .forEach { it.hide(player) }
    }

    @EventHandler
    fun handleQuit(event: PlayerQuitEvent) {
        val player = event.player
        holograms
            .values
            .filter { it.isShownFor(player) }
            .forEach { it.seeingPlayers.remove(player) }
    }

    /**
     * Starts the hologram tick.
     */
    private fun hologramTick() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, Runnable {
            for (player in ImmutableList.copyOf(Bukkit.getOnlinePlayers())) {
                for (hologram in this.holograms.values) {
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
