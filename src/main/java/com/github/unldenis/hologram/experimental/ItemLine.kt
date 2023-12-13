package com.github.unldenis.hologram.experimental

import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.line.ILine
import com.github.unldenis.hologram.line.Line
import com.github.unldenis.hologram.packet.PacketsFactory
import com.github.unldenis.hologram.packet.send
import com.github.unldenis.hologram.util.NMSUtils
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.util.EulerAngle
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
class ItemLine(line: Line, obj: ItemStack, handRotation: EulerAngle) : ILine<ItemStack> {
    private val line: Line

    private var obj: ItemStack

    var handRotation: EulerAngle? = null
        private set
    private var entityMetadataPacket: PacketContainer? = null

    init {
        if (!obj.type.isItem) {
            throw UnsupportedOperationException(
                "'%s' is not a item. Are you looking for the BlockLine class?".formatted(obj.type.name)
            )
        }
        this.line = line

        this.obj = obj
        setHandRotation(handRotation)
    }

    private fun updateHandRotation(handRotation: EulerAngle) {
        this.handRotation = handRotation
        val handRotationNMS = NMSUtils.newNMSVector(
            Math.toDegrees(handRotation.x),
            Math.toDegrees(handRotation.y), Math.toDegrees(handRotation.z)
        )
        this.entityMetadataPacket = PacketsFactory.get()
            .metadataPacket(line.entityID, handRotationNMS)
    }

    fun setHandRotation(handRotation: EulerAngle, seeingPlayers: Iterable<Player>) {
        updateHandRotation(handRotation)

        for (p in seeingPlayers) {
            entityMetadataPacket?.send(p) ?: throw RuntimeException("Missing metadata packet")
        }
    }

    fun setHandRotation(handRotation: EulerAngle, vararg seeingPlayers: Player) {
        updateHandRotation(handRotation)

        for (p in seeingPlayers) {
            entityMetadataPacket?.send(p) ?: throw RuntimeException("Missing metadata packet")
        }
    }

    override fun getPlugin(): Plugin {
        return line.plugin
    }

    override fun getType(): ILine.Type {
        return ILine.Type.ITEM_LINE
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
        return obj
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
        entityMetadataPacket?.send(player) ?: throw RuntimeException("Missing metadata packet")
        this.update(player)
    }

    override fun update(player: Player) {
        PacketsFactory.get()
            .equipmentPacket(line.entityID, this.obj, true)
            .send(player)
    }
}
