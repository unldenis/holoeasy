package com.github.unldenis.hologram

import com.github.unldenis.hologram.builder.Service
import com.github.unldenis.hologram.pool.HologramPool
import com.github.unldenis.hologram.pool.IHologramPool
import com.github.unldenis.hologram.pool.InteractiveHologramPool
import org.bukkit.plugin.Plugin

object HologramLib {

    @JvmField
    var useLastSupportedVersion: Boolean = false

    @JvmStatic
    fun startPool(plugin: Plugin, spawnDistance: Double): IHologramPool {
        val simplepool = HologramPool(plugin, spawnDistance)
        Service.lastPool.set(simplepool)
        return simplepool
    }

    @JvmStatic
    fun startInteractivePool(
        plugin: Plugin, spawnDistance: Double,
        minHitDistance: Float, maxHitDistance: Float
    ): IHologramPool {
        val simplepool = HologramPool(plugin, spawnDistance)
        val interactivepool = InteractiveHologramPool(simplepool, minHitDistance, maxHitDistance)
        Service.lastPool.set(interactivepool)
        return interactivepool
    }


}
