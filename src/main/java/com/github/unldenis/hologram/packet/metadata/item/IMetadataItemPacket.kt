package com.github.unldenis.hologram.packet.metadata.item

import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.packet.IPacket
import org.bukkit.inventory.ItemStack

interface IMetadataItemPacket : IPacket {
    fun metadata(entityId: Int, item: ItemStack): PacketContainer
}