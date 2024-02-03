package org.holoeasy.packet.metadata.text

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import org.holoeasy.ext.set
import org.holoeasy.ext.setBool
import org.holoeasy.ext.setByte
import org.holoeasy.ext.setChatComponent
import org.holoeasy.packet.packet
import org.holoeasy.util.VersionEnum

object MetadataTextPacketC : IMetadataTextPacket {
    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_13..VersionEnum.V1_18)

    override fun metadata(entityId: Int, nameTag: String?, invisible : Boolean): PacketContainer {
        val watcher = WrappedDataWatcher()

        if(invisible)
        watcher.setByte(0, 0x20.toByte())

        if(nameTag != null) {
            watcher.setChatComponent(2, nameTag)
            watcher.setBool(3, true)
        }
        return packet(PacketType.Play.Server.ENTITY_METADATA) {
            integers[0] = entityId
            watchableCollectionModifier[0] = watcher.watchableObjects
        }
    }


}