package org.holoeasy.line;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.util.VersionUtil;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@ApiStatus.Experimental
public class DisplayBlockLine extends Line<Material> {


    public DisplayBlockLine(Hologram hologram, Function<Player, Material> valueSupplier) {
        super(hologram, EntityTypes.BLOCK_DISPLAY, valueSupplier);
    }

    @Override
    public @NotNull Type getType() {
        return Type.DISPLAY_BLOCK_LINE;
    }

    @Override
    public void show(@NotNull Player player) {
        spawn(player);
        this.update(player);
    }

    @Override
    public void hide(@NotNull Player player) {
        destroy(player);
    }

    @Override
    public void update(@NotNull Player player) {
        WrappedBlockState blockState = SpigotConversionUtil.fromBukkitBlockData(getValue(player).createBlockData());

        List<EntityData<?>> entityData = new ArrayList<>();

        switch ( VersionUtil.CLEAN_VERSION) {
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
                throw new RuntimeException("DisplayBlockLine is available since 1.19.4");
            case V1_19:
                entityData.add(new EntityData<>(22, EntityDataTypes.BLOCK_STATE, blockState.getGlobalId()));
                break;
            default:
                entityData.add(new EntityData<>(23, EntityDataTypes.BLOCK_STATE, blockState.getGlobalId()));
                break;
        }

        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(entityID, entityData);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    // Builder

    public DisplayBlockLine yOffset(double yOffset) {
        super.setYOffset(yOffset);
        return this;
    }
}