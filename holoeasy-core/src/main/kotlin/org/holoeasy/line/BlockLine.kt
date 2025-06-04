package org.holoeasy.line


import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.holoeasy.hologram.Hologram

class BlockLine(
    hologram: Hologram,
    override var value: ItemStack
) : LineImpl<ItemStack>(hologram, EntityType.ARMOR_STAND) {

    override val type: Type = Type.BLOCK_LINE


    override fun show(player: Player) {
        spawn(player)

        hologram.lib.packetImpl
            .metadataText(player, entityID, nameTag = null, invisible = true)

        this.update(player)
    }

    override fun hide(player: Player) {
        destroy(player)
    }

    override fun update(player: Player) {
        hologram.lib.packetImpl
            .metadataItem(player, entityID, item = value)
    }

    override fun update(newValue: ItemStack) {
        this.value = newValue
        observerUpdate()
    }

}
