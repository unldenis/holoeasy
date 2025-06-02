package org.holoeasy.pool

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.holoeasy.HoloEasy
import org.holoeasy.action.ClickAction
import org.holoeasy.hologram.Hologram
import org.holoeasy.line.BlockLine
import org.holoeasy.line.ItemLine
import org.holoeasy.line.TextLine
import org.holoeasy.util.AABB

class InteractiveHologramPool<T : Hologram>(
    private val pool: HologramPool<T>,
    minHitDistance: Float,
    maxHitDistance: Float,
    val clickAction: ClickAction?
) : Listener, IHologramPool<T> {

    override val lib: HoloEasy
        get() = pool.lib

    override val holograms: Set<T>
        get() = pool.holograms

    override fun destroy() {
        pool.destroy()
    }

    val minHitDistance: Float
    val maxHitDistance: Float

    init {
        require(minHitDistance > 0) { "minHitDistance must be positive" }
        require(maxHitDistance < 120) { "maxHitDistance cannot be greater than 120" }
        this.minHitDistance = minHitDistance
        this.maxHitDistance = maxHitDistance

        Bukkit.getPluginManager().registerEvents(this, lib.plugin)
    }


    @EventHandler
    fun handleInteract(e: PlayerInteractEvent) {
        Bukkit.getScheduler().runTaskAsynchronously(lib.plugin, Runnable {
            val player = e.player

            if (clickAction == null) {
                if (e.action != Action.LEFT_CLICK_AIR && e.action != Action.RIGHT_CLICK_AIR) {
                    return@Runnable
                }
            } else {
                when (clickAction) {
                    ClickAction.LEFT_CLICK -> {
                        if (e.action != Action.LEFT_CLICK_AIR) {
                            return@Runnable
                        }
                    }

                    ClickAction.RIGHT_CLICK -> {
                        if (e.action != Action.RIGHT_CLICK_AIR) {
                            return@Runnable
                        }
                    }
                }
            }


            FST@ for (hologram in pool.holograms) {
                if (!hologram.isShownFor(player)) {
                    continue
                }
                for (line in hologram.lines) {
                    when (line) {
                        is TextLine -> {
                            if (!line.clickable) {
                                continue
                            }

                            if (line.hitbox == null) {
                                continue
                            }

                            val intersects = line.hitbox!!.intersectsRay(
                                AABB.Ray3D(player.eyeLocation), minHitDistance, maxHitDistance
                            )
                            if (intersects == null) {
                                continue
                            }

                            line.clickEvent?.onClick(player)
                            break@FST
                        }
                    }

                }
            }
        })
    }


}
