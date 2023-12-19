package com.github.unldenis.hologram.packet.equipment

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.ext.compressAngle
import com.github.unldenis.hologram.ext.fixCoordinate
import com.github.unldenis.hologram.ext.set
import com.github.unldenis.hologram.packet.packet
import com.github.unldenis.hologram.util.VersionEnum
import org.bukkit.Location
import org.bukkit.inventory.ItemStack

object EquipmentPacketA : IEquipmentPacket {
    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_8..VersionEnum.V1_8)

    override fun equipment(entityID: Int, helmet: ItemStack): PacketContainer {
        return packet(PacketType.Play.Server.ENTITY_EQUIPMENT) {
            integers[0] = entityID
            // Use legacy form to update the head slot.
            integers[1] = 4

            itemModifier[0] = helmet

        }
    }


}