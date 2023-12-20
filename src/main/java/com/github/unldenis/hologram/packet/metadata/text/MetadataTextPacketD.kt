package com.github.unldenis.hologram.packet.metadata.text

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedChatComponent
import com.comphenix.protocol.wrappers.WrappedDataValue
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import com.github.unldenis.hologram.util.VersionEnum
import java.util.*

object MetadataTextPacketD : IMetadataTextPacket {
    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_20..VersionEnum.V1_20)

    override fun metadata(entityId: Int, nameTag: String?, invisible : Boolean): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.ENTITY_METADATA)
        packet.integers.write(0, entityId)

        val watcher = WrappedDataWatcher()

        packet.watchableCollectionModifier.write(0, watcher.watchableObjects)
        val wrappedDataValueList: MutableList<WrappedDataValue> = ArrayList()

        if(invisible)
        wrappedDataValueList.add(
            WrappedDataValue(0, WrappedDataWatcher.Registry.get(Byte::class.java), 0x20.toByte())
        )

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
                WrappedDataValue(3, WrappedDataWatcher.Registry.get(Boolean::class.java), true)
            )
        }


        packet.dataValueCollectionModifier.write(0, wrappedDataValueList)

        return packet
    }


}