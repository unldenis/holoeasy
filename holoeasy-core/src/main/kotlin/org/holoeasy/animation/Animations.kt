package org.holoeasy.animation

import org.holoeasy.HoloEasy
import org.holoeasy.line.LineImpl
import org.holoeasy.util.BukkitFuture
import org.holoeasy.util.scheduler.SchedulerTask

enum class Animations(val task: (LineImpl<*>) -> SchedulerTask) {

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