package org.holoeasy.line

import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import org.holoeasy.animation.Animations
import org.holoeasy.hologram.Hologram
import org.jetbrains.annotations.ApiStatus
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

sealed class LineImpl<T>(
    val hologram: Hologram,
    val entityType: EntityType
) : Line<T> {

 private var location: Location? = null

    // Exposed Methods

    abstract override val type: Type

    override val entityID: Int = IDs_COUNTER.getAndIncrement()

    // abstract internal
    abstract override var value: T

    override fun currentLocation() = location

    abstract override fun update(newValue: T)


    private var animationTask: BukkitTask? = null

    override fun setAnimation(animation: Animations) {
        this.cancelAnimation()
        animationTask = animation.task(this)
    }

    override fun cancelAnimation() {
        animationTask?.cancel()
        animationTask = null
    }

    // Internal

    abstract fun show(player: Player)


    abstract fun hide(player: Player)


    abstract fun update(player: Player)


    fun destroy(player: Player) {
        hologram.lib.packetImpl.deletePacket(player, entityID)
    }


    fun spawn(player: Player): Boolean {
        hologram.lib.packetImpl.spawn(hologram.lib, player, entityID, entityType, location ?: return false)
        return true
    }


    fun teleport(player: Player): Boolean {
        hologram.lib.packetImpl.teleport(player, entityID, location ?: return false)
        return true
    }


    open fun setCurrentLocation(value: Location) {
        this.location = value
    }


    fun observerUpdate() {
        for (player in hologram.pvt.seeingPlayers) {
            update(player)
        }
    }

    companion object {
        val IDs_COUNTER = AtomicInteger(500 + Random().nextInt())
    }

    enum class Type {
        EXTERNAL,

        TEXT_LINE,
        CLICKABLE_TEXT_LINE,
        ITEM_LINE,
        BLOCK_LINE,

        @ApiStatus.Experimental
        DISPLAY_BLOCK_LINE,
        @ApiStatus.Experimental
        DISPLAY_TEXT_LINE
    }


}