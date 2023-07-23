package com.github.unldenis.hologram.line;

import com.github.unldenis.hologram.packet.PacketContainerSendable;
import com.github.unldenis.hologram.packet.PacketsFactory;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public final class BlockLine implements ILine<ItemStack> {

  private final Line line;
  private final PacketContainerSendable entityMetadataPacket;

  private ItemStack obj;

  public BlockLine(Line line, ItemStack obj) {
    if (!obj.getType().isBlock()) {
      throw new UnsupportedOperationException(
          "'%s' is not a block. Are you looking for the new experimental ItemLine class?".formatted(obj.getType().name()));
    }
    this.line = line;
    this.entityMetadataPacket = PacketsFactory.get().metadataPacket(line.getEntityID(), null);

    this.obj = obj;
  }

  @Override
  public Plugin getPlugin() {
    return line.getPlugin();
  }

  @Override
  public Type getType() {
    return Type.BLOCK_LINE;
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
  }

  @Override
  public ItemStack getObj() {
    return obj.clone();
  }

  @Override
  public void setObj(ItemStack obj) {
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
    line.spawn(player);
    entityMetadataPacket.send(player);
    this.update(player);
  }

  @Override
  public void update(Player player) {
    PacketsFactory.get()
        .equipmentPacket(line.getEntityID(), this.obj, false)
        .send(player);
  }

}
