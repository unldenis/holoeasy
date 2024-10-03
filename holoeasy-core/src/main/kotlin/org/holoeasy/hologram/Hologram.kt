package org.holoeasy.hologram

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.holoeasy.builder.BlockLineModifiers
import org.holoeasy.builder.TextLineModifiers
import org.holoeasy.line.*
import org.holoeasy.pool.IHologramPool
import org.jetbrains.annotations.ApiStatus.Internal
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class Hologram private constructor(
    plugin: Plugin,
    location: Location,
    loader: IHologramLoader,
    name: String?,
    showEvent: ShowEvent?,
    hideEvent: HideEvent?
) {

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




    class Builder(private val plugin: Plugin, private val location: Location) {

        private val lines = mutableListOf<ILine<*>>()

        private var loader: IHologramLoader = TextBlockStandardLoader()
        private var name: String? = null
        private var showEvent: ShowEvent? = null
        private var hideEvent: HideEvent? = null

        fun loader(loader: IHologramLoader): Builder {
            this.loader = loader
            return this
        }

        fun name(name: String): Builder {
            this.name = name
            return this
        }

        fun onShow(showEvent: ShowEvent): Builder {
            this.showEvent = showEvent
            return this
        }

        fun onHide(hideEvent: HideEvent): Builder {
            this.hideEvent = hideEvent
            return this
        }

        @JvmOverloads
        fun blockLine(item: ItemStack, modifiers: BlockLineModifiers = BlockLineModifiers.create()): Builder {
            if (modifiers.blockType) {
                lines.add(BlockLine(plugin, item))
            } else {
                lines.add(ItemLine(plugin, item))
            }
            return this
        }

        @JvmOverloads
        fun textLine(text: String, modifiers: TextLineModifiers = TextLineModifiers.create()): Builder {

            if (modifiers.clickable) {
                if (modifiers.clickableWithoutPool) {
                    val textLine = TextLine(plugin, text, clickable = false, args = modifiers.args)
                    val clickableTextLine =
                        ClickableTextLine(textLine, modifiers.minHitDistance, modifiers.maxHitDistance)
                    modifiers.clickEvent?.let { clickableTextLine.onClick(it) }
                    lines.add(clickableTextLine)
                } else {
                    val textLine = TextLine(plugin, text, clickable = true, args = modifiers.args)
                    modifiers.clickEvent?.let { textLine.onClick(it) }
                    lines.add(textLine)
                }

            } else {
                val textLine = TextLine(plugin, text, clickable = false, args = modifiers.args)
                lines.add(textLine)
            }
            return this
        }

        fun customLine(customLine: ILine<*>): Builder {
            lines.add(customLine)
            return this
        }

        fun build(plugin: Plugin): Hologram {
            val hologram = Hologram(plugin, location, loader, name, showEvent, hideEvent)

            if (lines.isEmpty()) {
                throw RuntimeException("its not possible to create an empty hologram")
            }
            hologram.pvt.load(*lines.toTypedArray<ILine<*>>())

            return hologram
        }

        fun buildAndLoad(pool: IHologramPool): Hologram {
            val hologram = build(plugin)
            pool.takeCareOf(hologram)
            return hologram
        }
    }

}
