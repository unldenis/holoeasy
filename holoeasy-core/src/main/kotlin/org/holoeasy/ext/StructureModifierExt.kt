package org.holoeasy.ext

import com.comphenix.protocol.reflect.StructureModifier

internal operator fun <T> StructureModifier<T>.set(index: Int, value: T) {
    write(index, value)
}