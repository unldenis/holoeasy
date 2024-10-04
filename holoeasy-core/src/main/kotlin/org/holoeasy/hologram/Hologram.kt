package org.holoeasy.hologram

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.holoeasy.HoloEasy
import org.holoeasy.builder.BlockLineModifiers
import org.holoeasy.builder.TextLineModifiers
import org.holoeasy.line.*
import org.holoeasy.pool.IHologramPool
import org.holoeasy.pool.KeyAlreadyExistsException
import org.jetbrains.annotations.ApiStatus.Internal
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

open class Hologram @JvmOverloads constructor(
    plugin: Plugin,
    location: Location,
    showEvent: ShowEvent? = null,
    hideEvent: HideEvent? = null
) {

    @Internal
    @Deprecated("Internal")
    val pvt = PrivateConfig(this, plugin, showEvent, hideEvent)
    var loader: IHologramLoader = TextBlockStandardLoader()
    val id = UUID.randomUUID()!!
    var location: Location = location
        private set
    val lines: MutableList<ILine<*>> = CopyOnWriteArrayList() // writes are slow and Iterators are fast and consistent.

    // if is first loaded
    private var loaded = false


    @JvmOverloads
    protected fun blockLine(item: ItemStack, modifiers: BlockLineModifiers = BlockLineModifiers()): ILine<ItemStack> {
        val line = if (modifiers.blockType) {
            (BlockLine(pvt.plugin, item))
        } else {
            (ItemLine(pvt.plugin, item))
        }
        lines.add(line)
        line.pvt.hologram = this
        return line
    }

    @JvmOverloads
    protected fun textLine(text: String, modifiers: TextLineModifiers = TextLineModifiers()): ITextLine {
        val line = if (modifiers.clickable) {
            if (modifiers.clickableWithoutPool) {
                val textLine = TextLine(pvt.plugin, text, clickable = false, args = modifiers.args)
                val clickableTextLine =
                    ClickableTextLine(textLine, modifiers.minHitDistance, modifiers.maxHitDistance)
                modifiers.clickEvent?.let { clickableTextLine.clickEvent = it }
                clickableTextLine
            } else {
                val textLine = TextLine(pvt.plugin, text, clickable = true, args = modifiers.args)
                modifiers.clickEvent?.let { textLine.clickEvent = it }
                textLine
            }
        } else {
            TextLine(pvt.plugin, text, clickable = false, args = modifiers.args)
        }
        lines.add(line)
        line.pvt.hologram = this
        return line
    }

    fun teleport(to: Location) {
        this.location = to.clone()
        loader.teleport(this)
    }

    fun isShownFor(player: Player): Boolean {
        return pvt.seeingPlayers.contains(player)
    }

    @JvmOverloads
    fun show(pool: IHologramPool = HoloEasy.STANDARD_POOL) {
        // if pool has no plugin
        if (pool.plugin == null) {
            pool.plugin = pvt.plugin
        }

        if (pool.holograms.any { it.id == this.id }) {
            throw KeyAlreadyExistsException(this.id)
        }
        (pool.holograms as MutableSet<Hologram>).add(this);
    }

    fun show(player: Player) {
        if (!loaded) {
            pvt.load()
            loaded = true
        }

        pvt.seeingPlayers.add(player)
        for (line in lines) {
            line.pvt.show(player)
        }

        pvt.showEvent?.onShow(player)
    }

    fun hide(player: Player) {
        for (line in lines) {
            line.pvt.hide(player)
        }
        pvt.seeingPlayers.remove(player)

        pvt.hideEvent?.onHide(player)
    }

    @JvmOverloads
    fun hide(pool: IHologramPool = HoloEasy.STANDARD_POOL) {
        // if removed
        val removed = (pool.holograms as MutableSet<Hologram>).remove(this)
        if (removed) {
            for (player in pvt.seeingPlayers) {
                hide(player)
            }
        }
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Hologram

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}
