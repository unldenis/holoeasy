package org.holoeasy.packet


import org.holoeasy.HoloEasy
import org.holoeasy.packet.delete.DeletePacketA
import org.holoeasy.packet.delete.DeletePacketB
import org.holoeasy.packet.metadata.item.*
import org.holoeasy.packet.metadata.text.*
import org.holoeasy.packet.spawn.*
import org.holoeasy.packet.teleport.TeleportPacketA
import org.holoeasy.packet.teleport.TeleportPacketB
import org.holoeasy.packet.velocity.IVelocityPacket
import org.holoeasy.packet.velocity.VelocityPacketA
import org.holoeasy.util.VersionEnum
import org.holoeasy.util.VersionUtil

interface IPacket {

    val versionSupport: Array<out ClosedRange<VersionEnum>>

    fun isCurrentVersion(): Boolean {
        for (range in versionSupport) {
            if (VersionUtil.CLEAN_VERSION in range) {
                return true
            }
        }
        return false
    }

}


object PacketType {
    private fun <T : IPacket> getCurrImpl(vararg impls: T): T {
        val rightImpl = impls.firstOrNull(IPacket::isCurrentVersion)
        if (rightImpl != null) {
            return rightImpl as T
        }

        if (HoloEasy.useLastSupportedVersion) {
            return impls.last() as T
        }

        throw RuntimeException(
            """
            No version support for this packet
            Set HoloEasy.useLastSupportedVersion to true or
            open an issue at https://github.com/unldenis/holoeasy
            """.trimIndent()
        )
    }

    val DELETE by lazy { getCurrImpl(DeletePacketA, DeletePacketB) }

    val METADATA_TEXT by lazy {
        getCurrImpl(
            MetadataTextPacketA,
            MetadataTextPacketB,
            MetadataTextPacketC,
            MetadataTextPacketD,
            MetadataTextPacketE
        )
    }

    val METADATA_ITEM by lazy {
        getCurrImpl(
            MetadataItemPacketA,
            MetadataItemPacketB,
            MetadataItemPacketC,
            MetadataItemPacketD,
            MetadataItemPacketE
        )
    }

    val SPAWN by lazy {
        getCurrImpl(SpawnPacketA, SpawnPacketB, SpawnPacketC, SpawnPacketD)
    }

    val TELEPORT by lazy {
        getCurrImpl(TeleportPacketA, TeleportPacketB)
    }

    val VELOCITY by lazy { getCurrImpl<IVelocityPacket>(VelocityPacketA) }

}