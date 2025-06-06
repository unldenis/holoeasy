package org.holoeasy.line;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityTeleport;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.holoeasy.animation.Animations;
import org.holoeasy.hologram.Hologram;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public abstract class Line<T> {
    protected static final AtomicInteger IDs_COUNTER = new AtomicInteger(500 + new Random().nextInt());

    protected final Hologram hologram;
    protected final EntityType entityType;
    protected final Function<Player, T> valueFunction;
    protected final int entityID;

    protected double yOffset = 0.28;

    private Location location;
    private BukkitTask animationTask;

    public Line(Hologram hologram, EntityType entityType, Function<Player, T> valueFunction) {
        this.hologram = hologram;
        this.entityType = entityType;
        this.valueFunction = valueFunction;
        this.entityID = IDs_COUNTER.getAndIncrement();
    }

    public abstract @NotNull Type getType();

    public @NotNull T getValue(@NotNull Player player) {
        return valueFunction.apply(player);
    }

    @ApiStatus.Internal
    public abstract void show(@NotNull Player player);

    @ApiStatus.Internal
    public abstract void hide(@NotNull Player player);

    @ApiStatus.Internal
    public abstract void update(@NotNull Player player);

    public void updateAll() {
        for (Player player : hologram.getPvt().getSeeingPlayers()) {
            update(player);
        }
    }

    public @Nullable Location getLocation() {
        return location;
    }

    public void setCurrentLocation(@NotNull Location value) {
        this.location = value;
    }

    public void setAnimation(Animations animation) {
        cancelAnimation();
        animationTask = animation.getTask().apply(this);
    }

    public void cancelAnimation() {
        if (animationTask != null) {
            animationTask.cancel();
            animationTask = null;
        }
    }

    public double yOffset() {
        return yOffset;
    }

    protected void setYOffset(double yOffset) {
        this.yOffset = yOffset;
    }

    public int getEntityID() {
        return entityID;
    }

    public @NotNull Hologram getHologram() {
        return hologram;
    }

    protected void destroy(Player player) {
        WrapperPlayServerDestroyEntities packet = new WrapperPlayServerDestroyEntities(entityID);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    protected boolean spawn(Player player) {
        Location loc = location;
        if (loc == null) return false;

        WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity(
                entityID,
                UUID.randomUUID(),
                entityType,
                SpigotConversionUtil.fromBukkitLocation(location),
                location.getYaw(),
                0,
                null
        );
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);

        return true;
    }



    public enum Type {
        EXTERNAL,
        TEXT_LINE,
        CLICKABLE_TEXT_LINE,
        ITEM_LINE,
        BLOCK_LINE,
        @ApiStatus.Experimental
        DISPLAY_BLOCK_LINE,
        @ApiStatus.Experimental
        DISPLAY_TEXT_LINE,
        @ApiStatus.Experimental
        INTERACTION_LINE,
    }
}