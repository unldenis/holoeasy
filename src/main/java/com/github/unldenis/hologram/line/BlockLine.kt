package com.github.unldenis.hologram.line

import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.packet.PacketsFactory
import com.github.unldenis.hologram.packet.send
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class BlockLine(line: Line, obj: ItemStack) : ILine<ItemStack> {
    private val line: Line
    private val entityMetadataPacket: PacketContainer

    private var obj: ItemStack

    init {
        if (!obj.type.isBlock) {
            throw UnsupportedOperationException(
                "'%s' is not a block. Are you looking for the new experimental ItemLine class?".formatted(obj.type.name)
            )
        }
        this.line = line
        this.entityMetadataPacket = PacketsFactory.get().metadataPacket(line.entityID, null)

        this.obj = obj
    }

    override fun getPlugin(): Plugin {
        return line.plugin
    }

    override fun getType(): ILine.Type {
        return ILine.Type.BLOCK_LINE
    }

    override fun getEntityId(): Int {
        return line.entityID
    }

    override fun getLocation(): Location? {
        return line.location
    }

    override fun setLocation(location: Location) {
        line.location = location
    }

    override fun getObj(): ItemStack {
        return obj.clone()
    }

    override fun setObj(obj: ItemStack) {
        this.obj = obj
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
