package org.holoeasy


import org.bukkit.plugin.Plugin
import org.holoeasy.action.ClickAction
import org.holoeasy.pool.HologramPool
import org.holoeasy.pool.IHologramPool
import org.holoeasy.pool.InteractiveHologramPool

object HoloEasy {

    @JvmField
    var useLastSupportedVersion: Boolean = false

    @JvmStatic
    fun startPool(plugin: Plugin, spawnDistance: Double): IHologramPool {
        val simplepool = HologramPool(plugin, spawnDistance)
        return simplepool
    }

    @JvmStatic
    @JvmOverloads
    fun startInteractivePool(
        plugin: Plugin,
        spawnDistance: Double,
        minHitDistance: Float = 0.5f,
        maxHitDistance: Float = 5f,
        clickAction: ClickAction? = null
    ): IHologramPool {
        val simplepool = HologramPool(plugin, spawnDistance)
        val interactivepool = InteractiveHologramPool(
            pool = simplepool,
            minHitDistance = minHitDistance,
            maxHitDistance = maxHitDistance,
            clickAction = clickAction
        )
        return interactivepool
    }


}
