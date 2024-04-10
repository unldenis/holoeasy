package org.holoeasy.packet.teleport

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import org.bukkit.Location
import org.holoeasy.ext.compressAngle
import org.holoeasy.ext.fixCoordinate
import org.holoeasy.ext.set
import org.holoeasy.packet.packet
import org.holoeasy.util.VersionEnum

object TeleportPacketA : ITeleportPacket {
    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_8..VersionEnum.V1_8)

    override fun teleport(entityId: Int, location: Location): PacketContainer {
        return packet(PacketType.Play.Server.ENTITY_TELEPORT) {
            integers[0] = entityId
            integers[1] = location.x.fixCoordinate
            integers[2] = location.y.fixCoordinate
            integers[3] = location.z.fixCoordinate
            bytes[0] = location.yaw.toDouble().compressAngle
            bytes[1] = location.pitch.toDouble().compressAngle
            booleans[0] = false
        }
    }


}