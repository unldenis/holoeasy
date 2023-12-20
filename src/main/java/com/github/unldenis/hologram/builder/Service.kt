package com.github.unldenis.hologram.builder

import com.github.unldenis.hologram.animation.AnimationType
import com.github.unldenis.hologram.builder.interfaces.HologramConfigGroup
import com.github.unldenis.hologram.builder.interfaces.PlayerFun
import com.github.unldenis.hologram.hologram.TextBlockStandardLoader
import com.github.unldenis.hologram.line.*
import com.github.unldenis.hologram.line.animated.BlockALine
import com.github.unldenis.hologram.line.animated.StandardAnimatedLine
import com.github.unldenis.hologram.pool.IHologramPool
import org.bukkit.entity.EntityType
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

        val line = Line(holo.plugin, EntityType.ARMOR_STAND)
        if (minHitDistance == null || maxHitDistance == null) {
            val textLine = TextLine(line, text, clickable = clickable, args = args)
            holo.lines.add(textLine)
        } else {
            val textLine = TextLine(line, text, clickable = false, args = args)
            val clickableTextLine = ClickableTextLine(textLine, minHitDistance, maxHitDistance)
            holo.lines.add(clickableTextLine)
        }

    }

    @JvmOverloads
    fun itemline(block: ItemStack, animationType: AnimationType? = null) {
        val holo = getStaticHolo()
        val line = Line(holo.plugin, EntityType.DROPPED_ITEM)
        val blockline = BlockLine(line, block)
        if (animationType == null) {
            holo.lines.add(blockline)
        } else {
            val blockALine = BlockALine(blockline, StandardAnimatedLine(line))
            holo.onLoad.add {
                blockALine.setAnimation(animationType, it)
            }
        }
    }

    fun customLine(customLine: ILine<*>) {
        val holo = getStaticHolo()
        holo.lines.add(customLine)
    }

    private fun <T> getFirstNonNull(vararg elements: T?): T? {
        return elements.firstOrNull() { it != null }
    }
}