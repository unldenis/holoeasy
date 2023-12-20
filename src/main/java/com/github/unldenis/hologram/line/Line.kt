package com.github.unldenis.hologram.line

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class Line(val plugin: Plugin, var location: Location? = null) {

    companion object {
        val IDs_COUNTER = AtomicInteger(Random().nextInt())
    }

    val entityID: Int = IDs_COUNTER.getAndIncrement()
//    private val entityDestroyPacket : PacketContainer = PacketsFactory.get().destroyPacket(entityID)

    fun destroy(player: Player) {
//        entityDestroyPacket.send(player)
    }

    fun spawn(player: Player) {
//
//        PacketsFactory.get()
//            .spawnPacket(entityID, location ?: throw RuntimeException("Forgot the location?"), plugin)
//            .send(player)

    }

    fun teleport(player: Player) {

//        PacketsFactory.get()
//            .teleportPacket(entityID, location ?: throw RuntimeException("Forgot the location?"))
//            .send(player)

    }


}