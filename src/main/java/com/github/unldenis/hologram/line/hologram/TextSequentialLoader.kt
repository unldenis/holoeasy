package com.github.unldenis.hologram.line.hologram

import com.github.unldenis.hologram.Hologram
import com.github.unldenis.hologram.line.ILine
import com.github.unldenis.hologram.line.ITextLine
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
class TextSequentialLoader : IHologramLoader {
    override fun load(hologram: Hologram, lines: Array<out ILine<*>>) {
        set(hologram, lines, true)
    }

    override fun teleport(hologram: Hologram) {
        set(hologram, hologram.lines.toTypedArray(), false)
        // TODO: When teleporting, the holograms unexpectedly become distant. Understand why.
    }

    private fun set(hologram: Hologram, lines: Array<ILine<*>>, add: Boolean) {
        val cloned = hologram.location.clone()
        for (line in lines) {
            when (line.getType()) {
                ILine.Type.TEXT_LINE, ILine.Type.TEXT_ANIMATED_LINE, ILine.Type.CLICKABLE_TEXT_LINE -> {
                    val tL = (line as ITextLine).asTextLine()

                    // add to lines
                    tL.setLocation(cloned.clone())

                    if (add) {
                        hologram.lines.addFirst(tL)
                    } else {
                        hologram.seeingPlayers.forEach { tL.teleport(it) }
                    }
                    cloned.z += 0.175 * tL.getObj().length
                }

                else -> throw RuntimeException("This method load supports only TextLine & TextALine & ClickableTextLine.")
            }
        }
    }
}