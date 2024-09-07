package org.holoeasy.packet.teleport

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import org.bukkit.Location
import org.holoeasy.ext.compressAngle
import org.holoeasy.ext.set
import org.holoeasy.packet.packet
import org.holoeasy.util.VersionEnum

object TeleportPacketB : ITeleportPacket {

    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_9..VersionEnum.LATEST)

    override fun teleport(entityId: Int, location: Location): PacketContainer {
        val teleportPacket = packet(PacketType.Play.Server.ENTITY_TELEPORT) {
            integers[0] = entityId

            doubles[0] = location.x
            doubles[1] = location.y
            doubles[2] = location.z

            bytes[0] = location.yaw.toDouble().compressAngle
            bytes[1] = location.pitch.toDouble().compressAngle

            booleans[0] = false
        }

        return teleportPacket
    }

}