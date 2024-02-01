import org.bukkit.Location
import org.bukkit.plugin.Plugin
import org.holoeasy.HoloEasy.startInteractivePool
import org.holoeasy.builder.HologramBuilder.*
import org.holoeasy.config.HologramKey


data class ExampleKotlin(val plugin: Plugin) {

    // you can use a Pool or a org.bukkit.Plugin for HologramKey
    val pool = startInteractivePool(plugin, 60.0, 0.5f, 5f)


    fun addHologram(location: Location) {
        val clickCount = mutableStateOf(0)

        hologram(HologramKey(pool, "unique-id-holo"), location) {
            textline("Count: {}", clickCount)
            clickable("Click me").onClick { clickCount.set(clickCount.get() + 1)}
        }

    }

}