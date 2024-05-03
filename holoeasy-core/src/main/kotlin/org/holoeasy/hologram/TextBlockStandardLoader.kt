package org.holoeasy.hologram

import org.holoeasy.line.ILine
import kotlin.math.abs

class TextBlockStandardLoader : IHologramLoader {
    override fun load(hologram: Hologram, lines: Array<out ILine<*>>) {
        val cloned = hologram.location.clone()

        if (lines.size == 1) {
            val line: ILine<*> = lines[0]

            line.setLocation(cloned)
            hologram.lines.add(line)
            return
        }

        // reverse A - B - C to C - B - A
        lines.reverse()

        cloned.subtract(0.0, 0.28, 0.0)

        for (j in lines.indices) {
            val line: ILine<*> = lines[j]
            var up = 0.28

            if (j > 0) {
                val before: ILine.Type = lines[j - 1].type
                when (before) {
                    ILine.Type.ITEM_LINE -> up = -1.5
                    ILine.Type.BLOCK_LINE -> up = -1.5
                    ILine.Type.EXTERNAL -> {}
                    ILine.Type.TEXT_LINE -> {}
                    ILine.Type.CLICKABLE_TEXT_LINE -> {}
                }
            }

            when (line.type) {
                ILine.Type.TEXT_LINE, ILine.Type.CLICKABLE_TEXT_LINE -> {
                    line.setLocation(cloned.add(0.0, up, 0.0).clone())
                    hologram.lines.add(0, line)
                }

                ILine.Type.ITEM_LINE -> {
                    line.setLocation(cloned.add(0.0, 0.6, 0.0).clone())
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
        var destY = (hologram.location.y - 0.28)

        destY += when (firstLine.type) {
            ILine.Type.TEXT_LINE, ILine.Type.CLICKABLE_TEXT_LINE -> 0.28
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
        tempLine.setLocation(dest)
        hologram.seeingPlayers.forEach(tempLine::teleport)
    }
}