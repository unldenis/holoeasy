package org.holoeasy.line


import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.holoeasy.ext.send
import org.holoeasy.packet.IPacket
import org.holoeasy.reactive.MutableState
import org.holoeasy.util.VersionEnum
import org.holoeasy.util.VersionUtil

class BlockLine(plugin: Plugin, obj: MutableState<ItemStack>) : ILine<ItemStack> {

    init {
        if(VersionUtil.isCompatible(VersionEnum.V1_8)) {
            throw IllegalStateException("This version does not support item lines")
        }
    }

    private val line: Line = Line(plugin, EntityType.DROPPED_ITEM)
    private val resetVelocity = IPacket.get(IPacket.Type.VELOCITY).velocity(line.entityID, 0, 0,0)

    private val _mutableStateOf = obj

    private var firstRender = true

    constructor(plugin: Plugin, obj: ItemStack) : this(plugin, MutableState(obj)){
    }

    override val plugin: Plugin
        get() = line.plugin

    override val type: ILine.Type
        get() = ILine.Type.BLOCK_LINE

    override val entityId: Int
        get() = line.entityID

    override val location: Location?
        get() = line.location

    override var obj : ItemStack
        get() = _mutableStateOf.get()
        set(value) = _mutableStateOf.set(value)

    override var pvt = ILine.PrivateConfig(this)

    override fun setLocation(value: Location) {
        line.location = value
    }

    override fun hide(player: Player) {
        line.destroy(player)
    }

    override fun teleport(player: Player) {
        line.teleport(player)
    }

    override fun show(player: Player) {
        line.spawn(player)
        this.update(player)

        resetVelocity.send(player)

        if(firstRender) {
            firstRender = false
            _mutableStateOf.addObserver(pvt)
        }
    }

    override fun update(player: Player) {
        IPacket.get(IPacket.Type.METADATA_ITEM)
            .metadata(entityId, obj).send(player)
    }
}
