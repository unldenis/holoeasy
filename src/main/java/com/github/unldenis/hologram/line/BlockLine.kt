package com.github.unldenis.hologram.line

import com.github.unldenis.hologram.ext.send
import com.github.unldenis.hologram.packet.IPacket
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class BlockLine(line: Line, override var obj: ItemStack) : ILine<ItemStack> {
    private val line: Line
    private var vehicle : Line
    override val plugin: Plugin
        get() = line.plugin

    override val type: ILine.Type
        get() = ILine.Type.BLOCK_LINE

    override val entityId: Int
        get() = vehicle.entityID // animations needs this, the item line is useless since attached

    override val location: Location?
        get() = line.location

    init {
        if (!obj.type.isBlock) {
            throw UnsupportedOperationException(
                "'${obj.type.name}' is not a block. Are you looking for the new experimental ItemLine class?"
            )
        }
        this.line = line
        vehicle = Line(plugin, EntityType.ARMOR_STAND, location)
    }


    override fun setLocation(value: Location) {
        vehicle.location = value
    }

    override fun hide(player: Player) {
        vehicle.destroy(player)
        line.destroy(player)
    }

    override fun teleport(player: Player) {
        vehicle.teleport(player)
    }

    override fun show(player: Player) {
        vehicle.spawn(player)
        IPacket.get(IPacket.Type.METADATA_TEXT)
            .metadata(vehicle.entityID, nameTag = null)

        line.spawn(player)
        this.update(player)
        IPacket.get(IPacket.Type.ATTACH_ENTITY)
            .attachEntity(vehicle.entityID, line.entityID).send(player)
    }

    override fun update(player: Player) {
        IPacket.get(IPacket.Type.METADATA_ITEM)
            .metadata(line.entityID, obj).send(player)
    }
}
