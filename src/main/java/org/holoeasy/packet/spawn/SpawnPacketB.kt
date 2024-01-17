package org.holoeasy.packet.spawn

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.holoeasy.ext.set
import org.holoeasy.packet.packet
import org.holoeasy.util.BukkitFuture
import org.holoeasy.util.VersionEnum
import org.holoeasy.util.VersionUtil
import java.util.*
import java.util.concurrent.CompletableFuture

object SpawnPacketB : ISpawnPacket {

    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_9..VersionEnum.V1_18)
    fun loadDefaultWatcher(plugin: Plugin, location: Location): WrappedDataWatcher {
        return BukkitFuture.supplySync(plugin) {
            val entity =
                location.world.dropItem(location, ItemStack(Material.GOLD_AXE))
            val e = WrappedDataWatcher.getEntityWatcher(entity).deepClone()
            return@supplySync e
        }.join()
    }
    override fun spawn(entityId: Int, entityType: EntityType, location: Location, plugin: Plugin?): PacketContainer {
        val extraData = 1

        println("$entityId $entityType ${location.y}")

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
                integers[1] = VersionUtil.CLEAN_VERSION.droppedItemId
//                integers[2] = extraData

                uuiDs[0] = UUID.randomUUID()

                doubles[0] = location.x
                doubles[1] = location.y
                doubles[2] = location.z
            }
        }

    }

}