package org.holoeasy


import org.bukkit.plugin.Plugin
import org.holoeasy.action.ClickAction
import org.holoeasy.hologram.Hologram
import org.holoeasy.packet.IPacket
import org.holoeasy.packet.PacketImpl
import org.holoeasy.pool.HologramPool
import org.holoeasy.pool.IHologramPool
import org.holoeasy.pool.InteractiveHologramPool


object HoloEasy {

    @JvmStatic
    @JvmOverloads
    fun bind(plugin: Plugin, packetImpl: PacketImpl = PacketImpl.ProtocolLib) {
        this.PLUGIN = plugin
        this.PACKET_IMPL = packetImpl.impl
    }

    @JvmStatic
    val standardPool by lazy {
        startInteractivePool<Hologram>(60.0)
    }

    @JvmField
    var useLastSupportedVersion: Boolean = false

    @JvmStatic
    fun <T : Hologram> startPool(spawnDistance: Double): IHologramPool<T> {
        val simplepool = HologramPool<T>(spawnDistance)
        return simplepool
    }

    @JvmStatic
    @JvmOverloads
    fun <T : Hologram> startInteractivePool(
        spawnDistance: Double,
        minHitDistance: Float = 0.5f,
        maxHitDistance: Float = 5f,
        clickAction: ClickAction? = null
    ): IHologramPool<T> {
        val simplepool = HologramPool<T>(spawnDistance)
        val interactivepool = InteractiveHologramPool<T>(
            pool = simplepool,
            minHitDistance = minHitDistance,
            maxHitDistance = maxHitDistance,
            clickAction = clickAction
        )
        return interactivepool
    }


    // Internal

    private var PLUGIN: Plugin? = null

    private var PACKET_IMPL: IPacket? = null

    fun plugin(): Plugin {
        if (PLUGIN == null) {
            throw IllegalStateException("HoloEasy Plugin is not set")
        }
        return PLUGIN!!
    }


    fun packetImpl(): IPacket {
        if (PACKET_IMPL == null) {
            throw IllegalStateException("HoloEasy PacketImpl is not set")
        }
        return PACKET_IMPL!!
    }


}
