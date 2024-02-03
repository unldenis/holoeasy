package org.holoeasy.packet.teleport

import com.comphenix.protocol.events.PacketContainer
import org.bukkit.Location
import org.holoeasy.packet.IPacket

interface ITeleportPacket : IPacket {
    fun teleport(entityId: Int, location: Location): PacketContainer
}