package org.holoeasy.line

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import org.holoeasy.animation.Animations
import org.holoeasy.hologram.Hologram
import org.holoeasy.reactive.Observer
import org.jetbrains.annotations.ApiStatus

interface ILine<T> {

    data class PrivateConfig(private val line: ILine<*>) : Observer {

        lateinit var hologram: Hologram

        var animationTask : BukkitTask? = null

        override fun observerUpdate() {
            hologram.let {
                line.update(it.seeingPlayers)
            }
        }
    }

    val plugin: Plugin

    val type: Type

    val entityId: Int

    val location: Location?

    var obj: T

    var pvt : PrivateConfig

    fun setLocation(value: Location)

    fun hide(player: Player)

    fun teleport(player: Player)

    fun show(player: Player)

    fun update(player: Player)

    fun update(seeingPlayers: Collection<Player>) {
        for (player in seeingPlayers) {
            update(player)
        }
    }

    fun setAnimation(animation: Animations) {
        this.cancelAnimation()
        pvt.animationTask = animation.task(this)
    }

    fun cancelAnimation() {
        pvt.animationTask?.cancel()
        pvt.animationTask = null
    }

    enum class Type {
        EXTERNAL,

        TEXT_LINE,
        @ApiStatus.Experimental CLICKABLE_TEXT_LINE,

        ITEM_LINE,
        @ApiStatus.Experimental BLOCK_LINE
    }
}