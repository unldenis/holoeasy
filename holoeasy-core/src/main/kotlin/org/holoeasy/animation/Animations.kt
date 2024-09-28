package org.holoeasy.animation

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitScheduler

import org.bukkit.scheduler.BukkitTask
import org.holoeasy.ext.send
import org.holoeasy.line.ILine
import org.holoeasy.packet.PacketType

enum class Animations(val task : (ILine<*>) -> BukkitTask) {

    CIRCLE({ line ->
        val holo = line.pvt.hologram
        var yaw = 0.0


        Bukkit.getScheduler().runTaskTimerAsynchronously(line.plugin, java.lang.Runnable {
            val packet = PacketType.ROTATE.rotate(line.entityId, yaw = yaw)

            holo.pvt.seeingPlayers.forEach { player ->
                packet.send(player)
            }

            yaw += 10
        }, 2, 2)

    })
    ;



}