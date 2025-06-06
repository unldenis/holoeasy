package org.holoeasy.line;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import org.bukkit.entity.Player;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.util.VersionUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InteractionLine extends Line<Object> {

    private Object value = new Object();

    private float width = 1.0f;
    private float height = 1.0f;

    public InteractionLine(Hologram hologram) {
        super(hologram, EntityTypes.INTERACTION);
    }

    @Override
    public @NotNull Type getType() {
        return Type.INTERACTION_LINE;
    }

    @Override
    public @NotNull Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
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
        List<EntityData<?>> entityData = new ArrayList<>();

        switch (VersionUtil.CLEAN_VERSION) {
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
                throw new RuntimeException("InteractionLine is available since 1.19.4");
            case V1_19:
            default:
                entityData.add(new EntityData<>(8, EntityDataTypes.FLOAT, width));
                entityData.add(new EntityData<>(9, EntityDataTypes.FLOAT, height));
                break;
        }

        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(entityID, entityData);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    @Override
    public void update(@NotNull Object newValue) {
        this.value = newValue;
        updateAll();
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