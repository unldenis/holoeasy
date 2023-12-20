package com.github.unldenis.hologram.packet.teleport

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.ext.compressAngle
import com.github.unldenis.hologram.ext.fixCoordinate
import com.github.unldenis.hologram.ext.set
import com.github.unldenis.hologram.packet.packet
import com.github.unldenis.hologram.util.VersionEnum
import org.bukkit.Location

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