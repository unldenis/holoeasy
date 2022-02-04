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

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.github.unldenis.hologram.util.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemLine extends AbstractLine<ItemStack> {

    public ItemLine(@NotNull Collection<Player> seeingPlayers, @NotNull Plugin plugin, int entityID, @NotNull ItemStack obj) {
        super(seeingPlayers, plugin, entityID, obj);
    }

    @Override
    public void show(@NotNull Player player) {
        super.show(player);
        /*
         * Set invisible
         */
        PacketContainer packetV = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packetV.getIntegers().write(0, entityID);
        WrappedDataWatcher watcher = new WrappedDataWatcher();

        if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
            watcher.setObject(0, (byte) 0x20);
        } else {
            WrappedDataWatcher.WrappedDataWatcherObject visible = new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class));
            watcher.setObject(visible, (byte) 0x20);
        }

        packetV.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        try {
            protocolManager.sendServerPacket(player, packetV);
        } catch (InvocationTargetException invocationTargetException) {
            invocationTargetException.printStackTrace();
        }
        /*
         * Entity Equipment
         */
        update(player);
    }

    @Override
    public void update(@NotNull Player player) {
        /*
         * Entity Equipment
         */
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
        packet.getIntegers().write(0, this.entityID);
        if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {

            // Use legacy form to update the head slot.
            packet.getIntegers().write(1, 4);
            packet.getItemModifier().write(0, this.obj);
        } else {
            List<Pair<EnumWrappers.ItemSlot, ItemStack>> pairList = new ArrayList<>();
            pairList.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, this.obj));
            packet.getSlotStackPairLists().write(0, pairList);
        }
        try {
            protocolManager.sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
