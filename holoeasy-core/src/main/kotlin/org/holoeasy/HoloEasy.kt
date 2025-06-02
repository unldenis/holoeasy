package org.holoeasy


import org.bukkit.plugin.Plugin
import org.holoeasy.action.ClickAction
import org.holoeasy.hologram.Hologram
import org.holoeasy.packet.IPacket
import org.holoeasy.packet.PacketImpl
import org.holoeasy.pool.HologramPool
import org.holoeasy.pool.IHologramPool
import org.holoeasy.pool.InteractiveHologramPool
import org.jetbrains.annotations.ApiStatus.Internal


class HoloEasy(val plugin : Plugin, packetImpl: PacketImpl) {

    // Internal
    @Internal
    internal val packetImpl : IPacket = packetImpl.impl

    fun <T : Hologram> startPool(spawnDistance: Double): IHologramPool<T> {
        val simplepool = HologramPool<T>(this, spawnDistance)
        return simplepool
    }

    @JvmOverloads
    fun <T : Hologram> startInteractivePool(
        spawnDistance: Double,
        minHitDistance: Float = 0.5f,
        maxHitDistance: Float = 5f,
        clickAction: ClickAction? = null
    ): IHologramPool<T> {
        val simplepool = HologramPool<T>(this, spawnDistance)
        val interactivepool = InteractiveHologramPool<T>(
            pool = simplepool,
            minHitDistance = minHitDistance,
            maxHitDistance = maxHitDistance,
            clickAction = clickAction
        )
        return interactivepool
    }

}
