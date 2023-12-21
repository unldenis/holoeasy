package com.github.unldenis.hologram.packet.metadata.item

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import com.github.unldenis.hologram.ext.*
import com.github.unldenis.hologram.packet.packet
import com.github.unldenis.hologram.util.VersionEnum
import org.bukkit.inventory.ItemStack


object MetadataItemPacketC : IMetadataItemPacket {
    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_13..VersionEnum.V1_19)

    override fun metadata(entityId: Int, item: ItemStack): PacketContainer {
        val watcher = WrappedDataWatcher()

        watcher.setBool(5, true)
        watcher.setItemStack(7, item)

        return packet(PacketType.Play.Server.ENTITY_METADATA) {
            integers[0] = entityId
            watchableCollectionModifier[0] = watcher.watchableObjects
        }
    }


}