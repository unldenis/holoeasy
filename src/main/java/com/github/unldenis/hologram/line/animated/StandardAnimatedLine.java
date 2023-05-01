package com.github.unldenis.hologram.line.animated;

import com.github.unldenis.hologram.animation.Animation;
import com.github.unldenis.hologram.line.Line;
import java.util.Collection;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public final class StandardAnimatedLine implements IAnimated {

  private final Line line;

  private Optional<Animation> animation = Optional.empty();
  private int taskID = -1;


  public StandardAnimatedLine(Line line) {
    this.line = line;
  }

  @Override
  public void setAnimation(Animation animation, Collection<Player> seeingPlayers) {
    this.animation = Optional.of(animation);

    Runnable taskR = () -> seeingPlayers.forEach(
        player -> animation.nextFrame(player, line));
    BukkitTask task;
    if (animation.async()) {
      task = Bukkit.getScheduler()
          .runTaskTimerAsynchronously(line.getPlugin(), taskR, animation.delay(),
              animation.delay());
    } else {
      task = Bukkit.getScheduler()
          .runTaskTimer(line.getPlugin(), taskR, animation.delay(), animation.delay());
    }
    this.taskID = task.getTaskId();
  }

  @Override
  public void removeAnimation() {
    if (taskID != -1) {
      Bukkit.getScheduler().cancelTask(taskID);
      taskID = -1;
    }
  }

  @Override
  public Optional<Animation> getAnimation() {
    return animation;
  }

  public int getTaskID() {
    return taskID;
  }
}
