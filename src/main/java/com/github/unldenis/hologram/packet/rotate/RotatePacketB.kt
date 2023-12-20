package com.github.unldenis.hologram.packet.rotate

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.ext.compressAngle
import com.github.unldenis.hologram.ext.set
import com.github.unldenis.hologram.packet.packet
import com.github.unldenis.hologram.util.VersionEnum
import org.bukkit.Location

object RotatePacketB : IRotatePacket {

    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_9..VersionEnum.V1_20)


    override fun rotate(entityId: Int, from: Location, yaw: Float): Array<PacketContainer> {
        val packet = packet(PacketType.Play.Server.ENTITY_LOOK) {
            integers[0] = entityId
            bytes[0] = yaw.toDouble().compressAngle
            bytes[1] = 0.toByte()
            booleans[0] = true
        }
        return arrayOf(packet)
    }

}