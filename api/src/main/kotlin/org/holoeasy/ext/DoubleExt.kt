package org.holoeasy.ext

import kotlin.math.floor

val Double.compressAngle: Byte
    get() = (this * 256f / 360f).toInt().toByte()

val Double.fixCoordinate: Int
    get() = floor(this * 32.0).toInt()
