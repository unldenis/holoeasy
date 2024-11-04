package org.holoeasy.animation

import org.holoeasy.HoloEasy
import org.holoeasy.line.ILine
import org.holoeasy.util.scheduler.SchedulerTask

enum class Animations(val task: (ILine<*>) -> SchedulerTask) {

    CIRCLE({ line ->
        val holo = line.pvt.hologram
        var yaw = 0.0


        HoloEasy.scheduler().createAsyncRepeatingTask(HoloEasy.plugin(), {


            holo.pvt.seeingPlayers.forEach { player ->

                HoloEasy.packetImpl()
                    .rotate(player, line.entityId, yaw = yaw)

            }

            yaw += 10
        }, 2, 2)

    })
    ;


}