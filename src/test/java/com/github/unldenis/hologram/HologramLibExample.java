package com.github.unldenis.hologram;

import com.github.unldenis.hologram.animation.Animation.AnimationType;
import com.github.unldenis.hologram.event.PlayerHologramHideEvent;
import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import com.github.unldenis.hologram.event.PlayerHologramShowEvent;
import com.github.unldenis.hologram.line.ClickableTextLine;
import com.github.unldenis.hologram.experimental.ItemLine;
import com.github.unldenis.hologram.line.ITextLine;
import com.github.unldenis.hologram.line.BlockLine;
import com.github.unldenis.hologram.line.Line;
import com.github.unldenis.hologram.line.TextLine;
import com.github.unldenis.hologram.line.animated.BlockALine;
import com.github.unldenis.hologram.line.animated.StandardAnimatedLine;
import com.github.unldenis.hologram.line.hologram.SingletonLoader;
import com.github.unldenis.hologram.line.hologram.TextBlockStandardLoader;
import com.github.unldenis.hologram.line.hologram.TextSequentialLoader;
import com.github.unldenis.hologram.placeholder.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.ApiStatus.Experimental;

public class HologramLibExample implements Listener {

  private final Plugin plugin;
  private final IHologramPool pool;

  private final Placeholders placeholders;

  public HologramLibExample(Plugin plugin) {
    this.plugin = plugin;

    // create new hologram pool
    HologramPool _pool = new HologramPool(plugin, 70);

    // compose an interactive hologram pool
    // this allows me to hear clicks towards text lines.
    this.pool = new InteractiveHologramPool(_pool, 0.5f, 5f);

    // create placeholders
    this.placeholders = new Placeholders(Placeholders.STRING | Placeholders.PAPI);
    this.placeholders.add("%%player%%", Player::getName);

    Bukkit.getPluginManager().registerEvents(this, plugin);
  }

  // Pool > Hologram > Lines
  /**
   * Creating lines, holograms and assigning them to pools makes it easy to manage,
   * as holograms will be made visible and hidden automatically by the pool.
   * Even the management of clicks is manageable only thanks to the pool.
   * It is recommended to use the pool where possible.
   */
  public Hologram firstExample(Location loc) {
    // create new line structure (armorstand)
    Line line = new Line(plugin);
    // compose an TextLine hologram
    TextLine textLine = new TextLine(line, "Hello", null, true);

    // create new line structure (armorstand)
    Line line2 = new Line(plugin);
    // compose this second TextLine hologram
    TextLine textLine2 = new TextLine(line2, "%%player%%", placeholders, true);

    // append to hologram that will make all the hard work for you
    // the TextSequentialLoader loader will load lines(text only) one after the other. It is an experimental function.
    Hologram hologram = new Hologram(plugin, loc, new TextSequentialLoader());
    // remember to call this method or hologram will not be visible
    hologram.load(textLine, textLine2);

    // add hologram to pool
    pool.takeCareOf(hologram);

    return hologram;
  }

  // Hologram > Lines
  /**
   * Creating lines and holograms allows you to manage multiple lines easily by assigning a single spawn.
   * Even the loader (or how the holograms will be spaced in the spawn) allow you to save many lines of code.
   */
  public Hologram secondExample(Location loc, Player player) {
    // create new line structure (armorstand)
    Line line = new Line(plugin);
    // compose an TextLine hologram
    TextLine textLine = new TextLine(line, "Hi %%player%%", placeholders);

    // create new line structure (armorstand)
    Line line2 = new Line(plugin);
    // compose this second BlockLine hologram
    BlockLine blockLine = new BlockLine(line2, new ItemStack(Material.GOLD_BLOCK));
    // compose this second ItemAnimatedLine hologram
    BlockALine blockALine = new BlockALine(blockLine, new StandardAnimatedLine(line2));

    // append to hologram that will make all the hard work for you
    // the TextBlockStandardLoader loader will load lines(text or item) one below the other.
    Hologram hologram = new Hologram(plugin, loc, new TextBlockStandardLoader());
    // remember to call this method or hologram will not be visible
    hologram.load(textLine, blockALine);

    // show to player
    hologram.show(player);

    // start animation
    blockALine.setAnimation(AnimationType.CIRCLE, hologram);

    // hide after 30 seconds to player
    Bukkit.getScheduler().runTaskLater(plugin, () -> hologram.hide(player), 20L * 30);

    return hologram;
  }

  // Only lines
  /**
   * If, on the other hand, you want total control over a line of hologram, as for temporary holograms,
   * to be visible to certain players it may be convenient to use only the lines without holograms and pools.
   */
  public TextLine thirdExample(Location loc, Player player) {
    // create new line structure (armorstand)
    Line line = new Line(plugin, loc);
    // compose an TextLine hologram
    TextLine textLine = new TextLine(line, "Hi %%player%%", placeholders);
    // show to player
    textLine.show(player);

    // hide after 30 seconds to player
    Bukkit.getScheduler().runTaskLater(plugin, () -> textLine.hide(player), 20L * 30);

    return textLine;
  }

  // Only lines & Clickable
  /**
   * ClickableTextLine is useful if you need to hear clicks but without using a pool.
   *
   * @see HologramLibExample#thirdExample(Location, Player)
   */
  @Experimental
  public ClickableTextLine fourthExample(Location loc, Player player) {
    // create new line structure (armorstand)
    Line line = new Line(plugin, loc);
    // compose an TextLine hologram
    TextLine textLine = new TextLine(line, "Click me");
    // compose an experimental ClickableTextLine hologram
    ClickableTextLine clickableTextLine = new ClickableTextLine(textLine, 0.5f, 5f);
    // show to player
    clickableTextLine.show(player);

    // hide after 30 seconds to player
    Bukkit.getScheduler().runTaskLater(plugin, () -> clickableTextLine.hide(player), 20L * 30);

    return clickableTextLine;
  }

  /**
   * Using the builder you can easily create basic holograms in a single line of code.
   * The builder will take care of creating the hologram, loading it and adding it to the pool.
   * <p>
   * You can check the {@link HologramBuilder} class for more information.
   *
   * @param loc The location where the hologram will be created
   */
  public Hologram builderExample(Location loc) {
    return Hologram.builder(plugin, loc) // initialize the builder
            .addLine("Hello")                                                      // add a text line
            .addLine("%%player%%")                                                 // add another text line
            .addClickableLine("Click me")                                           // add a clickable text line
            .addBlockLine(new ItemStack(Material.GOLD_BLOCK))                                // add a block line
            .addBlockLine(new ItemStack(Material.DIAMOND_BLOCK), AnimationType.CIRCLE)       // add a block line with animation
            .placeholders(placeholders)                                                 // add placeholders
            .loadAndBuild(pool);                                                        // load and build the hologram
  }

  public void itemlineExample(Location loc) {
    Hologram hologram = Hologram.builder(plugin, loc) // initialize the builder
        .addItemLine(new ItemStack(Material.TORCH), EulerAngle.ZERO)                // add experimental item line
        .loader(new SingletonLoader())                                              // a hologram loader with 1 line
        .name("Since v2.6.0")                                                       // just a name
        .loadAndBuild(pool);                                                        // load and build the hologram


//    ItemLine line = (ItemLine) hologram.getLines().get(0);
//    line.setHandRotation(new EulerAngle(Math.toRadians(270), 0, 0), hologram.getSeeingPlayers());
  }

  @EventHandler
  public void onHologramShow(PlayerHologramShowEvent event) {
    Hologram holo = event.getHologram();
    Player player = event.getPlayer();
  }

  /**
   * Doing something when a Hologram is hidden for a certain player.
   *
   * @param event The event instance
   */
  @EventHandler
  public void onHologramHide(PlayerHologramHideEvent event) {
    Hologram holo = event.getHologram();
    Player player = event.getPlayer();
  }

  /**
   * Doing something when a Hologram is left-clicked by a certain player.
   *
   * @param e The event instance
   */
  @EventHandler
  public void onHologramInteract(PlayerHologramInteractEvent e) {
    Player player = e.getPlayer();
    ITextLine line = e.getLine();
    player.sendMessage("Click at " + line.parse(player));
  }


}
