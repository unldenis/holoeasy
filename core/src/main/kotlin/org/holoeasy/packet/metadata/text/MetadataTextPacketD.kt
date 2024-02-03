package org.holoeasy.packet.metadata.text

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import org.holoeasy.ext.*
import org.holoeasy.packet.packet
import org.holoeasy.util.VersionEnum

object MetadataTextPacketD : IMetadataTextPacket {
    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_19..VersionEnum.V1_19)

    override fun metadata(entityId: Int, nameTag: String?, invisible : Boolean): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.ENTITY_METADATA)
        packet.integers.write(0, entityId)

        val watcher = WrappedDataWatcher()

        if(invisible)
        watcher.setByte(0, 0x20.toByte())

        if(nameTag != null) {
            watcher.setChatComponent(2, nameTag)
            watcher.setBool(3, true)
        }

        // https://www.spigotmc.org/threads/unable-to-modify-entity-metadata-packet-using-protocollib-1-19-3.582442/#post-4517187
        packet.parse119(watcher)

        return packet
    }


}