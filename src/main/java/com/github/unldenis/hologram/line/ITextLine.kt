package com.github.unldenis.hologram.line

import com.github.unldenis.hologram.placeholder.Placeholders
import org.bukkit.entity.Player

interface ITextLine : ILine<String> {

    fun isClickable() : Boolean

    fun parse(player: Player) : String

    fun asTextLine() : TextLine

    fun getPlaceholders() : Placeholders
}