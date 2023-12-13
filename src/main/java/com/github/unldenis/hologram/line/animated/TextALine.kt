package com.github.unldenis.hologram.line.animated

import com.github.unldenis.hologram.animation.Animation
import com.github.unldenis.hologram.line.ILine
import com.github.unldenis.hologram.line.ITextLine
import com.github.unldenis.hologram.line.TextLine
import com.github.unldenis.hologram.placeholder.Placeholders
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*

class TextALine(textLine: TextLine, private val animatedLine: IAnimated) : ITextLine, IAnimated {
    private val textLine: TextLine = textLine
    override fun isClickable(): Boolean {
        return textLine.isClickable()
    }

    override fun parse(player: Player): String {
        return textLine.parse(player)
    }

    override fun asTextLine(): TextLine {
        return textLine
    }

    override fun getPlaceholders(): Placeholders {
        return textLine.getPlaceholders()
    }

    override fun getPlugin(): Plugin {
        return textLine.getPlugin()
    }

    override fun getType(): ILine.Type {
        return textLine.getType()
    }

    override fun getEntityId(): Int {
        return textLine.getEntityId()
    }

    override fun getLocation(): Location? {
        return textLine.getLocation()
    }

    override fun setLocation(location: Location) {
        textLine.setLocation(location)
    }

    override fun getObj(): String {
        return textLine.getObj()
    }

    override fun setObj(obj: String) {
        textLine.setObj(obj)
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

    override val animation: Optional<Animation>
        get() = animatedLine.animation
}
