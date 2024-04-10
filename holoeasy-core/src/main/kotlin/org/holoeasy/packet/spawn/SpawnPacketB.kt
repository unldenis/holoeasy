package org.holoeasy.packet.spawn

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin
import org.holoeasy.ext.set
import org.holoeasy.packet.packet
import org.holoeasy.util.VersionEnum
import org.holoeasy.util.VersionUtil
import java.util.*

object SpawnPacketB : ISpawnPacket {

    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_9..VersionEnum.V1_15)

    override fun spawn(entityId: Int, entityType: EntityType, location: Location, plugin: Plugin?): PacketContainer {
        val extraData = 1

        return packet(PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
            modifier.writeDefaults()

            integers[0] = entityId
            integers[1] = if(entityType == EntityType.ARMOR_STAND)
                VersionUtil.CLEAN_VERSION.armorstandId else VersionUtil.CLEAN_VERSION.droppedItemId
            integers[2] = extraData

            uuiDs[0] = UUID.randomUUID()

            doubles[0] = location.x
            doubles[1] = location.y
            doubles[2] = location.z
        }

    }

}