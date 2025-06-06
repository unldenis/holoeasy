package org.holoeasy.plugin


import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.holoeasy.HoloEasy

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

            true
        }
    }

    override fun onDisable() {
        // Since 4.4.0: destroy pools
        holoEasy.destroyPools()
    }




}
