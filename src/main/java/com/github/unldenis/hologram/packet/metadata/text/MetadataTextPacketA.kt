package com.github.unldenis.hologram.packet.metadata.text

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import com.github.unldenis.hologram.ext.set
import com.github.unldenis.hologram.packet.packet
import com.github.unldenis.hologram.util.VersionEnum

object MetadataTextPacketA : IMetadataTextPacket {
    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_8..VersionEnum.V1_8)

    override fun metadata(entityId: Int, nameTag: String): PacketContainer {
        val watcher = WrappedDataWatcher()

        watcher.setObject(0, 0x20.toByte())

        watcher.setObject(2, nameTag)
        watcher.setObject(3, 1.toByte())

        return packet(PacketType.Play.Server.ENTITY_METADATA) {
            integers[0] = entityId
            watchableCollectionModifier[0] = watcher.watchableObjects
        }
    }


}