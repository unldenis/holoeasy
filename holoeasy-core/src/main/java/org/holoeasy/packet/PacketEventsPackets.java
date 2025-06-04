package org.holoeasy.packet;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import io.github.retrooper.packetevents.adventure.serializer.gson.GsonComponentSerializer;
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.holoeasy.HoloEasy;
import org.holoeasy.util.VersionEnum;
import org.holoeasy.util.VersionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PacketEventsPackets implements IPacket {

    private static final GsonComponentSerializer SERIALIZER = GsonComponentSerializer.builder().build();

    @Override
    public void deletePacket(Player player, int entityId) {
        WrapperPlayServerDestroyEntities packet = new WrapperPlayServerDestroyEntities(entityId);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    @Override
    public void equip(Player player, int entityId, ItemStack helmet) {
        List<Equipment> equipmentList = new ArrayList<>();
        equipmentList.add(new Equipment(EquipmentSlot.HELMET, SpigotConversionUtil.fromBukkitItemStack(helmet)));
        WrapperPlayServerEntityEquipment packet = new WrapperPlayServerEntityEquipment(entityId, equipmentList);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    @Override
    public void metadataItem(Player player, int entityId, ItemStack item) {
        List<EntityData<?>> entityData = new ArrayList<>();
        VersionEnum version = VersionUtil.CLEAN_VERSION;

        switch (version) {
            case V1_8:
                entityData.add(new EntityData<>(10, EntityDataTypes.ITEMSTACK, SpigotConversionUtil.fromBukkitItemStack(item)));
                break;
            case V1_9:
            case V1_10:
            case V1_11:
            case V1_12:
                entityData.add(new EntityData<>(5, EntityDataTypes.BOOLEAN, true));
                entityData.add(new EntityData<>(6, EntityDataTypes.ITEMSTACK, SpigotConversionUtil.fromBukkitItemStack(item)));
                break;
            case V1_13:
            case V1_14:
            case V1_15:
            case V1_16:
            case V1_17:
            case V1_18:
                entityData.add(new EntityData<>(5, EntityDataTypes.BOOLEAN, true));
                entityData.add(new EntityData<>(7, EntityDataTypes.ITEMSTACK, SpigotConversionUtil.fromBukkitItemStack(item)));
                break;
            default:
                entityData.add(new EntityData<>(5, EntityDataTypes.BOOLEAN, true));
                entityData.add(new EntityData<>(8, EntityDataTypes.ITEMSTACK, SpigotConversionUtil.fromBukkitItemStack(item)));
                break;
        }

        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(entityId, entityData);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    @Override
    public void metadataText(Player player, int entityId, String nameTag, boolean invisible) {
        List<EntityData<?>> entityData = new ArrayList<>();
        VersionEnum version = VersionUtil.CLEAN_VERSION;

        if (version == VersionEnum.V1_8) {
            if (invisible) {
                entityData.add(new EntityData<>(0, EntityDataTypes.BYTE, (byte) 0x20));
            }
            if (nameTag != null) {
                entityData.add(new EntityData<>(2, EntityDataTypes.STRING, nameTag));
                entityData.add(new EntityData<>(3, EntityDataTypes.BYTE, (byte) 1));
            }
        } else {
            if (invisible) {
                entityData.add(new EntityData<>(0, EntityDataTypes.BYTE, (byte) 0x20));
            }
            if (nameTag != null) {
                entityData.add(new EntityData<>(2, EntityDataTypes.OPTIONAL_ADV_COMPONENT, Optional.of(LegacyComponentSerializer.legacyAmpersand().deserialize(nameTag))));
                entityData.add(new EntityData<>(3, EntityDataTypes.BOOLEAN, true));
            }
        }

        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(entityId, entityData);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    @Override
    public void rotate(Player player, int entityId, double yaw) {
        WrapperPlayServerEntityRotation packet = new WrapperPlayServerEntityRotation(entityId, (float) yaw, 0f, false);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    @Override
    public void spawn(HoloEasy lib, Player player, int entityId, EntityType entityType, Location location) {
        WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity(
                entityId,
                UUID.randomUUID(),
                SpigotConversionUtil.fromBukkitEntityType(entityType),
                SpigotConversionUtil.fromBukkitLocation(location),
                location.getYaw(),
                0,
                null
        );
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    @Override
    public void teleport(Player player, int entityId, Location location) {
        WrapperPlayServerEntityTeleport packet = new WrapperPlayServerEntityTeleport(
                entityId,
                SpigotConversionUtil.fromBukkitLocation(location),
                false
        );
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    @Override
    public void velocity(Player player, int entityId, double x, double y, double z) {
        WrapperPlayServerEntityVelocity packet = new WrapperPlayServerEntityVelocity(entityId, new Vector3d(x, y, z));
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    @Override
    public void metadataDisplayBlock(Player player, int entityId, Material material) {
        List<EntityData<?>> entityData = new ArrayList<>();
        VersionEnum version = VersionUtil.CLEAN_VERSION;

        switch (version) {
            case V1_8:
            case V1_9:
            case V1_10:
            case V1_11:
            case V1_12:
            case V1_13:
            case V1_14:
            case V1_15:
            case V1_16:
            case V1_17:
            case V1_18:
                throw new RuntimeException("metadataDisplayBlock is available since 1.19.4");
            case V1_19:
                entityData.add(new EntityData<>(22, EntityDataTypes.BLOCK_STATE,
                        SpigotConversionUtil.fromBukkitBlockData(material.createBlockData()).getGlobalId()));
                break;
            default:
                entityData.add(new EntityData<>(23, EntityDataTypes.BLOCK_STATE,
                        SpigotConversionUtil.fromBukkitBlockData(material.createBlockData()).getGlobalId()));
                break;
        }

        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(entityId, entityData);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    @Override
    public void metadataDisplayText(Player player, int entityId, String text, int lineWidth, int backgroundColor, byte textOpacity) {
        List<EntityData<?>> entityData = new ArrayList<>();
        VersionEnum version = VersionUtil.CLEAN_VERSION;

        switch (version) {
            case V1_8:
            case V1_9:
            case V1_10:
            case V1_11:
            case V1_12:
            case V1_13:
            case V1_14:
            case V1_15:
            case V1_16:
            case V1_17:
            case V1_18:
                throw new RuntimeException("metadataDisplayText is available since 1.19.4");
            case V1_19:
                entityData.add(new EntityData<>(22, EntityDataTypes.OPTIONAL_ADV_COMPONENT,
                        Optional.of(LegacyComponentSerializer.legacyAmpersand().deserialize(text))));
                entityData.add(new EntityData<>(23, EntityDataTypes.INT, lineWidth));
                entityData.add(new EntityData<>(24, EntityDataTypes.INT, backgroundColor));
                entityData.add(new EntityData<>(25, EntityDataTypes.BYTE, textOpacity));
                break;
            default:
                entityData.add(new EntityData<>(23, EntityDataTypes.COMPONENT,
                        SERIALIZER.serialize(Component.text(text))));
                entityData.add(new EntityData<>(24, EntityDataTypes.INT, lineWidth));
                entityData.add(new EntityData<>(25, EntityDataTypes.INT, backgroundColor));
                entityData.add(new EntityData<>(26, EntityDataTypes.BYTE, textOpacity));
                break;
        }

        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(entityId, entityData);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }
}