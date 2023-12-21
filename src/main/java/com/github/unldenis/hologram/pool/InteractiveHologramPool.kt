package com.github.unldenis.hologram.pool

import com.github.unldenis.hologram.hologram.Hologram
import com.github.unldenis.hologram.line.ILine
import com.github.unldenis.hologram.line.ITextLine
import com.github.unldenis.hologram.util.AABB.Ray3D
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.Plugin

class InteractiveHologramPool(private val pool: HologramPool, minHitDistance: Float, maxHitDistance: Float) : Listener,
    IHologramPool {

    override val plugin: Plugin
        get() = pool.plugin

    override val holograms: Collection<Hologram>
        get() = pool.holograms

    val minHitDistance: Float
    val maxHitDistance: Float

    init {
        require(!(minHitDistance < 0)) { "minHitDistance must be positive" }
        require(!(maxHitDistance > 120)) { "maxHitDistance cannot be greater than 120" }
        this.minHitDistance = minHitDistance
        this.maxHitDistance = maxHitDistance

        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    override fun takeCareOf(hologram: Hologram) {
        pool.takeCareOf(hologram)
    }


    override fun remove(hologram: Hologram): Boolean {
        return pool.remove(hologram)
    }

    @EventHandler
    fun handleInteract(e: PlayerInteractEvent) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            val player = e.player
            if (e.action != Action.LEFT_CLICK_AIR) {
                return@Runnable
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
                                Ray3D(player.eyeLocation), minHitDistance, maxHitDistance
                            )
                            if (intersects == null) {
                                continue
                            }

                            // TODO:
//                            Bukkit.getScheduler().runTask(
//                                plugin,
//                                Runnable {
//                                    Bukkit.getPluginManager()
//                                        .callEvent(PlayerHologramInteractEvent(player, hologram, iTextLine))
//                                })
                            break@FST
                        }

                        ILine.Type.EXTERNAL -> {}
                        ILine.Type.CLICKABLE_TEXT_LINE -> {}
                        ILine.Type.BLOCK_LINE -> {}
                    }
                }
            }
        })
    }
}
