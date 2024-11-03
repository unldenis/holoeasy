package org.holoeasy.hologram

import org.bukkit.entity.Player
import org.jetbrains.annotations.NotNull

fun interface HideEvent {

    fun onHide(@NotNull player: Player)

}