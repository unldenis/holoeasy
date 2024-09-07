package org.holoeasy.packet.velocity

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import org.holoeasy.ext.set
import org.holoeasy.packet.packet
import org.holoeasy.util.VersionEnum

object VelocityPacketA : IVelocityPacket {

    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_8..VersionEnum.LATEST)

    override fun velocity(entityId: Int, x: Int, y: Int, z: Int): PacketContainer {
        return packet(PacketType.Play.Server.ENTITY_VELOCITY) {
            integers[0] = entityId
            integers[1] = x
            integers[2] = y
            integers[3] = z
        }
    }


}