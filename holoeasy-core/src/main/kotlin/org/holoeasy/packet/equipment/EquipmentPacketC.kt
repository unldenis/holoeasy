package org.holoeasy.packet.equipment

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot
import org.bukkit.inventory.ItemStack
import org.holoeasy.ext.set
import org.holoeasy.packet.packet
import org.holoeasy.util.VersionEnum

import com.comphenix.protocol.wrappers.Pair;
object EquipmentPacketC : IEquipmentPacket {

    override val versionSupport: Array<out ClosedRange<VersionEnum>>
        get() =  arrayOf(VersionEnum.V1_13..VersionEnum.V1_20)

    override fun equip(entityId: Int, helmet: ItemStack): PacketContainer {
        return packet(PacketType.Play.Server.ENTITY_EQUIPMENT) {
            integers[0] = entityId

            val pairList = ArrayList<Pair<ItemSlot, ItemStack>>()
            pairList.add(Pair(ItemSlot.HEAD, helmet))

            slotStackPairLists[0] = pairList
        }
    }



}