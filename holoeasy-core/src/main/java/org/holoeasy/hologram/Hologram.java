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

    protected BlockLine blockLine(@NotNull ItemStack item) {
        BlockLine line = new BlockLine(this, item);
        lines.add(line);
        return line;
    }

    protected ItemLine itemLine(@NotNull ItemStack item) {
        ItemLine line = new ItemLine(this, item);
        lines.add(line);
        return line;
    }

    protected TextLine textLine(@NotNull String text) {
        TextLine line = new TextLine(this, text);
        lines.add(line);
        return line;
    }

    @ApiStatus.Experimental
    protected DisplayTextLine displayTextLine(@NotNull String text) {
        DisplayTextLine line = new DisplayTextLine(this, text);
        lines.add(line);
        return line;
    }

    @ApiStatus.Experimental
    protected DisplayBlockLine displayBlockLine(@NotNull Material material) {
        DisplayBlockLine line = new DisplayBlockLine(this, material);
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

    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("location", location);
        List<Map<String, Object>> serializedLines = new ArrayList<>();
        for (Line<?> line : lines) {
            Map<String, Object> lineData = new HashMap<>();
            lineData.put("type", line.getType().name());
            lineData.put("value", line.getValue());
            serializedLines.add(lineData);
        }
        result.put("lines", serializedLines);
        return result;
    }

    public static <T extends Hologram> T deserialize(Map<String, Object> args, Class<T> clazz) throws Exception {
        Location location = (Location) args.get("location");
        List<Map<String, Object>> lines = (List<Map<String, Object>>) args.get("lines");

        T hologram = clazz.getDeclaredConstructor(Location.class).newInstance(location);

        for (int i = 0; i < lines.size(); i++) {
            Line.Type type = Line.Type.valueOf((String) lines.get(i).get("type"));
            Object value = lines.get(i).get("value");

            switch (type) {
                case EXTERNAL:
                    Line<Object> externalLine = (Line<Object>) hologram.getLines().get(i);
                    externalLine.setValue(value);
                    break;
                case TEXT_LINE:
                case CLICKABLE_TEXT_LINE:
                    TextLine textLine = (TextLine) hologram.getLines().get(i);
                    textLine.setValue((String) value);
                    break;
                case ITEM_LINE:
                case BLOCK_LINE:
                    Line<ItemStack> itemLine = (Line<ItemStack>) hologram.getLines().get(i);
                    itemLine.setValue((ItemStack) value);
                    break;
                case DISPLAY_BLOCK_LINE:
                    DisplayBlockLine displayBlockLine = (DisplayBlockLine) hologram.getLines().get(i);
                    displayBlockLine.setValue((Material) value);
                    break;
                case DISPLAY_TEXT_LINE:
                    DisplayTextLine displayTextLine = (DisplayTextLine) hologram.getLines().get(i);
                    displayTextLine.setValue((String) value);
                    break;
            }
        }
        return hologram;
    }

    public static <T extends Hologram> T deserialize(Map<String, Object> args) throws Exception {
        return (T) deserialize(args, Hologram.class);
    }
}