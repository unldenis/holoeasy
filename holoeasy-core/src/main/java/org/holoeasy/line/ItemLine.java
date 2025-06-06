package org.holoeasy.line;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityVelocity;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.util.VersionEnum;
import org.holoeasy.util.VersionUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ItemLine extends Line<ItemStack> {

    public ItemLine(Hologram hologram, Function<Player, ItemStack> valueSupplier) {
        super(hologram, EntityTypes.ITEM, valueSupplier);
        if (VersionUtil.isCompatible(VersionEnum.V1_8)) {
            throw new IllegalStateException("This version does not support item lines");
        }
    }

    @Override
    public @NotNull Type getType() {
        return Type.ITEM_LINE;
    }

    @Override
    public void show(@NotNull Player player) {
        spawn(player);
        this.update(player);

        WrapperPlayServerEntityVelocity packet = new WrapperPlayServerEntityVelocity(entityID, new Vector3d(0, 0, 0));
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    @Override
    public void hide(@NotNull Player player) {
        destroy(player);
    }

    @Override
    public void update(@NotNull Player player) {
        List<EntityData<?>> entityData = new ArrayList<>();

        com.github.retrooper.packetevents.protocol.item.ItemStack item = SpigotConversionUtil.fromBukkitItemStack(getValue(player));

        switch (VersionUtil.CLEAN_VERSION) {
            case V1_8:
                entityData.add(new EntityData<>(10, EntityDataTypes.ITEMSTACK, item));
                break;
            case V1_9:
            case V1_10:
            case V1_11:
            case V1_12:
                entityData.add(new EntityData<>(5, EntityDataTypes.BOOLEAN, true));
                entityData.add(new EntityData<>(6, EntityDataTypes.ITEMSTACK, item));
                break;
            case V1_13:
            case V1_14:
            case V1_15:
            case V1_16:
            case V1_17:
            case V1_18:
                entityData.add(new EntityData<>(5, EntityDataTypes.BOOLEAN, true));
                entityData.add(new EntityData<>(7, EntityDataTypes.ITEMSTACK, item));
                break;
            default:
                entityData.add(new EntityData<>(5, EntityDataTypes.BOOLEAN, true));
                entityData.add(new EntityData<>(8, EntityDataTypes.ITEMSTACK, item));
                break;
        }

        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(entityID, entityData);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    // Builder

    public ItemLine yOffset(double yOffset) {
        super.setYOffset(yOffset);
        return this;
    }
}