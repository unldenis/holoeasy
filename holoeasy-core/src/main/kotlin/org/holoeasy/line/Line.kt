package org.holoeasy.line

import com.comphenix.protocol.events.PacketContainer
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.holoeasy.ext.invoke
import org.holoeasy.packet.IPacket
import org.holoeasy.packet.PacketType
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class Line(val plugin: Plugin, private val entityType: EntityType, var location: Location? = null) {

    companion object {
        val IDs_COUNTER = AtomicInteger(Random().nextInt())
    }

    val entityID: Int = IDs_COUNTER.getAndIncrement()
    private val entityDestroyPacket : PacketContainer = PacketType.DELETE.delete(entityID)

    fun destroy(player: Player) {
        entityDestroyPacket(player)
    }

    fun spawn(player: Player) {
        val packet = PacketType.SPAWN
            .spawn(entityID, entityType, location ?: throw RuntimeException("Forgot the location?"), plugin)
        packet(player)
    }

    fun teleport(player: Player) {

        val packet = PacketType.TELEPORT
            .teleport(entityID, location ?: throw RuntimeException("Forgot the location?"))
        packet(player)

    }


}