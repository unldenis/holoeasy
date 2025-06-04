package org.holoeasy.line


import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.holoeasy.hologram.Hologram
import org.holoeasy.util.VersionEnum
import org.holoeasy.util.VersionUtil

class ItemLine(
    hologram: Hologram,
    override var value: ItemStack
) : LineImpl<ItemStack>(hologram, EntityType.ITEM) {

    private var firstRender = true


    init {
        if (VersionUtil.isCompatible(VersionEnum.V1_8)) {
            throw IllegalStateException("This version does not support item lines")
        }
    }

    override val type: Type = Type.ITEM_LINE


    override fun show(player: Player) {
        spawn(player)
        this.update(player)

        hologram.lib.packetImpl
            .velocity(player, entityID, 0.0, 0.0, 0.0)
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
