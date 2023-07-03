package com.github.unldenis.hologram;

import com.github.unldenis.hologram.animation.Animation;
import com.github.unldenis.hologram.experimental.ClickableTextLine;
import com.github.unldenis.hologram.line.ILine;
import com.github.unldenis.hologram.line.ItemLine;
import com.github.unldenis.hologram.line.Line;
import com.github.unldenis.hologram.line.TextLine;
import com.github.unldenis.hologram.line.animated.ItemALine;
import com.github.unldenis.hologram.line.animated.StandardAnimatedLine;
import com.github.unldenis.hologram.line.hologram.TextItemStandardLoader;
import com.github.unldenis.hologram.placeholder.Placeholders;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.LinkedList;
import java.util.List;

public class HologramBuilder {
  private final Plugin plugin;
  private final Placeholders placeholders;
  private final Hologram hologram;
  private final List<ILine<?>> lines;

  protected HologramBuilder(Plugin plugin, Location location, Placeholders placeholders) {
    this.plugin = plugin;
    this.placeholders = placeholders;
    this.hologram = new Hologram(plugin, location, new TextItemStandardLoader());
    this.lines = new LinkedList<>();
  }

  public HologramBuilder addTextLine(String text) {
    Line line = new Line(plugin);
    TextLine textLine = new TextLine(line, text, placeholders);
    lines.add(textLine);
    return this;
  }

  public HologramBuilder addItemLine(ItemStack item) {
    Line line = new Line(plugin);
    ItemLine itemLine = new ItemLine(line, item);
    lines.add(itemLine);
    return this;
  }

  public HologramBuilder addItemLine(ItemStack item, Animation.AnimationType animationType) {
    Line line = new Line(plugin);
    ItemLine itemLine = new ItemLine(line, item);

    ItemALine itemALine = new ItemALine(itemLine, new StandardAnimatedLine(line));
    itemALine.setAnimation(animationType, hologram);

    lines.add(itemALine);
    return this;
  }

  public HologramBuilder addClickableTextLine(String text) {
    Line line = new Line(plugin);
    TextLine textLine = new TextLine(line, text, placeholders, true);
    lines.add(textLine);
    return this;
  }

  public HologramBuilder addClickableTextLine(String text, float minHitDistance, float maxHitDistance) {
    Line line = new Line(plugin);
    TextLine textLine = new TextLine(line, text, placeholders);
    ClickableTextLine clickableTextLine = new ClickableTextLine(textLine, minHitDistance, maxHitDistance);

    lines.add(clickableTextLine);
    return this;
  }

  public Hologram build() {
    return hologram;
  }

  public Hologram loadAndBuild(IHologramPool pool) {
    this.hologram.load(lines.toArray(new ILine[0]));
    pool.takeCareOf(hologram);

    return hologram;
  }

}
