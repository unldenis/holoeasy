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
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class EntityEquipmentPacket extends AbstractPacket {

    private final ItemStack helmet;

    public EntityEquipmentPacket(int entityID, @NotNull ItemStack helmet) {
        super( entityID, PacketType.Play.Server.ENTITY_EQUIPMENT);
        this.helmet = helmet;
    }

    @Override
    public @NotNull AbstractPacket load() {
        packetContainer.getIntegers().write(0, this.entityID);
        if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {

            // Use legacy form to update the head slot.
            packetContainer.getIntegers().write(1, 4);
            packetContainer.getItemModifier().write(0, this.helmet);
        } else {
            List<Pair<EnumWrappers.ItemSlot, ItemStack>> pairList = new ArrayList<>();
            pairList.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, this.helmet));
            packetContainer.getSlotStackPairLists().write(0, pairList);
        }
        return this;
    }
}
