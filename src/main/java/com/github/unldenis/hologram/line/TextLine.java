package com.github.unldenis.hologram.line;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.github.unldenis.hologram.placeholder.Placeholders;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Optional;

public class TextLine extends AbstractLine<String> {

    private final Placeholders placeholders;

    public TextLine(@NotNull Collection<Player> seeingPlayers, @NotNull Plugin plugin, int entityID, @NotNull String obj, @NotNull Placeholders placeholders) {
        super(seeingPlayers, plugin, entityID, obj);
        this.placeholders = placeholders;
    }

    @Override
    public void show(@NotNull Player player) {
        super.show(player);
        /*
         * Entity Metadata
         */
        Optional<?> opt = Optional
                .of(WrappedChatComponent
                        .fromChatMessage(this.placeholders.parse(this.obj, player))[0].getHandle());
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, entityID);
        WrappedDataWatcher watcher = new WrappedDataWatcher();

        WrappedDataWatcher.WrappedDataWatcherObject visible = new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class));
        WrappedDataWatcher.WrappedDataWatcherObject nameVisible = new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class));

        watcher.setObject(visible, (byte) 0x20);
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), opt);
        watcher.setObject(nameVisible, true);

        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        try {
            protocolManager.sendServerPacket(player, packet);
        } catch (InvocationTargetException invocationTargetException) {
            invocationTargetException.printStackTrace();
        }
    }

    @Override
    public void update(@NotNull Player player) {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, this.entityID);

        WrappedDataWatcher watcher = new WrappedDataWatcher();

        Optional<?> opt = Optional
                .of(WrappedChatComponent
                        .fromChatMessage(this.placeholders.parse(this.obj, player))[0].getHandle());

        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), opt);

        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        try {
            protocolManager.sendServerPacket(player, packet);
        } catch (InvocationTargetException invocationTargetException) {
            invocationTargetException.printStackTrace();
        }
    }

}
