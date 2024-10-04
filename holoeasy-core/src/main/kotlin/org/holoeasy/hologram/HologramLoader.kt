package org.holoeasy.hologram


import org.holoeasy.line.ILine
import org.holoeasy.line.ITextLine
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

        val line: ILine<*> = hologram.lines[0]

        line.pvt.setLocation(cloned)
        hologram.lines.add(line)
    }, teleport = { hologram ->
        val line = hologram.lines[0]

        line.pvt.setLocation(hologram.location.clone())
        hologram.pvt.seeingPlayers.forEach(line.pvt::teleport)
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
            val line: ILine<*> = hologram.lines[0]

            line.pvt.setLocation(hologramLocation)
            hologram.lines.add(line)

        } else {
            // reverse A - B - C to C - B - A
            val lines = hologram.lines.reversed()


            hologramLocation.subtract(0.0, LINE_HEIGHT, 0.0)

            for (j in lines.indices) {
                val line = lines[j]

                var up = LINE_HEIGHT

                if (j > 0) {
                    when (lines[j - 1].type) {
                        ILine.Type.ITEM_LINE -> up = -1.5
                        ILine.Type.BLOCK_LINE -> up = -0.19
                        ILine.Type.EXTERNAL -> {}
                        ILine.Type.TEXT_LINE -> {}
                        ILine.Type.CLICKABLE_TEXT_LINE -> {}
                    }
                }

                when (line.type) {
                    ILine.Type.TEXT_LINE, ILine.Type.CLICKABLE_TEXT_LINE, ILine.Type.BLOCK_LINE -> {
                        line.pvt.setLocation(hologramLocation.add(0.0, up, 0.0).clone())
                        hologram.lines.add(0, line)
                    }

                    ILine.Type.ITEM_LINE -> {
                        line.pvt.setLocation(hologramLocation.add(0.0, 0.6, 0.0).clone())
                        hologram.lines.add(0, line)
                    }


                    else -> throw RuntimeException("This method load does not support line type " + line.type.name)
                }
            }
        }


    }, teleport = { hologram ->
        val lines: List<ILine<*>> = hologram.lines
        val firstLine: ILine<*> = lines[0]
        // Obtain the Y position of the first line and then calculate the distance to all lines to maintain this distance
        val baseY: kotlin.Double = firstLine.location?.y ?: throw RuntimeException("First line has not a location")
        // Get position Y where to teleport the first line
        var destY = (hologram.location.y - LINE_HEIGHT)

        destY += when (firstLine.type) {
            ILine.Type.TEXT_LINE, ILine.Type.CLICKABLE_TEXT_LINE, ILine.Type.BLOCK_LINE -> LINE_HEIGHT
            else -> 0.6
        }

        // Teleport the first line
        teleportStandard(hologram, destY, firstLine)
        var tempLine: ILine<*>
        for (j in 1 until lines.size) {
            tempLine = lines[j]
            /*
            Teleport from the second line onwards.
            The final height is found by adding to that of the first line the difference that was present when it was already spawned
            */
            teleportStandard(
                hologram, destY + abs(
                    baseY -
                            (tempLine.location?.y ?: throw RuntimeException("Missing location of line $tempLine"))
                ), tempLine
            )
        }
    }),


    ;
}


private const val LINE_HEIGHT = 0.28


private fun teleportStandard(hologram: Hologram, destY: Double, tempLine: ILine<*>) {
    val dest = hologram.location.clone()
    dest.y = destY
    tempLine.pvt.setLocation(dest)
    hologram.pvt.seeingPlayers.forEach(tempLine.pvt::teleport)
}


private fun textSequential(hologram: Hologram, add: Boolean) {
    val cloned = hologram.location.clone()
    for (line in hologram.lines) {
        when (line.type) {
            ILine.Type.TEXT_LINE, ILine.Type.CLICKABLE_TEXT_LINE -> {
                val tL = (line as ITextLine).textLine

                // add to lines
                tL.pvt.setLocation(cloned.clone())

                if (add) {
                    hologram.lines.add(0, tL)
                } else {
                    hologram.pvt.seeingPlayers.forEach { tL.pvt.teleport(it) }
                }
                cloned.z += 0.175 * tL.pvt.obj.length
            }

            else -> throw RuntimeException("This method load supports only TextLine & TextALine & ClickableTextLine.")
        }
    }
}