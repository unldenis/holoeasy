package org.holoeasy.line

import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.holoeasy.HoloEasy
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class Line( private val entityType: EntityType, var location: Location? = null) {

    companion object {
        val IDs_COUNTER = AtomicInteger(Random().nextInt())
    }
    val entityID: Int = IDs_COUNTER.getAndIncrement()

    fun destroy(player: Player) {
        HoloEasy.packetImpl()
            .deletePacket(player, entityID)
    }

    fun spawn(player: Player) {
        HoloEasy.packetImpl()
            .spawn(player, entityID, entityType,location ?: throw RuntimeException("Forgot the location?") )
    }

    fun teleport(player: Player) {
        HoloEasy.packetImpl()
            .teleport(player, entityID, location ?: throw RuntimeException("Forgot the location?"))

    }


}