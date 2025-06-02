package org.holoeasy.hologram


import org.holoeasy.line.LineImpl
import org.holoeasy.line.TextLine
import kotlin.math.abs

enum class HologramLoader(
    val load: (Hologram) -> Unit,
    val teleport: (Hologram) -> Unit
) {

    SINGLETON(load = { hologram ->
        if (hologram.lines.size > 1) {
            throw RuntimeException("Hologram '${hologram.id}' has more than 1 line.")
        }

        val cloned = hologram.location.clone()

        val line = hologram.lines[0] as LineImpl

        line.setCurrentLocation(cloned)
    }, teleport = { hologram ->
        val line = hologram.lines[0] as LineImpl

        line.setCurrentLocation(hologram.location.clone())
        hologram.pvt.seeingPlayers.forEach(line::teleport)
    }),

    TEXT_SEQUENTIAL(load = { hologram ->
        textSequential(hologram, true)

    }, teleport = { hologram ->
        textSequential(hologram, false)
        // TODO: When teleporting, the holograms unexpectedly become distant. Understand why.
    }),

    TEXT_BLOCK_STANDARD(load = { hologram ->
        val hologramLocation = hologram.location.clone()

        if (hologram.lines.size == 1) {
            val line = hologram.lines[0] as LineImpl

            line.setCurrentLocation(hologramLocation)
        } else {
            // reverse A - B - C to C - B - A
            val lines = hologram.lines.reversed()
            hologram.lines.clear()

            hologramLocation.subtract(0.0, LINE_HEIGHT, 0.0)

            for (j in lines.indices) {
                val line = lines[j] as LineImpl

                var up = LINE_HEIGHT

                if (j > 0) {
                    when (lines[j - 1].type) {
                        LineImpl.Type.ITEM_LINE -> up = -1.5
                        LineImpl.Type.BLOCK_LINE -> up = -0.19
                        LineImpl.Type.EXTERNAL -> {}
                        LineImpl.Type.TEXT_LINE -> {}
                        LineImpl.Type.CLICKABLE_TEXT_LINE -> {}
                        org.holoeasy.line.LineImpl.Type.DISPLAY_BLOCK_LINE -> {}
                    }
                }

                when (line.type) {
                    LineImpl.Type.TEXT_LINE, LineImpl.Type.CLICKABLE_TEXT_LINE, LineImpl.Type.BLOCK_LINE -> {
                        line.setCurrentLocation(hologramLocation.add(0.0, up, 0.0).clone())
                        hologram.lines.add(0, line)
                    }

                    LineImpl.Type.ITEM_LINE -> {
                        line.setCurrentLocation(hologramLocation.add(0.0, 0.6, 0.0).clone())
                        hologram.lines.add(0, line)
                    }


                    else -> throw RuntimeException("This method load does not support line type " + line.type.name)
                }
            }
        }


    }, teleport = { hologram ->
        val lines = hologram.lines
        val firstLine = lines[0] as LineImpl
        // Obtain the Y position of the first line and then calculate the distance to all lines to maintain this distance
        val baseY: Double =
            firstLine.currentLocation()?.y ?: throw RuntimeException("First line has not a location")
        // Get position Y where to teleport the first line
        var destY = (hologram.location.y - LINE_HEIGHT)

        destY += when (firstLine.type) {
            LineImpl.Type.TEXT_LINE, LineImpl.Type.CLICKABLE_TEXT_LINE, LineImpl.Type.BLOCK_LINE -> LINE_HEIGHT
            else -> 0.6
        }

        // Teleport the first line
        teleportStandard(hologram, destY, firstLine)
        var tempLine: LineImpl<*>
        for (j in 1 until lines.size) {
            tempLine = lines[j] as LineImpl
            /*
            Teleport from the second line onwards.
            The final height is found by adding to that of the first line the difference that was present when it was already spawned
            */
            teleportStandard(
                hologram, destY + abs(
                    baseY -
                            (tempLine.currentLocation()?.y
                                ?: throw RuntimeException("Missing location of line $tempLine"))
                ), tempLine
            )
        }
    }),


    ;
}


private const val LINE_HEIGHT = 0.28


private fun teleportStandard(hologram: Hologram, destY: Double, tempLine: LineImpl<*>) {
    val dest = hologram.location.clone()
    dest.y = destY
    tempLine.setCurrentLocation(dest)
    hologram.pvt.seeingPlayers.forEach(tempLine::teleport)
}


private fun textSequential(hologram: Hologram, add: Boolean) {
    val cloned = hologram.location.clone()
    for (line in hologram.lines) {
        when (line.type) {
            LineImpl.Type.TEXT_LINE, LineImpl.Type.CLICKABLE_TEXT_LINE -> {
                val tL = line as TextLine

                // add to lines
                tL.setCurrentLocation(cloned.clone())

                if (add) {
//                    hologram.lines.add(0, tL)
                } else {
                    hologram.pvt.seeingPlayers.forEach { tL.teleport(it) }
                }
                cloned.z += 0.175 * tL.value.length
            }

            else -> throw RuntimeException("This method load supports only TextLine & TextALine & ClickableTextLine.")
        }
    }
}