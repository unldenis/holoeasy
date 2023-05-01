package com.github.unldenis.hologram.line.animated;

import com.github.unldenis.hologram.animation.Animation;
import com.github.unldenis.hologram.line.ILine;
import com.github.unldenis.hologram.line.ItemLine;
import java.util.Collection;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemALine implements ILine<ItemStack>, IAnimated {

  private final ItemLine itemLine;
  private final IAnimated animatedLine;

  public ItemALine(ItemLine itemLine, IAnimated animatedLine) {
    this.itemLine = itemLine;
    this.animatedLine = animatedLine;
  }

  public ItemLine getItemLine() {
    return itemLine;
  }

  @Override
  public Type getType() {
    return EType.ITEM_ANIMATED_LINE;
  }

  @Override
  public int getEntityId() {
    return itemLine.getEntityId();
  }

  @Override
  public Location getLocation() {
    return itemLine.getLocation();
  }

  @Override
  public void setLocation(Location location) {
    itemLine.setLocation(location);
  }

  @Override
  public ItemStack getObj() {
    return itemLine.getObj();
  }

  @Override
  public void setObj(ItemStack obj) {
    itemLine.setObj(obj);
  }

  @Override
  public void hide(Player player) {
    itemLine.hide(player);
  }

  @Override
  public void teleport(Player player) {
    itemLine.teleport(player);
  }

  @Override
  public void show(Player player) {
    itemLine.show(player);
  }

  @Override
  public void update(Player player) {
    itemLine.update(player);
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
