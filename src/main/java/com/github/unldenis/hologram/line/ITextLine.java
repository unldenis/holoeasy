package com.github.unldenis.hologram.line;

import com.github.unldenis.hologram.placeholder.Placeholders;
import org.bukkit.entity.Player;

public interface ITextLine extends ILine<String> {

  boolean isClickable();

  String parse(Player player);

  TextLine asTextLine();

  Placeholders getPlaceholders();

}
