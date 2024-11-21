package org.holoeasy.packet

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.*
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.holoeasy.HoloEasy
import org.holoeasy.ext.*
import org.holoeasy.util.*
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.math.max
import kotlin.math.min

class ProtocolLibPackets : IPacket {
    override fun deletePacket(player: Player, entityId: Int) {

        val packet = if (VersionUtil.isBelow(VersionEnum.V1_16)) {
            packet(PacketType.Play.Server.ENTITY_DESTROY) {
                integerArrays[0] = intArrayOf(entityId)
            }
        } else {
            packet(PacketType.Play.Server.ENTITY_DESTROY) {
                intLists[0] = listOf(entityId)
            }
        }
        packet.send(player)
    }

    override fun equip(player: Player, entityId: Int, helmet: ItemStack) {

        val packet = when {
            VersionUtil.isCompatible(VersionEnum.V1_8) -> {
                packet(PacketType.Play.Server.ENTITY_EQUIPMENT) {
                    integers[0] = entityId

                    // Use legacy form to update the head slot.
                    integers[1] = 4

                    itemModifier[0] = helmet
                }
            }

            VersionUtil.CLEAN_VERSION in VersionEnum.V1_9..VersionEnum.V1_12 -> {
                packet(PacketType.Play.Server.ENTITY_EQUIPMENT) {
                    integers[0] = entityId

                    itemSlots[0] = EnumWrappers.ItemSlot.HEAD

                    itemModifier[0] = helmet
                }
            }

            else -> {
                // 1_13 >
                packet(PacketType.Play.Server.ENTITY_EQUIPMENT) {
                    integers[0] = entityId

                    val pairList = ArrayList<Pair<ItemSlot, ItemStack>>()
                    pairList.add(Pair(ItemSlot.HEAD, helmet))

                    slotStackPairLists[0] = pairList
                }
            }
        }
        packet.send(player)
    }

    override fun metadataItem(player: Player, entityId: Int, item: ItemStack) {

        val packet = when (VersionUtil.CLEAN_VERSION) {
            in VersionEnum.V1_8..VersionEnum.V1_8 -> {
                val watcher = WrappedDataWatcher()

                watcher.setObject(10, item.bukkitGeneric())

                packet(PacketType.Play.Server.ENTITY_METADATA) {
                    integers[0] = entityId
                    watchableCollectionModifier[0] = watcher.watchableObjects
                }
            }

            in VersionEnum.V1_9..VersionEnum.V1_12 -> {
                val watcher = WrappedDataWatcher()

                watcher.setBool(5, true)
                watcher.setItemStack(6, item)

                packet(PacketType.Play.Server.ENTITY_METADATA) {
                    integers[0] = entityId
                    watchableCollectionModifier[0] = watcher.watchableObjects
                }
            }

            in VersionEnum.V1_13..VersionEnum.V1_18 -> {
                val watcher = WrappedDataWatcher()

                watcher.setBool(5, true)
                watcher.setItemStack(7, item)

                packet(PacketType.Play.Server.ENTITY_METADATA) {
                    integers[0] = entityId
                    watchableCollectionModifier[0] = watcher.watchableObjects
                }
            }

            in VersionEnum.V1_19..VersionEnum.V1_19 -> {
                val packet = PacketContainer(PacketType.Play.Server.ENTITY_METADATA)
                packet.integers.write(0, entityId)

                val watcher = WrappedDataWatcher()

                val gravity = WrappedDataWatcherObject(
                    5, BOOL_SERIALIZER
                )
                watcher.setObject(gravity, true)

                val itemSer = WrappedDataWatcherObject(
                    8, ITEM_SERIALIZER
                )
                watcher.setObject(itemSer, item.bukkitGeneric())

                // https://www.spigotmc.org/threads/unable-to-modify-entity-metadata-packet-using-protocollib-1-19-3.582442/#post-4517187
                packet.parse119(watcher)

                packet
            }

            else -> {
                // 1.20 >
                val packet = PacketContainer(PacketType.Play.Server.ENTITY_METADATA)

                packet.integers.write(0, entityId)

                packet.dataValueCollectionModifier
                    .write(
                        0, listOf(
                            WrappedDataValue(5, BOOL_SERIALIZER, true),

                            WrappedDataValue(8, ITEM_SERIALIZER, item.bukkitGeneric())
                        )
                    )


                packet
            }
        }
        packet.send(player)
    }

    override fun metadataText(player: Player, entityId: Int, nameTag: String?, invisible: Boolean) {
        val packet = when (VersionUtil.CLEAN_VERSION) {
            in VersionEnum.V1_8..VersionEnum.V1_8 -> {
                val watcher = WrappedDataWatcher()

                if (invisible)
                    watcher.setObject(0, 0x20.toByte())

                if (nameTag != null) {
                    watcher.setObject(2, nameTag)
                    watcher.setObject(3, 1.toByte())
                }

                packet(PacketType.Play.Server.ENTITY_METADATA) {
                    integers[0] = entityId
                    watchableCollectionModifier[0] = watcher.watchableObjects
                }
            }

            in VersionEnum.V1_9..VersionEnum.V1_12 -> {
                val watcher = WrappedDataWatcher()

                if (invisible)
                    watcher.setByte(0, 0x20.toByte())

                if (nameTag != null) {
                    watcher.setString(2, nameTag)
                    watcher.setBool(3, true)
                }

                packet(PacketType.Play.Server.ENTITY_METADATA) {
                    modifier.writeDefaults()
                    integers[0] = entityId
                    watchableCollectionModifier[0] = watcher.watchableObjects
                }
            }

            in VersionEnum.V1_13..VersionEnum.V1_18 -> {
                val watcher = WrappedDataWatcher()

                if (invisible)
                    watcher.setByte(0, 0x20.toByte())

                if (nameTag != null) {
                    watcher.setChatComponent(2, nameTag)
                    watcher.setBool(3, true)
                }
                packet(PacketType.Play.Server.ENTITY_METADATA) {
                    integers[0] = entityId
                    watchableCollectionModifier[0] = watcher.watchableObjects
                }
            }

            in VersionEnum.V1_19..VersionEnum.V1_19 -> {
                val packet = PacketContainer(PacketType.Play.Server.ENTITY_METADATA)
                packet.integers.write(0, entityId)

                val watcher = WrappedDataWatcher()

                if (invisible)
                    watcher.setByte(0, 0x20.toByte())

                if (nameTag != null) {
                    watcher.setChatComponent(2, nameTag)
                    watcher.setBool(3, true)
                }

                // https://www.spigotmc.org/threads/unable-to-modify-entity-metadata-packet-using-protocollib-1-19-3.582442/#post-4517187
                packet.parse119(watcher)

                packet
            }

            else -> {
                // 1.20 >
                val packet = PacketContainer(PacketType.Play.Server.ENTITY_METADATA)
                packet.integers.write(0, entityId)

                val watcher = WrappedDataWatcher()

                packet.watchableCollectionModifier.write(0, watcher.watchableObjects)
                val wrappedDataValueList: MutableList<WrappedDataValue> = java.util.ArrayList()



                if (invisible) {
                    wrappedDataValueList.add(
                        WrappedDataValue(0, BYTE_SERIALIZER, 0x20.toByte())
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
                        WrappedDataValue(3, BOOL_SERIALIZER, true)
                    )
                }


                packet.dataValueCollectionModifier.write(0, wrappedDataValueList)

                packet
            }
        }
        packet.send(player)
    }

    override fun rotate(player: Player, entityId: Int, yaw: Double) {
        packet(PacketType.Play.Server.ENTITY_LOOK) {
            integers[0] = entityId
            bytes[0] = yaw.compressAngle
        }.send(player)
    }

    override fun spawn(player: Player, entityId: Int, entityType: EntityType, location: Location) {
        val packet = when (VersionUtil.CLEAN_VERSION) {
            in VersionEnum.V1_8..VersionEnum.V1_8 -> {
                packet(PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
                    integers[0] = entityId

                    integers[1] = VersionUtil.CLEAN_VERSION.armorstandId

                    integers[2] = (location.x * 32).toInt()
                    integers[3] = (location.y * 32).toInt()
                    integers[4] = (location.z * 32).toInt()

                    if (defaultDataWatcher == null) {
                        loadDefaultWatcher(HoloEasy.plugin()).join()
                    }

                    dataWatcherModifier[0] = defaultDataWatcher
                }
            }

            in VersionEnum.V1_9..VersionEnum.V1_15 -> {
                val extraData = 1

                packet(PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
                    modifier.writeDefaults()

                    integers[0] = entityId
                    integers[1] = if (entityType == EntityType.ARMOR_STAND)
                        VersionUtil.CLEAN_VERSION.armorstandId else VersionUtil.CLEAN_VERSION.droppedItemId
                    integers[2] = extraData

                    uuiDs[0] = UUID.randomUUID()

                    doubles[0] = location.x
                    doubles[1] = location.y
                    doubles[2] = location.z
                }
            }

            in VersionEnum.V1_16..VersionEnum.V1_18 -> {
                val extraData = 1

                if (entityType == EntityType.ARMOR_STAND) {
                    packet(PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
                        modifier.writeDefaults()

                        integers[0] = entityId
                        integers[1] = VersionUtil.CLEAN_VERSION.armorstandId
                        integers[2] = extraData

                        uuiDs[0] = UUID.randomUUID()

                        doubles[0] = location.x
                        doubles[1] = location.y
                        doubles[2] = location.z
                    }
                } else {
                    packet(PacketType.Play.Server.SPAWN_ENTITY) {
                        modifier.writeDefaults()

                        integers[0] = entityId

                        entityTypeModifier[0] = EntityType.DROPPED_ITEM

                        uuiDs[0] = UUID.randomUUID()

                        doubles[0] = location.x
                        doubles[1] = location.y
                        doubles[2] = location.z

                        integers[2] = convertVelocity(0.0)
                        integers[3] = convertVelocity(0.0)
                        integers[4] = convertVelocity(0.0)
                    }
                }
            }

            else -> {
                // 1.19 >
                packet(PacketType.Play.Server.SPAWN_ENTITY) {
                    integers[0] = entityId

                    entityTypeModifier[0] = entityType

                    uuiDs[0] = UUID.randomUUID()

                    doubles[0] = location.x
                    doubles[1] = location.y
                    doubles[2] = location.z

                }
            }
        }
        packet.send(player)
    }

    override fun teleport(player: Player, entityId: Int, location: Location) {
        val packet = when (VersionUtil.CLEAN_VERSION) {
            in VersionEnum.V1_8..VersionEnum.V1_8 -> {
                packet(PacketType.Play.Server.ENTITY_TELEPORT) {
                    integers[0] = entityId
                    integers[1] = location.x.fixCoordinate
                    integers[2] = location.y.fixCoordinate
                    integers[3] = location.z.fixCoordinate
                    bytes[0] = location.yaw.toDouble().compressAngle
                    bytes[1] = location.pitch.toDouble().compressAngle
                    booleans[0] = false
                }
            }

            else -> {
                // 1.19 >
                packet(PacketType.Play.Server.ENTITY_TELEPORT) {
                    integers[0] = entityId

                    doubles[0] = location.x
                    doubles[1] = location.y
                    doubles[2] = location.z

                    bytes[0] = location.yaw.toDouble().compressAngle
                    bytes[1] = location.pitch.toDouble().compressAngle

                    booleans[0] = false
                }
            }
        }

        packet.send(player)
    }

    override fun velocity(player: Player, entityId: Int, x: Double, y: Double, z: Double) {
        packet(PacketType.Play.Server.ENTITY_VELOCITY) {
            integers[0] = entityId
            integers[1] = x.toInt()
            integers[2] = y.toInt()
            integers[3] = z.toInt()
        }.send(player)
    }

    // 1.8
    private var defaultDataWatcher: WrappedDataWatcher? = null

    private fun loadDefaultWatcher(plugin: Plugin): CompletableFuture<Void> {
        return BukkitFuture.runSync(plugin) {
            val world = Bukkit.getWorlds()[0]
            val entity =
                world.spawnEntity(Location(world, 0.0, 256.0, 0.0), EntityType.ARMOR_STAND)
            entity.remove()
        }
    }


    private fun convertVelocity(velocity: Double): Int {
        /*
          Minecraft represents a velocity within 4 blocks per second, in any direction,
          by using the entire Short range, meaning you can only move up to 4 blocks/second
          on any given direction
        */
        return (clamp(velocity, -3.9, 3.9) * 8000).toInt()
    }

    private fun clamp(targetNum: Double, min: Double, max: Double): Double {
        // Makes sure a number is within a range
        return max(min, min(targetNum, max))
    }
}