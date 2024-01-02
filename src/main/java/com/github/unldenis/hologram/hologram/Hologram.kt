package com.github.unldenis.hologram.hologram

import com.github.unldenis.hologram.config.HologramKey
import com.github.unldenis.hologram.line.ILine
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class Hologram(val key: HologramKey, location: Location, val loader: IHologramLoader) {

    var location: Location = location
        private set


    private val hLines: MutableList<ILine<*>> =
        CopyOnWriteArrayList() // writes are slow and Iterators are fast and consistent.

    val lines: MutableList<ILine<*>>
        get() = hLines

    val seeingPlayers: MutableSet<Player> = ConcurrentHashMap.newKeySet() // faster writes


    fun load(vararg lines: ILine<*>) {
        hLines.clear()
        loader.load(this, lines)
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


        // TODO:
//        Bukkit.getScheduler().runTask(
//            plugin,
//            Runnable { Bukkit.getPluginManager().callEvent(PlayerHologramShowEvent(player, this)) })
    }

    fun hide(player: Player) {
        for (line in this.hLines) {
            line.hide(player)
        }
        seeingPlayers.remove(player)

        // TODO:
//        Bukkit.getScheduler().runTask(
//            plugin,
//            Runnable { Bukkit.getPluginManager().callEvent(PlayerHologramHideEvent(player, this)) })
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val hologram = other as Hologram

        return Objects.equals(key, hologram.key)
    }

    override fun toString(): String {
        return "Hologram[key=${key.id}]"
    }
}
