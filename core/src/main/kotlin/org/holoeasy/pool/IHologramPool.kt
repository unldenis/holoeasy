package org.holoeasy.pool

import org.holoeasy.config.HologramKey
import org.bukkit.plugin.Plugin
import org.holoeasy.hologram.Hologram
import org.jetbrains.annotations.ApiStatus.Experimental

interface IHologramPool {

    val plugin: Plugin

    fun get(key: HologramKey): Hologram

    @Experimental
    fun get(keyId: String) : Hologram

    fun takeCareOf(key: HologramKey, value: Hologram)

    fun remove(key: HologramKey): Hologram?

}
