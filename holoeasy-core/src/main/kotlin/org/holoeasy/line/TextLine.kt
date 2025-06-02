package org.holoeasy.line

import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.holoeasy.HoloEasy
import org.holoeasy.hologram.Hologram
import org.holoeasy.reactive.MutableState
import org.holoeasy.util.AABB

open class TextLine(
    hologram: Hologram,
    text: String,
    args: Array<*>? = null,
    val clickable: Boolean
) : LineImpl<String>(hologram, EntityType.ARMOR_STAND) {

    override var value: String = ""

    private val args: Array<*>?
    private var firstRender = true
    private var isEmpty = false
    var hitbox: AABB? = null
        protected set
    var clickEvent: ClickEvent? = null

    init {
        this.value = text

        this.args = args

        if (args == null) {
            this.value = text
        } else {
            this.value = text.replace("{}", "%s")
        }
    }



    fun parse(player: Player): String {
        if (args == null) {
            return value
        }
        val res = arrayOfNulls<Any>(args.size)
        for (i in args.indices) {
            val tmp = args[i]
            if (tmp is MutableState<*>) {
                res[i] = tmp.get()
                if (firstRender) {
                    firstRender = false
                    tmp.addObserver(this)
                }
            } else {
                res[i] = tmp
            }
        }

        return String.format(value, args = res)
    }


    override val type: Type = Type.TEXT_LINE


    override fun setCurrentLocation(value: Location) {
        super.setCurrentLocation(value)

        if (clickable) {
            val chars = this.value.length.toDouble()
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
        isEmpty = value.isEmpty()
        if (!isEmpty) {
            spawn(player)

            hologram.lib.packetImpl
                .metadataText(player, entityID, nameTag = parse(player))
        }
    }


    override fun hide(player: Player) {
        destroy(player)
    }



    override fun update(player: Player) {
        val spawnBefore = ((if (isEmpty) 1 else 0) or ((if (value.isEmpty()) 1 else 0) shl 1))
        /*  0x00  = is already showed
            0x01  = is hided but now has changed
            0x02  = is already showed but is empty
            0x03  = is hided and isn't changed      */
        when (spawnBefore) {
            0x03 -> {}
            0x02 -> {
                destroy(player)
                isEmpty = true
            }

            0x01 -> {
                spawn(player)
                isEmpty = false

                hologram.lib.packetImpl
                    .metadataText(player, entityID, nameTag = parse(player))
            }

            0x00 ->
                hologram.lib.packetImpl
                    .metadataText(player, entityID, nameTag = parse(player), invisible = false)
        }
    }

    override fun update(value: String) {
        this.value = value
        observerUpdate()
    }

}