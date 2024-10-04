package org.holoeasy.hologram

import org.holoeasy.line.ILine


class SingletonLoader : IHologramLoader {
    override fun load(hologram: Hologram, lines: List<ILine<*>>) {
        if (lines.size > 1) {
            throw RuntimeException("Hologram '${hologram.id}' has more than 1 line.")
        }

        val cloned = hologram.location.clone()

        val line: ILine<*> = lines[0]

        line.pvt.setLocation(cloned)
        hologram.lines.add(line)
    }

    override fun teleport(hologram: Hologram) {
        val line: ILine<*> = hologram.lines[0]

        line.pvt.setLocation(hologram.location.clone())
        hologram.pvt.seeingPlayers.forEach(line.pvt::teleport)
    }
}
