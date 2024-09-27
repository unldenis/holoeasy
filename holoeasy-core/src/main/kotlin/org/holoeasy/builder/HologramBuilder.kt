package org.holoeasy.builder

import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.holoeasy.hologram.Hologram
import org.holoeasy.hologram.IHologramLoader
import org.holoeasy.hologram.TextBlockStandardLoader
import org.holoeasy.line.*
import org.holoeasy.pool.IHologramPool

class HologramBuilder internal constructor(private val plugin: Plugin, private val location: Location) {

    private var loader: IHologramLoader = TextBlockStandardLoader()
    private val lines = mutableListOf<ILine<*>>()



    fun loader(loader: IHologramLoader): HologramBuilder {
        this.loader = loader
        return this
    }

    @JvmOverloads
    fun blockLine(item: ItemStack, modifiers: BlockLineModifiers = BlockLineModifiers.create()): HologramBuilder {
        if (modifiers.blockType) {
            lines.add(BlockLine(plugin, item))
        } else {
            lines.add(ItemLine(plugin, item))
        }
        return this
    }

    @JvmOverloads
    fun textLine(text: String, modifiers: TextLineModifiers = TextLineModifiers.create()): HologramBuilder {

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

    fun customLine(customLine: ILine<*>): HologramBuilder {
        lines.add(customLine)
        return this
    }

    fun build(plugin: Plugin): Hologram {
        val hologram = Hologram(plugin, location, loader)

        if (lines.isEmpty()) {
            throw RuntimeException("its not possible to create an empty hologram")
        }
        hologram.load(*lines.toTypedArray<ILine<*>>())

        return hologram
    }

    fun buildAndLoad(pool: IHologramPool): Hologram {
        val hologram = build(plugin)
        pool.takeCareOf(hologram)
        return hologram
    }
}