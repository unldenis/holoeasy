package org.holoeasy.pool

import org.bukkit.plugin.Plugin
import org.holoeasy.builder.HologramBuilder
import org.holoeasy.builder.interfaces.HologramRegisterGroup
import org.holoeasy.hologram.Hologram
import java.util.UUID

interface IHologramPool {

    val plugin: Plugin

    fun get(id: UUID): Hologram

    fun takeCareOf(value: Hologram)

    fun remove(id : UUID): Hologram?

    fun registerHolograms(registerGroup: HologramRegisterGroup) {
        HologramBuilder.registerHolograms(this, registerGroup)
    }

}
