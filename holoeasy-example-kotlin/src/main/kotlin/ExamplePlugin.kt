package org.holoeasy.plugin

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.holoeasy.HoloEasy
import org.holoeasy.action.ClickAction
import org.holoeasy.builder.HologramBuilder.*

class ExamplePlugin : JavaPlugin() {

    override fun onEnable() {
        val pool = HoloEasy.startInteractivePool(
            this,
            60.0,
            minHitDistance = 0.5f, /* optional */
            maxHitDistance = 5f, /* optional */
            clickAction = ClickAction.RIGHT_CLICK /* optional */
        )


        getCommand("hologram")?.setExecutor { sender, _, _, _ ->

            val location = (sender as Player).location

            pool.registerHolograms {

                hologram(location) {
                    val clickCount = mutableStateOf(0) // can be any type

                    textline("Hello")
                    textline("Count {}", clickCount)
                    clickable("Click me").onClick { clickCount.update { it + 1 } }
                    item(ItemStack(Material.APPLE))
                }


                // other holograms...

            }


            true
        }
    }

}
