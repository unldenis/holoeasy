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

package com.github.unldenis.hologram.line;

import com.github.unldenis.hologram.*;
import com.github.unldenis.hologram.packet.*;
import com.github.unldenis.hologram.placeholder.Placeholders;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TextLine extends AbstractLine<String> {

    public TextLine(@NotNull Hologram hologram,@NotNull String obj) {
        super(hologram, obj);
    }

    @Override
    protected void show(@NotNull Player player) {
        super.show(player);
        new EntityMetadataPacket(entityID, obj, player, hologram.getPlaceholders(), true)
                .load()
                .send(player);
    }

    @Override
    public void update(@NotNull Player player) {
        new EntityMetadataPacket(entityID, obj, player, hologram.getPlaceholders(), false)
                .load()
                .send(player);
    }

}
