package com.github.unldenis.hologram.line;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.github.unldenis.hologram.animation.AbstractAnimation;
import com.github.unldenis.hologram.animation.AnimationType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class AbstractLine<T> {

    private final Plugin plugin;
    protected final ProtocolManager protocolManager;
    protected final int entityID;
    protected Location location;

    protected T obj;

    protected Optional<AbstractAnimation> animation = Optional.empty();

    private final Collection<Player> animationPlayers;
    private int taskID = -1;

    public AbstractLine(@NotNull Collection<Player> seeingPlayers, @NotNull Plugin plugin, int entityID, @NotNull T obj) {
        this.animationPlayers = seeingPlayers; //copy rif
        this.plugin = plugin;
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.entityID = entityID;

        this.obj = obj;
    }

    public void setLocation(@NotNull Location location) {
        this.location = location;
    }

    public void set(T newObj) {
        this.obj = newObj;
    }

    public abstract void update(@NotNull Player player);

    public void hide(@NotNull Player player) {
        PacketContainer destroyEntity = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        destroyEntity.getIntLists().write(0, Collections.singletonList(this.entityID));
        try {
            protocolManager.sendServerPacket(player, destroyEntity);
        } catch (InvocationTargetException invocationTargetException) {
            invocationTargetException.printStackTrace();
        }

    }

    public void show(@NotNull Player player) {
        /*
         * Entity Living Spawn
         */
        final PacketContainer itemPacket = protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);

        final int entityType = 1;
        final int extraData = 1;

        StructureModifier<Integer> itemInts = itemPacket.getIntegers();
        itemInts.write(0, this.entityID);
        itemInts.write(1, entityType);
        itemInts.write(2, extraData);

        StructureModifier<UUID> itemIDs = itemPacket.getUUIDs();
        itemIDs.write(0, UUID.randomUUID());

        StructureModifier<Double> itemDoubles = itemPacket.getDoubles();
        itemDoubles.write(0, this.location.getX());
        itemDoubles.write(1, this.location.getY()/*+1.2*/);
        itemDoubles.write(2, this.location.getZ());

        try {
            protocolManager.sendServerPacket(player, itemPacket);
        } catch (InvocationTargetException invocationTargetException) {
            invocationTargetException.printStackTrace();
        }
    }

    public void setAnimation(@NotNull AnimationType animationType) {
        AbstractAnimation abstractAnimation = animationType.cloned();
        this.animation = Optional.of(abstractAnimation);
        abstractAnimation.setEntityID(this.entityID);
        abstractAnimation.setProtocolManager(this.protocolManager);

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin,
                ()->{ this.animationPlayers.forEach(abstractAnimation::nextFrame);
                }, abstractAnimation.delay(), abstractAnimation.delay());
        this.taskID = task.getTaskId();
    }

    public void removeAnimation() {
        if(taskID != -1)
            Bukkit.getScheduler().cancelTask(taskID);
        taskID = -1;
    }

}
