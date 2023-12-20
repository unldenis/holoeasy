package com.github.unldenis.hologram.packet.attach

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.ext.set
import com.github.unldenis.hologram.packet.packet
import com.github.unldenis.hologram.util.VersionEnum

object AttachPacketA : IAttachPacket {

    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_8..VersionEnum.V1_20)

    override fun attachEntity(entityId: Int, passenger: Int): PacketContainer {
        return packet(PacketType.Play.Server.ATTACH_ENTITY) {
            integers[0] = entityId
            integers[1] = passenger
            bytes[0] = 1
        }
    }
}