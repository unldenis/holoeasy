package org.holoeasy.animation;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRotation;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.line.Line;

import java.util.function.Function;

public enum Animations {

    CIRCLE(line -> {
        Hologram holo = line.getHologram();

        return new BukkitRunnable() {

            float yaw = 0.0f;
            @Override
            public void run() {
                for (Player seeingPlayer : holo.getPvt().getSeeingPlayers()) {
                    WrapperPlayServerEntityRotation packet = new WrapperPlayServerEntityRotation(line.getEntityID(), (float) yaw, 0f, false);
                    PacketEvents.getAPI().getPlayerManager().sendPacket(seeingPlayer, packet);
                }
            }
        }.runTaskTimerAsynchronously(holo.getLib().getPlugin(), 2, 2);
    });


    private final Function<Line<?>, BukkitTask> task;


    Animations(Function<Line<?>, BukkitTask> task) {
        this.task = task;
    }

    public Function<Line<?>, BukkitTask> getTask() {
        return task;
    }
}
