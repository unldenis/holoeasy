package com.github.unldenis.hologram.packet.equipment

import com.comphenix.protocol.events.PacketContainer
import com.github.unldenis.hologram.packet.IPacket
import com.github.unldenis.hologram.packet.spawn.SpawnPacketA
import com.github.unldenis.hologram.packet.spawn.SpawnPacketB
import com.github.unldenis.hologram.packet.spawn.SpawnPacketC
import org.bukkit.Location
import org.bukkit.inventory.ItemStack

interface IEquipmentPacket : IPacket {

    override val impls: Array<out IPacket>
        get() = arrayOf(EquipmentPacketA, EquipmentPacketB, EquipmentPacketC)
    fun equipment(entityID: Int, helmet: ItemStack) : PacketContainer
}