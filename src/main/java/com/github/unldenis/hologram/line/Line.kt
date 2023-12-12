package com.github.unldenis.hologram.line;

import com.github.unldenis.hologram.packet.PacketContainerSendable;
import com.github.unldenis.hologram.packet.PacketsFactory;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class Line {

  public static final AtomicInteger IDs_COUNTER = new AtomicInteger(new Random().nextInt());

  private final Plugin plugin;
  private final int entityID;
  private final PacketContainerSendable entityDestroyPacket;

  private Location location;


  public Line(Plugin plugin, Location spawn) {
    this.plugin = plugin;
    this.entityID = IDs_COUNTER.getAndIncrement();
    this.entityDestroyPacket = PacketsFactory.get().destroyPacket(entityID);

    this.location = spawn;
  }

  public Line(Plugin plugin) {
    this(plugin, null);
  }

  public void destroy(Player player) {
    entityDestroyPacket.send(player);
  }

  public void spawn(Player player) {
    PacketsFactory.get()
        .spawnPacket(entityID, location, plugin)
        .send(player);
  }

  public void teleport(Player player) {
    PacketsFactory.get()
        .teleportPacket(entityID, location)
        .send(player);
  }

  public Plugin getPlugin() {
    return plugin;
  }
  public int getEntityID() {
    return entityID;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

}
