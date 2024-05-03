package org.holoeasy.packet.rotate

import com.comphenix.protocol.events.PacketContainer
import org.holoeasy.packet.IPacket

interface IRotatePacket : IPacket {

    fun rotate(entityId : Int, yaw : Double) : PacketContainer
}