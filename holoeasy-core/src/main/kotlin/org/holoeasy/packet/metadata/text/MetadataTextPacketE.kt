package org.holoeasy.packet.metadata.text

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedChatComponent
import com.comphenix.protocol.wrappers.WrappedDataValue
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import org.holoeasy.util.BOOL_SERIALIZER
import org.holoeasy.util.BYTE_SERIALIZER
import org.holoeasy.util.VersionEnum
import java.util.*

object MetadataTextPacketE : IMetadataTextPacket {
    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_20..VersionEnum.LATEST)

    override fun metadata(entityId: Int, nameTag: String?, invisible : Boolean): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.ENTITY_METADATA)
        packet.integers.write(0, entityId)

        val watcher = WrappedDataWatcher()

        packet.watchableCollectionModifier.write(0, watcher.watchableObjects)
        val wrappedDataValueList: MutableList<WrappedDataValue> = ArrayList()



        if(invisible) {
            wrappedDataValueList.add(
                WrappedDataValue(0, BYTE_SERIALIZER, 0x20.toByte())
            )
        }


        nameTag?.let {
            val opt: Optional<*> = Optional.of(
                WrappedChatComponent.fromChatMessage(
                    it
                )[0].handle
            )

            wrappedDataValueList.add(
                WrappedDataValue(
                    2, WrappedDataWatcher.Registry.getChatComponentSerializer(true),
                    opt
                )
            )

            wrappedDataValueList.add(
                WrappedDataValue(3, BOOL_SERIALIZER, true)
            )
        }


        packet.dataValueCollectionModifier.write(0, wrappedDataValueList)

        return packet
    }


}