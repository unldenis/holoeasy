package com.github.unldenis.hologram

import org.bukkit.plugin.Plugin

interface IHologramPool {

    val plugin: Plugin
    val holograms: Collection<Hologram>

    fun takeCareOf(hologram: Hologram)

    fun remove(hologram: Hologram): Boolean


}
