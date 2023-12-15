package com.github.unldenis.hologram.line.animated

import com.github.unldenis.hologram.animation.Animation
import com.github.unldenis.hologram.line.BlockLine
import com.github.unldenis.hologram.line.ILine
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import java.util.*

class BlockALine(private val blockLine: BlockLine, private val animatedLine: IAnimated) : ILine<ItemStack>, IAnimated {

    override val plugin: Plugin
        get() = blockLine.plugin

    override val type: ILine.Type
        get() = ILine.Type.BLOCK_ANIMATED_LINE

    override val entityId: Int
        get() = blockLine.entityId

    override val location: Location?
        get() = blockLine.location

    override var obj: ItemStack
        get() = blockLine.obj
        set(value) {
            blockLine.obj = value
        }

    override fun setLocation(location: Location) {
        blockLine.setLocation(location)
    }

    override fun hide(player: Player) {
        blockLine.hide(player)
    }

    override fun teleport(player: Player) {
        blockLine.teleport(player)
    }

    override fun show(player: Player) {
        blockLine.show(player)
    }

    override fun update(player: Player) {
        blockLine.update(player)
    }

    override fun setAnimation(animation: Animation, seeingPlayers: Collection<Player>) {
        animatedLine.setAnimation(animation, seeingPlayers)
    }

    override fun removeAnimation() {
        animatedLine.removeAnimation()
    }

    override var animation: Animation = animatedLine.animation
}
