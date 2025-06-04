package org.holoeasy.builder

import org.bukkit.Color

class DisplayTextLineModifiers() {

    internal var lineWidth : Int = 200
    internal var backgroundColor : Int = 0x40000000
    internal var textOpacity : Byte = -1

    fun lineWidth(lineWidth: Int): DisplayTextLineModifiers {
        this.lineWidth = lineWidth
        return this
    }

    fun backgroundColor(backgroundColor: Color): DisplayTextLineModifiers {
        this.backgroundColor = backgroundColor.asARGB()
        return this
    }

    fun backgroundColor(backgroundColor: Int): DisplayTextLineModifiers {
        this.backgroundColor = backgroundColor
        return this
    }

    fun textOpacity(textOpacity: Byte): DisplayTextLineModifiers {
        this.textOpacity = textOpacity
        return this
    }

}