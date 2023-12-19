package com.github.unldenis.hologram.packet.rotate

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.ext.compressAngle
import com.github.unldenis.hologram.ext.fixCoordinate
import com.github.unldenis.hologram.ext.set
import com.github.unldenis.hologram.packet.packet
import com.github.unldenis.hologram.packet.rotate.IRotatePacket
import com.github.unldenis.hologram.util.VersionEnum
import org.bukkit.Location

object RotatePacketA : IRotatePacket {

    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_8..VersionEnum.V1_8)

    override fun rotate(entityId: Int, from: Location, yaw: Float): Array<PacketContainer> {
        val rotationPacket = packet(PacketType.Play.Server.ENTITY_HEAD_ROTATION) {
            integers[0] = entityId
            bytes[0] = yaw.toDouble().compressAngle
        }

        val teleportPacket = packet(PacketType.Play.Server.ENTITY_TELEPORT) {
            integers[0] = entityId

            integers[1] = from.x.fixCoordinate
            integers[2] = from.y.fixCoordinate
            integers[3] = from.z.fixCoordinate

            bytes[0] = yaw.toDouble().compressAngle
            bytes[1] = 0.toByte()

            booleans[0] = true
        }

        return arrayOf(rotationPacket, teleportPacket)
    }



}