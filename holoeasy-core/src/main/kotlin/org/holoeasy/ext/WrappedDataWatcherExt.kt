package org.holoeasy.ext

import com.comphenix.protocol.wrappers.WrappedChatComponent
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject
import org.bukkit.inventory.ItemStack
import org.holoeasy.util.BOOL_SERIALIZER
import org.holoeasy.util.BYTE_SERIALIZER
import org.holoeasy.util.ITEM_SERIALIZER
import org.holoeasy.util.STRING_SERIALIZER
import java.util.*


internal fun WrappedDataWatcher.setByte(index: Int, value: Byte) {
    val obj = WrappedDataWatcherObject(
        index,
        BYTE_SERIALIZER
    )
    this.setObject(obj, value)
}

internal fun WrappedDataWatcher.setString(index: Int, value: String) {
    this.setObject(
        WrappedDataWatcherObject(
            index,
            STRING_SERIALIZER
        ), value
    )
}

internal fun WrappedDataWatcher.setBool(index: Int, value: Boolean) {
    val obj = WrappedDataWatcherObject(
        index,
        BOOL_SERIALIZER
    )
    this.setObject(obj, value)
}

internal fun WrappedDataWatcher.setVectorSerializer(index: Int, value: Any) {
    val obj = WrappedDataWatcherObject(
        index,
        WrappedDataWatcher.Registry.getVectorSerializer()
    )
    this.setObject(obj, value)
}

internal fun WrappedDataWatcher.setChatComponent(index: Int, value: String) {
    val opt: Optional<*> = Optional.of(WrappedChatComponent.fromChatMessage(value)[0].handle)
    this.setObject(
        WrappedDataWatcherObject(
            index,
            WrappedDataWatcher.Registry.getChatComponentSerializer(true)
        ), opt
    )
}

internal fun WrappedDataWatcher.setItemStack(index: Int, value: ItemStack) {
    val obj = WrappedDataWatcherObject(index, ITEM_SERIALIZER)
    this.setObject(obj, value.bukkitGeneric())
}