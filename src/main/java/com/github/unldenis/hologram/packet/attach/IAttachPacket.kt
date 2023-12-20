package com.github.unldenis.hologram.packet.attach

import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.packet.IPacket

interface IAttachPacket : IPacket {

    fun attachEntity(entityId : Int, passenger : Int) : PacketContainer
}