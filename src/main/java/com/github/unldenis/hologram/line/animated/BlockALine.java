package com.github.unldenis.hologram.line.animated;

import com.github.unldenis.hologram.animation.Animation;
import com.github.unldenis.hologram.line.BlockLine;
import com.github.unldenis.hologram.line.ILine;
import java.util.Collection;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class BlockALine implements ILine<ItemStack>, IAnimated {

  private final BlockLine blockLine;
  private final IAnimated animatedLine;

  public BlockALine(BlockLine blockLine, IAnimated animatedLine) {
    this.blockLine = blockLine;
    this.animatedLine = animatedLine;
  }

  public BlockLine getBlockLine() {
    return blockLine;
  }

  @Override
  public Plugin getPlugin() {
    return blockLine.getPlugin();
  }

  @Override
  public Type getType() {
    return Type.BLOCK_ANIMATED_LINE;
  }

  @Override
  public int getEntityId() {
    return blockLine.getEntityId();
  }

  @Override
  public Location getLocation() {
    return blockLine.getLocation();
  }

  @Override
  public void setLocation(Location location) {
    blockLine.setLocation(location);
  }

  @Override
  public ItemStack getObj() {
    return blockLine.getObj();
  }

  @Override
  public void setObj(ItemStack obj) {
    blockLine.setObj(obj);
  }

  @Override
  public void hide(Player player) {
    blockLine.hide(player);
  }

  @Override
  public void teleport(Player player) {
    blockLine.teleport(player);
  }

  @Override
  public void show(Player player) {
    blockLine.show(player);
  }

  @Override
  public void update(Player player) {
    blockLine.update(player);
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
