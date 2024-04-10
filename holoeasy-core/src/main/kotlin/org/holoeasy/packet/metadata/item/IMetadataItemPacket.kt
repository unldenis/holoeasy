package org.holoeasy.packet.metadata.item

import com.comphenix.protocol.events.PacketContainer
import org.bukkit.inventory.ItemStack
import org.holoeasy.packet.IPacket

interface IMetadataItemPacket : IPacket {
    fun metadata(entityId: Int, item: ItemStack): PacketContainer
}