package org.holoeasy.util

import com.comphenix.protocol.wrappers.WrappedDataWatcher


fun test() {

}

internal val BYTE_SERIALIZER : WrappedDataWatcher.Serializer
    get() = WrappedDataWatcher.Registry.get(java.lang.Byte::class.java)

internal val BOOL_SERIALIZER : WrappedDataWatcher.Serializer
    get() = WrappedDataWatcher.Registry.get(java.lang.Boolean::class.java)

internal val STRING_SERIALIZER : WrappedDataWatcher.Serializer
    get() = WrappedDataWatcher.Registry.get(java.lang.String::class.java)

internal val ITEM_SERIALIZER : WrappedDataWatcher.Serializer
    get() = WrappedDataWatcher.Registry.getItemStackSerializer(false)

