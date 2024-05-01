package org.holoeasy.plugin

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.holoeasy.HoloEasy
import org.holoeasy.builder.HologramBuilder.*

class ExamplePlugin : JavaPlugin() {

    override fun onEnable() {
        val pool = HoloEasy.startInteractivePool(this, 60.0, minHitDistance = 0.5f, maxHitDistance = 5f)


        getCommand("hologram")?.setExecutor { sender, _, _, _ ->

            val location = (sender as Player).location

            pool.registerHolograms {

                hologram(location) {
                    var clickCount = mutableStateOf(0) // can be any type

                    textline("Hello")
                    textline("Count {}", clickCount)
                    clickable("Click me").onClick { player -> clickCount.set(clickCount.get() + 1) }
                    item(ItemStack(Material.APPLE))
                }


                // other holograms...

            }


            true
        }
    }

}
