package org.holoeasy.plugin

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.holoeasy.HoloEasy
import org.holoeasy.animation.Animations
import org.holoeasy.builder.HologramBuilder.*
import org.holoeasy.ext.get
import org.holoeasy.line.ITextLine
import org.holoeasy.line.TextLine

class ExamplePlugin : JavaPlugin() {

    override fun onEnable() {
        val pool = HoloEasy.startInteractivePool(this, 60.0, minHitDistance = 0.5f, maxHitDistance = 5f)


        getCommand("hologram")?.setExecutor { sender, _, _, _ ->

            val location = (sender as Player).location

            val blocks = arrayOf(
                ItemStack(Material.DIRT),
                ItemStack(Material.IRON_BLOCK),
                ItemStack(Material.GOLD_BLOCK),
                ItemStack(Material.DIAMOND_BLOCK),
            ).iterator()

            pool.registerHolograms {

               val holo = hologram(location) {
                    val currBlock = mutableStateOf(blocks.next()) // can be any type

                    textline("Hello")
                    clickable("Update me").onClick { currBlock.set(blocks.next()) }
                    block(currBlock)
                }
                holo[2].setAnimation(Animations.CIRCLE)
            }

            true
        }
    }

}
