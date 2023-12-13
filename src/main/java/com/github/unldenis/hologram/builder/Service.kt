package com.github.unldenis.hologram.builder

import com.github.unldenis.hologram.animation.Animation
import com.github.unldenis.hologram.animation.AnimationType
import com.github.unldenis.hologram.builder.interfaces.HologramConfigGroup
import com.github.unldenis.hologram.experimental.ItemLine
import com.github.unldenis.hologram.hologram.TextBlockStandardLoader
import com.github.unldenis.hologram.line.*
import com.github.unldenis.hologram.line.animated.BlockALine
import com.github.unldenis.hologram.line.animated.StandardAnimatedLine
import com.github.unldenis.hologram.placeholder.Placeholders
import com.github.unldenis.hologram.pool.IHologramPool
import org.bukkit.inventory.ItemStack
import org.bukkit.util.EulerAngle
import java.util.concurrent.atomic.AtomicReference

object Service {

    val staticHologram : ThreadLocal<HologramConfig> = ThreadLocal()

    val lastPool : AtomicReference<IHologramPool> = AtomicReference()


    private fun getStaticHolo() : HologramConfig {
        val holo = staticHologram.get() ?: throw RuntimeException("You must call config() inside hologram block")
        return holo
    }

    fun config(configGroup: HologramConfigGroup) {
        val holo = getStaticHolo()

        val config = HologramConfig()
        configGroup.configure(config)

        if(config.plugin == null) {
            if(config.pool == null) {
                throw RuntimeException("Missing Plugin or Pool")
            } else {
                config.plugin = config.pool.plugin
            }
        }

        config.location ?: throw RuntimeException("Required location")

        config.loader = config.loader ?: TextBlockStandardLoader()

        config.name?.let {
            holo.name = it
        }

        config.placeholders = config.placeholders ?: Placeholders(0x00)
    }

    fun textline(text : String, clickable : Boolean = false, minHitDistance : Float ? = null,
                 maxHitDistance : Float ? = null) {
        val holo = getStaticHolo()

        val line = Line(holo.plugin)
        if(minHitDistance == null || maxHitDistance == null) {
            val textLine = TextLine(line, text, holo.placeholders, clickable)
            holo.lines.add(textLine)
        } else {
            val textLine = TextLine(line, text, holo.placeholders, false)
            val clickableTextLine = ClickableTextLine(textLine, minHitDistance, maxHitDistance)
            holo.lines.add(clickableTextLine)
        }

    }

    fun blockline(block : ItemStack, animationType: AnimationType? = null) {
        if(!block.type.isBlock) {
            throw RuntimeException("Itemstack ${block.type} is not a block")
        }
        val holo = getStaticHolo()
        val line = Line(holo.plugin)
        val blockline = BlockLine(line, block)
        if(animationType == null) {
            holo.lines.add(blockline)
        } else {
            val blockALine = BlockALine(blockline, StandardAnimatedLine(line))
            holo.onLoad.add {
                blockALine.setAnimation(animationType, it)
            }
        }
    }

    fun itemline(item: ItemStack, handRotation : EulerAngle) {
        if(!item.type.isItem) {
            throw RuntimeException("Itemstack ${item.type} is not a item")
        }
        val holo = getStaticHolo()
        val line = Line(holo.plugin)
        val blockline = ItemLine(line, item, handRotation)
        holo.lines.add(blockline)
    }

    fun customLine(customLine : ILine<*>) {
        val holo = getStaticHolo()
        holo.lines.add(customLine)
    }

    private fun <T> getFirstNonNull(vararg elements: T?): T? {
        return elements.firstOrNull() { it != null }
    }
}