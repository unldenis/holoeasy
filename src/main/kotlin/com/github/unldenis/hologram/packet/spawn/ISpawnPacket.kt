package com.github.unldenis.hologram.packet.spawn

import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.packet.IPacket
import com.github.unldenis.hologram.packet.teleport.TeleportPacketA
import com.github.unldenis.hologram.packet.teleport.TeleportPacketB
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin

interface ISpawnPacket : IPacket {

    override val impls: Array<out IPacket>
        get() = arrayOf(SpawnPacketA, SpawnPacketB, SpawnPacketC)

    fun spawn(entityId : Int, entityType: EntityType, location: Location,
              plugin: Plugin? = null) : PacketContainer
}