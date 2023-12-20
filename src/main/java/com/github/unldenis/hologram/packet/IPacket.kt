package com.github.unldenis.hologram.packet


import com.github.unldenis.hologram.HologramLib
import com.github.unldenis.hologram.packet.delete.DeletePacketA
import com.github.unldenis.hologram.packet.delete.DeletePacketB
import com.github.unldenis.hologram.packet.delete.IDeletePacket
import com.github.unldenis.hologram.packet.equipment.EquipmentPacketA
import com.github.unldenis.hologram.packet.equipment.EquipmentPacketB
import com.github.unldenis.hologram.packet.equipment.EquipmentPacketC
import com.github.unldenis.hologram.packet.equipment.IEquipmentPacket
import com.github.unldenis.hologram.packet.metadata.item.IMetadataItemPacket
import com.github.unldenis.hologram.packet.metadata.item.MetadataItemPacketA
import com.github.unldenis.hologram.packet.metadata.text.*
import com.github.unldenis.hologram.packet.rotate.IRotatePacket
import com.github.unldenis.hologram.packet.rotate.RotatePacketA
import com.github.unldenis.hologram.packet.rotate.RotatePacketB
import com.github.unldenis.hologram.packet.spawn.ISpawnPacket
import com.github.unldenis.hologram.packet.spawn.SpawnPacketA
import com.github.unldenis.hologram.packet.spawn.SpawnPacketB
import com.github.unldenis.hologram.packet.spawn.SpawnPacketC
import com.github.unldenis.hologram.packet.teleport.ITeleportPacket
import com.github.unldenis.hologram.packet.teleport.TeleportPacketA
import com.github.unldenis.hologram.packet.teleport.TeleportPacketB
import com.github.unldenis.hologram.util.VersionEnum
import com.github.unldenis.hologram.util.VersionUtil
import kotlin.reflect.KClass

interface IPacket {

    val versionSupport: Array<out ClosedRange<VersionEnum>>

    private fun isCurrentVersion(): Boolean {
        for (range in versionSupport) {
            if (VersionUtil.CLEAN_VERSION in range) {
                return true
            }
        }
        return false
    }

    companion object {

        private val cache = mutableMapOf<Type<*>, IPacket>()

        @JvmStatic
        fun <T : IPacket> get(type: Type<T>): T {
            val cached = cache[type]
            if (cached != null) {
                return cached as T
            }

            val rightImpl = type.impls.firstOrNull(IPacket::isCurrentVersion)
            if (rightImpl != null) {
                cache[type] = rightImpl
                return rightImpl as T
            }

            if (HologramLib.useLastSupportedVersion) {
                return type.impls.last() as T
            }

            throw RuntimeException(
                """
                No version support for ${type.abs.simpleName} packet
                Set HologramLib.useLastSupportedVersion to true or
                open an issue at https://github.com/unldenis/Hologram-Lib
            """.trimIndent()
            )
        }

    }

    class Type<T : IPacket>(internal val abs: KClass<T>, vararg val impls: IPacket) {

        companion object {
            @JvmField
            val DELETE = Type(IDeletePacket::class, DeletePacketA, DeletePacketB)

            @JvmField
            val EQUIPMENT = Type(IEquipmentPacket::class, EquipmentPacketA, EquipmentPacketB, EquipmentPacketC)

            @JvmField
            val METADATA_TEXT = Type(
                IMetadataTextPacket::class,
                MetadataTextPacketA,
                MetadataTextPacketB,
                MetadataTextPacketC,
                MetadataTextPacketD
            )

            @JvmField
            val METADATA_ITEM = Type(IMetadataItemPacket::class, MetadataItemPacketA)

            @JvmField
            val ROTATE = Type(IRotatePacket::class, RotatePacketA, RotatePacketB)

            @JvmField
            val SPAWN = Type(ISpawnPacket::class, SpawnPacketA, SpawnPacketB, SpawnPacketC)

            @JvmField
            val TELEPORT = Type(ITeleportPacket::class, TeleportPacketA, TeleportPacketB)
        }

    }


}