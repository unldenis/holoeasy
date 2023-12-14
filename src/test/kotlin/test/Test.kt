package test

import com.github.unldenis.hologram.HologramLib.Companion.startInteractivePool
import com.github.unldenis.hologram.animation.AnimationType
import com.github.unldenis.hologram.builder.HologramBuilder.*
import com.github.unldenis.hologram.placeholder.Placeholders
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class Test {
    fun init(plugin: Plugin) {
        startInteractivePool(plugin, 60.0, 0.5f, 5f)
    }

    fun code(loc: Location, placeholders: Placeholders) {
        hologram {
            config {
                it.location = loc
                it.placeholders = placeholders
            }
            textline("Hello")
            textline("%%player%%")
            clickable("Click me")

            block(ItemStack(Material.GOLD_BLOCK))
            block(ItemStack(Material.DIAMOND_BLOCK), AnimationType.CIRCLE)
        }
    }
}