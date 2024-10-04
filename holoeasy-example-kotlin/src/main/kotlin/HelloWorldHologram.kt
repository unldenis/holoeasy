import org.bukkit.Location
import org.bukkit.plugin.Plugin
import org.holoeasy.hologram.Hologram

class HelloWorldHologram(plugin: Plugin, location: Location) : Hologram(plugin, location) {
    var line = textLine("Hello World")
}
