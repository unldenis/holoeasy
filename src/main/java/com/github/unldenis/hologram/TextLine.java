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

import com.github.unldenis.hologram.packet.*;
import com.github.unldenis.hologram.util.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.*;

public class TextLine extends AbstractLine<String> {

    private final boolean clickable;
    protected AABB hitbox;

    public TextLine(@NotNull Hologram hologram, @NotNull String obj, boolean clickable) {
        super(hologram, obj);
        this.clickable = clickable;
    }

    @Override
    protected void show(@NotNull Player player) {
        super.show(player);
        PacketsFactory.get()
                .metadataPacket(entityID, obj, player, hologram.getPlaceholders(), true)
                .send(player);
    }

    @Override
    protected void update(@NotNull Player player) {
        PacketsFactory.get()
                .metadataPacket(entityID, obj, player, hologram.getPlaceholders(), false)
                .send(player);
    }

    @Override
    @NotNull
    public String get() {
        return obj;
    }

    /**
     * Parse the possible placeholder for the player.
     * @param player - the target player
     * @return the parsed strings
     */
    @NotNull
    public String parse(Player player) {
        return hologram.getPlaceholders().parse(obj, player);
    }

    @Override
    protected void setLocation(@NotNull Location location) {
        super.setLocation(location);
        if(clickable) {
            double chars = obj.length();
            double size = 0.105;
            double dist = size * (chars / 2d);

            hitbox = new AABB(
                    new AABB.Vec3D(-dist, - 0.039, -dist),
                    new AABB.Vec3D(dist, + 0.039, dist));
            hitbox.translate(AABB.Vec3D.fromLocation(location.clone().add(0, 2.425, 0)));
        }
    }

    public boolean isClickable() {
        return clickable;
    }
}
