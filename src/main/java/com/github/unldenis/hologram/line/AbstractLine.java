/*
 * Hologram-Lib - Asynchronous, high-performance Minecraft Hologram
 * library for 1.8-1.18 servers.
 * Copyright (C) unldenis <https://github.com/unldenis>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.unldenis.hologram.line;

import com.github.unldenis.hologram.animation.*;
import com.github.unldenis.hologram.packet.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class AbstractLine<T> {
    protected final Plugin plugin;
    protected final int entityID;
    protected Location location;
    protected T obj;
    protected Optional<Animation> animation = Optional.empty();
    private int taskID = -1;

    private final EntityDestroyPacket entityDestroyPacket;

    public AbstractLine(@NotNull Plugin plugin, int entityID, @NotNull T obj) {
        this.plugin = plugin;
        this.entityID = entityID;
        this.obj = obj;
        entityDestroyPacket = new EntityDestroyPacket(entityID);
        entityDestroyPacket.load();
    }

    public void set(T newObj) {
        this.obj = newObj;
    }

    public abstract void update(@NotNull Player player);

    public void hide(@NotNull Player player) {
        entityDestroyPacket.send(player);
    }

    public void show(@NotNull Player player) {
        new SpawnEntityLivingPacket(entityID, location, plugin)
                .load()
                .send(player);
    }

    /**
     * Method used to teleport a certain line
     * @param player player to teleport it to
     * @since 1.2-SNAPSHOT
     */
    public void teleport(@NotNull Player player) {
        new EntityTeleportPacket(entityID, location)
                .load()
                .send(player);
    }

    public void setAnimation(@NotNull Animation animation, @NotNull Collection<Player> seeingPlayers) {
        this.animation = Optional.of(animation);
        animation.setEntityID(this.entityID);

        Runnable taskR = ()-> seeingPlayers.forEach(animation::nextFrame);
        BukkitTask task;
        if(animation.async()) {
            task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, taskR, animation.delay(), animation.delay());
        } else {
            task = Bukkit.getScheduler().runTaskTimer(plugin, taskR, animation.delay(), animation.delay());
        }
        this.taskID = task.getTaskId();
    }

    public void removeAnimation() {
        if(taskID != -1) {
            Bukkit.getScheduler().cancelTask(taskID);
            taskID = -1;
        }
    }

    public void setLocation(@NotNull Location location) {
        this.location = location;
    }

    public @NotNull Location getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractLine<?> that = (AbstractLine<?>) o;
        return entityID == that.entityID && Objects.equals(obj, that.obj);
    }

}
