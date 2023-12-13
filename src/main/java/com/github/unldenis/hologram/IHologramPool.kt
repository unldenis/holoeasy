package com.github.unldenis.hologram

import org.bukkit.plugin.Plugin

interface IHologramPool {
    fun takeCareOf(hologram: Hologram)

    val plugin: Plugin

    fun remove(hologram: Hologram): Boolean

    val holograms: Collection<Hologram>
}
