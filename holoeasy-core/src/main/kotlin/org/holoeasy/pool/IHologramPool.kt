package org.holoeasy.pool

import org.holoeasy.HoloEasy
import org.holoeasy.hologram.Hologram

interface IHologramPool<T : Hologram> {

    val lib : HoloEasy

    val holograms: Set<T>

}
