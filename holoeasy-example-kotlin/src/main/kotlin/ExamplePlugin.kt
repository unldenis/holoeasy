package org.holoeasy.plugin

import HelloWorldHologram
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
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


            val hologram = HelloWorldHologram(this, location)
            hologram.show(pool)

            true
        }
    }


    class MyHolo(plugin: Plugin, location: Location) : Hologram(plugin, location) {

        private val clickCount = MutableState(0) // can be any type

        val counter = textLine("Clicked {} times", TextLineModifiers()
            .args(clickCount)
            .clickable { _ -> clickCount.update { it + 1 } })

        var status= blockLine(ItemStack(Material.RED_DYE))

    }


}
