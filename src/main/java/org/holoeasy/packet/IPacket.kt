package org.holoeasy.packet


import org.holoeasy.HoloEasy
import org.holoeasy.packet.delete.DeletePacketA
import org.holoeasy.packet.delete.DeletePacketB
import org.holoeasy.packet.delete.IDeletePacket
import org.holoeasy.packet.metadata.item.*
import org.holoeasy.packet.metadata.text.*
import org.holoeasy.packet.spawn.*
import org.holoeasy.packet.teleport.ITeleportPacket
import org.holoeasy.packet.teleport.TeleportPacketA
import org.holoeasy.packet.teleport.TeleportPacketB
import org.holoeasy.packet.velocity.IVelocityPacket
import org.holoeasy.packet.velocity.VelocityPacketA
import org.holoeasy.util.VersionEnum
import org.holoeasy.util.VersionUtil
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

            if (HoloEasy.useLastSupportedVersion) {
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
            val METADATA_TEXT = Type(
                IMetadataTextPacket::class,
                MetadataTextPacketA,
                MetadataTextPacketB,
                MetadataTextPacketC,
                MetadataTextPacketD
            )

            @JvmField
            val METADATA_ITEM = Type(
                IMetadataItemPacket::class,
                MetadataItemPacketA,
                MetadataItemPacketB,
                MetadataItemPacketC,
                MetadataItemPacketD
            )

            @JvmField
            val SPAWN = Type(ISpawnPacket::class, SpawnPacketA, SpawnPacketB, SpawnPacketC, SpawnPacketD)

            @JvmField
            val TELEPORT = Type(ITeleportPacket::class, TeleportPacketA, TeleportPacketB)

            @JvmField
            val VELOCITY = Type(IVelocityPacket::class, VelocityPacketA)
        }

    }


}