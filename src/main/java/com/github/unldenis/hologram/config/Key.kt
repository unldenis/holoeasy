package com.github.unldenis.hologram.config

import com.github.unldenis.hologram.hologram.Hologram
import com.github.unldenis.hologram.pool.IHologramPool
import org.bukkit.plugin.Plugin

data class HologramKey(val plugin: Plugin, val id: String) {

    var pool: IHologramPool? = null
        private set

    constructor(pool: IHologramPool, id: String) : this(pool.plugin, id) {
        this.pool = pool
    }


}

class KeyAlreadyExistsException(key: HologramKey) : IllegalStateException("Key '$key' already exists")
class NoValueForKeyException(key: HologramKey) : IllegalStateException("No value for key '$key'")

class AppDataManager {

    private val data: MutableMap<HologramKey, Hologram?> = mutableMapOf()

    fun register(key: HologramKey, value: Hologram) {
        if (data.containsKey(key)) {
            throw KeyAlreadyExistsException(key)
        }
        data[key] = value
    }

    fun registerIfAbsent(key: HologramKey, value: Hologram) {
        data.putIfAbsent(key, value)
    }

    fun get(key: HologramKey): Hologram {
        return data[key] ?: throw NoValueForKeyException(key)
    }

}