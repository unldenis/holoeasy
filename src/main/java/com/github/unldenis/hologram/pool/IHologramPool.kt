package com.github.unldenis.hologram.pool

import com.github.unldenis.hologram.hologram.Hologram
import org.bukkit.plugin.Plugin

interface IHologramPool {

    val plugin: Plugin
    val holograms: Collection<Hologram>

    fun takeCareOf(hologram: Hologram)

    fun remove(hologram: Hologram): Boolean

}
