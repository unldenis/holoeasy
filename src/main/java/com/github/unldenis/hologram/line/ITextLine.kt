package com.github.unldenis.hologram.line

import com.github.unldenis.hologram.placeholder.Placeholders
import org.bukkit.entity.Player

interface ITextLine : ILine<String> {

    val clickable : Boolean

    val textLine : TextLine

    val placeholders : Placeholders

    fun parse(player: Player) : String

}