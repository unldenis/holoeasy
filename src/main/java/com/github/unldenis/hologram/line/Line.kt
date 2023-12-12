package com.github.unldenis.hologram.line

import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.packet.PacketsFactory
import com.github.unldenis.hologram.packet.send
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class Line(val plugin: Plugin, spawn : Location? = null) {

    companion object {
        val IDs_COUNTER = AtomicInteger(Random().nextInt())

    }

    val entityID : Int = IDs_COUNTER.getAndIncrement()
    private val entityDestroyPacket : PacketContainer = PacketsFactory.get().destroyPacket(entityID)

    var location : Location? = spawn

    fun destroy(player: Player) {
        entityDestroyPacket.send(player)
    }

    fun spawn(player: Player) {
        location?.let {
            PacketsFactory.get()
                .spawnPacket(entityID, it, plugin)
                .send(player)
        }
    }

    fun teleport(player: Player) {
        location?.let {
            PacketsFactory.get()
                .teleportPacket(entityID, it)
                .send(player)
        }
    }



}