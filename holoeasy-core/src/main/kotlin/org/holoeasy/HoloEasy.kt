package org.holoeasy


import org.bukkit.plugin.Plugin
import org.holoeasy.action.ClickAction
import org.holoeasy.hologram.Hologram
import org.holoeasy.pool.HologramPool
import org.holoeasy.pool.IHologramPool
import org.holoeasy.pool.InteractiveHologramPool


object HoloEasy {

    private var PLUGIN: Plugin? = null

    fun plugin(): Plugin {
        if (PLUGIN == null) {
            throw IllegalStateException("HoloEasy Plugin is not set")
        }
        return PLUGIN!!
    }

    @JvmStatic
    fun bind(plugin: Plugin) {
        this.PLUGIN = plugin
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


}
