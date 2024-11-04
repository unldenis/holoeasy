package org.holoeasy.line

import org.bukkit.Location
import org.bukkit.entity.Player
import org.holoeasy.animation.Animations
import org.holoeasy.hologram.Hologram
import org.holoeasy.reactive.Observer
import org.holoeasy.util.scheduler.SchedulerTask
import org.jetbrains.annotations.ApiStatus

interface ILine<T : Any> {

    abstract class PrivateConfig<T> : Observer {

        lateinit var hologram: Hologram

        var animationTask: SchedulerTask? = null

        override fun observerUpdate() {
            for (player in hologram.pvt.seeingPlayers) {
                update(player)
            }
        }

        // abstract internal
        abstract var obj: T

        abstract fun setLocation(value: Location)

        abstract fun show(player: Player)

        abstract fun hide(player: Player)

        abstract fun teleport(player: Player)

        abstract fun update(player: Player)

    }

    val type: Type

    val entityId: Int

    val location: Location?

    @Deprecated("Internal")
    var pvt: PrivateConfig<T>

    fun setAnimation(animation: Animations) {
        this.cancelAnimation()
        pvt.animationTask = animation.task(this)
    }

    fun cancelAnimation() {
        pvt.animationTask?.cancel()
        pvt.animationTask = null
    }

    fun update(value: T)


    enum class Type {
        EXTERNAL,

        TEXT_LINE,

        @ApiStatus.Experimental
        CLICKABLE_TEXT_LINE,

        ITEM_LINE,

        @ApiStatus.Experimental
        BLOCK_LINE
    }
}