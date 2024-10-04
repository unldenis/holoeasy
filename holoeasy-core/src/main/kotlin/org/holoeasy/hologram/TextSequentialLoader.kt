package org.holoeasy.hologram


import org.holoeasy.line.ILine
import org.holoeasy.line.ITextLine
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
class TextSequentialLoader : IHologramLoader {
    override fun load(hologram: Hologram, lines: List<ILine<*>>) {
        set(hologram, lines, true)
    }

    override fun teleport(hologram: Hologram) {
        set(hologram, hologram.lines, false)
        // TODO: When teleporting, the holograms unexpectedly become distant. Understand why.
    }

    private fun set(hologram: Hologram, lines: List<ILine<*>>, add: Boolean) {
        val cloned = hologram.location.clone()
        for (line in lines) {
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
}
