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
import com.github.unldenis.hologram.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.*;

public class SpawnEntityLivingPacket extends AbstractPacket {

    private static WrappedDataWatcher defaultDataWatcher;

    private final Location location;
    private final Plugin plugin;

    public SpawnEntityLivingPacket(int entityID, @NotNull Location location, @NotNull Plugin plugin) {
        super(entityID, PacketType.Play.Server.SPAWN_ENTITY_LIVING);
        this.location = location;
        this.plugin = plugin;
    }

    @Override
    public @NotNull AbstractPacket load() {
        if(VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
            packetContainer.getIntegers().write(0, this.entityID);
            packetContainer.getIntegers().write(1, (int) EntityType.ARMOR_STAND.getTypeId());
            packetContainer.getIntegers().write(2, (int) (this.location.getX() * 32));
            packetContainer.getIntegers().write(3, (int) (this.location.getY() * 32));
            packetContainer.getIntegers().write(4, (int) (this.location.getZ() * 32));
            if(defaultDataWatcher == null) {
                loadDefaultWatcher().join();
            }
            packetContainer.getDataWatcherModifier().write(0, SpawnEntityLivingPacket.defaultDataWatcher);

        }else{
            final int entityType = 1;
            final int extraData = 1;
            packetContainer.getIntegers().write(0, this.entityID);
            packetContainer.getIntegers().write(1, entityType);
            packetContainer.getIntegers().write(2, extraData);

            packetContainer.getUUIDs().write(0, UUID.randomUUID());

            packetContainer.getDoubles().write(0, this.location.getX());
            packetContainer.getDoubles().write(1, this.location.getY()/*+1.2*/);
            packetContainer.getDoubles().write(2, this.location.getZ());
        }
        return this;
    }

    protected CompletableFuture<Void> loadDefaultWatcher() {
        return BukkitFuture.runSync(plugin, () -> {
            World world = Bukkit.getWorlds().get(0);
            Entity entity = world.spawnEntity(new Location(world, 0, 256, 0), EntityType.ARMOR_STAND);
            defaultDataWatcher = WrappedDataWatcher.getEntityWatcher(entity).deepClone();
            entity.remove();
        });
    }
}
