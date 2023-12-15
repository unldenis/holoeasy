package com.github.unldenis.hologram.line.animated

import com.github.unldenis.hologram.animation.Animation
import com.github.unldenis.hologram.line.Line
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import java.util.function.Consumer

class StandardAnimatedLine(private val line: Line) : IAnimated {

    override lateinit var animation: Animation
    private var taskID: Int = -1

    override fun setAnimation(animation: Animation, seeingPlayers: Collection<Player>) {
        this.animation = animation

        val taskR = Runnable {
            seeingPlayers.forEach(
                Consumer { player -> animation.nextFrame(player, line) })
        }
        val task = if (animation.async()) {
            Bukkit.getScheduler()
                .runTaskTimerAsynchronously(
                    line.plugin, taskR, animation.delay(),
                    animation.delay()
                )
        } else {
            Bukkit.getScheduler()
                .runTaskTimer(line.plugin, taskR, animation.delay(), animation.delay())
        }
        this.taskID = task.taskId
    }

    override fun removeAnimation() {
        if (taskID != -1) {
            Bukkit.getScheduler().cancelTask(taskID)
            taskID = -1
        }
    }
}
