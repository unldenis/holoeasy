package org.holoeasy.pool

import org.bukkit.plugin.Plugin
import org.holoeasy.hologram.Hologram

interface IHologramPool {

    @Deprecated("Internal")
    var plugin: Plugin?

    val holograms: Set<Hologram>

}
