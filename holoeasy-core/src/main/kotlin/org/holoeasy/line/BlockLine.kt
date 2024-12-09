package org.holoeasy.line


import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.holoeasy.HoloEasy
import org.holoeasy.hologram.Hologram
import org.holoeasy.reactive.MutableState

class BlockLine(
    hologram: Hologram,
    block: MutableState<ItemStack>
) : LineImpl<ItemStack>(hologram, EntityType.ARMOR_STAND) {

    constructor(
        hologram: Hologram, obj: ItemStack
    ) : this(hologram, MutableState(obj))

    private val _mutableStateOf = block
    private var firstRender = true

    override var value: ItemStack
        get() = _mutableStateOf.get()
        set(value) = _mutableStateOf.set(value)

    override val type: Type = Type.BLOCK_LINE


    override fun show(player: Player) {
        spawn(player)

        HoloEasy.packetImpl()
            .metadataText(player, entityID, nameTag = null, invisible = true)

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
        HoloEasy.packetImpl()
            .metadataItem(player, entityID, item = value)
    }

    override fun update(value: ItemStack) {
        this.value = value
        observerUpdate()
    }

}
