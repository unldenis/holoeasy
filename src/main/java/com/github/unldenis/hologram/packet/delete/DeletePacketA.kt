package com.github.unldenis.hologram.packet.delete

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.ext.set
import com.github.unldenis.hologram.packet.packet
import com.github.unldenis.hologram.util.VersionEnum

object DeletePacketA : IDeletePacket {
    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_8..VersionEnum.V1_16)

    override fun delete(entityId: Int): PacketContainer {
        return packet(PacketType.Play.Server.ENTITY_DESTROY) {
            integerArrays[0] = intArrayOf(entityId)
        }
    }


}