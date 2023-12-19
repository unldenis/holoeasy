package com.github.unldenis.hologram.packet.delete

import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.packet.IPacket
import com.github.unldenis.hologram.packet.spawn.SpawnPacketA
import com.github.unldenis.hologram.packet.spawn.SpawnPacketB
import com.github.unldenis.hologram.packet.spawn.SpawnPacketC

interface IDeletePacket : IPacket {

    override val impls: Array<out IPacket>
        get() = arrayOf(DeletePacketA, DeletePacketB)

    fun delete(entityId : Int) : PacketContainer
}