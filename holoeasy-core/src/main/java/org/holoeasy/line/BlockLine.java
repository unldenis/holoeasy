package org.holoeasy.line;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEquipment;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.holoeasy.hologram.Hologram;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BlockLine extends Line<ItemStack> {

    public BlockLine(Hologram hologram, Function<Player, ItemStack> valueSupplier) {
        super(hologram, EntityTypes.ARMOR_STAND, valueSupplier);
    }

    @Override
    public @NotNull Type getType() {
        return Type.BLOCK_LINE;
    }

    @Override
    public void show(@NotNull Player player) {
        spawn(player);

        List<EntityData<?>> entityData = new ArrayList<>();
        entityData.add(new EntityData<>(0, EntityDataTypes.BYTE, (byte) 0x20));

        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(entityID, entityData);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);

        this.update(player);
    }

    @Override
    public void hide(@NotNull Player player) {
        destroy(player);
    }

    @Override
    public void update(@NotNull Player player) {
        List<Equipment> equipmentList = new ArrayList<>();
        equipmentList.add(new Equipment(EquipmentSlot.HELMET, SpigotConversionUtil.fromBukkitItemStack(getValue(player))));
        WrapperPlayServerEntityEquipment packet = new WrapperPlayServerEntityEquipment(entityID, equipmentList);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    // Builder

    public BlockLine yOffset(double yOffset) {
        super.setYOffset(yOffset);
        return this;
    }
}