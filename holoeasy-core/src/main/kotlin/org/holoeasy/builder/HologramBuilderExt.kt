package org.holoeasy.builder

import org.bukkit.plugin.Plugin
import org.holoeasy.builder.interfaces.HologramRegisterGroup

fun <T : Plugin> T.registerHolograms(registerGroup: HologramRegisterGroup) {
    HologramBuilder.registerHolograms(this, registerGroup)
}

