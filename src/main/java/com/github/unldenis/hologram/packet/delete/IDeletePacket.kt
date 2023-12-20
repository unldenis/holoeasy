package com.github.unldenis.hologram.packet.delete

import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.packet.IPacket

interface IDeletePacket : IPacket {

    fun delete(entityId: Int): PacketContainer
}