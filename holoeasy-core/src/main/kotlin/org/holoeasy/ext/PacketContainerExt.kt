package org.holoeasy.ext

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.utility.MinecraftVersion
import com.comphenix.protocol.wrappers.WrappedDataValue
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import com.google.common.collect.Lists
import org.bukkit.entity.Player
import java.util.*

internal fun PacketContainer.send(player: Player) {
    ProtocolLibrary.getProtocolManager().sendServerPacket(player, this)
}

internal operator fun PacketContainer.invoke(player: Player) {
    send(player)
}

internal fun PacketContainer.parse119(watcher: WrappedDataWatcher) {
    if (MinecraftVersion.getCurrentVersion().isAtLeast(MinecraftVersion("1.19.3"))) {
        val wrappedDataValueList: MutableList<WrappedDataValue> = Lists.newArrayList()
        watcher.watchableObjects.stream().filter(Objects::nonNull).forEach { entry ->
            val dataWatcherObject: WrappedDataWatcher.WrappedDataWatcherObject = entry.watcherObject
            wrappedDataValueList.add(
                WrappedDataValue(
                    dataWatcherObject.index,
                    dataWatcherObject.serializer,
                    entry.rawValue
                )
            )
        }
        dataValueCollectionModifier.write(0, wrappedDataValueList)
    } else {
        watchableCollectionModifier.write(0, watcher.watchableObjects)
    }
}