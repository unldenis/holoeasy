package org.holoeasy.hologram

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.concurrent.ConcurrentHashMap

data class PrivateConfig(
    private val hologram: Hologram,
    val plugin: Plugin,
    var showEvent: ShowEvent?,
    var hideEvent: HideEvent?
    ) {
        val seeingPlayers: MutableSet<Player> = ConcurrentHashMap.newKeySet() // faster writes

        fun load() {
            if (hologram.lines.isEmpty()) {
                throw RuntimeException("its not possible to create an empty hologram")
            }
            hologram.lines.forEach { it.pvt.hologram = hologram }
            hologram.loader.load(hologram, hologram.lines)
        }
    }