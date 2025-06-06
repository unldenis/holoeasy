package org.holoeasy.plugin


import com.github.retrooper.packetevents.PacketEvents
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.holoeasy.HoloEasy

class ExamplePlugin : JavaPlugin() {

    lateinit var holoEasy : HoloEasy

    override fun onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this))

        //On Bukkit, calling this here is essential, hence the name "load"
        PacketEvents.getAPI().load()

        // ** Bind the library
        holoEasy = HoloEasy(this)
    }

    override fun onEnable() {

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
        holoEasy.destroyPools()

        //Terminate the instance (clean up process)
        PacketEvents.getAPI().terminate()
    }




}
