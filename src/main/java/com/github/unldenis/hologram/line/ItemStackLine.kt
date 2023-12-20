package com.github.unldenis.hologram.line

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class ItemStackLine(private val line: Line, override var obj: ItemStack) : ILine<ItemStack> {
    override val plugin: Plugin
        get() = line.plugin

    override val type: ILine.Type
        get() = ILine.Type.BLOCK_LINE

    override val entityId: Int
        get() = line.entityID

    override val location: Location?
        get() = line.location


    override fun setLocation(value: Location) {
        line.location = value
    }

    override fun hide(player: Player) {
        line.destroy(player)
    }

    override fun teleport(player: Player) {
        line.teleport(player)
    }

    override fun show(player: Player) {
        line.spawn(player)
        this.update(player)
        this.update(player)
    }

    override fun update(player: Player) {
//        PacketsFactory.get().metadataPacket(line.entityID, null, obj)
    }
}
