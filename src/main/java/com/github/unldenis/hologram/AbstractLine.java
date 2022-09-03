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

package com.github.unldenis.hologram;

import com.github.unldenis.hologram.animation.Animation;
import com.github.unldenis.hologram.packet.PacketContainerSendable;
import com.github.unldenis.hologram.packet.PacketsFactory;
import com.github.unldenis.hologram.util.Validate;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public abstract class AbstractLine<T> {

  protected final Hologram hologram;
  protected final int entityID;
  private final PacketContainerSendable entityDestroyPacket;
  protected Location location;
  protected T obj;
  protected Optional<Animation> animation = Optional.empty();
  private int taskID = -1;

  public AbstractLine(
      @NotNull Hologram hologram,
      @NotNull T obj
  ) {
    this.hologram = hologram;
    this.entityID = HologramPool.IDs_COUNTER.getAndIncrement();
    this.obj = obj;
    entityDestroyPacket = PacketsFactory.get().destroyPacket(entityID);
  }

  protected void hide(@NotNull Player player) {
    entityDestroyPacket.send(player);
  }

  protected void show(@NotNull Player player) {
    PacketsFactory.get()
        .spawnPacket(entityID, location, hologram.getPlugin())
        .send(player);
  }

  /**
   * Method used to teleport a certain line
   *
   * @param player player to teleport it to
   * @since 1.2-SNAPSHOT
   */
  protected void teleport(@NotNull Player player) {
    PacketsFactory.get()
        .teleportPacket(entityID, location)
        .send(player);
  }

  protected abstract void update(@NotNull Player player);

  @NotNull
  @Unmodifiable
  public abstract T get();

  public void set(T newObj) {
    Validate.notNull(newObj, "New line cannot be null");
    this.obj = newObj;
    this.update();
  }

  @ApiStatus.AvailableSince("1.2.6")
  public void update() {
    hologram.seeingPlayers.forEach(this::update);
  }

  public void setAnimation(@NotNull Animation a) {
    Validate.notNull(animation, "Animation cannot be null");
    Animation animation = a.newInstance();

    this.animation = Optional.of(animation);

    Runnable taskR = () -> hologram.seeingPlayers.forEach(
        player -> animation.nextFrame(player, entityID, location));
    BukkitTask task;
    if (animation.async()) {
      task = Bukkit.getScheduler()
          .runTaskTimerAsynchronously(hologram.getPlugin(), taskR, animation.delay(),
              animation.delay());
    } else {
      task = Bukkit.getScheduler()
          .runTaskTimer(hologram.getPlugin(), taskR, animation.delay(), animation.delay());
    }
    this.taskID = task.getTaskId();
  }

  public void setAnimation(Animation.AnimationType animationType) {
    Validate.notNull(animationType, "AnimationType cannot be null");
    setAnimation(animationType.type);
  }

  public void removeAnimation() {
    if (taskID != -1) {
      Bukkit.getScheduler().cancelTask(taskID);
      taskID = -1;
    }
  }

  @NotNull
  @Unmodifiable
  public Location getLocation() {
    return location.clone();
  }

  protected void setLocation(@NotNull Location location) {
    this.location = location;
  }

  public Hologram getHologram() {
    return hologram;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbstractLine<?> that = (AbstractLine<?>) o;
    return entityID == that.entityID && Objects.equals(obj, that.obj);
  }

}
