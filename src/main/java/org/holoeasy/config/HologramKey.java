package org.holoeasy.config;

import org.bukkit.plugin.Plugin;
import org.holoeasy.pool.IHologramPool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class HologramKey {

    private final Plugin plugin;
    private final String id;

    private final IHologramPool pool;


    public HologramKey(@NotNull Plugin plugin, @NotNull String id, @Nullable IHologramPool pool) {
        this.plugin = plugin;
        this.id = id;
        this.pool = pool;
    }

    public HologramKey(@NotNull Plugin plugin, @NotNull String id) {
        this(plugin, id, null);
    }

    public HologramKey(@NotNull IHologramPool pool, @NotNull String id) {
        this(pool.getPlugin(), id, pool);
    }

    @NotNull
    public Plugin getPlugin() {
        return plugin;
    }

    @NotNull
    public String getId() {
        return id;
    }

    @Nullable
    public IHologramPool getPool() {
        return pool;
    }

    @Override
    public int hashCode() {
        return Objects.hash(plugin, id, pool);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HologramKey that = (HologramKey) o;
        return Objects.equals(plugin, that.plugin) && Objects.equals(id, that.id) && Objects.equals(pool, that.pool);
    }
}
