package org.holoeasy.packet.spawn

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedDataWatcher

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin
import org.holoeasy.ext.set
import org.holoeasy.packet.packet
import org.holoeasy.util.BukkitFuture
import org.holoeasy.util.VersionEnum
import java.util.concurrent.CompletableFuture

object SpawnPacketA : ISpawnPacket {

    private fun loadDefaultWatcher(plugin: Plugin): CompletableFuture<Void> {
        return BukkitFuture.runSync(plugin) {
            val world = Bukkit.getWorlds()[0]
            val entity =
                world.spawnEntity(Location(world, 0.0, 256.0, 0.0), EntityType.ARMOR_STAND)
            defaultDataWatcher =
                WrappedDataWatcher.getEntityWatcher(entity).deepClone()
            entity.remove()
        }
    }

    private var defaultDataWatcher: WrappedDataWatcher? = null


    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_8..VersionEnum.V1_8)

    override fun spawn(entityId: Int, entityType: EntityType, location: Location, plugin: Plugin?): PacketContainer {
        return packet(PacketType.Play.Server.SPAWN_ENTITY) {
            integers[0] = entityId
            integers[1] = entityType.typeId.toInt()
            integers[2] = (location.x * 32).toInt()
            integers[3] = (location.y * 32).toInt()
            integers[4] = (location.z * 32).toInt()

            if (defaultDataWatcher == null) {

                loadDefaultWatcher(
                    plugin ?: throw RuntimeException("Plugin cannot be null")
                ).join()
            }

            dataWatcherModifier[0] = defaultDataWatcher
        }
    }


}