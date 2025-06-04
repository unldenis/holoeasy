package org.holoeasy.animation;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.line.LineImpl;

import java.util.function.Function;

public enum Animations {

    CIRCLE(line -> {
        Hologram holo = line.getHologram();

        return new BukkitRunnable() {

            float yaw = 0.0f;
            @Override
            public void run() {
                for (Player seeingPlayer : holo.getPvt().getSeeingPlayers()) {
                    holo.getLib().getPacketImpl().rotate(seeingPlayer, line.getEntityID(),yaw );
                }
            }
        }.runTaskTimerAsynchronously(holo.getLib().getPlugin(), 2, 2);
    });


    private final Function<LineImpl<?>, BukkitTask> task;


    Animations(Function<LineImpl<?>, BukkitTask> task) {
        this.task = task;
    }

    public Function<LineImpl<?>, BukkitTask> getTask() {
        return task;
    }
}
