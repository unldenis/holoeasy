package org.holoeasy.packet.velocity

import com.comphenix.protocol.events.PacketContainer
import org.holoeasy.packet.IPacket

interface IVelocityPacket : IPacket {
    fun velocity(entityId: Int, x: Int, y : Int, z : Int): PacketContainer

}