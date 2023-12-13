package com.github.unldenis.hologram.hologram

import com.github.unldenis.hologram.event.PlayerHologramHideEvent
import com.github.unldenis.hologram.event.PlayerHologramShowEvent
import com.github.unldenis.hologram.line.ILine
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class Hologram(val plugin: Plugin, location: Location, val loader: IHologramLoader) {
    var location : Location = location
        private set


    private val hLines : MutableList<ILine<*>> = CopyOnWriteArrayList() // writes are slow and Iterators are fast and consistent.

    val lines : MutableList<ILine<*>>
        get() = hLines

    val seeingPlayers: MutableSet<Player> = ConcurrentHashMap.newKeySet() // faster writes

    var name: String = UUID.randomUUID().toString()

    private var hashCode: Int? = null

    fun load(vararg lines: ILine<*>) {
        hLines.clear()
        loader.load(this, lines)
        this.hashCode = hLines.map { it.entityId }.toIntArray().contentHashCode()
    }

    fun teleport(to: Location) {
        this.location = to.clone()
        loader.teleport(this)
    }

    fun isShownFor(player: Player): Boolean {
        return seeingPlayers.contains(player)
    }

    fun show(player: Player) {
        seeingPlayers.add(player)
        for (line in this.hLines) {
            line.show(player)
        }
        Bukkit.getScheduler().runTask(
            plugin,
            Runnable { Bukkit.getPluginManager().callEvent(PlayerHologramShowEvent(player, this)) })
    }

    fun hide(player: Player) {
        for (line in this.hLines) {
            line.hide(player)
        }
        seeingPlayers.remove(player)

        Bukkit.getScheduler().runTask(
            plugin,
            Runnable { Bukkit.getPluginManager().callEvent(PlayerHologramHideEvent(player, this)) })
    }

    override fun hashCode(): Int {
        return hashCode ?: super.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val hologram = other as Hologram

        return name == hologram.name && location == hologram.location && hashCode() == other.hashCode()
    }

    override fun toString(): String {
        return "Hologram{" +
                ", name='" + name + '\'' +
                ", location=" + location +
                '}'
    }
}
