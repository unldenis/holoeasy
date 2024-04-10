import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.holoeasy.config.HologramKey
import org.holoeasy.hologram.Hologram
import org.holoeasy.hologram.IHologramLoader
import org.holoeasy.hologram.TextBlockStandardLoader
import org.holoeasy.line.BlockLine
import org.holoeasy.line.ILine
import org.holoeasy.line.TextLine


open class SimpleHologram() {

    internal val lines: MutableList<BuilderLine<*>> = ArrayList()


    sealed class BuilderLine<T>(val type: ILine.Type, var initialValue: T) {

        class Text(initialValue: String, val clickable: Boolean, val args: Array<*>) :
            BuilderLine<String>(ILine.Type.TEXT_LINE, initialValue)

        class Item(initialValue: ItemStack) :
            BuilderLine<ItemStack>(ILine.Type.BLOCK_LINE, initialValue)
    }

    fun textline(
        initialValue: String,
        clickable: Boolean = false,
        vararg args: Any
    ): BuilderLine<String> {
        val line = BuilderLine.Text(initialValue, clickable, args)
        this.lines.add(line)
        return line
    }

    fun itemline(
        initialValue: ItemStack,
    ): BuilderLine<ItemStack> {
        val line = BuilderLine.Item(initialValue)
        this.lines.add(line)
        return line
    }
}

class UpdateStatement<T : SimpleHologram> {
    operator fun <E> set(line: SimpleHologram.BuilderLine<E>, value: E) {

    }

}

object PluginTest : JavaPlugin() {

}

object LobbyHologram : SimpleHologram() {

    val name = textline("sdds")
    val block = itemline(ItemStack(Material.DIAMOND))


}
fun <T : SimpleHologram> T.create(key : HologramKey, location: Location, loader: IHologramLoader = TextBlockStandardLoader()) : Hologram {
    val holo = Hologram(key, location, loader)
    val _lines = lines.map {
        when (it) {
            is SimpleHologram.BuilderLine.Item -> {
                BlockLine(holo.key.plugin, it.initialValue)
            }
            is SimpleHologram.BuilderLine.Text -> {
                    TextLine(holo.key.plugin, it.initialValue, it.args, it.clickable)
            }
        }
    }
    holo.load(*_lines.toTypedArray())
    return holo
}

inline fun <T : SimpleHologram> T.update(hologram: Hologram, block: (UpdateStatement<T>) -> Unit) {}
inline fun <T : SimpleHologram> T.updateAll(block: (UpdateStatement<T>) -> Unit) {}

fun main() {

    val id = LobbyHologram.create(HologramKey(PluginTest, ""), Bukkit.getWorlds()[0].spawnLocation)


    LobbyHologram.update(id) {
        it[LobbyHologram.name] = ""
        it[LobbyHologram.block] = ItemStack(Material.AIR)
    }


}

