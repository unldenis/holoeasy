package org.holoeasy.hologram

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.holoeasy.builder.HologramBuilder
import org.holoeasy.line.*
import org.jetbrains.annotations.ApiStatus.Internal
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class Hologram internal constructor(
    plugin: Plugin,
    location: Location,
    loader: IHologramLoader,
    name: String?,
    showEvent: ShowEvent?,
    hideEvent: HideEvent?
) {

    companion object {

        @JvmStatic
        fun create(plugin: Plugin, location: Location): HologramBuilder {
            val builder = HologramBuilder(plugin, location)
            return builder
        }
    }

    @Internal
    internal val pvt = PrivateConfig(this, plugin, loader, showEvent, hideEvent)
    val name : String? = name
    val id = UUID.randomUUID()!!
    var location: Location = location
        private set
    val lines: MutableList<ILine<*>> = CopyOnWriteArrayList() // writes are slow and Iterators are fast and consistent.

    fun <T : ILine<*>> lineAt(index: Int): T {
        return lines[index] as T
    }

    fun teleport(to: Location) {
        this.location = to.clone()
        pvt.loader.teleport(this)
    }

    fun isShownFor(player: Player): Boolean {
        return pvt.seeingPlayers.contains(player)
    }

    fun show(player: Player) {
        pvt.seeingPlayers.add(player)
        for (line in lines) {
            line.show(player)
        }

        pvt.showEvent?.onShow(player)
    }

    fun hide(player: Player) {
        for (line in lines) {
            line.hide(player)
        }
        pvt.seeingPlayers.remove(player)

        pvt.hideEvent?.onHide(player)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Hologram

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


    data class PrivateConfig(
        private val hologram: Hologram,
        val plugin: Plugin,
        val loader: IHologramLoader,
        var showEvent: ShowEvent?,
        var hideEvent: HideEvent?
    ) {
        val seeingPlayers: MutableSet<Player> = ConcurrentHashMap.newKeySet() // faster writes

        fun load(vararg lines: ILine<*>) {
            hologram.lines.clear()

            lines.forEach { it.pvt.hologram = hologram }
            loader.load(hologram, lines)
        }
    }



}
