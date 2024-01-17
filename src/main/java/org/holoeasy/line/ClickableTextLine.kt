package org.holoeasy.line

import org.holoeasy.builder.interfaces.PlayerFun
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.Plugin
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

        if (line.location != null) {
            this.updateHitBox()
        }

        Bukkit.getPluginManager().registerEvents(this, line.plugin)
    }

    override val clickable: Boolean
        get() = false

    override val textLine: TextLine
        get() = line

    override val args: Array<PlayerFun>?
        get() = textLine.args


    override fun parse(player: Player): String {
        return line.parse(player)
    }

    override fun onClick(clickEvent: ClickEvent) {
        line.onClick(clickEvent)
    }

    override val plugin: Plugin
        get() = line.plugin

    override val type: ILine.Type
        get() = ILine.Type.CLICKABLE_TEXT_LINE

    override val entityId: Int
        get() = line.entityId

    override val location: Location?
        get() = line.location

    override var obj: String
        get() = line.obj
        set(value) {
            line.obj = value
        }

    override fun setLocation(value: Location) {
        line.setLocation(value)
        this.updateHitBox()
    }

    override fun hide(player: Player) {
        line.hide(player)

        playersClickable.remove(player.entityId)
    }

    override fun teleport(player: Player) {
        line.teleport(player)
    }

    override fun show(player: Player) {
        line.show(player)

        playersClickable.add(player.entityId)
    }

    override fun update(player: Player) {
        line.update(player)
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

        val intersects = hitbox!!.intersectsRay(AABB.Ray3D(player.eyeLocation), minHitDistance, maxHitDistance) ?: return
        line.clickEvent?.onClick(player)
    }

    private fun updateHitBox() {
        val chars = obj.length.toDouble()
        val size = 0.105
        val dist = size * (chars / 2.0)

        hitbox = AABB(
            AABB.Vec3D(-dist, -0.039, -dist),
            AABB.Vec3D(dist, +0.039, dist)
        )
        hitbox!!.translate(fromLocation(location!!.clone().add(0.0, 2.42, 0.0)))
    }
}
