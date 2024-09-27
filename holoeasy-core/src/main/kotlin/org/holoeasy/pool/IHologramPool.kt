package org.holoeasy.pool

import org.holoeasy.hologram.Hologram
import java.util.UUID

interface IHologramPool {

    fun get(id: UUID): Hologram

    fun takeCareOf(value: Hologram)

    fun remove(id : UUID): Hologram?

    fun holograms() : Set<Hologram>

}
