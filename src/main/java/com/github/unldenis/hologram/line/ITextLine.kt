package com.github.unldenis.hologram.line

import com.github.unldenis.hologram.builder.interfaces.PlayerFun
import org.bukkit.entity.Player

interface ITextLine : ILine<String> {

    val clickable: Boolean

    val textLine: TextLine

    val args: Array<PlayerFun>?

    fun parse(player: Player): String

}