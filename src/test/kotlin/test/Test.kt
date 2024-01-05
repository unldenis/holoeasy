package test

import com.github.unldenis.hologram.HologramLib.startInteractivePool
import com.github.unldenis.hologram.builder.HologramBuilder.*
import com.github.unldenis.hologram.config.HologramKey
import com.github.unldenis.hologram.pool.IHologramPool
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class Test(plugin: Plugin) {

    private val pool: IHologramPool

    init {
        pool = startInteractivePool(plugin, 60.0, 0.5f, 5f)
    }

    fun code(loc: Location, id: String) {
        hologram(HologramKey(pool, id), loc) {
            textline("Hello")
            textline("{} Stats", Player::getName)
            textline("Score {} - {}", { 0 }, { 1 })
            clickable("Click me")
                .onClick { it.sendMessage("Hi") }

            item(ItemStack(Material.GOLDEN_APPLE))
            item(ItemStack(Material.DIAMOND_BLOCK))
        }.onHide { it.sendMessage("Hi im $id") }
    }

    fun getAndAddHideEvent(key: HologramKey) {
        val hologram = pool.get(key)
        hologram.onHide { it.sendMessage("See you again...") }
    }
}