package org.holoeasy.line

import org.bukkit.entity.Player

interface ITextLine : ILine<String> {

    val clickable: Boolean

    val textLine: TextLine

    val args: Array<*>?

    fun parse(player: Player): String

    fun onClick(clickEvent: ClickEvent)

}