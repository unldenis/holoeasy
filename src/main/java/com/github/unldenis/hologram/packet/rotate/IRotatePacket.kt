package com.github.unldenis.hologram.packet.rotate

import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.packet.IPacket
import org.bukkit.Location

interface IRotatePacket : IPacket {
    fun rotate(entityId: Int, from: Location, yaw: Float): Array<PacketContainer>
}