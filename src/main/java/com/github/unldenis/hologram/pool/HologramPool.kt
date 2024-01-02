package com.github.unldenis.hologram.pool

import com.github.unldenis.hologram.config.HologramKey
import com.github.unldenis.hologram.hologram.Hologram
import com.google.common.collect.ImmutableList
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.plugin.Plugin
import java.util.concurrent.ConcurrentHashMap

class KeyAlreadyExistsException(key: HologramKey) : IllegalStateException("Key '$key' already exists")
class NoValueForKeyException(key: String) : IllegalStateException("No value for key '$key'")

class HologramPool(override val plugin: Plugin, private val spawnDistance: Double) : Listener, IHologramPool {

    val holograms: MutableMap<HologramKey, Hologram> = ConcurrentHashMap()

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)

        hologramTick()
    }

    override fun get(key: HologramKey): Hologram {
        return holograms[key] ?: throw NoValueForKeyException(key.id)
    }

    override fun get(keyId: String) : Hologram {
        for((key, holo) in holograms) {
            if(key.id == keyId) {
                return holo
            }
        }
        throw NoValueForKeyException(keyId)
    }

    override fun takeCareOf(key: HologramKey, value: Hologram) {
        if (holograms.containsKey(key)) {
            throw KeyAlreadyExistsException(key)
        }
        holograms[key] = value
    }

    /**
     * Removes the given hologram by from the handled Holograms of this pool.
     *
     * @param hologram the hologram of the pool to remove.
     * @return true if any elements were removed
     */
    override fun remove(key: HologramKey): Hologram? {
        // if removed
        val removed = holograms.remove(key)
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
