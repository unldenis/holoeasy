package org.holoeasy.line


import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.holoeasy.HoloEasy
import org.holoeasy.hologram.Hologram
import org.holoeasy.reactive.MutableState
import org.holoeasy.util.VersionEnum
import org.holoeasy.util.VersionUtil

class ItemLine(
    hologram: Hologram,

    item: MutableState<ItemStack>
) : LineImpl<ItemStack>(hologram, EntityType.ITEM) {

    private val _mutableStateOf = item
    private var firstRender = true

    constructor(
        hologram: Hologram,
        obj: ItemStack
    ) : this(hologram, MutableState(obj))

    init {
        if (VersionUtil.isCompatible(VersionEnum.V1_8)) {
            throw IllegalStateException("This version does not support item lines")
        }
    }

    override val type: Type = Type.ITEM_LINE


    override var value: ItemStack
        get() = _mutableStateOf.get()
        set(value) {
            _mutableStateOf.set(value)
        }



    override fun show(player: Player) {
        spawn(player)
        this.update(player)

        hologram.lib.packetImpl
            .velocity(player, entityID, 0.0, 0.0, 0.0)

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
            .metadataItem(player, entityID, item = value)
    }

    override fun update(value: ItemStack) {
        this.value = value
        observerUpdate()
    }

}
