package com.github.unldenis.hologram.packet.equipment

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.EnumWrappers
import com.comphenix.protocol.wrappers.Pair
import com.github.unldenis.hologram.ext.compressAngle
import com.github.unldenis.hologram.ext.set
import com.github.unldenis.hologram.packet.packet
import com.github.unldenis.hologram.util.VersionEnum
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import java.util.ArrayList

object EquipmentPacketC : IEquipmentPacket {

    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() = arrayOf(VersionEnum.V1_13..VersionEnum.V1_20)

    override fun equipment(entityID: Int, helmet: ItemStack): PacketContainer {
        val pairList: MutableList<Pair<EnumWrappers.ItemSlot, ItemStack>> = ArrayList()
        pairList.add(
            Pair(
                EnumWrappers.ItemSlot.HEAD,
                helmet
            )
        )

        return packet(PacketType.Play.Server.ENTITY_EQUIPMENT) {
            integers[0] = entityID
            slotStackPairLists[0] = pairList
        }
    }

}