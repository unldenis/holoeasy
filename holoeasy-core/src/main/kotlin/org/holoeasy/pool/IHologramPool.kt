package org.holoeasy.pool

import org.holoeasy.hologram.Hologram

interface IHologramPool<T : Hologram> {

    val holograms: Set<T>

}
