package org.holoeasy.pool

import org.holoeasy.config.HologramKey
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.Plugin
import org.holoeasy.hologram.Hologram
import org.holoeasy.line.ILine
import org.holoeasy.line.ITextLine
import org.holoeasy.util.AABB

class InteractiveHologramPool(private val pool: HologramPool, minHitDistance: Float, maxHitDistance: Float) : Listener,
    IHologramPool {

    override val plugin: Plugin
        get() = pool.plugin

    override fun get(key: HologramKey): Hologram {
        return pool.get(key)
    }

    override fun get(keyId: String): Hologram {
        return pool.get(keyId)
    }

    override fun takeCareOf(key: HologramKey, value: Hologram) {
        pool.takeCareOf(key, value)
    }

    override fun remove(key: HologramKey): Hologram? {
        return pool.remove(key)
    }


    val minHitDistance: Float
    val maxHitDistance: Float

    init {
        require(!(minHitDistance < 0)) { "minHitDistance must be positive" }
        require(!(maxHitDistance > 120)) { "maxHitDistance cannot be greater than 120" }
        this.minHitDistance = minHitDistance
        this.maxHitDistance = maxHitDistance

        Bukkit.getPluginManager().registerEvents(this, plugin)
    }


    @EventHandler
    fun handleInteract(e: PlayerInteractEvent) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            val player = e.player
            if (e.action != Action.LEFT_CLICK_AIR) {
                return@Runnable
            }
            FST@ for (hologram in pool.holograms.values) {
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
                        ILine.Type.BLOCK_LINE -> {}
                    }
                }
            }
        })
    }
}
