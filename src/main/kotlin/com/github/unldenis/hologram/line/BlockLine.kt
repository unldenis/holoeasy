package com.github.unldenis.hologram.line

import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.ext.send
import com.github.unldenis.hologram.packet.PacketsFactory
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class BlockLine(line: Line, override var obj: ItemStack) : ILine<ItemStack> {
    private val line: Line
    private val entityMetadataPacket: PacketContainer
    override val plugin: Plugin
        get() = line.plugin

    override val type: ILine.Type
        get() = ILine.Type.BLOCK_LINE

    override val entityId: Int
        get() = line.entityID

    override val location: Location?
        get() = line.location

    init {
        if (!obj.type.isBlock) {
            throw UnsupportedOperationException(
                "'%s' is not a block. Are you looking for the new experimental ItemLine class?".formatted(obj.type.name)
            )
        }
        this.line = line
        this.entityMetadataPacket = PacketsFactory.get().metadataPacket(line.entityID, null)
    }


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
        entityMetadataPacket.send(player)
        this.update(player)
    }

    override fun update(player: Player) {
        PacketsFactory.get()
            .equipmentPacket(line.entityID, this.obj, false)
            .send(player)
    }
}
