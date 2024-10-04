package org.holoeasy.line

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.Plugin
import org.holoeasy.HoloEasy
import org.holoeasy.util.AABB
import org.holoeasy.util.AABB.Vec3D.Companion.fromLocation

class ClickableTextLine(private val line: TextLine, minHitDistance: Float, maxHitDistance: Float) : Listener,
    ITextLine {


    private val minHitDistance: Float
    private val maxHitDistance: Float
    private var hitbox: AABB? = null
    private val playersClickable = mutableSetOf<Int>()


    init {
        require(!(minHitDistance < 0)) { "minHitDistance must be positive" }
        require(!(maxHitDistance > 120)) { "maxHitDistance cannot be greater than 120" }
        this.minHitDistance = minHitDistance
        this.maxHitDistance = maxHitDistance

        Bukkit.getPluginManager().registerEvents(this, HoloEasy.plugin())
    }


    override val clickable: Boolean
        get() = false

    override val textLine: TextLine
        get() = line

    override val args: Array<*>?
        get() = textLine.args

    override var clickEvent: ClickEvent? = null

    override fun parse(player: Player): String {
        return line.parse(player)
    }

    override val type: ILine.Type
        get() = ILine.Type.CLICKABLE_TEXT_LINE

    override val entityId: Int
        get() = line.entityId

    override val location: Location?
        get() = line.location


    @Deprecated("Internal")
    override var pvt = object : ILine.PrivateConfig<String>() {
        init {
            if (line.location != null) {
                updateHitBox()
            }
        }

        override var obj: String
            get() = line.pvt.obj
            set(value) {
                line.pvt.obj = value
            }

        override fun setLocation(value: Location) {
            line.pvt.setLocation(value)
            this.updateHitBox()
        }

        override fun show(player: Player) {
            line.pvt.show(player)

            playersClickable.add(player.entityId)
        }

        override fun hide(player: Player) {
            line.pvt.hide(player)

            playersClickable.remove(player.entityId)
        }

        override fun teleport(player: Player) {
            line.pvt.teleport(player)
        }

        override fun update(player: Player) {
            line.pvt.update(player)
        }

        fun updateHitBox() {
            val chars = obj.length.toDouble()
            val size = 0.105
            val dist = size * (chars / 2.0)

            hitbox = AABB(
                AABB.Vec3D(-dist, -0.040, -dist),
                AABB.Vec3D(dist, +0.040, dist)
            )
            hitbox!!.translate(fromLocation(location!!.clone().add(0.0, 2.35, 0.0)))
        }

    }

    override fun update(value: String) {
        line.update(value)
    }


    @EventHandler
    fun handleInteract(e: PlayerInteractEvent) {
        val player = e.player
        if (e.action != Action.LEFT_CLICK_AIR) {
            return
        }
        if (hitbox == null) {
            return
        }

        if (!playersClickable.contains(player.entityId)) {
            return
        }

        val intersects =
            hitbox!!.intersectsRay(AABB.Ray3D(player.eyeLocation), minHitDistance, maxHitDistance) ?: return
        line.clickEvent?.onClick(player)
    }


}
