package org.holoeasy


import org.bukkit.plugin.Plugin
import org.holoeasy.hologram.Hologram
import org.holoeasy.packet.IPacket
import org.holoeasy.packet.PacketImpl
import org.holoeasy.pool.HologramPool
import org.holoeasy.pool.IHologramPool
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.ApiStatus.Internal


class HoloEasy(val plugin : Plugin, packetImpl: PacketImpl) {

    private val pools = mutableListOf<IHologramPool<*>>()

    // Internal
    @Internal
    internal val packetImpl : IPacket = packetImpl.impl

    fun <T : Hologram> startPool(spawnDistance: Double): IHologramPool<T> {
        val simplepool = HologramPool<T>(this, spawnDistance)
        return simplepool
    }

    @ApiStatus.Experimental
    fun destroyPools() {
        for (pool in pools) {
            pool.destroy()
        }
        pools.clear()
    }
}
