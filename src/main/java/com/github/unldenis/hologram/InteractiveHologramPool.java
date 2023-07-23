package com.github.unldenis.hologram;

import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import com.github.unldenis.hologram.line.ILine;
import com.github.unldenis.hologram.line.ITextLine;
import com.github.unldenis.hologram.line.TextLine;
import com.github.unldenis.hologram.util.AABB;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class InteractiveHologramPool implements Listener, IHologramPool {

  private final HologramPool pool;
  private final float minHitDistance, maxHitDistance;

  public InteractiveHologramPool(HologramPool pool, float minHitDistance, float maxHitDistance) {
    this.pool = pool;
    if (minHitDistance < 0) {
      throw new IllegalArgumentException("minHitDistance must be positive");
    }
    if (maxHitDistance > 120) {
      throw new IllegalArgumentException("maxHitDistance cannot be greater than 120");
    }
    this.minHitDistance = minHitDistance;
    this.maxHitDistance = maxHitDistance;

    Bukkit.getPluginManager().registerEvents(this, getPlugin());
  }

  @Override
  public void takeCareOf(Hologram hologram) {
    pool.takeCareOf(hologram);
  }

  @Override
  public Plugin getPlugin() {
    return pool.getPlugin();
  }

  @Override
  public boolean remove(@NotNull Hologram hologram) {
    return pool.remove(hologram);
  }

  @Override
  public Collection<Hologram> getHolograms() {
    return pool.getHolograms();
  }

  @EventHandler
  public void handleInteract(PlayerInteractEvent e) {
    Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
      final Player player = e.getPlayer();
      if (e.getAction() != Action.LEFT_CLICK_AIR) {
        return;
      }
      FST:
      for (Hologram hologram : pool.getHolograms()) {
        if (!hologram.isShownFor(player)) {
          continue;
        }
        for (ILine<?> line : hologram.getLines()) {
          switch (line.getType()) {
            case TEXT_LINE, TEXT_ANIMATED_LINE -> {
              ITextLine iTextLine = (ITextLine) line;
              if(!iTextLine.isClickable()) {
                continue;
              }

              TextLine tL = iTextLine.asTextLine();
              if(tL.getHitbox() == null) {
                continue;
              }

              AABB.Vec3D intersects = tL.getHitbox().intersectsRay(
                  new AABB.Ray3D(player.getEyeLocation()), minHitDistance, maxHitDistance);
              if (intersects == null) {
                continue;
              }

              Bukkit.getScheduler().runTask(
                  getPlugin(),
                  () -> Bukkit.getPluginManager()
                      .callEvent(new PlayerHologramInteractEvent(player, hologram, iTextLine)));
              break FST;
            }
          }
        }
      }
    });
  }


}
