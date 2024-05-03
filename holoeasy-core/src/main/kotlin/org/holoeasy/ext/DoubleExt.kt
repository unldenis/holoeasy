package org.holoeasy.ext

import kotlin.math.floor

internal val Double.compressAngle: Byte
    get() = (this * 256f / 360f).toInt().toByte()

internal val Double.fixCoordinate: Int
    get() = floor(this * 32.0).toInt()
