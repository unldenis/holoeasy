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
import org.bukkit.plugin.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class EntityDestroyPacket extends AbstractPacket {

    public EntityDestroyPacket( int entityID) {
        super(entityID, PacketType.Play.Server.ENTITY_DESTROY);
    }

    @Override
    public @NotNull AbstractPacket load() {
        if(VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
            packetContainer.getIntegerArrays().write(0, new int[] { this.entityID });
        }else{
            packetContainer.getIntLists().write(0, Collections.singletonList(this.entityID));
        }
        return this;
    }
}
