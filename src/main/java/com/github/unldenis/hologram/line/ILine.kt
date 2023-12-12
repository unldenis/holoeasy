package com.github.unldenis.hologram.line;

import java.util.Collection;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus.Experimental;

public interface ILine<T> {

  Plugin getPlugin();

  Type getType();

  int getEntityId();

  Location getLocation();

  void setLocation(Location location);

  T getObj();

  void setObj(T obj);

  void hide(Player player);

  void teleport(Player player);

  void show(Player player);

  void update(Player player);

  default void update(Collection<Player> seeingPlayers) {
    for (Player player : seeingPlayers) {
      update(player);
    }
  }

  enum Type {
    EXTERNAL,
    TEXT_LINE,
    TEXT_ANIMATED_LINE,
    @Experimental
    CLICKABLE_TEXT_LINE,


    BLOCK_LINE,
    BLOCK_ANIMATED_LINE,


    ITEM_LINE,

    ;

  }
}
