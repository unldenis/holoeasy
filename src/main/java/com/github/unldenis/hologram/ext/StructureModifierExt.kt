package com.github.unldenis.hologram.ext

import com.comphenix.protocol.reflect.StructureModifier

operator fun <T> StructureModifier<T>.set(index: Int, value: T) {
    write(index, value)
}