package com.github.unldenis.hologram.line.animated;

import com.github.unldenis.hologram.animation.Animation;

import com.github.unldenis.hologram.line.ILine;
import com.github.unldenis.hologram.line.TextLine;
import java.util.Collection;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class TextALine implements ILine<String>, IAnimated {

  private final TextLine textLine;
  private final IAnimated animatedLine;

  public TextALine(TextLine textLine, IAnimated animatedLine) {
    this.textLine = textLine;
    this.animatedLine = animatedLine;
  }

  public TextLine asTextLine() {
    return textLine;
  }

  @Override
  public Type getType() {
    return EType.TEXT_ANIMATED_LINE;
  }

  @Override
  public int getEntityId() {
    return textLine.getEntityId();
  }

  @Override
  public Location getLocation() {
    return textLine.getLocation();
  }

  @Override
  public void setLocation(Location location) {
    textLine.setLocation(location);
  }

  @Override
  public String getObj() {
    return textLine.getObj();
  }

  @Override
  public void setObj(String obj) {
    textLine.setObj(obj);
  }

  @Override
  public void hide(Player player) {
    textLine.hide(player);
  }

  @Override
  public void teleport(Player player) {
    textLine.teleport(player);
  }

  @Override
  public void show(Player player) {
    textLine.show(player);
  }

  @Override
  public void update(Player player) {
    textLine.update(player);
  }

  @Override
  public void setAnimation(Animation animation, Collection<Player> seeingPlayers) {
    animatedLine.setAnimation(animation, seeingPlayers);
  }

  @Override
  public void removeAnimation() {
    animatedLine.removeAnimation();
  }

  @Override
  public Optional<Animation> getAnimation() {
    return animatedLine.getAnimation();
  }
}
