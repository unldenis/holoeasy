package org.holoeasy.packet.metadata.text

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import org.holoeasy.ext.set
import org.holoeasy.ext.setBool
import org.holoeasy.ext.setByte
import org.holoeasy.ext.setString
import org.holoeasy.packet.packet
import org.holoeasy.util.VersionEnum

object MetadataTextPacketB : IMetadataTextPacket {
    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_9..VersionEnum.V1_12)

    override fun metadata(entityId: Int, nameTag: String?, invisible : Boolean): PacketContainer {
        val watcher = WrappedDataWatcher()

        if(invisible)
        watcher.setByte(0, 0x20.toByte())

        if(nameTag != null) {
            watcher.setString(2, nameTag)
            watcher.setBool(3, true)
        }

        return packet(PacketType.Play.Server.ENTITY_METADATA) {
            modifier.writeDefaults()
            integers[0] = entityId
            watchableCollectionModifier[0] = watcher.watchableObjects
        }
    }


}