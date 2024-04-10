package org.holoeasy.util

import com.comphenix.protocol.wrappers.WrappedDataWatcher

val BYTE_SERIALIZER : WrappedDataWatcher.Serializer
    get() = WrappedDataWatcher.Registry.get(java.lang.Byte::class.java)

val BOOL_SERIALIZER : WrappedDataWatcher.Serializer
    get() = WrappedDataWatcher.Registry.get(java.lang.Boolean::class.java)

val STRING_SERIALIZER : WrappedDataWatcher.Serializer
    get() = WrappedDataWatcher.Registry.get(java.lang.String::class.java)

val ITEM_SERIALIZER : WrappedDataWatcher.Serializer
    get() = WrappedDataWatcher.Registry.getItemStackSerializer(false)

