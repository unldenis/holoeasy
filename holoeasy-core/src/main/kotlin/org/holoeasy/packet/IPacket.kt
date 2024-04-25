package org.holoeasy.packet


import com.comphenix.protocol.events.PacketContainer
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
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

    fun isCurrentVersion(): Boolean {
        for (range in versionSupport) {
            if (VersionUtil.CLEAN_VERSION in range) {
                return true
            }
        }
        return false
    }

}

sealed class PacketType<T : IPacket>(private vararg val impls: T) {

    protected val currImpl : T by lazy {
        val rightImpl = impls.firstOrNull(IPacket::isCurrentVersion)
        if (rightImpl != null) {
            return@lazy rightImpl as T
        }

        if (HoloEasy.useLastSupportedVersion) {
            return@lazy impls.last() as T
        }

        throw RuntimeException(
            """
            No version support for this packet
            Set HoloEasy.useLastSupportedVersion to true or
            open an issue at https://github.com/unldenis/holoeasy
            """.trimIndent()
        )
    }

    data object DELETE : PacketType<IDeletePacket>(DeletePacketA, DeletePacketB)
        , IDeletePacket {
        override fun delete(entityId: Int): PacketContainer {
            return currImpl.delete(entityId)
        }

        override val versionSupport: Array<out ClosedRange<VersionEnum>>
            get() = currImpl.versionSupport

    }

    data object METADATA_TEXT : PacketType<IMetadataTextPacket>(MetadataTextPacketA, MetadataTextPacketB, MetadataTextPacketC, MetadataTextPacketD, MetadataTextPacketE)
        , IMetadataTextPacket {
        override fun metadata(entityId: Int, nameTag: String?, invisible: Boolean): PacketContainer {
            return currImpl.metadata(entityId, nameTag, invisible)
        }

        override val versionSupport: Array<out ClosedRange<VersionEnum>>
            get() = currImpl.versionSupport

    }

    data object METADATA_ITEM : PacketType<IMetadataItemPacket>(MetadataItemPacketA, MetadataItemPacketB, MetadataItemPacketC, MetadataItemPacketD, MetadataItemPacketE)
        , IMetadataItemPacket {
        override fun metadata(entityId: Int, item: ItemStack): PacketContainer {
            return currImpl.metadata(entityId, item)
        }

        override val versionSupport: Array<out ClosedRange<VersionEnum>>
            get() = currImpl.versionSupport

    }

    data object SPAWN : PacketType<ISpawnPacket>(SpawnPacketA, SpawnPacketB, SpawnPacketC, SpawnPacketD)
        , ISpawnPacket {
        override fun spawn(
            entityId: Int,
            entityType: EntityType,
            location: Location,
            plugin: Plugin?
        ): PacketContainer {
            return currImpl.spawn(entityId, entityType, location, plugin)
        }

        override val versionSupport: Array<out ClosedRange<VersionEnum>>
            get() = currImpl.versionSupport

    }

    data object TELEPORT : PacketType<ITeleportPacket>(TeleportPacketA, TeleportPacketB)
        , ITeleportPacket {
        override fun teleport(entityId: Int, location: Location): PacketContainer {
            return currImpl.teleport(entityId, location)
        }

        override val versionSupport: Array<out ClosedRange<VersionEnum>>
            get() = currImpl.versionSupport

    }

    data object VELOCITY : PacketType<IVelocityPacket>(VelocityPacketA)
        , IVelocityPacket {
        override fun velocity(entityId: Int, x: Int, y: Int, z: Int): PacketContainer {
            return currImpl.velocity(entityId, x, y, z)
        }

        override val versionSupport: Array<out ClosedRange<VersionEnum>>
            get() = currImpl.versionSupport


    }






}