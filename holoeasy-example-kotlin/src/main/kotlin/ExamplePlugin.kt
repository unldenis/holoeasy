package org.holoeasy.plugin

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.holoeasy.HoloEasy
import org.holoeasy.action.ClickAction
import org.holoeasy.builder.TextLineModifiers
import org.holoeasy.hologram.Hologram
import org.holoeasy.reactive.MutableState

class ExamplePlugin : JavaPlugin() {

    override fun onEnable() {
        val pool = HoloEasy.startInteractivePool(
            this,
            60.0,
            minHitDistance = 0.5f, /* optional */
            maxHitDistance = 5f, /* optional */
            clickAction = ClickAction.LEFT_CLICK /* optional */
        )


        getCommand("hologram")?.setExecutor { sender, _, _, _ ->

            val location = (sender as Player).location


            val clickCount = MutableState(0) // can be any type
            Hologram.Builder(this, location)
                .name("my-holo")
                .textLine("Hello")
                .textLine("Count {}", TextLineModifiers().args(clickCount))
                .textLine("Click me", TextLineModifiers()
                    .clickable { clickCount.update { it + 1 } }
                )
                .blockLine(ItemStack(Material.APPLE))
                .onShow { it.sendMessage("Hi :)") }
                .buildAndLoad(pool)

            true
        }
    }

}
