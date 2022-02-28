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
import com.github.unldenis.hologram.util.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class EntityTeleportPacket extends AbstractPacket {

    private final Location location;

    public EntityTeleportPacket(int entityID, @NotNull Location location) {
        super(entityID, PacketType.Play.Server.ENTITY_TELEPORT);
        this.location = location;
    }

    @Override
    public @NotNull AbstractPacket load() {
        packetContainer.getIntegers().write(0, this.entityID);
        if(VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
            packetContainer.getIntegers().write(1, (int) Math.floor(location.getX() * 32.0D));
            packetContainer.getIntegers().write(2, (int) Math.floor(location.getY() * 32.0D));
            packetContainer.getIntegers().write(3, (int) Math.floor(location.getZ() * 32.0D));
        } else {
            packetContainer.getDoubles().write(0, location.getX());
            packetContainer.getDoubles().write(1, location.getY());
            packetContainer.getDoubles() .write(2, location.getZ());
        }
        packetContainer.getBytes().write(0,  this.getCompressAngle(location.getYaw()));
        packetContainer.getBytes().write(1, this.getCompressAngle(location.getPitch()));

        packetContainer.getBooleans().write(0, false);
        return this;
    }

    protected byte getCompressAngle(double angle) {
        return (byte) (angle * 256F / 360F);
    }
}
