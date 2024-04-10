package org.holoeasy.packet.metadata.item

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import org.bukkit.inventory.ItemStack
import org.holoeasy.ext.bukkitGeneric
import org.holoeasy.ext.set
import org.holoeasy.ext.setByte
import org.holoeasy.packet.packet
import org.holoeasy.util.VersionEnum


object MetadataItemPacketA : IMetadataItemPacket {
    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_8..VersionEnum.V1_8)

    override fun metadata(entityId: Int, item: ItemStack): PacketContainer {
        val watcher = WrappedDataWatcher()


//        watcher.setObject(5, true)
//        watcher.setObject(15, 1.toByte())
        watcher.setObject(10, item.bukkitGeneric())


        return packet(PacketType.Play.Server.ENTITY_METADATA) {
            integers[0] = entityId
            watchableCollectionModifier[0] = watcher.watchableObjects
        }
    }


}