package org.holoeasy;

import org.bukkit.plugin.Plugin;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.pool.HologramPool;
import org.holoeasy.pool.IHologramPool;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

public class HoloEasy {

    private final Plugin plugin;
    private final List<IHologramPool<?>> pools = new ArrayList<>();

    public HoloEasy(Plugin plugin) {
        this.plugin = plugin;
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
}