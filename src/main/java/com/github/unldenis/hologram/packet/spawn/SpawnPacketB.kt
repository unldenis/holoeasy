package com.github.unldenis.hologram.packet.spawn

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.ext.set
import com.github.unldenis.hologram.packet.packet
import com.github.unldenis.hologram.util.VersionEnum
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin
import java.util.*

object SpawnPacketB : ISpawnPacket {

    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_9..VersionEnum.V1_18)

    override fun spawn(entityId: Int, entityType: EntityType, location: Location, plugin: Plugin?): PacketContainer {
        val extraData = 1

        return packet(PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
            modifier.writeDefaults()

            integers[0] = entityId
            integers[1] = entityType.typeId.toInt()
            integers[2] = extraData

            uuiDs[0] = UUID.randomUUID()

            doubles[0] = location.x
            doubles[1] = location.y
            doubles[2] = location.z
        }
    }

}