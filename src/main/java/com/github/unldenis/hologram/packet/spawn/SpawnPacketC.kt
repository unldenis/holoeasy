package com.github.unldenis.hologram.packet.spawn

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.utility.MinecraftReflection
import com.comphenix.protocol.wrappers.BukkitConverters
import com.comphenix.protocol.wrappers.Vector3F
import com.github.unldenis.hologram.ext.set
import com.github.unldenis.hologram.packet.packet
import com.github.unldenis.hologram.util.VersionEnum
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin
import org.bukkit.util.Vector
import java.util.*

object SpawnPacketC : ISpawnPacket {

    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_19..VersionEnum.V1_20)

    override fun spawn(entityId: Int, entityType: EntityType, location: Location, plugin: Plugin?): PacketContainer {
        return packet(PacketType.Play.Server.SPAWN_ENTITY) {
            integers[0] = entityId

            entityTypeModifier[0] = entityType

            uuiDs[0] = UUID.randomUUID()

            doubles[0] = location.x
            doubles[1] = location.y
            doubles[2] = location.z

        }
    }

}