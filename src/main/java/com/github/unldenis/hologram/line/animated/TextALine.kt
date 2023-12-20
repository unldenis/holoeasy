package com.github.unldenis.hologram.line.animated

import com.github.unldenis.hologram.animation.Animation
import com.github.unldenis.hologram.builder.interfaces.PlayerFun
import com.github.unldenis.hologram.line.ILine
import com.github.unldenis.hologram.line.ITextLine
import com.github.unldenis.hologram.line.TextLine
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class TextALine(override val textLine: TextLine, private val animatedLine: IAnimated) : ITextLine, IAnimated {
    override val clickable: Boolean
        get() = textLine.clickable
    override val args: Array<PlayerFun>?
        get() = textLine.args

    override fun parse(player: Player): String {
        return textLine.parse(player)
    }

    override val plugin: Plugin
        get() = textLine.plugin

    override val type: ILine.Type
        get() = ILine.Type.TEXT_ANIMATED_LINE

    override val entityId: Int
        get() = textLine.entityId

    override val location: Location?
        get() = textLine.location

    override var obj: String
        get() = textLine.obj
        set(value) {
            textLine.obj = value
        }

    override fun setLocation(value: Location) {
        textLine.setLocation(value)
    }

    override fun hide(player: Player) {
        textLine.hide(player)
    }

    override fun teleport(player: Player) {
        textLine.teleport(player)
    }

    override fun show(player: Player) {
        textLine.show(player)
    }

    override fun update(player: Player) {
        textLine.update(player)
    }

    override fun setAnimation(animation: Animation, seeingPlayers: Collection<Player>) {
        animatedLine.setAnimation(animation, seeingPlayers)
    }

    override fun removeAnimation() {
        animatedLine.removeAnimation()
    }

    override var animation: Animation = animatedLine.animation

}
