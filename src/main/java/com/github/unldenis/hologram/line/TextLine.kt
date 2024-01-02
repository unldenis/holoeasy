package com.github.unldenis.hologram.line

import com.github.unldenis.hologram.builder.interfaces.PlayerFun
import com.github.unldenis.hologram.ext.send
import com.github.unldenis.hologram.packet.IPacket
import com.github.unldenis.hologram.util.AABB
import com.github.unldenis.hologram.util.AABB.Vec3D
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class TextLine(plugin: Plugin, obj: String, override val args: Array<PlayerFun>? = null,
    override val clickable: Boolean = false
) : ITextLine {

    private val line: Line = Line(plugin, EntityType.ARMOR_STAND)

    override var obj: String = ""

    var clickEvent : ClickEvent? = null

    init {

        if (args == null) {
            this.obj = obj
        } else {
            this.obj = obj.replace("{}", "%s")
        }
    }

    var hitbox: AABB? = null
        private set
    private var isEmpty = false

    override val textLine: TextLine
        get() = this

    override fun parse(player: Player): String {
        if (args == null) {
            return obj
        }
        val res = arrayOfNulls<Any>(args.size)
        for (i in args.indices) {
            res[i] = args[i](player)
        }

        return String.format(obj, args = res)
    }

    override fun onClick(clickEvent: ClickEvent) {
        this.clickEvent = clickEvent
    }

    override val plugin: Plugin
        get() = line.plugin
    override val type: ILine.Type
        get() = ILine.Type.TEXT_LINE
    override val entityId: Int
        get() = line.entityID
    override val location: Location?
        get() = line.location

    override fun setLocation(value: Location) {
        line.location = value
        if (clickable) {
            val chars = obj.length.toDouble()
            val size = 0.105
            val dist = size * (chars / 2.0)

            hitbox = AABB(
                Vec3D(-dist, -0.039, -dist),
                Vec3D(dist, +0.039, dist)
            ).also {
                it.translate(Vec3D.fromLocation(value.clone().add(0.0, 1.40, 0.0)))
            }
        }
    }

    override fun hide(player: Player) {
        line.destroy(player)
    }

    override fun teleport(player: Player) {
        line.teleport(player)
    }

    override fun show(player: Player) {
        isEmpty = obj.isEmpty()
        if (!isEmpty) {
            line.spawn(player)
            val packet = IPacket.get(IPacket.Type.METADATA_TEXT)
                .metadata(entityId, parse(player))
            packet.send(player)
        }
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
                IPacket.get(IPacket.Type.METADATA_TEXT)
                    .metadata(entityId, parse(player)).send(player)
            }

            0x00 ->
                IPacket.get(IPacket.Type.METADATA_TEXT)
                    .metadata(entityId, parse(player), invisible = false)
                    .send(player)
        }
    }


}