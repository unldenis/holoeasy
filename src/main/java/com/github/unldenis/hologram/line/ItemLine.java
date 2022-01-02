package com.github.unldenis.hologram.line;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
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

        WrappedDataWatcher.WrappedDataWatcherObject visible = new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class));

        watcher.setObject(visible, (byte) 0x20);

        packetV.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        try {
            protocolManager.sendServerPacket(player, packetV);
        } catch (InvocationTargetException invocationTargetException) {
            invocationTargetException.printStackTrace();
        }
        /*
         * Entity Equipment
         */
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
        packet.getIntegers().write(0,this.entityID);
        List<Pair<EnumWrappers.ItemSlot, ItemStack>> pairList = new ArrayList<>();
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, this.obj));
        packet.getSlotStackPairLists().write(0, pairList);
        try {
            protocolManager.sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(@NotNull Player player) {
        /*
         * Entity Equipment
         */
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
        packet.getIntegers().write(0,this.entityID);
        List<Pair<EnumWrappers.ItemSlot, ItemStack>> pairList = new ArrayList<>();
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, this.obj));
        packet.getSlotStackPairLists().write(0, pairList);
        try {
            protocolManager.sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
