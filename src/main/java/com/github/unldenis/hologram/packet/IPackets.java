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
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.plugin.*;

import java.util.*;
import java.util.concurrent.*;


public interface IPackets {

    PacketContainerSendable spawnPacket(int entityID, Location location, Plugin plugin);

    PacketContainerSendable destroyPacket(int entityID);

    PacketContainerSendable equipmentPacket(int entityID, ItemStack helmet);

    PacketContainerSendable metadataPacket(int entityID, String nameTag, Player player, Placeholders placeholders, boolean setInvisible);

    default PacketContainerSendable metadataPacket(int entityID) {
        return metadataPacket(entityID, null, null, null, true);
    }

    PacketContainerSendable teleportPacket(int entityID, Location location);

    List<PacketContainerSendable> rotatePackets(int entityID, Location from, float yaw);

    default PacketContainerSendable newPacket(PacketType packetType) {
        return new PacketContainerSendable(packetType);
    }

    default byte getCompressAngle(double angle) {
        return (byte) (angle * 256F / 360F);
    }

    default int fixCoordinate(double v) {
        return (int) Math.floor(v * 32.0D);
    }

    class PacketsV1_8 implements IPackets {

        private static WrappedDataWatcher defaultDataWatcher;

        @Override
        public PacketContainerSendable spawnPacket(int entityID, Location location, Plugin plugin) {
            PacketContainerSendable packet = newPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
            packet.getIntegers().write(0, entityID);
            packet.getIntegers().write(1, (int) EntityType.ARMOR_STAND.getTypeId());
            packet.getIntegers().write(2, (int) (location.getX() * 32));
            packet.getIntegers().write(3, (int) (location.getY() * 32));
            packet.getIntegers().write(4, (int) (location.getZ() * 32));
            if(defaultDataWatcher == null) {
                loadDefaultWatcher(plugin).join();
            }
            packet.getDataWatcherModifier().write(0, PacketsV1_8.defaultDataWatcher);


            return packet;
        }

        @Override
        public PacketContainerSendable destroyPacket(int entityID) {
            PacketContainerSendable packet = newPacket(PacketType.Play.Server.ENTITY_DESTROY);
            packet.getIntegerArrays().write(0, new int[] { entityID });
            return packet;
        }

        @Override
        public PacketContainerSendable equipmentPacket(int entityID, ItemStack helmet) {
            PacketContainerSendable packet = newPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
            packet.getIntegers().write(0, entityID);
            // Use legacy form to update the head slot.
            packet.getIntegers().write(1, 4);
            packet.getItemModifier().write(0, helmet);
            return packet;
        }

        @Override
        public PacketContainerSendable metadataPacket(int entityID, String nameTag, Player player, Placeholders placeholders, boolean setInvisible) {
            PacketContainerSendable packet = newPacket(PacketType.Play.Server.ENTITY_METADATA);
            packet.getIntegers().write(0, entityID);
            WrappedDataWatcher watcher = new WrappedDataWatcher();
            if(setInvisible) {
                watcher.setObject(0, (byte) 0x20);
            }
            if(placeholders != null) {
                watcher.setObject(2, placeholders.parse(nameTag, player));
                watcher.setObject(3, (byte) 1);
            }
            packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
            return packet;
        }

        @Override
        public PacketContainerSendable teleportPacket(int entityID, Location location) {
            PacketContainerSendable packet = newPacket(PacketType.Play.Server.ENTITY_TELEPORT);
            packet.getIntegers().write(0, entityID);
            packet.getIntegers().write(1, fixCoordinate(location.getX()));
            packet.getIntegers().write(2, fixCoordinate(location.getY()));
            packet.getIntegers().write(3, fixCoordinate(location.getZ()));
            packet.getBytes().write(0,  this.getCompressAngle(location.getYaw()));
            packet.getBytes().write(1, this.getCompressAngle(location.getPitch()));
            packet.getBooleans().write(0, false);
            return packet;
        }

        @Override
        public List<PacketContainerSendable> rotatePackets(int entityID, Location from, float yaw) {
            PacketContainerSendable rotationPacket = newPacket(PacketType.Play.Server.ENTITY_HEAD_ROTATION);

            rotationPacket.getIntegers().write(0, entityID);
            rotationPacket.getBytes().write(0, getCompressAngle(yaw));

            PacketContainerSendable teleportPacket = newPacket(PacketType.Play.Server.ENTITY_TELEPORT);

            teleportPacket.getIntegers().write(0, entityID);

            teleportPacket.getIntegers().write(1, fixCoordinate(from.getX()));
            teleportPacket.getIntegers().write(2, fixCoordinate(from.getY()));
            teleportPacket.getIntegers().write(3, fixCoordinate(from.getZ()));

            teleportPacket.getBytes().write(0, getCompressAngle(yaw));
            teleportPacket.getBytes().write(1, (byte) 0);

            teleportPacket.getBooleans().write(0, true);

            return Arrays.asList(rotationPacket, teleportPacket);
        }

        protected CompletableFuture<Void> loadDefaultWatcher(Plugin plugin) {
            return BukkitFuture.runSync(plugin, () -> {
                World world = Bukkit.getWorlds().get(0);
                Entity entity = world.spawnEntity(new Location(world, 0, 256, 0), EntityType.ARMOR_STAND);
                defaultDataWatcher = WrappedDataWatcher.getEntityWatcher(entity).deepClone();
                entity.remove();
            });
        }
    }

    class PacketsV1_9V1_18 implements IPackets {
        @Override
        public PacketContainerSendable spawnPacket(int entityID, Location location, Plugin plugin) {
            PacketContainerSendable packet = newPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
            final int entityType = 1;
            final int extraData = 1;
            packet.getIntegers().write(0, entityID);
            packet.getIntegers().write(1, entityType);
            packet.getIntegers().write(2, extraData);
            packet.getUUIDs().write(0, UUID.randomUUID());
            packet.getDoubles().write(0, location.getX());
            packet.getDoubles().write(1, location.getY()/*+1.2*/);
            packet.getDoubles().write(2, location.getZ());
            return packet;
        }

        @Override
        public PacketContainerSendable destroyPacket(int entityID) {
            PacketContainerSendable packet = newPacket(PacketType.Play.Server.ENTITY_DESTROY);
            packet.getIntLists().write(0, Collections.singletonList(entityID));
            return packet;
        }

        @Override
        public PacketContainerSendable equipmentPacket(int entityID, ItemStack helmet) {
            PacketContainerSendable packet = newPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
            packet.getIntegers().write(0, entityID);
            List<Pair<EnumWrappers.ItemSlot, ItemStack>> pairList = new ArrayList<>();
            pairList.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, helmet));
            packet.getSlotStackPairLists().write(0, pairList);
            return packet;
        }

        @Override
        public PacketContainerSendable metadataPacket(int entityID, String nameTag, Player player, Placeholders placeholders, boolean setInvisible) {
            PacketContainerSendable packet = newPacket(PacketType.Play.Server.ENTITY_METADATA);
            packet.getIntegers().write(0, entityID);
            WrappedDataWatcher watcher = new WrappedDataWatcher();
            if(setInvisible) {
                WrappedDataWatcher.WrappedDataWatcherObject visible = new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class));
                watcher.setObject(visible, (byte) 0x20);
            }
            if(placeholders != null) {
                Optional<?> opt = Optional
                        .of(WrappedChatComponent
                                .fromChatMessage(placeholders.parse(nameTag, player))[0].getHandle());
                watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), opt);

                WrappedDataWatcher.WrappedDataWatcherObject nameVisible = new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class));
                watcher.setObject(nameVisible, true);
            };

            packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
            return packet;
        }

        @Override
        public PacketContainerSendable teleportPacket(int entityID, Location location) {
            PacketContainerSendable packet = newPacket(PacketType.Play.Server.ENTITY_TELEPORT);
            packet.getIntegers().write(0, entityID);
            packet.getDoubles().write(0, location.getX());
            packet.getDoubles().write(1, location.getY());
            packet.getDoubles() .write(2, location.getZ());
            packet.getBytes().write(0,  this.getCompressAngle(location.getYaw()));
            packet.getBytes().write(1, this.getCompressAngle(location.getPitch()));
            packet.getBooleans().write(0, false);
            return packet;
        }

        @Override
        public List<PacketContainerSendable> rotatePackets(int entityID, Location from, float yaw) {
            PacketContainerSendable packet = newPacket(PacketType.Play.Server.ENTITY_LOOK);
            packet.getIntegers().write(0, entityID);
            packet.getBytes()
                    .write(0, this.getCompressAngle(yaw))
                    .write(1, (byte) 0);
            packet.getBooleans().write(0, true);
            return Collections.singletonList(packet);
        }
    }

    class PacketsV1_19 extends PacketsV1_9V1_18 {

        @Override
        public PacketContainerSendable spawnPacket(int entityID, Location location, Plugin plugin) {
            PacketContainerSendable packet = newPacket(PacketType.Play.Server.SPAWN_ENTITY);
            final int extraData = 1;
            packet.getIntegers().write(0, entityID);
            packet.getIntegers().write(1, extraData);
            packet.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);
            packet.getUUIDs().write(0, UUID.randomUUID());
            packet.getDoubles().write(0, location.getX());
            packet.getDoubles().write(1, location.getY()/*+1.2*/);
            packet.getDoubles().write(2, location.getZ());
            return packet;
        }
    }
}
