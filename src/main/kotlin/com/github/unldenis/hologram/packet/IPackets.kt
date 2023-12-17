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

    fun spawnPacket(entityID: Int, location: Location, plugin: Plugin): PacketContainer

    fun destroyPacket(entityID: Int): PacketContainer

    fun equipmentPacket(entityID: Int, helmet: ItemStack, itemLine: Boolean): PacketContainer

    fun metadataPacket(
        entityID: Int, nameTag: String?,
        setInvisible: Boolean, setSmall: Boolean, handRotationNMS: Any?
    ): PacketContainer

    fun metadataPacket(entityID: Int, handRotationNMS: Any?): PacketContainer {
        return metadataPacket(entityID, null, true, true, handRotationNMS)
    }

    fun teleportPacket(entityID: Int, location: Location): PacketContainer

    fun rotatePackets(entityID: Int, from: Location, yaw: Float): List<PacketContainer>

}

class PacketsV1_8 : IPackets {
    companion object {
        fun loadDefaultWatcher(plugin: Plugin): CompletableFuture<Void> {
            return BukkitFuture.runSync(plugin) {
                val world = Bukkit.getWorlds()[0]
                val entity =
                    world.spawnEntity(Location(world, 0.0, 256.0, 0.0), EntityType.ARMOR_STAND)
                defaultDataWatcher =
                    WrappedDataWatcher.getEntityWatcher(entity).deepClone()
                entity.remove()
            }
        }
        var defaultDataWatcher : WrappedDataWatcher? = null
    }
    override fun spawnPacket(entityID: Int, location: Location, plugin: Plugin): PacketContainer {
        return packet(PacketType.Play.Server.SPAWN_ENTITY) {
            integers[0] = entityID
            integers[1] = EntityType.ARMOR_STAND.typeId.toInt()
            integers[2] = (location.x * 32).toInt()
            integers[3] = (location.y * 32).toInt()
            integers[4] = (location.z * 32).toInt()

            if (defaultDataWatcher == null) {
                loadDefaultWatcher(plugin).join()
            }

            dataWatcherModifier[0] = defaultDataWatcher
        }
    }

    override fun destroyPacket(entityID: Int): PacketContainer {
        return packet(PacketType.Play.Server.ENTITY_DESTROY) {
            integerArrays[0] = intArrayOf(entityID)
        }
    }

    override fun equipmentPacket(entityID: Int, helmet: ItemStack, itemLine: Boolean): PacketContainer {
        return packet(PacketType.Play.Server.ENTITY_EQUIPMENT) {
            integers[0] = entityID
            if (itemLine) {
                integers[1] = 0
            } else {
                // Use legacy form to update the head slot.
                integers[1] = 4
            }

            itemModifier[0] = helmet

        }
    }

    override fun metadataPacket(
        entityID: Int,
        nameTag: String?,
        setInvisible: Boolean,
        setSmall: Boolean,
        handRotationNMS: Any?
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

        return packet(PacketType.Play.Server.ENTITY_METADATA) {
            integers[0] = entityID
            watchableCollectionModifier[0] = watcher.watchableObjects
        }
    }

    override fun teleportPacket(entityID: Int, location: Location): PacketContainer {
        return packet(PacketType.Play.Server.ENTITY_TELEPORT) {
            integers[0] = entityID
            integers[1] = location.x.fixCoordinate
            integers[2] = location.y.fixCoordinate
            integers[3] = location.z.fixCoordinate
            bytes[0] = location.yaw.toDouble().compressAngle
            bytes[1] = location.pitch.toDouble().compressAngle
            booleans[0] = false
        }

//        old api, look difference
//        val packet = PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT)
//        packet.integers.write(0, entityID)
//        packet.integers.write(1, location.x.fixCoordinate)
//        packet.integers.write(2, location.y.fixCoordinate)
//        packet.integers.write(3, location.z.fixCoordinate)
//        packet.bytes.write(0, location.yaw.toDouble().compressAngle)
//        packet.bytes.write(1, location.pitch.toDouble().compressAngle)
//        packet.booleans.write(0, false)
//        return packet
    }

    override fun rotatePackets(entityID: Int, from: Location, yaw: Float): List<PacketContainer> {
        val rotationPacket = packet(PacketType.Play.Server.ENTITY_HEAD_ROTATION) {
            integers[0] = entityID
            bytes[0] = yaw.toDouble().compressAngle
        }

        val teleportPacket = packet(PacketType.Play.Server.ENTITY_TELEPORT) {
            integers[0] = entityID

            integers[1] = from.x.fixCoordinate
            integers[2] = from.y.fixCoordinate
            integers[3] = from.z.fixCoordinate

            bytes[0] = yaw.toDouble().compressAngle
            bytes[1] = 0.toByte()

            booleans[0] = true
        }

        return listOf(rotationPacket, teleportPacket)
    }

}

open class PacketsV1_9V1_12 : IPackets {
    override fun spawnPacket(entityID: Int, location: Location, plugin: Plugin): PacketContainer {
        val entityType = 30
        val extraData = 1

        return packet(PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
            modifier.writeDefaults()

            integers[0] = entityID
            integers[1] = entityType
            integers[2] = extraData

            uuiDs[0] = UUID.randomUUID()

            doubles[0] = location.x
            doubles[1] = location.y
            doubles[2] = location.z
        }

    }

    override fun destroyPacket(entityID: Int): PacketContainer {
        return packet(PacketType.Play.Server.ENTITY_DESTROY) {
            integerArrays[0] = intArrayOf(entityID)
        }
    }

    override fun equipmentPacket(entityID: Int, helmet: ItemStack, itemLine: Boolean): PacketContainer {
        return packet(PacketType.Play.Server.ENTITY_EQUIPMENT) {
            integers[0] = entityID
            itemSlots[0] = if(itemLine) ItemSlot.MAINHAND else ItemSlot.HEAD
            itemModifier[0] = helmet
        }
    }

    override fun metadataPacket(
        entityID: Int,
        nameTag: String?,
        setInvisible: Boolean,
        setSmall: Boolean,
        handRotationNMS: Any?
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

        return packet(PacketType.Play.Server.ENTITY_METADATA) {
            modifier.writeDefaults()
            integers[0] = entityID
            watchableCollectionModifier[0] = watcher.watchableObjects
        }

    }

    override fun teleportPacket(entityID: Int, location: Location): PacketContainer {
        val teleportPacket = packet(PacketType.Play.Server.ENTITY_TELEPORT) {
            integers[0] = entityID

            doubles[0] = location.x
            doubles[1] = location.y
            doubles[2] = location.z

            bytes[0] = location.yaw.toDouble().compressAngle
            bytes[1] = location.pitch.toDouble().compressAngle

            booleans[0] = false
        }

        return teleportPacket
    }

    override fun rotatePackets(entityID: Int, from: Location, yaw: Float): List<PacketContainer> {
        val packet = packet(PacketType.Play.Server.ENTITY_LOOK) {
            integers[0] = entityID
            bytes[0] = yaw.toDouble().compressAngle
            bytes[1] = 0.toByte()
            booleans[0] = true
        }
        return listOf(packet)
    }

}

open class PacketsV1_13V1_16 : PacketsV1_9V1_12() {


    override fun equipmentPacket(entityID: Int, helmet: ItemStack, itemLine: Boolean): PacketContainer {
        val pairList: MutableList<Pair<ItemSlot, ItemStack>> = ArrayList()
        pairList.add(
            Pair(
                if (itemLine) ItemSlot.MAINHAND else ItemSlot.HEAD,
                helmet
            )
        )

        return packet(PacketType.Play.Server.ENTITY_EQUIPMENT) {
            integers[0] = entityID
            slotStackPairLists[0] = pairList
        }
    }

    override fun metadataPacket(
        entityID: Int,
        nameTag: String?,
        setInvisible: Boolean,
        setSmall: Boolean,
        handRotationNMS: Any?
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

        return packet(PacketType.Play.Server.ENTITY_METADATA) {
            integers[0] = entityID
            watchableCollectionModifier[0] = watcher.watchableObjects
        }
    }

}

open class PacketsV1_17_V18 : PacketsV1_13V1_16() {

    override fun destroyPacket(entityID: Int): PacketContainer {
        return packet(PacketType.Play.Server.ENTITY_DESTROY) {
            intLists[0] = listOf(entityID)
        }
    }

}

open class PacketsV1_19 : PacketsV1_17_V18() {
    override fun spawnPacket(entityID: Int, location: Location, plugin: Plugin): PacketContainer {
        val extraData = 1
        return packet(PacketType.Play.Server.SPAWN_ENTITY) {
            integers[0] = entityID
            integers[1] = extraData

            entityTypeModifier[0] = EntityType.ARMOR_STAND

            uuiDs[0] = UUID.randomUUID()

            doubles[0] = location.x
            doubles[1] = location.y
            doubles[2] = location.z
        }
    }


    override fun metadataPacket(
        entityID: Int,
        nameTag: String?,
        setInvisible: Boolean,
        setSmall: Boolean,
        handRotationNMS: Any?
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
        handRotationNMS: Any?
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


        packet.dataValueCollectionModifier.write(0, wrappedDataValueList)

        return packet
    }
}