package com.github.unldenis.hologram

import com.github.unldenis.hologram.event.PlayerHologramInteractEvent
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
    private val minHitDistance: Float
    private val maxHitDistance: Float

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

    override val plugin: Plugin
        get() = pool.plugin

    override fun remove(hologram: Hologram): Boolean {
        return pool.remove(hologram)
    }

    override val holograms: Collection<Hologram>
        get() = pool.holograms

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
                    when (line.getType()) {
                        ILine.Type.TEXT_LINE, ILine.Type.TEXT_ANIMATED_LINE -> {
                            val iTextLine = line as ITextLine
                            if (!iTextLine.isClickable()) {
                                continue
                            }

                            val tL = iTextLine.asTextLine()
                            if (tL.hitbox == null) {
                                continue
                            }

                            val intersects = tL.hitbox!!.intersectsRay(
                                Ray3D(player.eyeLocation), minHitDistance, maxHitDistance
                            )
                            if (intersects == null) {
                                continue
                            }

                            Bukkit.getScheduler().runTask(
                                plugin,
                                Runnable {
                                    Bukkit.getPluginManager()
                                        .callEvent(PlayerHologramInteractEvent(player, hologram, iTextLine))
                                })
                            break@FST
                        }

                    }
                }
            }
        })
    }
}
