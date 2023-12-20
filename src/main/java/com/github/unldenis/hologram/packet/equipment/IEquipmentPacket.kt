package com.github.unldenis.hologram.packet.equipment

import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.packet.IPacket
import org.bukkit.inventory.ItemStack

interface IEquipmentPacket : IPacket {

    fun equipment(entityID: Int, helmet: ItemStack): PacketContainer
}