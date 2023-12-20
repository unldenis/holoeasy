package com.github.unldenis.hologram.packet.metadata.text

import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.packet.IPacket

interface IMetadataTextPacket : IPacket {

    fun metadata(entityId: Int, nameTag: String): PacketContainer
}