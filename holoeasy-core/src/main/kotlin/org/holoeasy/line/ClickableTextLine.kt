package org.holoeasy.line

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.holoeasy.HoloEasy
import org.holoeasy.hologram.Hologram
import org.holoeasy.util.AABB
import org.holoeasy.util.AABB.Vec3D.Companion.fromLocation

class ClickableTextLine(
    hologram: Hologram,
    text: String,
    args: Array<*>? = null,
    clickable: Boolean,
    minHitDistance: Float,
    maxHitDistance: Float,
) : Listener, TextLine(hologram, text, args, clickable) {


    private val minHitDistance: Float
    private val maxHitDistance: Float
    private val playersClickable = mutableSetOf<Int>()


    init {
        require(!(minHitDistance < 0)) { "minHitDistance must be positive" }
        require(!(maxHitDistance > 120)) { "maxHitDistance cannot be greater than 120" }
        this.minHitDistance = minHitDistance
        this.maxHitDistance = maxHitDistance

        Bukkit.getPluginManager().registerEvents(this, HoloEasy.plugin())

//        if (location != null) {
//            updateHitBox()
//        }
    }

    override val type = Type.CLICKABLE_TEXT_LINE


    override fun setCurrentLocation(value: Location) {
        super.setCurrentLocation(value)
        this.updateHitBox()
    }


    override fun show(player: Player) {
        super.show(player)

        playersClickable.add(player.entityId)
    }


    override fun hide(player: Player) {
        super.hide(player)

        playersClickable.remove(player.entityId)
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
        clickEvent?.onClick(player)
    }

    private fun updateHitBox() {
        val chars = value.length.toDouble()
        val size = 0.105
        val dist = size * (chars / 2.0)

        hitbox = AABB(
            AABB.Vec3D(-dist, -0.040, -dist),
            AABB.Vec3D(dist, +0.040, dist)
        )
        hitbox!!.translate(fromLocation(currentLocation()!!.clone().add(0.0, 2.35, 0.0)))
    }


}
