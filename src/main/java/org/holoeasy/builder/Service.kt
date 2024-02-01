package org.holoeasy.builder


import org.bukkit.inventory.ItemStack
import org.holoeasy.builder.interfaces.HologramConfigGroup
import org.holoeasy.builder.interfaces.PlayerFun
import org.holoeasy.hologram.TextBlockStandardLoader
import org.holoeasy.line.*
import org.holoeasy.reactive.MutableState

object Service {

    val staticHologram: ThreadLocal<HologramConfig> = ThreadLocal()

    private fun getStaticHolo(): HologramConfig {
        val holo = staticHologram.get() ?: throw RuntimeException("You must call config() inside hologram block")
        return holo
    }

    fun config(configGroup: HologramConfigGroup) {
        val config = getStaticHolo()

        configGroup.configure(config)


        config.loader = config.loader ?: TextBlockStandardLoader()
    }

    @JvmOverloads
    fun textline(
        text: String, clickable: Boolean = false, minHitDistance: Float? = null,
        maxHitDistance: Float? = null, args: Array<PlayerFun>? = null
    ) : ITextLine {
        val holo = getStaticHolo()

        if (minHitDistance == null || maxHitDistance == null) {
            val textLine = TextLine(holo.key.plugin, text, clickable = clickable, args = args)
            holo.lines.add(textLine)
            return textLine
        } else {
            val textLine = TextLine(holo.key.plugin, text, clickable = false, args = args)
            val clickableTextLine = ClickableTextLine(textLine, minHitDistance, maxHitDistance)
            holo.lines.add(clickableTextLine)
            return clickableTextLine
        }

    }

    fun itemline(block: ItemStack) {
        val holo = getStaticHolo()
        val blockline = BlockLine(holo.key.plugin, block)
        holo.lines.add(blockline)
    }

    fun itemline(block: MutableState<ItemStack>) {
        val holo = getStaticHolo()
        val blockline = BlockLine(holo.key.plugin, block)
        holo.lines.add(blockline)
    }

    fun customLine(customLine: ILine<*>) {
        val holo = getStaticHolo()
        holo.lines.add(customLine)
    }

}