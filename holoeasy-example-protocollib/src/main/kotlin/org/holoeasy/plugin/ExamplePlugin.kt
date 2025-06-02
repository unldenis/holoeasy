package org.holoeasy.plugin

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.holoeasy.HoloEasy
import org.holoeasy.builder.TextLineModifiers
import org.holoeasy.hologram.Hologram
import org.holoeasy.packet.PacketImpl

class ExamplePlugin : JavaPlugin() {

    lateinit var holoEasy : HoloEasy

    override fun onEnable() {

        // ** Bind the library
        holoEasy = HoloEasy(plugin = this, PacketImpl.ProtocolLib)

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


    class MyHolo(lib : HoloEasy, location: Location) : Hologram(lib, location) {

        private val clickCount = mutableStateOf(0) // can be any type

        val counter = textLine("Clicked {} times", TextLineModifiers()
            .args(clickCount)
            .clickable { _ -> clickCount.update { it + 1 } })

        var status= blockLine(ItemStack(Material.RED_DYE))

    }


}
