package org.holoeasy.hologram

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.holoeasy.HoloEasy
import org.holoeasy.builder.TextLineModifiers
import org.holoeasy.line.*
import org.holoeasy.pool.IHologramPool
import org.holoeasy.pool.KeyAlreadyExistsException
import org.holoeasy.reactive.MutableState
import org.jetbrains.annotations.ApiStatus.Internal
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList


open class Hologram @JvmOverloads constructor(
    location: Location,
    showEvent: ShowEvent? = null,
    hideEvent: HideEvent? = null
) {

    @Internal
    val pvt = PrivateConfig(this, showEvent, hideEvent)
    val id = UUID.randomUUID()!!
    var location: Location = location
        private set
    private val stateVars = mutableListOf<MutableState<*>>()
    val lines: MutableList<Line<*>> = CopyOnWriteArrayList() // writes are slow and Iterators are fast and consistent.

    // if is first loaded
    private var loaded = false

    open fun loader(): HologramLoader = HologramLoader.TEXT_BLOCK_STANDARD

    protected fun <T : Any> mutableStateOf(value: T): MutableState<T> {
        val state = MutableState(value)
        stateVars.add(state)
        return state
    }

    protected fun blockLine(item: ItemStack): Line<ItemStack> {
        val line = BlockLine(hologram = this, obj = item)
        lines.add(line)
        return line
    }

    protected fun itemLine(item: ItemStack): Line<ItemStack> {
        val line = ItemLine(hologram = this, obj = item)
        lines.add(line)
        return line
    }

    @JvmOverloads
    protected fun textLine(text: String, modifiers: TextLineModifiers = TextLineModifiers()): Line<String> {
        val line = if (modifiers.clickable) {
            if (modifiers.clickableWithoutPool) {
                val clickableTextLine =
                    ClickableTextLine(
                        hologram = this,
                        text = text,
                        args = modifiers.args,
                        clickable = false,
                        minHitDistance = modifiers.minHitDistance,
                        maxHitDistance = modifiers.maxHitDistance,
                    )
                modifiers.clickEvent?.let { clickableTextLine.clickEvent = it }
                clickableTextLine
            } else {
                val textLine = TextLine(hologram = this, text = text, clickable = true, args = modifiers.args)
                modifiers.clickEvent?.let { textLine.clickEvent = it }
                textLine
            }
        } else {
            TextLine(hologram = this, text = text, clickable = false, args = modifiers.args)
        }
        lines.add(line)
        return line
    }

    fun teleport(to: Location) {
        this.location = to.clone()
        loader().teleport(this)
    }

    fun isShownFor(player: Player): Boolean {
        return pvt.seeingPlayers.contains(player)
    }

    @JvmOverloads
    fun show(pool: IHologramPool<*> = HoloEasy.standardPool) {
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
            (line as LineImpl).show(player)
        }

        pvt.showEvent?.onShow(player)
    }

    fun hide(player: Player) {
        for (line in lines) {
            (line as LineImpl).hide(player)
        }
        pvt.seeingPlayers.remove(player)

        pvt.hideEvent?.onHide(player)
    }

    @JvmOverloads
    fun hide(pool: IHologramPool<*> = HoloEasy.standardPool) {
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

    fun serialize(): MutableMap<String, Any> {
        val result = LinkedHashMap<String, Any>()
        result["location"] = location
        result["lines"] = lines
            .map { mapOf("type" to it.type.name, "value" to it.value) }
        result["stateVars"] = stateVars.map { it.get() }
        return result
    }

    companion object {

        @JvmStatic
        fun <T : Hologram> deserialize(args: Map<String, Any>, clazz: Class<T>): T {
            val location = args["location"] as Location
            val lines = args["lines"] as List<Map<String, Any>>
            val stateVars = args["stateVars"] as List<Any>

            val hologram = clazz
                .getDeclaredConstructor(Location::class.java)
                .newInstance(location) as T

            for (i in lines.indices) {
                val type = LineImpl.Type.valueOf(lines[i]["type"] as String)
                val value = lines[i]["value"]

                when (type) {
                    LineImpl.Type.EXTERNAL -> {
                        val hologramLine = hologram.lines[i] as LineImpl<Any>
                        hologramLine.value = value as Any
                    }

                    LineImpl.Type.TEXT_LINE, LineImpl.Type.CLICKABLE_TEXT_LINE -> {
                        val hologramLine = hologram.lines[i] as TextLine
                        hologramLine.value = value as String
                    }

                    LineImpl.Type.ITEM_LINE, LineImpl.Type.BLOCK_LINE -> {
                        val hologramLine = hologram.lines[i] as LineImpl<ItemStack>
                        hologramLine.value = value as ItemStack
                    }
                }
            }
            for (i in stateVars.indices) {
                val hologramState = hologram.stateVars[i] as MutableState<Any>
                hologramState.set(stateVars[i])
            }


            return hologram
        }

        inline fun <reified T : Hologram> deserialize(args: Map<String, Any>): T {
            return deserialize(args, T::class.java)
        }

    }
}
