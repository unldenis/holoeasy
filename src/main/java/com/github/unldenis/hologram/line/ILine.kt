package com.github.unldenis.hologram.line

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.jetbrains.annotations.ApiStatus

interface ILine<T> {

    val plugin : Plugin

    val type : Type

    val entityId : Int

    val location : Location?

    var obj : T

    fun setLocation(value : Location)
    
    fun hide(player: Player)

    fun teleport(player: Player)

    fun show(player: Player)

    fun update(player: Player)

    fun update(seeingPlayers: Collection<Player>) {
        for (player in seeingPlayers) {
            update(player)
        }
    }

    enum class Type {
        EXTERNAL,
        TEXT_LINE,
        TEXT_ANIMATED_LINE,
        @ApiStatus.Experimental
        CLICKABLE_TEXT_LINE,


        BLOCK_LINE,
        BLOCK_ANIMATED_LINE,


        ITEM_LINE,
    }
}