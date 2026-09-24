package org.holoeasy.line;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import org.bukkit.entity.Player;
import org.holoeasy.hologram.Hologram;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InteractionLine extends Line<Object> {

    private static final Object VALUE = new Object(); // Placeholder value, as InteractionLine does not use a specific value

    private float width = 1.0f;
    private float height = 1.0f;

    public InteractionLine(Hologram hologram) {
        super(hologram, EntityTypes.INTERACTION, player -> VALUE);
    }

    @Override
    public @NotNull Type getType() {
        return Type.INTERACTION_LINE;
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
            throw new RuntimeException("InteractionLine is available since 1.19.4");
        }

        // EntityLib InteractionMeta: width@8, height@9, responsive@10
        List<EntityData<?>> entityData = new ArrayList<>();
        entityData.add(new EntityData<>(8, EntityDataTypes.FLOAT, width));
        entityData.add(new EntityData<>(9, EntityDataTypes.FLOAT, height));
        entityData.add(new EntityData<>(10, EntityDataTypes.BOOLEAN, true));

        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(entityID, entityData);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    // Builder

    public InteractionLine yOffset(double yOffset) {
        super.setYOffset(yOffset);
        return this;
    }

    public InteractionLine width(float width) {
        this.width = width;
        return this;
    }

    public InteractionLine height(float height) {
        this.height = height;
        return this;
    }


}
