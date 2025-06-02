package org.holoeasy.packet

import com.github.retrooper.packetevents.protocol.entity.data.EntityData
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes
import com.github.retrooper.packetevents.protocol.player.Equipment
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot
import com.github.retrooper.packetevents.util.Vector3d
import com.github.retrooper.packetevents.wrapper.play.server.*
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer
import io.github.retrooper.packetevents.util.SpigotConversionUtil
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.holoeasy.HoloEasy
import org.holoeasy.util.VersionEnum
import org.holoeasy.util.VersionUtil
import java.util.*


class PacketEventsPackets : IPacket {

    override fun deletePacket(player: Player, entityId: Int) {
        val packet = WrapperPlayServerDestroyEntities(entityId)
        packet.send(player)
    }

    override fun equip(player: Player, entityId: Int, helmet: ItemStack) {
        val packet = WrapperPlayServerEntityEquipment(
            entityId, listOf(
                Equipment(
                    EquipmentSlot.HELMET,
                    SpigotConversionUtil.fromBukkitItemStack(helmet)
                )
            )
        )
        packet.send(player)
    }

    override fun metadataItem(player: Player, entityId: Int, item: ItemStack) {

        val entityData = mutableListOf<EntityData<*>>()


        if (VersionUtil.CLEAN_VERSION in VersionEnum.V1_8..VersionEnum.V1_8) {
            entityData.add(
                EntityData(
                    10,
                    EntityDataTypes.ITEMSTACK,
                    SpigotConversionUtil.fromBukkitItemStack(item)
                )
            )

        } else if (VersionUtil.CLEAN_VERSION in VersionEnum.V1_9..VersionEnum.V1_12) {
            entityData.add(
                EntityData(
                    5,
                    EntityDataTypes.BOOLEAN,
                    true
                )
            )
            entityData.add(
                EntityData(
                    6,
                    EntityDataTypes.ITEMSTACK,
                    SpigotConversionUtil.fromBukkitItemStack(item)
                )
            )

        } else if (VersionUtil.CLEAN_VERSION in VersionEnum.V1_13..VersionEnum.V1_18) {
            entityData.add(
                EntityData(
                    5,
                    EntityDataTypes.BOOLEAN,
                    true
                )
            )
            entityData.add(
                EntityData(
                    7,
                    EntityDataTypes.ITEMSTACK,
                    SpigotConversionUtil.fromBukkitItemStack(item)
                )
            )
        } else {
            entityData.add(
                EntityData(
                    5,
                    EntityDataTypes.BOOLEAN,
                    true
                )
            )
            entityData.add(
                EntityData(
                    8,
                    EntityDataTypes.ITEMSTACK,
                    SpigotConversionUtil.fromBukkitItemStack(item)
                )
            )
        }

        val packet = WrapperPlayServerEntityMetadata(entityId, entityData)
        packet.send(player)
    }

    override fun metadataText(player: Player, entityId: Int, nameTag: String?, invisible: Boolean) {
        val entityData = mutableListOf<EntityData<*>>()

        if (VersionUtil.CLEAN_VERSION in VersionEnum.V1_8..VersionEnum.V1_8) {

            if (invisible) {
                entityData.add(
                    EntityData(
                        0,
                        EntityDataTypes.BYTE,
                        0x20.toByte()
                    )
                )
            }

            if (nameTag != null) {
                entityData.add(
                    EntityData(
                        2,
                        EntityDataTypes.STRING,
                        nameTag
                    )
                )
                entityData.add(
                    EntityData(
                        3,
                        EntityDataTypes.BYTE,
                        1.toByte()
                    )
                )
            }

        } else {

            if (invisible) {
                entityData.add(
                    EntityData(
                        0,
                        EntityDataTypes.BYTE,
                        0x20.toByte()
                    )
                )
            }

            if (nameTag != null) {
                entityData.add(
                    EntityData(
                        2,
                        EntityDataTypes.OPTIONAL_ADV_COMPONENT,
                        Optional.of(LegacyComponentSerializer.legacyAmpersand().deserialize(nameTag))
                    )
                )

                entityData.add(
                    EntityData(
                        3,
                        EntityDataTypes.BOOLEAN,
                        true
                    )
                )
            }

        }

        val packet = WrapperPlayServerEntityMetadata(entityId, entityData)
        packet.send(player)
    }

    override fun rotate(player: Player, entityId: Int, yaw: Double) {
        val packet = WrapperPlayServerEntityRotation(entityId, yaw.toFloat(), 0f, false)
        packet.send(player)
    }

    override fun spawn(lib : HoloEasy, player: Player, entityId: Int, entityType: EntityType, location: Location) {
        val packet = WrapperPlayServerSpawnEntity(
            entityId,
            UUID.randomUUID(),
            SpigotConversionUtil.fromBukkitEntityType(entityType),
            SpigotConversionUtil.fromBukkitLocation(location),
            location.yaw,
            0,
            null
        )
        packet.send(player)
    }

    override fun teleport(player: Player, entityId: Int, location: Location) {
        val packet = WrapperPlayServerEntityTeleport(entityId, SpigotConversionUtil.fromBukkitLocation(location), false)
        packet.send(player)
    }

    override fun velocity(player: Player, entityId: Int, x: Double, y: Double, z: Double) {
        val packet = WrapperPlayServerEntityVelocity(entityId, Vector3d(x, y, z))
        packet.send(player)
    }


}