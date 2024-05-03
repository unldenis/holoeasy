package org.holoeasy.packet.rotate

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import org.holoeasy.ext.compressAngle
import org.holoeasy.ext.set
import org.holoeasy.packet.packet
import org.holoeasy.util.VersionEnum

object RotatePacketA : IRotatePacket {

    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_8..VersionEnum.V1_20)

    override fun rotate(entityId: Int, yaw: Double) : PacketContainer {
        return packet(PacketType.Play.Server.ENTITY_LOOK) {
            integers[0] = entityId
            bytes[0] = yaw.compressAngle
        }
    }

}