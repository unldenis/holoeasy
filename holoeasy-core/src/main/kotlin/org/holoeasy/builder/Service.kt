package org.holoeasy.builder


import org.bukkit.inventory.ItemStack
import org.holoeasy.builder.interfaces.HologramConfigGroup
import org.holoeasy.hologram.TextBlockStandardLoader
import org.holoeasy.line.*
import org.holoeasy.reactive.MutableState

object Service {

    enum class RegistrationType {
        PLUGIN,
        POOL
    }

    // might be Plugin as well
    val staticPool : ThreadLocal<Pair<RegistrationType, Any>> = ThreadLocal()

    val staticHologram: ThreadLocal<HologramConfig> = ThreadLocal()

    fun getStaticRegistration(): Pair<RegistrationType, Any> {
        val pair = staticPool.get() ?: throw IllegalStateException("hologram block must be inside a registerHolograms block")
        return pair
    }


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
        maxHitDistance: Float? = null, args: Array<*>? = null
    ) : ITextLine {
        val holo = getStaticHolo()

        if (minHitDistance == null || maxHitDistance == null) {
//            if(holo.pool == null && clickable) {
//                throw IllegalStateException("This hologram is not in a pool,so use the method #clickable(text, minHitDistance, maxHitDistance)")
//            }

            val textLine = TextLine(holo.plugin, text, clickable = clickable, args = args)
            holo.lines.add(textLine)
            return textLine

        }
        val textLine = TextLine(holo.plugin, text, clickable = false, args = args)
        val clickableTextLine = ClickableTextLine(textLine, minHitDistance, maxHitDistance)
        holo.lines.add(clickableTextLine)
        return clickableTextLine
    }

    fun itemline(block: ItemStack) {
        val holo = getStaticHolo()
        val itemline = ItemLine(holo.plugin, block)
        holo.lines.add(itemline)
    }

    fun itemlineMutable(item: MutableState<ItemStack>) {
        val holo = getStaticHolo()
        val itemline = ItemLine(holo.plugin, item)
        holo.lines.add(itemline)
    }

    fun blockline(block: ItemStack) {
        val holo = getStaticHolo()
        val blockline = BlockLine(holo.plugin, block)
        holo.lines.add(blockline)
    }

    fun blocklineMutable(block: MutableState<ItemStack>) {
        val holo = getStaticHolo()
        val blockline = BlockLine(holo.plugin, block)
        holo.lines.add(blockline)
    }

    fun customLine(customLine: ILine<*>) {
        val holo = getStaticHolo()
        holo.lines.add(customLine)
    }

}