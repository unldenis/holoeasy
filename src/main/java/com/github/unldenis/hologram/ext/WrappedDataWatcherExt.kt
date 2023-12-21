package com.github.unldenis.hologram.ext

import com.comphenix.protocol.wrappers.WrappedChatComponent
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject
import com.github.unldenis.hologram.util.BOOL_SERIALIZER
import com.github.unldenis.hologram.util.BYTE_SERIALIZER
import com.github.unldenis.hologram.util.ITEM_SERIALIZER
import com.github.unldenis.hologram.util.STRING_SERIALIZER
import org.bukkit.inventory.ItemStack
import java.util.*


fun WrappedDataWatcher.setByte(index: Int, value: Byte) {
    val obj = WrappedDataWatcherObject(
        index,
        BYTE_SERIALIZER
    )
    this.setObject(obj, value)
}

fun WrappedDataWatcher.setString(index: Int, value: String) {
    this.setObject(
        WrappedDataWatcherObject(
            index,
            STRING_SERIALIZER
        ), value
    )
}

fun WrappedDataWatcher.setBool(index: Int, value: Boolean) {
    val obj = WrappedDataWatcherObject(
        index,
        BOOL_SERIALIZER
    )
    this.setObject(obj, value)
}

fun WrappedDataWatcher.setVectorSerializer(index: Int, value: Any) {
    val obj = WrappedDataWatcherObject(
        index,
        WrappedDataWatcher.Registry.getVectorSerializer()
    )
    this.setObject(obj, value)
}

fun WrappedDataWatcher.setChatComponent(index: Int, value: String) {
    val opt: Optional<*> = Optional.of(WrappedChatComponent.fromChatMessage(value)[0].handle)
    this.setObject(
        WrappedDataWatcherObject(
            index,
            WrappedDataWatcher.Registry.getChatComponentSerializer(true)
        ), opt
    )
}

fun WrappedDataWatcher.setItemStack(index: Int, value: ItemStack) {
    val obj = WrappedDataWatcherObject(index, ITEM_SERIALIZER)
    this.setObject(obj, value.bukkitGeneric())
}