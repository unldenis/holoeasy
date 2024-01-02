package com.github.unldenis.hologram.pool

import com.github.unldenis.hologram.config.HologramKey
import com.github.unldenis.hologram.hologram.Hologram
import org.bukkit.plugin.Plugin
import org.jetbrains.annotations.ApiStatus.Experimental

interface IHologramPool {

    val plugin: Plugin

    fun get(key: HologramKey): Hologram

    @Experimental
    fun get(keyId: String) : Hologram

    fun takeCareOf(key: HologramKey, value: Hologram)

    fun remove(key: HologramKey): Hologram?

}
