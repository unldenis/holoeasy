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

fun PacketContainer.send(player: Player) {
    ProtocolLibrary.getProtocolManager().sendServerPacket(player, this)
}

interface IPackets {

    fun getCompressAngle(angle: Double): Byte {
        return (angle * 256f / 360f).toInt().toByte()
    }

    fun fixCoordinate(v: Double): Int {
        return floor(v * 32.0).toInt()
    }

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
        val packet = PacketContainer(PacketType.Play.Server.SPAWN_ENTITY)

        packet.integers.write(0, entityID)
        packet.integers.write(1, EntityType.ARMOR_STAND.typeId.toInt())
        packet.integers.write(2, (location.x * 32).toInt())
        packet.integers.write(3, (location.y * 32).toInt())
        packet.integers.write(4, (location.z * 32).toInt())
        if (defaultDataWatcher == null) {
            loadDefaultWatcher(plugin).join()
        }
        packet.dataWatcherModifier.write(0, PacketsV1_8.defaultDataWatcher)

        return packet

    }

    override fun destroyPacket(entityID: Int): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.ENTITY_DESTROY)
        packet.integerArrays.write(0, intArrayOf(entityID))
        return packet
    }

    override fun equipmentPacket(entityID: Int, helmet: ItemStack, itemLine: Boolean): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT)
        packet.integers.write(0, entityID)
        if (itemLine) {
            packet.integers.write(1, 0)
        } else {
            // Use legacy form to update the head slot.
            packet.integers.write(1, 4)
        }

        packet.itemModifier.write(0, helmet)
        return packet
    }

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
        packet.watchableCollectionModifier.write(0, watcher.watchableObjects)
        return packet
    }

    override fun teleportPacket(entityID: Int, location: Location): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT)
        packet.integers.write(0, entityID)
        packet.integers.write(1, fixCoordinate(location.x))
        packet.integers.write(2, fixCoordinate(location.y))
        packet.integers.write(3, fixCoordinate(location.z))
        packet.bytes.write(0, this.getCompressAngle(location.yaw.toDouble()))
        packet.bytes.write(1, this.getCompressAngle(location.pitch.toDouble()))
        packet.booleans.write(0, false)
        return packet
    }

    override fun rotatePackets(entityID: Int, from: Location, yaw: Float): List<PacketContainer> {
        val rotationPacket = PacketContainer(
            PacketType.Play.Server.ENTITY_HEAD_ROTATION
        )

        rotationPacket.integers.write(0, entityID)
        rotationPacket.bytes.write(0, getCompressAngle(yaw.toDouble()))

        val teleportPacket = PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT)

        teleportPacket.integers.write(0, entityID)

        teleportPacket.integers.write(1, fixCoordinate(from.x))
        teleportPacket.integers.write(2, fixCoordinate(from.y))
        teleportPacket.integers.write(3, fixCoordinate(from.z))

        teleportPacket.bytes.write(0, getCompressAngle(yaw.toDouble()))
        teleportPacket.bytes.write(1, 0.toByte())

        teleportPacket.booleans.write(0, true)

        return listOf(rotationPacket, teleportPacket)
    }

}

open class PacketsV1_9V1_12 : IPackets {
    override fun spawnPacket(entityID: Int, location: Location, plugin: Plugin): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING)
        packet.modifier.writeDefaults()

        val entityType = 30
        val extraData = 1
        packet.integers.write(0, entityID)
        packet.integers.write(1, entityType)
        packet.integers.write(2, extraData)
        packet.uuiDs.write(0, UUID.randomUUID())
        packet.doubles.write(0, location.x)
        packet.doubles.write(1, location.y)
        packet.doubles.write(2, location.z)
        return packet
    }

    override fun destroyPacket(entityID: Int): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.ENTITY_DESTROY)
        packet.integerArrays.write(0, intArrayOf(entityID))
        return packet
    }

    override fun equipmentPacket(entityID: Int, helmet: ItemStack, itemLine: Boolean): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT)

        packet.integers.write(0, entityID)
        if (itemLine) {
            packet.itemSlots.write(0, ItemSlot.MAINHAND)
        } else {
            packet.itemSlots.write(0, ItemSlot.HEAD)
        }
        packet.itemModifier.write(0, helmet)

        return packet
    }

    override fun metadataPacket(
        entityID: Int,
        nameTag: String?,
        setInvisible: Boolean,
        setSmall: Boolean,
        handRotationNMS: Any?
    ): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.ENTITY_METADATA)
        packet.modifier.writeDefaults()
        packet.integers.write(0, entityID)

        val watcher = WrappedDataWatcher()
        if (setInvisible) {
            val visible = WrappedDataWatcherObject(
                0, WrappedDataWatcher.Registry.get(Byte::class.java)
            )
            watcher.setObject(visible, 0x20.toByte())
        }
        if (nameTag != null) {
            watcher.setObject(
                WrappedDataWatcherObject(
                    2,
                    WrappedDataWatcher.Registry.get(String::class.java)
                ), nameTag
            )

            val nameVisible = WrappedDataWatcherObject(
                3, WrappedDataWatcher.Registry.get(Boolean::class.java)
            )
            watcher.setObject(nameVisible, true)
        }
        if (setSmall) {
            val small = WrappedDataWatcherObject(
                10, WrappedDataWatcher.Registry.get(Byte::class.java)
            )
            watcher.setObject(small, (0x01 or 0x04).toByte())
        }
        if (handRotationNMS != null) {
            val handRotation = WrappedDataWatcherObject(
                19, WrappedDataWatcher.Registry.getVectorSerializer()
            )
            watcher.setObject(handRotation, handRotationNMS)
        }

        packet.watchableCollectionModifier.write(0, watcher.watchableObjects)
        return packet
    }

    override fun teleportPacket(entityID: Int, location: Location): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT)
        packet.integers.write(0, entityID)
        packet.doubles.write(0, location.x)
        packet.doubles.write(1, location.y)
        packet.doubles.write(2, location.z)
        packet.bytes.write(0, this.getCompressAngle(location.yaw.toDouble()))
        packet.bytes.write(1, this.getCompressAngle(location.pitch.toDouble()))
        packet.booleans.write(0, false)
        return packet
    }

    override fun rotatePackets(entityID: Int, from: Location, yaw: Float): List<PacketContainer> {
        val packet = PacketContainer(PacketType.Play.Server.ENTITY_LOOK)
        packet.integers.write(0, entityID)
        packet.bytes.write(0, this.getCompressAngle(yaw.toDouble())).write(1, 0.toByte())
        packet.booleans.write(0, true)
        return listOf(packet)
    }

}

open class PacketsV1_13V1_16 : PacketsV1_9V1_12() {


    override fun equipmentPacket(entityID: Int, helmet: ItemStack, itemLine: Boolean): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT)
        packet.integers.write(0, entityID)
        val pairList: MutableList<Pair<ItemSlot, ItemStack>> = ArrayList()
        pairList.add(
            Pair(
                if (itemLine) ItemSlot.MAINHAND else ItemSlot.HEAD,
                helmet
            )
        )
        packet.slotStackPairLists.write(0, pairList)
        return packet
    }

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
        if (setInvisible) {
            val visible = WrappedDataWatcherObject(
                0, WrappedDataWatcher.Registry.get(Byte::class.java)
            )
            watcher.setObject(visible, 0x20.toByte())
        }
        if (nameTag != null) {
            val opt: Optional<*> = Optional.of(WrappedChatComponent.fromChatMessage(nameTag)[0].handle)
            watcher.setObject(
                WrappedDataWatcherObject(
                    2,
                    WrappedDataWatcher.Registry.getChatComponentSerializer(true)
                ), opt
            )

            val nameVisible = WrappedDataWatcherObject(
                3, WrappedDataWatcher.Registry.get(Boolean::class.java)
            )
            watcher.setObject(nameVisible, true)
        }
        if (setSmall) {
            val small = WrappedDataWatcherObject(
                15, WrappedDataWatcher.Registry.get(Byte::class.java)
            )
            watcher.setObject(small, (0x01 or 0x04).toByte())
        }
        if (handRotationNMS != null) {
            val handRotation = WrappedDataWatcherObject(
                19, WrappedDataWatcher.Registry.getVectorSerializer()
            )
            watcher.setObject(handRotation, handRotationNMS)
        }
        packet.watchableCollectionModifier.write(0, watcher.watchableObjects)
        return packet
    }

}

open class PacketsV1_17_V18 : PacketsV1_13V1_16() {

    override fun destroyPacket(entityID: Int): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.ENTITY_DESTROY)
        packet.intLists.write(0, listOf(entityID))
        return packet
    }

}

class PacketsV1_19 : PacketsV1_17_V18() {
    override fun spawnPacket(entityID: Int, location: Location, plugin: Plugin): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.SPAWN_ENTITY)
        val extraData = 1
        packet.integers.write(0, entityID)
        packet.integers.write(1, extraData)
        packet.entityTypeModifier.write(0, EntityType.ARMOR_STAND)
        packet.uuiDs.write(0, UUID.randomUUID())
        packet.doubles.write(0, location.x)
        packet.doubles.write(1, location.y /*+1.2*/)
        packet.doubles.write(2, location.z)
        return packet
    }


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

        try {
            Class.forName("com.comphenix.protocol.wrappers.WrappedDataValue")

            packet.watchableCollectionModifier.write(0, watcher.watchableObjects)
            val wrappedDataValueList: MutableList<WrappedDataValue> = ArrayList()

            if (setInvisible) {
                wrappedDataValueList.add(
                    WrappedDataValue(0, WrappedDataWatcher.Registry.get(Byte::class.java), 0x20.toByte())
                )
            }
            if (nameTag != null) {
                val opt: Optional<*> = Optional.of(
                    WrappedChatComponent.fromChatMessage(
                        nameTag
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
            if (handRotationNMS != null) {
                wrappedDataValueList.add(
                    WrappedDataValue(
                        19, WrappedDataWatcher.Registry.getVectorSerializer(),
                        handRotationNMS
                    )
                )
            }
            packet.dataValueCollectionModifier.write(0, wrappedDataValueList)
        } catch (e: ClassNotFoundException) {
            if (setInvisible) {
                val visible = WrappedDataWatcherObject(
                    0, WrappedDataWatcher.Registry.get(Byte::class.java)
                )
                watcher.setObject(visible, 0x20.toByte())
            }
            if (nameTag != null) {
                val opt: Optional<*> = Optional.of(
                    WrappedChatComponent.fromChatMessage(
                        nameTag
                    )[0].handle
                )
                watcher.setObject(
                    WrappedDataWatcherObject(
                        2,
                        WrappedDataWatcher.Registry.getChatComponentSerializer(true)
                    ), opt
                )

                val nameVisible = WrappedDataWatcherObject(
                    3, WrappedDataWatcher.Registry.get(Boolean::class.java)
                )
                watcher.setObject(nameVisible, true)
            }
            if (setSmall) {
                val small = WrappedDataWatcherObject(
                    15, WrappedDataWatcher.Registry.get(Byte::class.java)
                )
                watcher.setObject(small, (0x01 or 0x04).toByte())
            }
            if (handRotationNMS != null) {
                val handRotation = WrappedDataWatcherObject(
                    19, WrappedDataWatcher.Registry.getVectorSerializer()
                )
                watcher.setObject(handRotation, handRotationNMS)
            }
            packet.watchableCollectionModifier.write(0, watcher.watchableObjects)
        }
        return packet
    }


}