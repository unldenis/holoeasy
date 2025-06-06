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

@ApiStatus.Experimental
public class DisplayBlockLine extends LineImpl<Material> {

    private Material value;

    public DisplayBlockLine(Hologram hologram, Material value) {
        super(hologram, EntityTypes.BLOCK_DISPLAY);
        this.value = value;
    }

    @Override
    public @NotNull Type getType() {
        return Type.DISPLAY_BLOCK_LINE;
    }

    @Override
    public @NotNull Material getValue() {
        return value;
    }

    @Override
    public void setValue(Material value) {
        this.value = value;
    }

    @Override
    public void show(Player player) {
        spawn(player);
        this.update(player);
    }

    @Override
    public void hide(Player player) {
        destroy(player);
    }

    @Override
    public void update(Player player) {
        WrappedBlockState blockState = SpigotConversionUtil.fromBukkitBlockData(value.createBlockData());

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

    @Override
    public void update(@NotNull Material newValue) {
        this.value = newValue;
        observerUpdate();
    }
}