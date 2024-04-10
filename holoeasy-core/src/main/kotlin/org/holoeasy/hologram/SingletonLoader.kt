package org.holoeasy.hologram

import org.holoeasy.line.ILine


class SingletonLoader : IHologramLoader {
    override fun load(hologram: Hologram, lines: Array<out ILine<*>>) {
        if (lines.size > 1) {
            throw RuntimeException("Hologram '${hologram.key}' has more than 1 line.")
        }

        val cloned = hologram.location.clone()

        val line: ILine<*> = lines[0]

        line.setLocation(cloned)
        hologram.lines.add(line)
    }

    override fun teleport(hologram: Hologram) {
        val line: ILine<*> = hologram.lines[0]

        line.setLocation(hologram.location.clone())
        hologram.seeingPlayers.forEach(line::teleport)
    }
}
