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

import com.github.unldenis.hologram.animation.*;
import com.github.unldenis.hologram.event.*;
import com.github.unldenis.hologram.line.AbstractLine;
import com.github.unldenis.hologram.line.ItemLine;
import com.github.unldenis.hologram.line.TextLine;
import com.github.unldenis.hologram.placeholder.Placeholders;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

public class Hologram {
    private final Plugin plugin;
    private Location location;

    protected final AbstractLine<?>[] lines;
    private final Collection<Player> seeingPlayers;

    private final Placeholders placeholders;

    /**
     * @param plugin The org.bukkit.Plugin
     * @param location The location of the hologram
     * @param placeholders Reference passage of placeholders
     * @param seeingPlayers Visible player reference, used when changing lines or in animations
     * @param lines Inverted array of hologram lines
     * @deprecated Deprecated because you have to use the Builder of the class.
     */
    @Deprecated
    @ApiStatus.Internal
    public Hologram(
            @NotNull Plugin plugin,
            @NotNull Location location,
            @Nullable Placeholders placeholders,
            @NotNull Collection<Player> seeingPlayers,
            @NotNull Object... lines
    ) {
        this.plugin = plugin;
        this.location = location;
        this.placeholders = placeholders == null ? new Placeholders() : placeholders;
        this.seeingPlayers = seeingPlayers;
        this.lines = new AbstractLine[lines.length];

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        Location cloned = this.location.clone().subtract(0, 0.28, 0);

        Object line;
        AbstractLine<?> tempLine;
        for(int j=0; j<lines.length; j++) {
            line = lines[j];
            double up = 0.28D;
            if(j>0 && lines[j-1] instanceof ItemStack) {
                up = 0.0D;
            }
            if(line instanceof String) {
                tempLine = new TextLine(this.seeingPlayers, plugin, random.nextInt(), (String) line, this.placeholders);
                tempLine.setLocation(cloned.add(0.0, up, 0).clone());
                this.lines[j] = tempLine;
            }else if (line instanceof ItemStack) {
                tempLine = new ItemLine(this.seeingPlayers, plugin, random.nextInt(), (ItemStack) line);
                tempLine.setLocation(cloned.add(0.0, 0.60D, 0).clone());
                this.lines[j] = tempLine;
            }
        }
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
        // Clone the given location
        this.location = to.clone();
        // Obtain the Y position of the first line and then calculate the distance to all lines to maintain this distance
        double baseY = this.lines[0].getLocation().getY();
        // Get position Y where to teleport the first line
        double destY = (this.location.getY()-0.28D) + (this.lines[0] instanceof TextLine ? 0.28D : 0.60D);
        // Teleport the first line
        this.teleportLine(destY, this.lines[0]);
        AbstractLine<?> tempLine;
        for(int j=1; j<this.lines.length; j++) {
            tempLine = this.lines[j];
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

    public void setLine(int index, @NotNull ItemStack itemStack) {
        Validate.notNull(itemStack, "New line cannot be null");
        AbstractLine<ItemStack> line = (ItemLine) getLine(index);
        line.set(itemStack);
        this.seeingPlayers.forEach(line::update);
    }

    public void setLine(int index, @NotNull String text) {
        Validate.notNull(text, "New line cannot be null");
        AbstractLine<String> line = (TextLine) getLine(index);
        line.set(text);
        this.seeingPlayers.forEach(line::update);
    }

    public void setAnimation(int index, @NotNull Animation animationType) {
        Validate.notNull(animationType, "AnimationType cannot be null");
        getLine(index).setAnimation(animationType.clone());
    }

    public void removeAnimation(int index) {
        getLine(index).removeAnimation();
    }

    public Location getLocation() {
        return location;
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
    protected AbstractLine<?> getLine(int index) {
        return this.lines[Math.abs(index-this.lines.length+1)];
    }

    protected boolean isShownFor(@NotNull Player player) {
        return this.seeingPlayers.contains(player);
    }

    @NotNull
    protected Collection<Player> getSeeingPlayers() {
        return seeingPlayers;
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
        return Objects.equals(location, hologram.location) && Arrays.equals(lines, hologram.lines);
    }

    public static class Builder {

        private final ConcurrentLinkedDeque<Object> lines = new ConcurrentLinkedDeque<>();
        private Location location;
        private final Placeholders placeholders = new Placeholders();

        @NotNull
        public Builder addLine(@NotNull String line) {
            Validate.notNull(line, "Line cannot be null");
            this.lines.addFirst(line);
            return this;
        }

        @NotNull
        public Builder addLine(@NotNull ItemStack item) {
            Validate.notNull(item, "Item cannot be null");
            this.lines.addFirst(item);
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
            Hologram hologram = new Hologram(pool.getPlugin(), this.location, this.placeholders, new CopyOnWriteArraySet<>(), this.lines.toArray());
            pool.takeCareOf(hologram);
            return hologram;
        }
    }
}
