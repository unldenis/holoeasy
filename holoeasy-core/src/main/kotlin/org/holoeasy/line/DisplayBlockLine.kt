package org.holoeasy.line


import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.holoeasy.hologram.Hologram
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
class DisplayBlockLine(
    hologram: Hologram,
    override var value : Material
) : LineImpl<Material>(hologram, EntityType.BLOCK_DISPLAY) {


    override val type: Type = Type.DISPLAY_BLOCK_LINE

    override fun show(player: Player) {
        spawn(player)

        this.update(player)
    }

    override fun hide(player: Player) {
        destroy(player)
    }

    override fun update(player: Player) {
        hologram.lib.packetImpl
            .metadataDisplayBlock(player, entityID, value)
    }

    override fun update(newValue: Material) {
        this.value = newValue
        observerUpdate()
    }

}
