package org.holoeasy.animation

import org.bukkit.Bukkit

import org.bukkit.scheduler.BukkitTask
import org.holoeasy.HoloEasy
import org.holoeasy.line.ILine

enum class Animations(val task : (ILine<*>) -> BukkitTask) {

    CIRCLE({ line ->
        val holo = line.pvt.hologram
        var yaw = 0.0


        Bukkit.getScheduler().runTaskTimerAsynchronously(HoloEasy.plugin(), java.lang.Runnable {


            holo.pvt.seeingPlayers.forEach { player ->

                HoloEasy.packetImpl()
                    .rotate(player, line.entityId, yaw = yaw)

            }

            yaw += 10
        }, 2, 2)

    })
    ;



}