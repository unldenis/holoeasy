package org.holoeasy.hologram;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityTeleport;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.holoeasy.HoloEasy;
import org.holoeasy.line.*;
import org.holoeasy.pool.IHologramPool;
import org.holoeasy.pool.KeyAlreadyExistsException;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class Hologram {
    private final HoloEasy lib;
    private final PrivateConfig pvt;
    private final UUID id = UUID.randomUUID();
    private Location location;
    private final List<Line<?>> lines = new CopyOnWriteArrayList<>();
    private boolean loaded = false;

    public Hologram(@NotNull HoloEasy lib, @NotNull Location location, ShowEvent showEvent, HideEvent hideEvent) {
        this.lib = lib;
        this.location = location;
        this.pvt = new PrivateConfig(this, showEvent, hideEvent);
    }

    public Hologram(@NotNull HoloEasy lib, @NotNull Location location) {
        this(lib, location, null, null);
    }

    public @NotNull HoloEasy getLib() {
        return lib;
    }

    @ApiStatus.Internal
    public @NotNull PrivateConfig getPvt() {
        return pvt;
    }

    public @NotNull UUID getId() {
        return id;
    }

    public @NotNull Location getLocation() {
        return location;
    }

    public @NotNull List<Line<?>> getLines() {
        return lines;
    }

    protected @NotNull BlockLine blockLine(@NotNull Function<@NotNull Player, @NotNull ItemStack> blockSupplier) {
        BlockLine line = new BlockLine(this, blockSupplier);
        lines.add(line);
        return line;
    }

    protected @NotNull ItemLine itemLine(@NotNull Function<@NotNull Player, @NotNull ItemStack> itemSupplier) {
        ItemLine line = new ItemLine(this, itemSupplier);
        lines.add(line);
        return line;
    }

    protected @NotNull TextLine textLine(@NotNull Function<@NotNull Player, @NotNull String> textSupplier) {
        TextLine line = new TextLine(this, textSupplier);
        lines.add(line);
        return line;
    }

    @ApiStatus.Experimental
    protected @NotNull DisplayTextLine displayTextLine(@NotNull Function<@NotNull Player, @NotNull String> textSupplier) {
        DisplayTextLine line = new DisplayTextLine(this, textSupplier);
        lines.add(line);
        return line;
    }

    @ApiStatus.Experimental
    protected @NotNull DisplayBlockLine displayBlockLine(@NotNull Function<@NotNull Player, @NotNull Material> materialSupplier) {
        DisplayBlockLine line = new DisplayBlockLine(this, materialSupplier);
        lines.add(line);
        return line;
    }

    @ApiStatus.Experimental
    protected @NotNull InteractionLine interactionLine() {
        InteractionLine line = new InteractionLine(this);
        lines.add(line);
        return line;
    }

    public void teleport(@NotNull Location to) {
        this.location = to.clone();

        // update the location of all lines
        pvt.updateLinesLocation();

        // send teleport packets to all players seeing this hologram
        for (Player seeingPlayer : pvt.getSeeingPlayers()) {
            for (Line<?> line : lines) {

                WrapperPlayServerEntityTeleport packet = new WrapperPlayServerEntityTeleport(
                        line.getEntityID(),
                        SpigotConversionUtil.fromBukkitLocation(line.getLocation()),
                        false
                );
                PacketEvents.getAPI().getPlayerManager().sendPacket(seeingPlayer, packet);
            }
        }
    }

    public boolean isShownFor(@NotNull Player player) {
        return pvt.getSeeingPlayers().contains(player);
    }

    public <T extends Hologram> void show(@NotNull IHologramPool<T> pool) {
        if (pool.getHolograms().stream().anyMatch(h -> h.getId().equals(this.id))) {
            throw new KeyAlreadyExistsException(this.id);
        }
        pool.getHolograms().add((T) this);
    }

    public void show(@NotNull Player player) {
        if (!loaded) {
            if(lines.isEmpty()) {
                throw new IllegalStateException("Cannot show hologram with no lines.");
            }
            pvt.updateLinesLocation();
            loaded = true;
        }

        pvt.getSeeingPlayers().add(player);
        for (Line<?> line : lines) {
            (line).show(player);
        }

        if (pvt.getShowEvent() != null) {
            pvt.getShowEvent().onShow(player);
        }
    }

    public void hide(@NotNull Player player) {
        for (Line<?> line : lines) {
            (line).hide(player);
        }
        pvt.getSeeingPlayers().remove(player);

        if (pvt.getHideEvent() != null) {
            pvt.getHideEvent().onHide(player);
        }
    }

    public void hide(@NotNull IHologramPool<?> pool) {
        boolean removed = pool.getHolograms().remove(this);
        if (removed) {
            for (Player player : pvt.getSeeingPlayers()) {
                hide(player);
            }
        }
    }

    public void updateLines() {
        for (Line<?> line : lines) {
            line.updateAll();
        }
    }

    public void replaceLines(@NotNull List<? extends Line<?>> newLines) {
        // hide current lines from all players
        for (Player player : pvt.getSeeingPlayers()) {
            for (Line<?> line : lines) {
                line.hide(player);
            }
        }

        lines.clear();
        loaded = false;
        lines.addAll(newLines);
        pvt.updateLinesLocation();

        // show new lines to all players
        for (Player player : pvt.getSeeingPlayers()) {
            for (Line<?> line : lines) {
                line.show(player);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hologram)) return false;
        Hologram hologram = (Hologram) o;
        return id.equals(hologram.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}