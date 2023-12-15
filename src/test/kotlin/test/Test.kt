package test

import com.github.unldenis.hologram.HologramLib.Companion.startInteractivePool
import com.github.unldenis.hologram.animation.AnimationType
import com.github.unldenis.hologram.builder.HologramBuilder.*
import com.github.unldenis.hologram.builder.interfaces.PlayerFun
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

fun main() {

}

class Test {


    fun init(plugin: Plugin) {
        startInteractivePool(plugin, 60.0, 0.5f, 5f)
    }

    fun code(loc: Location) {

        hologram(loc) {

            textline("Hello")
            textline("{} Stats", Player::name)
            textline("Score {} - {}", { 0 }, { 1 })
            clickable("Click me")

            block(ItemStack(Material.GOLD_BLOCK))
            block(ItemStack(Material.DIAMOND_BLOCK), AnimationType.CIRCLE)
        }
    }
}