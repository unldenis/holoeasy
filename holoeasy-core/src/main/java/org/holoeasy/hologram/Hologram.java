package org.holoeasy.hologram;

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

    public HoloEasy getLib() {
        return lib;
    }

    @ApiStatus.Internal
    public PrivateConfig getPvt() {
        return pvt;
    }

    public UUID getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public List<Line<?>> getLines() {
        return lines;
    }

    protected BlockLine blockLine(@NotNull Function<Player, ItemStack> blockSupplier) {
        BlockLine line = new BlockLine(this, blockSupplier);
        lines.add(line);
        return line;
    }

    protected ItemLine itemLine(@NotNull Function<Player, ItemStack> itemSupplier) {
        ItemLine line = new ItemLine(this, itemSupplier);
        lines.add(line);
        return line;
    }

    protected TextLine textLine(@NotNull Function<Player, String> textSupplier) {
        TextLine line = new TextLine(this, textSupplier);
        lines.add(line);
        return line;
    }

    @ApiStatus.Experimental
    protected DisplayTextLine displayTextLine(@NotNull Function<Player, String> textSupplier) {
        DisplayTextLine line = new DisplayTextLine(this, textSupplier);
        lines.add(line);
        return line;
    }

    @ApiStatus.Experimental
    protected DisplayBlockLine displayBlockLine(@NotNull Function<Player, Material> materialSupplier) {
        DisplayBlockLine line = new DisplayBlockLine(this, materialSupplier);
        lines.add(line);
        return line;
    }

    @ApiStatus.Experimental
    protected InteractionLine interactionLine() {
        InteractionLine line = new InteractionLine(this);
        lines.add(line);
        return line;
    }

    public void teleport(Location to) {
        this.location = to.clone();
//        loader().teleport(this);
        // TODO: Implement teleport logic
    }

    public boolean isShownFor(Player player) {
        return pvt.getSeeingPlayers().contains(player);
    }

    public <T extends Hologram> void show(IHologramPool<T> pool) {
        if (pool.getHolograms().stream().anyMatch(h -> h.getId().equals(this.id))) {
            throw new KeyAlreadyExistsException(this.id);
        }
        pool.getHolograms().add((T) this);
    }

    public void show(Player player) {
        if (!loaded) {
            pvt.load();
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

    public void hide(Player player) {
        for (Line<?> line : lines) {
            (line).hide(player);
        }
        pvt.getSeeingPlayers().remove(player);

        if (pvt.getHideEvent() != null) {
            pvt.getHideEvent().onHide(player);
        }
    }

    public void hide(IHologramPool<?> pool) {
        boolean removed = pool.getHolograms().remove(this);
        if (removed) {
            for (Player player : pvt.getSeeingPlayers()) {
                hide(player);
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