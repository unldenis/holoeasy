package com.github.unldenis.hologram.packet.teleport

import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.packet.IPacket
import org.bukkit.Location

interface ITeleportPacket : IPacket {
    fun teleport(entityId: Int, location: Location): PacketContainer
}