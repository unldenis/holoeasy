package org.holoeasy.hologram

import org.bukkit.entity.Player
import org.jetbrains.annotations.NotNull

fun interface ShowEvent {

    fun onShow(@NotNull player: Player)

}