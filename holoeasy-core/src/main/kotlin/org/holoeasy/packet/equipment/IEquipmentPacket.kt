package org.holoeasy.packet.equipment

import com.comphenix.protocol.events.PacketContainer
import org.bukkit.inventory.ItemStack
import org.holoeasy.packet.IPacket

interface IEquipmentPacket : IPacket {

    fun equip(entityId : Int, helmet : ItemStack) : PacketContainer
}