package org.holoeasy.hologram

import org.holoeasy.line.ILine


interface IHologramLoader {
    fun load(hologram: Hologram, lines: Array<out ILine<*>>)

    fun teleport(hologram: Hologram)
}