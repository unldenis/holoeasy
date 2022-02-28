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

package com.github.unldenis.hologram.packet;

import com.comphenix.protocol.*;
import com.comphenix.protocol.wrappers.*;
import com.github.unldenis.hologram.placeholder.*;
import com.github.unldenis.hologram.util.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class EntityMetadataPacket extends AbstractPacket {

    private final String nameTag;
    private final Player player;
    private final Optional<Placeholders> placeholders;
    private final boolean setInvisible;

    public EntityMetadataPacket(int entityID, @NotNull String nameTag, @NotNull Player player, @NotNull Placeholders placeholders, boolean setInvisible) {
        super(entityID, PacketType.Play.Server.ENTITY_METADATA);
        this.nameTag = nameTag;
        this.player = player;
        this.placeholders = Optional.of(placeholders);
        this.setInvisible = setInvisible;
    }

    public EntityMetadataPacket(int entityID) {
        super(entityID, PacketType.Play.Server.ENTITY_METADATA);
        this.nameTag = null;
        this.player = null;
        this.placeholders = Optional.empty();
        this.setInvisible = true;
    }

    @Override
    public @NotNull AbstractPacket load() {
        packetContainer.getIntegers().write(0, entityID);

        WrappedDataWatcher watcher = new WrappedDataWatcher();
        if(VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
            if(setInvisible) {
                watcher.setObject(0, (byte) 0x20);
            }
            this.placeholders.ifPresent(ph -> {
                watcher.setObject(2, ph.parse(this.nameTag, player));
                watcher.setObject(3, (byte) 1);
            });
        }else{
            if(setInvisible) {
                WrappedDataWatcher.WrappedDataWatcherObject visible = new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class));
                watcher.setObject(visible, (byte) 0x20);
            }

            this.placeholders.ifPresent(ph -> {
                Optional<?> opt = Optional
                        .of(WrappedChatComponent
                                .fromChatMessage(ph.parse(this.nameTag, player))[0].getHandle());
                watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), opt);

                WrappedDataWatcher.WrappedDataWatcherObject nameVisible = new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class));
                watcher.setObject(nameVisible, true);
            });
        }
        packetContainer.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        return this;
    }
}
