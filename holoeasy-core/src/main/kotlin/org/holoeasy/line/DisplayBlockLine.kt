package org.holoeasy.line


import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.holoeasy.hologram.Hologram
import org.holoeasy.reactive.MutableState
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
class DisplayBlockLine(
    hologram: Hologram,
    block: MutableState<Material>
) : LineImpl<Material>(hologram, EntityType.BLOCK_DISPLAY) {

    constructor(
        hologram: Hologram, obj: Material
    ) : this(hologram, MutableState(obj))

    private val _mutableStateOf = block
    private var firstRender = true

    override var value: Material
        get() = _mutableStateOf.get()
        set(value) = _mutableStateOf.set(value)

    override val type: Type = Type.DISPLAY_BLOCK_LINE

    override fun show(player: Player) {
        spawn(player)

        this.update(player)

        if (firstRender) {
            firstRender = false
            _mutableStateOf.addObserver(this)
        }
    }

    override fun hide(player: Player) {
        destroy(player)
    }

    override fun update(player: Player) {
        hologram.lib.packetImpl
            .metadataDisplayBlock(player, entityID, value)
    }

    override fun update(value: Material) {
        this.value = value
        observerUpdate()
    }

}
