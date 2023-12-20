package com.github.unldenis.hologram.packet.teleport

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.ext.compressAngle
import com.github.unldenis.hologram.ext.set
import com.github.unldenis.hologram.packet.packet
import com.github.unldenis.hologram.util.VersionEnum
import org.bukkit.Location

object TeleportPacketB : ITeleportPacket {

    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_9..VersionEnum.V1_20)

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