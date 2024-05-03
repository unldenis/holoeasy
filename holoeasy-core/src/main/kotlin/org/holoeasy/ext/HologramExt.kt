package org.holoeasy.ext

import org.holoeasy.hologram.Hologram
import org.holoeasy.line.ILine

operator fun Hologram.get(index: Int): ILine<*> = lineAt(index)