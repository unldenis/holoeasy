import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.holoeasy.HoloEasy.startInteractivePool
import org.holoeasy.builder.HologramBuilder.*
import org.holoeasy.config.HologramKey


data class ExampleKotlin(val plugin: Plugin) {

    // you can use a Pool or a org.bukkit.Plugin for HologramKey
    val pool = startInteractivePool(plugin, 60.0, 0.5f, 5f)


    fun addHologram(location: Location) {
        val itemState = mutableStateOf(ItemStack(Material.APPLE))

        hologram(HologramKey(pool, "unique-id-holo"), location) {
            clickable("Click me").onClick {
                itemState.set(ItemStack(Material.ENCHANTED_GOLDEN_APPLE))
            }
            item(itemState)
        }

    }

}