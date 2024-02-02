package org.holoeasy.packet.metadata.text

import com.comphenix.protocol.events.PacketContainer
import org.holoeasy.packet.IPacket

interface IMetadataTextPacket : IPacket {

    fun metadata(entityId: Int, nameTag: String?, invisible : Boolean = true): PacketContainer
}