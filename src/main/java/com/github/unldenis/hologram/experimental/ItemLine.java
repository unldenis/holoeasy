package com.github.unldenis.hologram.experimental;

import com.github.unldenis.hologram.line.ILine;
import com.github.unldenis.hologram.line.Line;
import com.github.unldenis.hologram.packet.PacketContainerSendable;
import com.github.unldenis.hologram.packet.PacketsFactory;
import com.github.unldenis.hologram.util.NMSUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;

@Experimental
public final class ItemLine implements ILine<ItemStack> {

  private final Line line;

  private ItemStack obj;

  private EulerAngle handRotation;
  private PacketContainerSendable entityMetadataPacket;

  public ItemLine(Line line, ItemStack obj, @NotNull EulerAngle handRotation) {
    if (!obj.getType().isItem()) {
      throw new UnsupportedOperationException(
          "'%s' is not a item. Are you looking for the BlockLine class?".formatted(obj.getType().name()));
    }
    this.line = line;

    this.obj = obj;
    setHandRotation(handRotation);
  }

  public EulerAngle getHandRotation() {
    return handRotation;
  }

  private void updateHandRotation(EulerAngle handRotation) {
    this.handRotation = handRotation;
    Object handRotationNMS = NMSUtils.newNMSVector(Math.toDegrees(handRotation.getX()),
        Math.toDegrees(handRotation.getY()), Math.toDegrees(handRotation.getZ()));
    this.entityMetadataPacket = PacketsFactory.get()
        .metadataPacket(line.getEntityID(), handRotationNMS);
  }

  public void setHandRotation(EulerAngle handRotation, Iterable<Player> seeingPlayers) {
    updateHandRotation(handRotation);

    for(Player p: seeingPlayers) {
      entityMetadataPacket.send(p);
    }
  }

  public void setHandRotation(EulerAngle handRotation, Player... seeingPlayers) {
    updateHandRotation(handRotation);

    for(Player p: seeingPlayers) {
      entityMetadataPacket.send(p);
    }
  }


  @Override
  public Plugin getPlugin() {
    return line.getPlugin();
  }

  @Override
  public Type getType() {
    return Type.ITEM_LINE;
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
        .equipmentPacket(line.getEntityID(), this.obj, true)
        .send(player);
  }

}
