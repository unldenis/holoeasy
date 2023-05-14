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

  default Optional<EType> getEType() {
    Type type = getType();
    return type instanceof EType ? Optional.of((EType) type) : Optional.empty();
  }

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


  interface Type {

    String name();

    boolean isText();

    boolean isItem();
  }

  enum EType implements Type {
    EXTERNAL(false, false),
    TEXT_LINE(true),
    ITEM_LINE(false),
    TEXT_ANIMATED_LINE(true),
    ITEM_ANIMATED_LINE(false),

    @Experimental
    CLICKABLE_TEXT_LINE(true),

    ;

    private final boolean text;
    private final boolean item;

    EType(boolean text) {
      this(text, !text);
    }

    EType(boolean text, boolean item) {
      this.text = text;
      this.item = item;
    }

    @Override
    public boolean isText() {
      return text;
    }

    @Override
    public boolean isItem() {
      return item;
    }
  }
}
