package org.holoeasy.packet.equipment

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import org.bukkit.inventory.ItemStack
import org.holoeasy.ext.set
import org.holoeasy.packet.packet
import org.holoeasy.util.VersionEnum

object EquipmentPacketA : IEquipmentPacket {

    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() =  arrayOf(VersionEnum.V1_8..VersionEnum.V1_8)

    override fun equip(entityId: Int, helmet: ItemStack): PacketContainer {
        return packet(PacketType.Play.Server.ENTITY_EQUIPMENT) {
            integers[0] = entityId

            // Use legacy form to update the head slot.
            integers[1] = 4

            itemModifier[0] = helmet
        }
    }



}