package org.holoeasy.line;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.holoeasy.animation.Animations;
import org.holoeasy.hologram.Hologram;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class LineImpl<T> implements Line<T> {

    protected final Hologram hologram;
    protected final EntityType entityType;
    private Location location;

    public abstract @NotNull Type getType();

    public final int entityID;

    @Override
    public int getEntityID() {
        return entityID;
    }

    @Override
    public abstract @NotNull T getValue();

    public abstract void setValue(T value);

    private BukkitTask animationTask;

    public LineImpl(Hologram hologram, EntityType entityType) {
        this.hologram = hologram;
        this.entityType = entityType;
        this.entityID = IDs_COUNTER.getAndIncrement();
    }

    public Hologram getHologram() {
        return hologram;
    }

    @Override
    public Location currentLocation() {
        return location;
    }

    @Override
    public abstract void update(@NotNull T newValue);

    @Override
    public void setAnimation(Animations animation) {
        cancelAnimation();
        animationTask = animation.getTask().apply(this);
    }

    @Override
    public void cancelAnimation() {
        if (animationTask != null) {
            animationTask.cancel();
            animationTask = null;
        }
    }

    public abstract void show(Player player);

    public abstract void hide(Player player);

    public abstract void update(Player player);

    public void destroy(Player player) {
        hologram.getLib().getPacketImpl().deletePacket(player, entityID);
    }

    public boolean spawn(Player player) {
        Location loc = location;
        if (loc == null) return false;
        hologram.getLib().getPacketImpl().spawn(hologram.getLib(), player, entityID, entityType, loc);
        return true;
    }

    public boolean teleport(Player player) {
        Location loc = location;
        if (loc == null) return false;
        hologram.getLib().getPacketImpl().teleport(player, entityID, loc);
        return true;
    }

    public void setCurrentLocation(Location value) {
        this.location = value;
    }

    public void observerUpdate() {
        for (Player player : hologram.getPvt().getSeeingPlayers()) {
            update(player);
        }
    }

    public static final AtomicInteger IDs_COUNTER = new AtomicInteger(500 + new Random().nextInt());

    public enum Type {
        EXTERNAL,
        TEXT_LINE,
        CLICKABLE_TEXT_LINE,
        ITEM_LINE,
        BLOCK_LINE,
        @ApiStatus.Experimental
        DISPLAY_BLOCK_LINE,
        @ApiStatus.Experimental
        DISPLAY_TEXT_LINE
    }
}