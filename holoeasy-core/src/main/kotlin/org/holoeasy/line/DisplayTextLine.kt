package org.holoeasy.line

import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.holoeasy.hologram.Hologram

class DisplayTextLine(
    hologram: Hologram,
    override var value: String,
    var lineWidth : Int,
    var backgroundColor : Int,
    var textOpacity : Byte
) : LineImpl<String>(hologram, EntityType.TEXT_DISPLAY) {

    fun parse(player: Player): String {
        return value
    }

    override val type: Type = Type.DISPLAY_TEXT_LINE

    override fun show(player: Player) {
        if (value.isNotEmpty()) {
            spawn(player)

            this.update(player)
        }
    }

    override fun hide(player: Player) {
        destroy(player)
    }

    override fun update(player: Player) {
        hologram.lib.packetImpl
            .metadataDisplayText(
                player = player,
                entityId = entityID,
                text = parse(player),
                lineWidth = lineWidth,
                backgroundColor = backgroundColor,
                textOpacity = textOpacity,
            )
    }

    override fun update(newValue: String) {
        this.value = newValue
        observerUpdate()
    }

}