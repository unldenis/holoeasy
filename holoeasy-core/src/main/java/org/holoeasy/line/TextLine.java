package org.holoeasy.line;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.util.VersionEnum;
import org.holoeasy.util.VersionUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TextLine extends Line<String> {


    public TextLine(Hologram hologram, Function<Player, String> valueSupplier) {
        super(hologram, EntityTypes.ARMOR_STAND, valueSupplier);
    }

    @Override
    public @NotNull Type getType() {
        return Type.TEXT_LINE;
    }


    @Override
    public void show(@NotNull Player player) {
        spawn(player);
        update(player);
    }

    @Override
    public void hide(@NotNull Player player) {
        destroy(player);
    }

    @Override
    public void update(@NotNull Player player) {
        List<EntityData<?>> entityData = new ArrayList<>();
        VersionEnum version = VersionUtil.CLEAN_VERSION;

        if (version == VersionEnum.V1_8) {
            entityData.add(new EntityData<>(0, EntityDataTypes.BYTE, (byte) 0x20));

            entityData.add(new EntityData<>(2, EntityDataTypes.STRING, getValue(player)));
            entityData.add(new EntityData<>(3, EntityDataTypes.BYTE, (byte) 1));
        } else {
            entityData.add(new EntityData<>(0, EntityDataTypes.BYTE, (byte) 0x20));

            entityData.add(new EntityData<>(2, EntityDataTypes.OPTIONAL_ADV_COMPONENT,
                    Optional.of(LegacyComponentSerializer.legacyAmpersand().deserialize(getValue(player)))));
            entityData.add(new EntityData<>(3, EntityDataTypes.BOOLEAN, true));
        }

        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(entityID, entityData);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    // Builder

    public TextLine yOffset(double yOffset) {
        super.setYOffset(yOffset);
        return this;
    }
}