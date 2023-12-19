package com.github.unldenis.hologram.packet.equipment

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.EnumWrappers
import com.github.unldenis.hologram.ext.compressAngle
import com.github.unldenis.hologram.ext.set
import com.github.unldenis.hologram.packet.packet
import com.github.unldenis.hologram.util.VersionEnum
import org.bukkit.Location
import org.bukkit.inventory.ItemStack

object EquipmentPacketB : IEquipmentPacket {

    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_9..VersionEnum.V1_12)

    override fun equipment(entityID: Int, helmet: ItemStack): PacketContainer {
        return packet(PacketType.Play.Server.ENTITY_EQUIPMENT) {
            integers[0] = entityID
            itemSlots[0] = EnumWrappers.ItemSlot.HEAD
            itemModifier[0] = helmet
        }
    }

}