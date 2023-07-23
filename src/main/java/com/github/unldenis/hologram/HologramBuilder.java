package com.github.unldenis.hologram;

import com.github.unldenis.hologram.animation.Animation;
import com.github.unldenis.hologram.line.ClickableTextLine;
import com.github.unldenis.hologram.experimental.ItemLine;
import com.github.unldenis.hologram.line.BlockLine;
import com.github.unldenis.hologram.line.ILine;
import com.github.unldenis.hologram.line.ITextLine;
import com.github.unldenis.hologram.line.Line;
import com.github.unldenis.hologram.line.TextLine;
import com.github.unldenis.hologram.line.animated.BlockALine;
import com.github.unldenis.hologram.line.animated.StandardAnimatedLine;
import com.github.unldenis.hologram.line.hologram.IHologramLoader;
import com.github.unldenis.hologram.line.hologram.TextBlockStandardLoader;
import com.github.unldenis.hologram.placeholder.Placeholders;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.EulerAngle;

public class HologramBuilder {

  private final Plugin plugin;
  private final Hologram hologram;
  private final List<ILine<?>> lines;

  // no placeholder
  private Placeholders placeholders = new Placeholders(0x00);

  protected HologramBuilder(Plugin plugin, Location location) {
    this.plugin = plugin;
    this.hologram = new Hologram(plugin, location, new TextBlockStandardLoader());
    this.lines = new LinkedList<>();
  }

  public HologramBuilder placeholders(Placeholders placeholders) {
    // update already added lines
    for (ILine<?> line : lines) {
      switch (line.getType()) {
        case TEXT_LINE, TEXT_ANIMATED_LINE, CLICKABLE_TEXT_LINE ->
            ((ITextLine) line).getPlaceholders().add(placeholders);
      }
    }
    //
    this.placeholders = placeholders;
    return this;
  }

  public HologramBuilder addLine(String text) {
    Line line = new Line(plugin);
    TextLine textLine = new TextLine(line, text, placeholders);
    lines.add(textLine);
    return this;
  }

  public HologramBuilder addBlockLine(ItemStack item) {
    if(item.getType().isBlock()) {
      Line line = new Line(plugin);
      BlockLine blockLine = new BlockLine(line, item);
      lines.add(blockLine);
      return this;
    }
    throw new IllegalArgumentException("ItemStack '%s' must be a block.".formatted(item.getType()));
  }

  public HologramBuilder addItemLine(ItemStack item, EulerAngle handRotation) {
    if(item.getType().isItem()) {
      Line line = new Line(plugin);
      ItemLine blockLine = new ItemLine(line, item, handRotation);
      lines.add(blockLine);
      return this;
    }
    throw new IllegalArgumentException("ItemStack '%s' must be an item.".formatted(item.getType()));
  }

  public HologramBuilder addBlockLine(ItemStack item, Animation.AnimationType animationType) {
    if(!item.getType().isBlock()) {
      throw new UnsupportedOperationException("Item '%s' is not a block, this class is not yet implemented but you can start from ItemLine and composite it."
          .formatted(item.getType().name()));
    }
    Line line = new Line(plugin);
    BlockLine blockLine = new BlockLine(line, item);

    BlockALine blockALine = new BlockALine(blockLine, new StandardAnimatedLine(line));
    blockALine.setAnimation(animationType, hologram);

    lines.add(blockALine);
    return this;
  }

  public HologramBuilder addClickableLine(String text) {
    Line line = new Line(plugin);
    TextLine textLine = new TextLine(line, text, placeholders, true);
    lines.add(textLine);
    return this;
  }

  public HologramBuilder addClickableLine(String text, float minHitDistance, float maxHitDistance) {
    Line line = new Line(plugin);
    TextLine textLine = new TextLine(line, text, placeholders);
    ClickableTextLine clickableTextLine = new ClickableTextLine(textLine, minHitDistance,
        maxHitDistance);

    lines.add(clickableTextLine);
    return this;
  }

  public HologramBuilder loader(IHologramLoader loader) {
    this.hologram.setLoader(loader);
    return this;
  }

  public HologramBuilder name(String name) {
    this.hologram.setName(name);
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
