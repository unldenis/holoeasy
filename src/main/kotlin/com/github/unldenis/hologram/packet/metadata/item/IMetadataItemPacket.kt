package com.github.unldenis.hologram.packet.metadata.item

import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.packet.IPacket
import org.bukkit.inventory.ItemStack

interface IMetadataItemPacket : IPacket {

    override val impls: Array<out IPacket>
        get() = arrayOf(MetadataItemPacketA)
    fun metadata(entityId : Int, item : ItemStack) : PacketContainer
}