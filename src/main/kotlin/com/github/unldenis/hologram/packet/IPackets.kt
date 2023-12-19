package com.github.unldenis.hologram.packet

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot
import com.comphenix.protocol.wrappers.Pair
import com.comphenix.protocol.wrappers.WrappedChatComponent
import com.comphenix.protocol.wrappers.WrappedDataValue
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject
import com.github.unldenis.hologram.ext.*
import com.github.unldenis.hologram.util.BukkitFuture
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.math.floor

interface IPackets {




    fun metadataPacket(
        entityID: Int,
        nameTag: String?,
        setInvisible: Boolean,
        setSmall: Boolean,
        handRotationNMS: Any?,
        itemEntity: ItemStack?
    ): PacketContainer

    fun metadataPacket(entityID: Int, handRotationNMS: Any?, itemEntity: ItemStack?): PacketContainer {
        return metadataPacket(entityID, null, true, true, handRotationNMS, itemEntity)
    }


}

class PacketsV1_8 : IPackets {




    override fun metadataPacket(
        entityID: Int,
        nameTag: String?,
        setInvisible: Boolean,
        setSmall: Boolean,
        handRotationNMS: Any?,
        itemEntity: ItemStack?
    ): PacketContainer {
        val watcher = WrappedDataWatcher()
        if (setInvisible) {
            watcher.setObject(0, 0x20.toByte())
        }
        if (nameTag != null) {
            watcher.setObject(2, nameTag)
            watcher.setObject(3, 1.toByte())
        }
        if (setSmall) {
            watcher.setObject(10, (0x01 or 0x04).toByte())
        }
        if (handRotationNMS != null) {
            watcher.setObject(19, handRotationNMS)
        }

        itemEntity?.let {
            watcher.setItemStack(10, it)
        }

        return packet(PacketType.Play.Server.ENTITY_METADATA) {
            integers[0] = entityID
            watchableCollectionModifier[0] = watcher.watchableObjects
        }
    }


}

open class PacketsV1_9V1_12 : IPackets {


    override fun metadataPacket(
        entityID: Int,
        nameTag: String?,
        setInvisible: Boolean,
        setSmall: Boolean,
        handRotationNMS: Any?,
        itemEntity: ItemStack?
    ): PacketContainer {

        val watcher = WrappedDataWatcher()
        if (setInvisible) {
            watcher.setByte(0, 0x20.toByte())
        }
        if (nameTag != null) {
            watcher.setString(2, nameTag)

            watcher.setBool(3, true)
        }
        if (setSmall) {
            watcher.setByte(10, (0x01 or 0x04).toByte())
        }
        if (handRotationNMS != null) {
            watcher.setVectorSerializer(19, handRotationNMS)
        }

        itemEntity?.let {
            watcher.setItemStack(6, it)
        }

        return packet(PacketType.Play.Server.ENTITY_METADATA) {
            modifier.writeDefaults()
            integers[0] = entityID
            watchableCollectionModifier[0] = watcher.watchableObjects
        }

    }


}

open class PacketsV1_13V1_16 : PacketsV1_9V1_12() {


    override fun metadataPacket(
        entityID: Int,
        nameTag: String?,
        setInvisible: Boolean,
        setSmall: Boolean,
        handRotationNMS: Any?,
        itemEntity: ItemStack?
    ): PacketContainer {

        val watcher = WrappedDataWatcher()
        if (setInvisible) {
            watcher.setByte(0, 0x20.toByte())
        }
        if (nameTag != null) {
            watcher.setChatComponent(2, nameTag)

            watcher.setBool(3, true)
        }
        if (setSmall) {
            watcher.setByte(15, (0x01 or 0x04).toByte())
        }
        if (handRotationNMS != null) {
            watcher.setVectorSerializer(19, handRotationNMS)
        }

        itemEntity?.let {
            watcher.setItemStack(7, it)
        }

        return packet(PacketType.Play.Server.ENTITY_METADATA) {
            integers[0] = entityID
            watchableCollectionModifier[0] = watcher.watchableObjects
        }
    }

}

open class PacketsV1_17_V18 : PacketsV1_13V1_16() {


}

open class PacketsV1_19 : PacketsV1_17_V18() {


    override fun metadataPacket(
        entityID: Int,
        nameTag: String?,
        setInvisible: Boolean,
        setSmall: Boolean,
        handRotationNMS: Any?,
        itemEntity: ItemStack?
    ): PacketContainer {
        val watcher = WrappedDataWatcher()

        if (setInvisible) {
            watcher.setByte(0, 0x20.toByte())
        }
        if (nameTag != null) {
            watcher.setChatComponent(2, nameTag)
            watcher.setBool(3, true)
        }
        if (setSmall) {
            watcher.setByte(15, (0x01 or 0x04).toByte())
        }
        if (handRotationNMS != null) {
            watcher.setVectorSerializer(19, handRotationNMS)
        }

        itemEntity?.let {
            watcher.setItemStack(8, it)
        }

        return packet(PacketType.Play.Server.ENTITY_METADATA) {
            integers[0] = entityID
            watchableCollectionModifier[0] = watcher.watchableObjects
        }
    }


}


class PacketsV1_20 : PacketsV1_19() {
    override fun metadataPacket(
        entityID: Int,
        nameTag: String?,
        setInvisible: Boolean,
        setSmall: Boolean,
        handRotationNMS: Any?,
        itemEntity: ItemStack?
    ): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.ENTITY_METADATA)
        packet.integers.write(0, entityID)

        val watcher = WrappedDataWatcher()

        packet.watchableCollectionModifier.write(0, watcher.watchableObjects)
        val wrappedDataValueList: MutableList<WrappedDataValue> = ArrayList()

        if (setInvisible) {
            wrappedDataValueList.add(
                WrappedDataValue(0, WrappedDataWatcher.Registry.get(Byte::class.java), 0x20.toByte())
            )
        }

        nameTag?.let {
            val opt: Optional<*> = Optional.of(
                WrappedChatComponent.fromChatMessage(
                    it
                )[0].handle
            )

            wrappedDataValueList.add(
                WrappedDataValue(
                    2, WrappedDataWatcher.Registry.getChatComponentSerializer(true),
                    opt
                )
            )

            wrappedDataValueList.add(
                WrappedDataValue(3, WrappedDataWatcher.Registry.get(Boolean::class.java), true)
            )
        }

        if (setSmall) {
            wrappedDataValueList.add(
                WrappedDataValue(
                    15, WrappedDataWatcher.Registry.get(Byte::class.java),
                    (0x01 or 0x04).toByte()
                )
            )
        }
        handRotationNMS?.let {
            wrappedDataValueList.add(
                WrappedDataValue(
                    19, WrappedDataWatcher.Registry.getVectorSerializer(),
                    it
                )
            )
        }

        itemEntity?.let {
            wrappedDataValueList.add(
                WrappedDataValue(
                    8,  WrappedDataWatcher.Registry.get(ItemStack::class.java),
                    it
                )
            )
        }


        packet.dataValueCollectionModifier.write(0, wrappedDataValueList)

        return packet
    }
}