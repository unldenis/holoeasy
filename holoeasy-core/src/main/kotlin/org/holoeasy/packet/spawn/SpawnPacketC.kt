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
import kotlin.math.max
import kotlin.math.min

object SpawnPacketC : ISpawnPacket {

    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_16..VersionEnum.V1_18)

    override fun spawn(entityId: Int, entityType: EntityType, location: Location, plugin: Plugin?): PacketContainer {
        val extraData = 1

        if(entityType == EntityType.ARMOR_STAND) {
            return packet(PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
                modifier.writeDefaults()

                integers[0] = entityId
                integers[1] = VersionUtil.CLEAN_VERSION.armorstandId
                integers[2] = extraData

                uuiDs[0] = UUID.randomUUID()

                doubles[0] = location.x
                doubles[1] = location.y
                doubles[2] = location.z
            }
        } else {
            return packet(PacketType.Play.Server.SPAWN_ENTITY) {
                modifier.writeDefaults()

                integers[0] = entityId

                entityTypeModifier[0] = EntityType.DROPPED_ITEM

                uuiDs[0] = UUID.randomUUID()

                doubles[0] = location.x
                doubles[1] = location.y
                doubles[2] = location.z

                integers[2] = convertVelocity(0.0)
                integers[3] = convertVelocity(0.0)
                integers[4] = convertVelocity(0.0)
            }
        }

    }

    private fun convertVelocity(velocity: Double): Int {
        /*
          Minecraft represents a velocity within 4 blocks per second, in any direction,
          by using the entire Short range, meaning you can only move up to 4 blocks/second
          on any given direction
        */
        return (clamp(velocity, -3.9, 3.9) * 8000).toInt()
    }
    private fun clamp(targetNum: Double, min: Double, max: Double): Double {
        // Makes sure a number is within a range
        return max(min, min(targetNum, max))
    }

}