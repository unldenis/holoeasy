package org.holoeasy.plugin

import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.entity.Player
import org.holoeasy.HoloEasy
import org.holoeasy.hologram.Hologram
import java.util.*

class MyCounterHolo( lib: HoloEasy, location: Location) : Hologram(lib, location) {

    private var clickCount = 0
    private val playerClickCounts: MutableMap<UUID, Int> = mutableMapOf()

    private val global_counter = displayTextLine { "Clicked " + (++clickCount) + " times" }
        .backgroundColor(Color.GREEN)

    private val player_counter =
        textLine { ("Clicked " + playerClickCounts.compute(it.uniqueId) { _, count -> if (count == null) 1 else count + 1 }) + " times by ${it.name}" }

    private val interactionLine = interactionLine()


    fun onClick(player: Player) {
        global_counter.updateAll()
        player_counter.update(player)
    }
}