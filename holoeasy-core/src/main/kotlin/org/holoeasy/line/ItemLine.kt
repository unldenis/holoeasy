package org.holoeasy.line


import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.holoeasy.ext.send
import org.holoeasy.packet.PacketType
import org.holoeasy.reactive.MutableState
import org.holoeasy.util.VersionEnum
import org.holoeasy.util.VersionUtil

class ItemLine(item: MutableState<ItemStack>) : ILine<ItemStack> {

    constructor(obj: ItemStack) : this(MutableState(obj))

    init {
        if(VersionUtil.isCompatible(VersionEnum.V1_8)) {
            throw IllegalStateException("This version does not support item lines")
        }
    }

    private val line: Line = Line(EntityType.DROPPED_ITEM)
    private val resetVelocity = PacketType.VELOCITY.velocity(line.entityID, 0, 0,0)
    private val _mutableStateOf = item
    private var firstRender = true

    override val type: ILine.Type
        get() = ILine.Type.ITEM_LINE

    override val entityId: Int
        get() = line.entityID

    override val location: Location?
        get() = line.location

    @Deprecated("Internal")
    override var pvt = object  : ILine.PrivateConfig<ItemStack>() {

        override var obj: ItemStack
            get() = _mutableStateOf.get()
            set(value) = _mutableStateOf.set(value)

        override fun setLocation(value: Location) {
            line.location = value
        }

        override fun show(player: Player) {
            line.spawn(player)
            this.update(player)

            resetVelocity.send(player)

            if(firstRender) {
                firstRender = false
                _mutableStateOf.addObserver(this)
            }
        }

        override fun hide(player: Player) {
            line.destroy(player)
        }

        override fun teleport(player: Player) {
            line.teleport(player)
        }

        override fun update(player: Player) {
            PacketType.METADATA_ITEM
                .metadata(entityId, obj).send(player)
        }

    }

    override fun update(value: ItemStack) {
        pvt.obj = value
        pvt.observerUpdate()
    }

}
