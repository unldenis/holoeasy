package org.holoeasy.packet.equipment

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.EnumWrappers
import org.bukkit.inventory.ItemStack
import org.holoeasy.ext.set
import org.holoeasy.packet.packet
import org.holoeasy.util.VersionEnum

object EquipmentPacketB : IEquipmentPacket {

    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() =  arrayOf(VersionEnum.V1_9..VersionEnum.V1_12)

    override fun equip(entityId: Int, helmet: ItemStack): PacketContainer {
        return packet(PacketType.Play.Server.ENTITY_EQUIPMENT) {
            integers[0] = entityId

            itemSlots[0] = EnumWrappers.ItemSlot.HEAD

            itemModifier[0] = helmet
        }
    }



}