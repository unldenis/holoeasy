package org.holoeasy.line

import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.holoeasy.ext.send
import org.holoeasy.packet.IPacket
import org.holoeasy.packet.PacketType
import org.holoeasy.reactive.MutableState
import org.holoeasy.util.AABB

class TextLine(
    plugin: Plugin,
    obj: String,
    override val args: Array<*>? = null,
    override val clickable: Boolean = false
) : ITextLine {

    private val line: Line = Line(plugin, EntityType.ARMOR_STAND)
    private var firstRender = true
    var hitbox: AABB? = null
        private set
    private var isEmpty = false

    override var clickEvent : ClickEvent? = null

    override val textLine: TextLine
        get() = this

    override fun parse(player: Player): String {
        if (args == null) {
            return pvt.obj
        }
        val res = arrayOfNulls<Any>(args.size)
        for (i in args.indices) {
            val tmp = args[i]
            if(tmp is MutableState<*>) {
                res[i] = tmp.get()
                if(firstRender) {
                    firstRender = false
                    tmp.addObserver(pvt)
                }
            } else {
                res[i] = tmp
            }
        }

        return String.format(pvt.obj, args = res)
    }


    override val type: ILine.Type
        get() = ILine.Type.TEXT_LINE
    override val entityId: Int
        get() = line.entityID
    override val location: Location?
        get() = line.location


    @Deprecated("Internal")
    override var pvt = object  : ILine.PrivateConfig<String>() {

        override val plugin: Plugin
            get() = line.plugin
        override var obj: String = ""

        init {
            if (args == null) {
                this.obj = obj
            } else {
                this.obj = obj.replace("{}", "%s")
            }
        }

        override fun setLocation(value: Location) {
            line.location = value
            if (clickable) {
                val chars = obj.length.toDouble()
                val size = 0.105
                val dist = size * (chars / 2.0)

                hitbox = AABB(
                    AABB.Vec3D(-dist, -0.040, -dist),
                    AABB.Vec3D(dist, +0.040, dist)
                ).also {
                    it.translate(AABB.Vec3D.fromLocation(value.clone().add(0.0, 2.35, 0.0)))
                }
            }
        }

        override fun show(player: Player) {
            isEmpty = obj.isEmpty()
            if (!isEmpty) {
                line.spawn(player)
                val packet = PacketType.METADATA_TEXT.metadata(entityId, parse(player))
                packet.send(player)
            }
        }

        override fun hide(player: Player) {
            line.destroy(player)
        }

        override fun teleport(player: Player) {
            line.teleport(player)
        }

        override fun update(player: Player) {
            val spawnBefore = ((if (isEmpty) 1 else 0) or ((if (obj.isEmpty()) 1 else 0) shl 1))
            /*  0x00  = is already showed
                0x01  = is hided but now has changed
                0x02  = is already showed but is empty
                0x03  = is hided and isn't changed      */
            when (spawnBefore) {
                0x03 -> {}
                0x02 -> {
                    line.destroy(player)
                    isEmpty = true
                }

                0x01 -> {
                    line.spawn(player)
                    isEmpty = false
                    PacketType.METADATA_TEXT
                        .metadata(entityId, parse(player)).send(player)
                }

                0x00 ->
                    PacketType.METADATA_TEXT
                        .metadata(entityId, parse(player), invisible = false)
                        .send(player)
            }
        }

    }

    override fun update(value: String) {
        pvt.obj = value
        pvt.observerUpdate()
    }


}