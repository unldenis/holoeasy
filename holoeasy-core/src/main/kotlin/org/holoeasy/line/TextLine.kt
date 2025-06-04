package org.holoeasy.line

import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.holoeasy.hologram.Hologram

open class TextLine(
    hologram: Hologram,
    override var value: String,
) : LineImpl<String>(hologram, EntityType.ARMOR_STAND) {

    private var isEmpty = false
    fun parse(player: Player): String {
        return value
    }

    override val type: Type = Type.TEXT_LINE

    override fun show(player: Player) {
        isEmpty = value.isEmpty()
        if (!isEmpty) {
            spawn(player)

            hologram.lib.packetImpl
                .metadataText(player, entityID, nameTag = parse(player))
        }
    }

    override fun hide(player: Player) {
        destroy(player)
    }

    override fun update(player: Player) {
        val spawnBefore = ((if (isEmpty) 1 else 0) or ((if (value.isEmpty()) 1 else 0) shl 1))
        /*  0x00  = is already showed
            0x01  = is hided but now has changed
            0x02  = is already showed but is empty
            0x03  = is hided and isn't changed      */
        when (spawnBefore) {
            0x03 -> {}
            0x02 -> {
                destroy(player)
                isEmpty = true
            }

            0x01 -> {
                spawn(player)
                isEmpty = false

                hologram.lib.packetImpl
                    .metadataText(player, entityID, nameTag = parse(player))
            }

            0x00 ->
                hologram.lib.packetImpl
                    .metadataText(player, entityID, nameTag = parse(player), invisible = false)
        }
    }

    override fun update(newValue: String) {
        this.value = newValue
        observerUpdate()
    }

}