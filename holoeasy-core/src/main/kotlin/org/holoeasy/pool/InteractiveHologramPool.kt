package org.holoeasy.pool

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.Plugin
import org.holoeasy.HoloEasy
import org.holoeasy.action.ClickAction
import org.holoeasy.hologram.Hologram
import org.holoeasy.line.ILine
import org.holoeasy.line.ITextLine
import org.holoeasy.util.AABB
import java.util.UUID

class InteractiveHologramPool<T : Hologram>(private val pool: HologramPool<T>, minHitDistance: Float, maxHitDistance: Float, val clickAction: ClickAction?) : Listener,
    IHologramPool<T> {

    override val holograms: Set<T>
        get() = pool.holograms

    val minHitDistance: Float
    val maxHitDistance: Float

    init {
        require(minHitDistance > 0) { "minHitDistance must be positive" }
        require(maxHitDistance < 120) { "maxHitDistance cannot be greater than 120" }
        this.minHitDistance = minHitDistance
        this.maxHitDistance = maxHitDistance

        Bukkit.getPluginManager().registerEvents(this, HoloEasy.plugin())
    }


    @EventHandler
    fun handleInteract(e: PlayerInteractEvent) {
        Bukkit.getScheduler().runTaskAsynchronously(HoloEasy.plugin(), Runnable {
            val player = e.player

            if(clickAction == null) {
                if (e.action != Action.LEFT_CLICK_AIR && e.action != Action.RIGHT_CLICK_AIR) {
                    return@Runnable
                }
            } else {
                when(clickAction) {
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
                    when (line.type) {
                        ILine.Type.TEXT_LINE -> {
                            val iTextLine = line as ITextLine
                            if (!iTextLine.clickable) {
                                continue
                            }

                            val tL = iTextLine.textLine
                            if (tL.hitbox == null) {
                                continue
                            }

                            val intersects = tL.hitbox!!.intersectsRay(
                                AABB.Ray3D(player.eyeLocation), minHitDistance, maxHitDistance
                            )
                            if (intersects == null) {
                                continue
                            }

                            tL.clickEvent?.onClick(player)
                            break@FST
                        }

                        ILine.Type.EXTERNAL -> {}
                        ILine.Type.CLICKABLE_TEXT_LINE -> {}
                        ILine.Type.ITEM_LINE -> {}
                        ILine.Type.BLOCK_LINE -> {}
                    }
                }
            }
        })
    }


}
