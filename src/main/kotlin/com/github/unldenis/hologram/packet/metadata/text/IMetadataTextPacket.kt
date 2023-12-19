package com.github.unldenis.hologram.packet.metadata.text

import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.packet.IPacket
import com.github.unldenis.hologram.packet.spawn.SpawnPacketA
import com.github.unldenis.hologram.packet.spawn.SpawnPacketB
import com.github.unldenis.hologram.packet.spawn.SpawnPacketC

interface IMetadataTextPacket : IPacket {

    override val impls: Array<out IPacket>
        get() = arrayOf(MetadataTextPacketA, MetadataTextPacketB,
            MetadataTextPacketC, MetadataTextPacketD)
    fun metadata(entityId : Int, nameTag : String) : PacketContainer
}