package com.github.unldenis.hologram.line;


import com.github.unldenis.hologram.packet.PacketsFactory;
import com.github.unldenis.hologram.placeholder.Placeholders;
import com.github.unldenis.hologram.util.AABB;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public final class TextLine implements ITextLine {

  private final Line line;
  private final Placeholders placeholders;
  private final boolean clickable;

  private String obj;
  private boolean isEmpty;
  private AABB hitbox;

  public TextLine(Line line, String obj, @Nullable Placeholders placeholders, boolean clickable) {
    this.line = line;
    this.placeholders = placeholders == null ? new Placeholders(0x00) : placeholders;
    this.clickable = clickable;

    this.obj = obj;
  }

  public TextLine(Line line, String obj, Placeholders placeholders) {
    this(line, obj, placeholders, false);
  }

  public TextLine(Line line, String obj) {
    this(line, obj, null);
  }

  @Override
  public boolean isClickable() {
    return clickable;
  }

  @Override
  public String parse(Player player) {
    return placeholders.parse(this.obj, player);
  }

  @Override
  public TextLine asTextLine() {
    return this;
  }

  @Override
  public Placeholders getPlaceholders() {
    return placeholders;
  }

  public AABB getHitbox() {
    return hitbox;
  }

  @Override
  public Plugin getPlugin() {
    return line.getPlugin();
  }

  @Override
  public Type getType() {
    return Type.TEXT_LINE;
  }

  @Override
  public int getEntityId() {
    return line.getEntityID();
  }

  @Override
  public Location getLocation() {
    return line.getLocation();
  }

  @Override
  public void setLocation(Location location) {
    line.setLocation(location);
    if (clickable) {
      double chars = this.obj.length();
      double size = 0.105;
      double dist = size * (chars / 2d);

      hitbox = new AABB(
          new AABB.Vec3D(-dist, -0.039, -dist),
          new AABB.Vec3D(dist, +0.039, dist));
      hitbox.translate(AABB.Vec3D.fromLocation(location.clone().add(0, 1.40, 0)));
    }
  }

  @Override
  public String getObj() {
    return this.obj;
  }

  @Override
  public void setObj(String obj) {
    this.obj = obj;
  }

  @Override
  public void hide(Player player) {
    line.destroy(player);
  }

  @Override
  public void teleport(Player player) {
    line.teleport(player);
  }

  @Override
  public void show(Player player) {
    isEmpty = this.obj.isEmpty();
    if(!isEmpty) {
      line.spawn(player);
      PacketsFactory.get()
          .metadataPacket(line.getEntityID(), parse(player), true, true, null)
          .send(player);
    }
  }

  @Override
  public void update(Player player) {
    byte spawnBefore = (byte) ((isEmpty ? 1 : 0) | (this.obj.isEmpty() ? 1 : 0) << 1);
    /*  0x00  = is already showed
        0x01  = is hided but now has changed
        0x02  = is already showed but is empty
        0x03  = is hided and isn't changed      */
    switch (spawnBefore) {
      case 0x03:
        break;
      case 0x02:
        line.destroy(player);
        isEmpty = true;
        break;
      case 0x01:
        line.spawn(player);
        isEmpty = false;
      case 0x00:
        PacketsFactory.get()
            .metadataPacket(line.getEntityID(), parse(player), spawnBefore == 0x01, spawnBefore == 0x01, null)
            .send(player);
    }
  }

}
