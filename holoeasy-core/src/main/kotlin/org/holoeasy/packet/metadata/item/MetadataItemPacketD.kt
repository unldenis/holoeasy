package org.holoeasy.packet.metadata.item

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.utility.MinecraftVersion
import com.comphenix.protocol.wrappers.WrappedChatComponent
import com.comphenix.protocol.wrappers.WrappedDataValue
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject
import com.google.common.collect.Lists
import org.bukkit.inventory.ItemStack
import org.holoeasy.ext.bukkitGeneric
import org.holoeasy.ext.parse119
import org.holoeasy.ext.setBool
import org.holoeasy.util.BOOL_SERIALIZER
import org.holoeasy.util.ITEM_SERIALIZER
import org.holoeasy.util.VersionEnum
import java.util.*


object MetadataItemPacketD : IMetadataItemPacket {
    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_19..VersionEnum.V1_19)

    override fun metadata(entityId: Int, item: ItemStack): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.ENTITY_METADATA)
        packet.integers.write(0, entityId)

        val watcher = WrappedDataWatcher()

        val gravity = WrappedDataWatcherObject(
            5, BOOL_SERIALIZER
        )
        watcher.setObject(gravity,true)

        val itemSer = WrappedDataWatcherObject(
            8, ITEM_SERIALIZER
        )
        watcher.setObject(itemSer, item.bukkitGeneric())

        // https://www.spigotmc.org/threads/unable-to-modify-entity-metadata-packet-using-protocollib-1-19-3.582442/#post-4517187
        packet.parse119(watcher)

        return packet
    }

}