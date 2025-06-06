package org.holoeasy.line;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import io.github.retrooper.packetevents.adventure.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.util.VersionUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DisplayTextLine extends Line<String> {
    private static final GsonComponentSerializer SERIALIZER = GsonComponentSerializer.builder().build();


    private int lineWidth = 200;
    private int backgroundColor = 0x40000000;
    private byte textOpacity = -1;

    public DisplayTextLine(Hologram hologram, Function<Player, String> valueSupplier) {
        super(hologram, EntityTypes.TEXT_DISPLAY, valueSupplier);
    }

    @Override
    public @NotNull Type getType() {
        return Type.DISPLAY_TEXT_LINE;
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
                throw new RuntimeException("DisplayTextLine is available since 1.19.4");
            case V1_19:
                entityData.add(new EntityData<>(22, EntityDataTypes.COMPONENT, SERIALIZER.serialize(Component.text(getValue(player)))));
                entityData.add(new EntityData<>(23, EntityDataTypes.INT, lineWidth));
                entityData.add(new EntityData<>(24, EntityDataTypes.INT, backgroundColor));
                entityData.add(new EntityData<>(25, EntityDataTypes.BYTE, textOpacity));
                break;
            default:
                entityData.add(new EntityData<>(23, EntityDataTypes.COMPONENT, SERIALIZER.serialize(Component.text(getValue(player)))));
                entityData.add(new EntityData<>(24, EntityDataTypes.INT, lineWidth));
                entityData.add(new EntityData<>(25, EntityDataTypes.INT, backgroundColor));
                entityData.add(new EntityData<>(26, EntityDataTypes.BYTE, textOpacity));
                break;
        }

        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(entityID, entityData);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    // Builder

    public DisplayTextLine yOffset(double yOffset) {
        super.setYOffset(yOffset);
        return this;
    }

    public DisplayTextLine lineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public DisplayTextLine backgroundColor(@NotNull Color backgroundColor) {
        this.backgroundColor = backgroundColor.asRGB();
        return this;
    }

    public DisplayTextLine backgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public DisplayTextLine textOpacity(byte textOpacity) {
        this.textOpacity = textOpacity;
        return this;
    }

}