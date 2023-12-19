package com.github.unldenis.hologram.packet.rotate

import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.packet.IPacket
import com.github.unldenis.hologram.packet.spawn.SpawnPacketA
import com.github.unldenis.hologram.packet.spawn.SpawnPacketB
import com.github.unldenis.hologram.packet.spawn.SpawnPacketC
import org.bukkit.Location

interface IRotatePacket : IPacket {

    override val impls: Array<out IPacket>
        get() = arrayOf(RotatePacketA, RotatePacketB)

    fun rotate(entityId: Int, from: Location, yaw: Float) : Array<PacketContainer>
}