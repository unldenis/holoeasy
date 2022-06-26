/*
 * Hologram-Lib - Asynchronous, high-performance Minecraft Hologram
 * library for 1.8-1.18 servers.
 * Copyright (C) unldenis <https://github.com/unldenis>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.unldenis.hologram;

import com.github.unldenis.hologram.event.*;
import com.github.unldenis.hologram.line.ItemLine;
import com.github.unldenis.hologram.placeholder.Placeholders;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

public class Hologram {
    private final Plugin plugin;
    private Location location;

    protected final List<AbstractLine<?>> lines;
    protected final Set<Player> seeingPlayers = new CopyOnWriteArraySet<>();
    protected final Set<Player> excludedPlayers = new CopyOnWriteArraySet<>();

    private final Placeholders placeholders;

    /**
     * @param plugin The org.bukkit.Plugin
     * @param location The location of the hologram
     * @param placeholders Reference passage of placeholders
     * @param l Inverted array of hologram lines
     * @deprecated Deprecated because you have to use the Builder of the class.
     */
    @Deprecated
    @ApiStatus.Internal
    public Hologram(
            @NotNull Plugin plugin,
            @NotNull Location location,
            @Nullable Placeholders placeholders,
            @NotNull Object[]... l
    ) {
        this.plugin = plugin;
        this.location = location;
        this.placeholders = placeholders == null ? new Placeholders() : placeholders;

        LinkedList<AbstractLine<?>> tempReversed = new LinkedList<>();
        Location cloned = this.location.clone().subtract(0, 0.28, 0);
        for(int j=0; j< l.length; j++) {
            Object[] line = l[j];
            double up = 0.28D;
            if(j>0 && l[j-1].length == 1 /* ItemStack */) {
                up = 0.0D;
            }
            Object val = line[0];
            if(val instanceof String) {
                TextLine tempLine = new TextLine(this, (String) val, (boolean) line[1]);
                tempLine.setLocation(cloned.add(0.0, up, 0).clone());
                tempReversed.addFirst(tempLine);
            }else if (val instanceof ItemStack) {
                ItemLine tempLine = new ItemLine(this, (ItemStack) val);
                tempLine.setLocation(cloned.add(0.0, 0.60D, 0).clone());
                tempReversed.addFirst(tempLine);
            }
        }
        this.lines = Collections.unmodifiableList(tempReversed);
    }

    /**
     * Method used to teleport the hologram.
     * Note that this method runs on the main-thread.
     * @param to location to teleport it
     * @since 1.2-SNAPSHOT
     */
    @ApiStatus.Experimental
    @ApiStatus.AvailableSince("1.2-SNAPSHOT")
    public void teleport(@NotNull Location to) {
        Validate.notNull(to, "Destination cannot be null");
        AbstractLine<?> firstLine = this.lines.get(0);
        // Clone the given location
        this.location = to.clone();
        // Obtain the Y position of the first line and then calculate the distance to all lines to maintain this distance
        double baseY = firstLine.getLocation().getY();
        // Get position Y where to teleport the first line
        double destY = (this.location.getY()-0.28D) + (firstLine instanceof TextLine ? 0.28D : 0.60D);
        // Teleport the first line
        this.teleportLine(destY, firstLine);
        AbstractLine<?> tempLine;
        for(int j=1; j<this.lines.size(); j++) {
            tempLine = this.lines.get(j);
            /*
            Teleport from the second line onwards.
            The final height is found by adding to that of the first line the difference that was present when it was already spawned
            */
            this.teleportLine(destY + Math.abs(baseY - tempLine.getLocation().getY()), tempLine);
        }
    }

    /**
        Private method of teleporting a certain line.
        Note that the position of this class must already have changed.
     */
    private void teleportLine(double destY, AbstractLine<?> tempLine) {
        Location dest = this.location.clone();
        dest.setY(destY);
        tempLine.setLocation(dest);
        this.seeingPlayers.forEach(tempLine::teleport);
    }

    protected void show(@NotNull Player player) {
        this.seeingPlayers.add(player);
        for(AbstractLine<?> line: this.lines) {
            line.show(player);
        }
        Bukkit.getScheduler().runTask(
                plugin, ()->Bukkit.getPluginManager().callEvent(new PlayerHologramShowEvent(player, this)));
    }

    protected void hide(@NotNull Player player) {
        for(AbstractLine<?> line: this.lines) {
            line.hide(player);
        }
        this.seeingPlayers.remove(player);

        Bukkit.getScheduler().runTask(
                plugin, ()->Bukkit.getPluginManager().callEvent(new PlayerHologramHideEvent(player, this)));
    }

    @NotNull
    protected Plugin getPlugin() {
        return plugin;
    }

    public boolean isShownFor(@NotNull Player player) {
        return this.seeingPlayers.contains(player);
    }

    public void addExcludedPlayer(@NotNull Player player) {
        this.excludedPlayers.add(player);
    }

    public void removeExcludedPlayer(@NotNull Player player) {
        this.excludedPlayers.remove(player);
    }

    @NotNull
    public Set<Player> getExcludedPlayers() {
        return excludedPlayers;
    }

    public boolean isExcluded(@NotNull Player player) {
        return this.excludedPlayers.contains(player);
    }

    @NotNull
    @Unmodifiable
    public List<AbstractLine<?>> getLines() {
        return lines;
    }

    protected void removeSeeingPlayer(Player player) {
        this.seeingPlayers.remove(player);
    }

    @NotNull
    @Unmodifiable
    public Set<Player> getSeeingPlayers() {
        return Collections.unmodifiableSet(seeingPlayers);
    }

    @NotNull
    @Unmodifiable
    public Location getLocation() {
        return location.clone();
    }

    @NotNull
    public Placeholders getPlaceholders() {
        return placeholders;
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hologram hologram = (Hologram) o;
        return Objects.equals(location, hologram.location) && Objects.equals(lines, hologram.lines);
    }

    public static class Builder {
        private static final Object[][] CACHE_ARR = new Object[0][0];

        private final ConcurrentLinkedDeque<Object[]> lines = new ConcurrentLinkedDeque<>();
        private Location location;
        private final Placeholders placeholders = new Placeholders();


        @NotNull
        public Builder addLine(@NotNull String line, boolean clickable) {
            Validate.notNull(line, "Line cannot be null");
            this.lines.addFirst(new Object[]{ line, clickable});
            return this;
        }

        @NotNull
        public Builder addLine(@NotNull ItemStack item) {
            Validate.notNull(item, "Item cannot be null");
            this.lines.addFirst(new Object[]{ item });
            return this;
        }

        @NotNull
        public Builder location(@NotNull Location location) {
            Validate.notNull(location, "Location cannot be null");
            this.location = location;
            return this;
        }

        @NotNull
        public Builder addPlaceholder(@NotNull String key, @NotNull Function<Player, String> result) {
            this.placeholders.add(key, result);
            return this;
        }

        @NotNull
        public Hologram build(@NotNull HologramPool pool) {
            if(location==null || lines.isEmpty() || pool==null) {
                throw new IllegalArgumentException("No location given or not completed");
            }
            Hologram hologram = new Hologram(pool.getPlugin(), this.location, this.placeholders, this.lines.toArray(CACHE_ARR));
            pool.takeCareOf(hologram);
            return hologram;
        }
    }
}
