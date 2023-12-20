package com.github.unldenis.hologram.packet.metadata.item

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.utility.MinecraftReflection
import com.comphenix.protocol.wrappers.BukkitConverters
import com.comphenix.protocol.wrappers.WrappedDataValue
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import com.github.unldenis.hologram.util.VersionEnum
import org.bukkit.inventory.ItemStack
import java.util.*


object MetadataItemPacketA : IMetadataItemPacket {
    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_8..VersionEnum.V1_20)

    override fun metadata(entityId: Int, item: ItemStack): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.ENTITY_METADATA)

        packet.integers.write(0, entityId)

        packet.dataValueCollectionModifier
            .write(
                0, listOf(
                    WrappedDataValue(8, WrappedDataWatcher.Registry.getItemStackSerializer(false),
                        BukkitConverters.getItemStackConverter().getGeneric(item))
                )
            )

        //gravity

        return packet
    }


}