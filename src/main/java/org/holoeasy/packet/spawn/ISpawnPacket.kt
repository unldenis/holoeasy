package org.holoeasy.packet.spawn

import com.comphenix.protocol.events.PacketContainer
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin
import org.holoeasy.packet.IPacket

interface ISpawnPacket : IPacket {

    fun spawn(
        entityId: Int, entityType: EntityType, location: Location,
        plugin: Plugin? = null
    ): PacketContainer
}