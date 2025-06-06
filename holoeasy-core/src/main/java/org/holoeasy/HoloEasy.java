package org.holoeasy;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.holoeasy.event.AsyncHologramInteractEvent;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.line.Line;
import org.holoeasy.pool.HologramPool;
import org.holoeasy.pool.IHologramPool;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

public class HoloEasy implements PacketListener {

    private final Plugin plugin;
    private final List<IHologramPool<?>> pools = new ArrayList<>();

    public HoloEasy(Plugin plugin) {
        this.plugin = plugin;

        /* We will register our packet listeners in the onLoad method */
        PacketEvents.getAPI().getEventManager().registerListener(this, PacketListenerPriority.NORMAL);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(event.getPacketType() != PacketType.Play.Client.INTERACT_ENTITY) {
            return;
        }
        Player player = event.getPlayer();
        if (player == null) {
            return;
        }

        WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(event);

        // TODO: Optimize entity ID lookup
        for (IHologramPool<?> pool : pools) {
            if(!pool.isInteractive()) {
                continue;
            }
            for (Hologram hologram : pool.getHolograms()) {
                for (Line<?> line : hologram.getLines()) {
                    if(line.getEntityID() == packet.getEntityId()) {
                        // found, call event
                        Bukkit.getPluginManager().callEvent(new AsyncHologramInteractEvent(player, line));
                        break;
                    }
                }
            }
        }
    }

    public <T extends Hologram> IHologramPool<T> startPool(double spawnDistance, boolean isInteractive) {
        HologramPool<T> simplepool = new HologramPool<>(this, spawnDistance, isInteractive);
        pools.add(simplepool);
        return simplepool;
    }

    @ApiStatus.Experimental
    public void destroyPools() {
        for (IHologramPool<?> pool : pools) {
            pool.destroy();
        }
        pools.clear();
    }

    public Plugin getPlugin() {
        return plugin;
    }
}