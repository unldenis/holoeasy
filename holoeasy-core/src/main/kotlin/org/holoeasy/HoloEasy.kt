package org.holoeasy


import org.holoeasy.action.ClickAction
import org.holoeasy.pool.HologramPool
import org.holoeasy.pool.IHologramPool
import org.holoeasy.pool.InteractiveHologramPool

object HoloEasy {

    @JvmField
    val STANDARD_POOL = startInteractivePool(60.0)

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
