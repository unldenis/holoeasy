package test


import com.github.unldenis.hologram.HologramLib.Companion.startPool
import com.github.unldenis.hologram.animation.AnimationType
import com.github.unldenis.hologram.builder.HologramBuilder.*
import com.github.unldenis.hologram.placeholder.Placeholders
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

fun init(plugin: Plugin) {
    startPool(plugin, 60.0)
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