package org.holoeasy.ext

import org.holoeasy.hologram.Hologram
import org.holoeasy.line.ILine

operator fun <T : ILine<*>> Hologram.get(index: Int): T = lineAt(index)