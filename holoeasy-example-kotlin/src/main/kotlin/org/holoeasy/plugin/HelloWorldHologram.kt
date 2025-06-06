package org.holoeasy.plugin

import org.bukkit.Location
import org.holoeasy.HoloEasy
import org.holoeasy.hologram.Hologram

class HelloWorldHologram(lib : HoloEasy, location: Location) : Hologram(lib, location) {
    val line = textLine { "Hello World" }
}
