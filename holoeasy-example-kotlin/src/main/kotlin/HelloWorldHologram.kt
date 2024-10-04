import org.bukkit.Location
import org.holoeasy.hologram.Hologram

class HelloWorldHologram(location: Location) : Hologram(location) {
    var line = textLine("Hello World")
}
