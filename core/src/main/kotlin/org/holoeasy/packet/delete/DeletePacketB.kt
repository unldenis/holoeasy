package org.holoeasy.packet.delete

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import org.holoeasy.ext.set
import org.holoeasy.packet.packet
import org.holoeasy.util.VersionEnum

object DeletePacketB : IDeletePacket {

    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_17..VersionEnum.V1_20)

    override fun delete(entityId: Int): PacketContainer {
        return packet(PacketType.Play.Server.ENTITY_DESTROY) {
            intLists[0] = listOf(entityId)
        }
    }
}