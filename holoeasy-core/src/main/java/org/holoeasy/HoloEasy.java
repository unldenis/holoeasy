package org.holoeasy;

import org.bukkit.plugin.Plugin;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.packet.IPacket;
import org.holoeasy.packet.PacketImpl;
import org.holoeasy.pool.HologramPool;
import org.holoeasy.pool.IHologramPool;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.ArrayList;
import java.util.List;

public class HoloEasy {

    private final Plugin plugin;
    private final List<IHologramPool<?>> pools = new ArrayList<>();

    @Internal
    final IPacket packetImpl;

    public HoloEasy(Plugin plugin, PacketImpl packetImpl) {
        this.plugin = plugin;
        this.packetImpl = packetImpl.impl;
    }

    public <T extends Hologram> IHologramPool<T> startPool(double spawnDistance) {
        HologramPool<T> simplepool = new HologramPool<>(this, spawnDistance);
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

    public IPacket getPacketImpl() {
        return packetImpl;
    }
}