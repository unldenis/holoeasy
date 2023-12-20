package com.github.unldenis.hologram.hologram

import com.github.unldenis.hologram.line.ILine

interface IHologramLoader {
    fun load(hologram: Hologram, lines: Array<out ILine<*>>)

    fun teleport(hologram: Hologram)
}