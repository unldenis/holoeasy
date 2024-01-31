package org.holoeasy.line


import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.holoeasy.ext.send
import org.holoeasy.packet.IPacket

class BlockLine(plugin: Plugin, override var obj: ItemStack) : ILine<ItemStack> {

    private val line: Line = Line(plugin, EntityType.DROPPED_ITEM)
    private val resetVelocity = IPacket.get(IPacket.Type.VELOCITY).velocity(line.entityID, 0, 0,0)

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

        resetVelocity.send(player)
    }

    override fun update(player: Player) {
        IPacket.get(IPacket.Type.METADATA_ITEM)
            .metadata(entityId, obj).send(player)
    }
}
