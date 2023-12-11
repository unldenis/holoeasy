package com.github.unldenis.hologram.line;

import com.github.unldenis.hologram.collection.IntHashSet;
import com.github.unldenis.hologram.experimental.PlayerTextLineInteractEvent;
import com.github.unldenis.hologram.util.AABB;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class ClickableTextLine implements Listener, ITextLine {

  private final TextLine line;
  private final float minHitDistance, maxHitDistance;
  private AABB hitbox;

  private final IntHashSet playersClickable = new IntHashSet();

  public ClickableTextLine(TextLine line, float minHitDistance, float maxHitDistance) {
    this.line = line;
    if (minHitDistance < 0) {
      throw new IllegalArgumentException("minHitDistance must be positive");
    }
    if (maxHitDistance > 120) {
      throw new IllegalArgumentException("maxHitDistance cannot be greater than 120");
    }
    this.minHitDistance = minHitDistance;
    this.maxHitDistance = maxHitDistance;

    if(line.getLocation() != null) {
      this.updateHitBox();
    }

    Bukkit.getPluginManager().registerEvents(this, line.getPlugin());
  }

  @Override
  public boolean isClickable() {
    return false;
  }

  @Override
  public String parse(Player player) {
    return line.parse(player);
  }

  @Override
  public TextLine asTextLine() {
    return line;
  }

  @Override
  public PlaceholdersJava getPlaceholders() {
    return line.getPlaceholders();
  }

  @Override
  public Plugin getPlugin() {
    return line.getPlugin();
  }

  @Override
  public Type getType() {
    return Type.CLICKABLE_TEXT_LINE;
  }

  @Override
  public int getEntityId() {
    return line.getEntityId();
  }

  @Override
  public Location getLocation() {
    return line.getLocation();
  }

  @Override
  public void setLocation(Location location) {
    line.setLocation(location);

    this.updateHitBox();
  }

  @Override
  public String getObj() {
    return line.getObj();
  }

  @Override
  public void setObj(String obj) {
    line.setObj(obj);
  }

  @Override
  public void hide(Player player) {
    line.hide(player);

    this.playersClickable.remove(player.getEntityId());
  }

  @Override
  public void teleport(Player player) {
    line.teleport(player);
  }

  @Override
  public void show(Player player) {
    line.show(player);

    this.playersClickable.add(player.getEntityId());
  }

  @Override
  public void update(Player player) {
    line.update(player);
  }

  @EventHandler
  public void handleInteract(PlayerInteractEvent e) {
    final Player player = e.getPlayer();
    if (e.getAction() != Action.LEFT_CLICK_AIR) {
      return;
    }
    if(hitbox == null) {
      return;
    }

    if(!playersClickable.contains(player.getEntityId())) {
      return;
    }

    AABB.Vec3D intersects = hitbox.intersectsRay(new AABB.Ray3D(player.getEyeLocation()), minHitDistance, maxHitDistance);
    if (intersects == null) {
      return;
    }

    Bukkit.getScheduler().runTask(line.getPlugin(),
        () -> Bukkit.getPluginManager().callEvent(new PlayerTextLineInteractEvent(player, this)));
  }

  private void updateHitBox() {
    double chars = line.getObj().length();
    double size = 0.105;
    double dist = size * (chars / 2d);

    hitbox = new AABB(
        new AABB.Vec3D(-dist, -0.039, -dist),
        new AABB.Vec3D(dist, +0.039, dist));
    hitbox.translate(AABB.Vec3D.fromLocation(line.getLocation().clone().add(0, 1.40, 0)));
  }
}
