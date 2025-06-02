package org.holoeasy.pool

import org.holoeasy.HoloEasy
import org.holoeasy.hologram.Hologram
import org.jetbrains.annotations.ApiStatus

interface IHologramPool<T : Hologram> {

    val lib : HoloEasy

    val holograms: Set<T>

    @ApiStatus.Experimental
    fun destroy()
}
