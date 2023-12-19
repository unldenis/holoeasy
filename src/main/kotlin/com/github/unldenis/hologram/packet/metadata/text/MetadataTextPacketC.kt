package com.github.unldenis.hologram.packet.metadata.text

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import com.github.unldenis.hologram.ext.*
import com.github.unldenis.hologram.packet.packet
import com.github.unldenis.hologram.util.VersionEnum

object MetadataTextPacketC : IMetadataTextPacket {
    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_13..VersionEnum.V1_19)

    override fun metadata(entityId: Int, nameTag: String): PacketContainer {
        val watcher = WrappedDataWatcher()

        watcher.setByte(0, 0x20.toByte())

        watcher.setChatComponent(2, nameTag)
        watcher.setBool(3, true)

        return packet(PacketType.Play.Server.ENTITY_METADATA) {
            integers[0] = entityId
            watchableCollectionModifier[0] = watcher.watchableObjects
        }
    }


}