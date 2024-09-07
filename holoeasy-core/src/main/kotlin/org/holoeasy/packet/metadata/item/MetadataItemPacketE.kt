package org.holoeasy.packet.metadata.item

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedDataValue
import org.bukkit.inventory.ItemStack
import org.holoeasy.ext.bukkitGeneric
import org.holoeasy.util.BOOL_SERIALIZER
import org.holoeasy.util.ITEM_SERIALIZER
import org.holoeasy.util.VersionEnum


object MetadataItemPacketE : IMetadataItemPacket {
    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_19..VersionEnum.LATEST)

    override fun metadata(entityId: Int, item: ItemStack): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.ENTITY_METADATA)

        packet.integers.write(0, entityId)

        packet.dataValueCollectionModifier
            .write(
                0, listOf(
                    WrappedDataValue(5, BOOL_SERIALIZER, true),

                    WrappedDataValue(8, ITEM_SERIALIZER, item.bukkitGeneric())
                )
            )


        return packet
    }


}