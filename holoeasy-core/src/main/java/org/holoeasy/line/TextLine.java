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

public class TextLine extends LineImpl<String> {

    private String value;
    private boolean isEmpty = false;

    public TextLine(Hologram hologram, String value) {
        super(hologram, EntityTypes.ARMOR_STAND);
        this.value = value;
    }

    @Override
    public @NotNull Type getType() {
        return Type.TEXT_LINE;
    }

    @Override
    public @NotNull String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void show(Player player) {
        isEmpty = value.isEmpty();
        if (!isEmpty) {
            spawn(player);
            sendTo(player, true);
        }
    }

    @Override
    public void hide(Player player) {
        destroy(player);
    }

    @Override
    public void update(Player player) {
        int spawnBefore = ((isEmpty ? 1 : 0) | ((value.isEmpty() ? 1 : 0) << 1));
        // 0x00 = già mostrato
        // 0x01 = era nascosto ma ora è cambiato
        // 0x02 = già mostrato ma ora è vuoto
        // 0x03 = era nascosto e non è cambiato
        switch (spawnBefore) {
            case 0x03:
                break;
            case 0x02:
                destroy(player);
                isEmpty = true;
                break;
            case 0x01:
                spawn(player);
                isEmpty = false;
                sendTo(player, true);
                break;
            case 0x00:
                sendTo(player, false);
                break;
        }
    }

    @Override
    public void update(@NotNull String newValue) {
        this.value = newValue;
        observerUpdate();
    }

    private void sendTo(Player player, boolean invisible) {
        List<EntityData<?>> entityData = new ArrayList<>();
        VersionEnum version = VersionUtil.CLEAN_VERSION;

        if (version == VersionEnum.V1_8) {
            if (invisible) {
                entityData.add(new EntityData<>(0, EntityDataTypes.BYTE, (byte) 0x20));
            }
            if (value != null) {
                entityData.add(new EntityData<>(2, EntityDataTypes.STRING, value));
                entityData.add(new EntityData<>(3, EntityDataTypes.BYTE, (byte) 1));
            }
        } else {
            if (invisible) {
                entityData.add(new EntityData<>(0, EntityDataTypes.BYTE, (byte) 0x20));
            }
            if (value != null) {
                entityData.add(new EntityData<>(2, EntityDataTypes.OPTIONAL_ADV_COMPONENT, Optional.of(LegacyComponentSerializer.legacyAmpersand().deserialize(value))));
                entityData.add(new EntityData<>(3, EntityDataTypes.BOOLEAN, true));
            }
        }

        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(entityID, entityData);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }
}