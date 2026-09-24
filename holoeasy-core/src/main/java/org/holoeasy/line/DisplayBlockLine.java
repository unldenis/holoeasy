package org.holoeasy.line;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.holoeasy.hologram.Hologram;
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
        ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
        if (version.isOlderThan(ServerVersion.V_1_19_4)) {
            throw new RuntimeException("DisplayBlockLine is available since 1.19.4");
        }

        WrappedBlockState blockState = SpigotConversionUtil.fromBukkitBlockData(getValue(player).createBlockData());

        // Block state index is 22 before 1.20.2, 23 on 1.20.2+ (EntityLib BlockDisplayMeta).
        int blockIndex = version.isOlderThan(ServerVersion.V_1_20_2) ? 22 : 23;

        List<EntityData<?>> entityData = new ArrayList<>();
        entityData.add(new EntityData<>(blockIndex, EntityDataTypes.BLOCK_STATE, blockState.getGlobalId()));

        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(entityID, entityData);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    // Builder

    public DisplayBlockLine yOffset(double yOffset) {
        super.setYOffset(yOffset);
        return this;
    }
}
