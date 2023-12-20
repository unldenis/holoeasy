package com.github.unldenis.hologram.packet.spawn

import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.packet.IPacket
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin

interface ISpawnPacket : IPacket {

    fun spawn(
        entityId: Int, entityType: EntityType, location: Location,
        plugin: Plugin? = null
    ): PacketContainer
}