package org.holoeasy.hologram

import org.holoeasy.line.ILine
import kotlin.math.abs

class TextBlockStandardLoader : IHologramLoader {

    val LINE_HEIGHT = 0.28

    override fun load(hologram: Hologram, lines: List<ILine<*>>) {
        val hologramLocation = hologram.location.clone()

        if (lines.size == 1) {
            val line: ILine<*> = lines[0]

            line.pvt.setLocation(hologramLocation)
            hologram.lines.add(line)
            return
        }

        // reverse A - B - C to C - B - A
        val lines = lines.reversed()


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

    override fun teleport(hologram: Hologram) {
        val lines: List<ILine<*>> = hologram.lines
        val firstLine: ILine<*> = lines[0]
        // Obtain the Y position of the first line and then calculate the distance to all lines to maintain this distance
        val baseY: Double = firstLine.location?.y ?: throw RuntimeException("First line has not a location")
        // Get position Y where to teleport the first line
        var destY = (hologram.location.y - LINE_HEIGHT)

        destY += when (firstLine.type) {
            ILine.Type.TEXT_LINE, ILine.Type.CLICKABLE_TEXT_LINE, ILine.Type.BLOCK_LINE -> LINE_HEIGHT
            else -> 0.6
        }

        // Teleport the first line
        this.teleportLine(hologram, destY, firstLine)
        var tempLine: ILine<*>
        for (j in 1 until lines.size) {
            tempLine = lines[j]
            /*
            Teleport from the second line onwards.
            The final height is found by adding to that of the first line the difference that was present when it was already spawned
            */
            this.teleportLine(
                hologram, destY + abs(
                    baseY -
                            (tempLine.location?.y ?: throw RuntimeException("Missing location of line $tempLine"))
                ), tempLine
            )
        }
    }

    private fun teleportLine(hologram: Hologram, destY: Double, tempLine: ILine<*>) {
        val dest = hologram.location.clone()
        dest.y = destY
        tempLine.pvt.setLocation(dest)
        hologram.pvt.seeingPlayers.forEach(tempLine.pvt::teleport)
    }
}