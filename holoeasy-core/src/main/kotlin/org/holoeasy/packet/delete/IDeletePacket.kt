package org.holoeasy.packet.delete

import com.comphenix.protocol.events.PacketContainer
import org.holoeasy.packet.IPacket

interface IDeletePacket : IPacket {

    fun delete(entityId: Int): PacketContainer
}