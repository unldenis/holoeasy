package org.holoeasy


import org.bukkit.plugin.Plugin
import org.holoeasy.action.ClickAction
import org.holoeasy.pool.HologramPool
import org.holoeasy.pool.IHologramPool
import org.holoeasy.pool.InteractiveHologramPool


object HoloEasy {

    private var PLUGIN : Plugin? = null

    @JvmStatic
    fun bind(plugin: Plugin) {
        this.PLUGIN = plugin
    }

    fun plugin() : Plugin {
        if(PLUGIN == null) {
            throw IllegalStateException("HoloEasy Plugin is not set")
        }
        return PLUGIN!!
    }

    val STANDARD_POOL by lazy {
        startInteractivePool(60.0)
    }

    @JvmField
    var useLastSupportedVersion: Boolean = false

    @JvmStatic
    fun startPool(spawnDistance: Double): IHologramPool {
        val simplepool = HologramPool(spawnDistance)
        return simplepool
    }

    @JvmStatic
    @JvmOverloads
    fun startInteractivePool(
        spawnDistance: Double,
        minHitDistance: Float = 0.5f,
        maxHitDistance: Float = 5f,
        clickAction: ClickAction? = null
    ): IHologramPool {
        val simplepool = HologramPool(spawnDistance)
        val interactivepool = InteractiveHologramPool(
            pool = simplepool,
            minHitDistance = minHitDistance,
            maxHitDistance = maxHitDistance,
            clickAction = clickAction
        )
        return interactivepool
    }


}
