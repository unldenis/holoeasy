package org.holoeasy.animation

import org.bukkit.scheduler.BukkitTask
import org.holoeasy.HoloEasy
import org.holoeasy.line.LineImpl
import org.holoeasy.util.BukkitFuture

enum class Animations(val task: (LineImpl<*>) -> BukkitTask) {

    CIRCLE({ line ->
        val holo = line.hologram
        var yaw = 0.0

        BukkitFuture.runTaskTimerAsynchronously(2, 2) {
            holo.pvt.seeingPlayers.forEach { player ->
                HoloEasy.packetImpl()
                    .rotate(player, line.entityID, yaw = yaw)
            }

            yaw += 10
        }

    })
    ;


}