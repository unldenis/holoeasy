package org.holoeasy.plugin

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.holoeasy.HoloEasy
import org.holoeasy.hologram.Hologram

class ExamplePlugin : JavaPlugin() {

    lateinit var holoEasy : HoloEasy

    override fun onEnable() {

        // ** Bind the library
        holoEasy = HoloEasy(this)

        getCommand("hologram")?.setExecutor { sender, _, _, _ ->
            if(sender !is Player){
                return@setExecutor true
            }

            val location = (sender).location

            // ** Create a basic hologram and add on the Standard Pool
            val hologram = HelloWorldHologram(holoEasy, location)
            hologram.show(sender)


            //  ** Serialize
            val serialized: Map<String, Any> = hologram.serialize()

            // ** Now you can remove it from the Standard Pool
            hologram.hide(sender)

            // ** Deserialize the previous hologram
            val deserialized : HelloWorldHologram = Hologram.deserialize(serialized)

            // ** Show it
            deserialized.show(sender)

            true
        }
    }

    override fun onDisable() {
        // Since 4.4.0: destroy pools
        holoEasy.destroyPools()
    }

    class MyHolo(lib : HoloEasy, location: Location) : Hologram(lib, location) {


        var clickCount = 0

        val counter = textLine("Clicked $clickCount time")

        val status= blockLine(ItemStack(Material.RED_DYE))


        fun onClick() {
            clickCount++
            counter.update("Clicked $clickCount times")
            status.update(ItemStack(if (clickCount % 2 == 0) Material.GREEN_DYE else Material.RED_DYE))
        }

    }


}
