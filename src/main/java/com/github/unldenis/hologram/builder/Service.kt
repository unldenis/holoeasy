package com.github.unldenis.hologram.builder

import com.github.unldenis.hologram.builder.interfaces.HologramConfigGroup
import com.github.unldenis.hologram.builder.interfaces.PlayerFun
import com.github.unldenis.hologram.hologram.TextBlockStandardLoader
import com.github.unldenis.hologram.line.*
import com.github.unldenis.hologram.pool.IHologramPool
import org.bukkit.inventory.ItemStack
import java.util.concurrent.atomic.AtomicReference

object Service {

    val staticHologram: ThreadLocal<HologramConfig> = ThreadLocal()

    val lastPool: AtomicReference<IHologramPool> = AtomicReference()


    private fun getStaticHolo(): HologramConfig {
        val holo = staticHologram.get() ?: throw RuntimeException("You must call config() inside hologram block")
        return holo
    }

    fun config(configGroup: HologramConfigGroup) {
        val config = getStaticHolo()

        configGroup.configure(config)

        if (config.plugin == null) {
            if (config.pool == null) {
                throw RuntimeException("Missing Plugin or Pool")
            } else {
                config.plugin = config.pool.plugin
            }
        }

        config.loader = config.loader ?: TextBlockStandardLoader()

        config.name?.let {
            config.name = it
        }

    }

    @JvmOverloads
    fun textline(
        text: String, clickable: Boolean = false, minHitDistance: Float? = null,
        maxHitDistance: Float? = null, args: Array<PlayerFun>? = null
    ) {
        val holo = getStaticHolo()

        if (minHitDistance == null || maxHitDistance == null) {
            val textLine = TextLine(holo.plugin, text, clickable = clickable, args = args)
            holo.lines.add(textLine)
        } else {
            val textLine = TextLine(holo.plugin, text, clickable = false, args = args)
            val clickableTextLine = ClickableTextLine(textLine, minHitDistance, maxHitDistance)
            holo.lines.add(clickableTextLine)
        }

    }

    @JvmOverloads
    fun itemline(block: ItemStack) {
        val holo = getStaticHolo()
        val blockline = BlockLine(holo.plugin, block)
        holo.lines.add(blockline)
    }

    fun customLine(customLine: ILine<*>) {
        val holo = getStaticHolo()
        holo.lines.add(customLine)
    }

}