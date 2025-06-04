package org.holoeasy.line;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.holoeasy.hologram.Hologram;
import org.jetbrains.annotations.NotNull;

public class TextLine extends LineImpl<String> {

    private String value;
    private boolean isEmpty = false;
    private final Type type = Type.TEXT_LINE;

    public TextLine(Hologram hologram, String value) {
        super(hologram, EntityType.ARMOR_STAND);
        this.value = value;
    }

    public String parse(Player player) {
        return value;
    }

    @Override
    public @NotNull Type getType() {
        return type;
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
            hologram.getLib().getPacketImpl()
                .metadataText(player, entityID, parse(player), true);
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
                hologram.getLib().getPacketImpl()
                    .metadataText(player, entityID, parse(player), true);
                break;
            case 0x00:
                hologram.getLib().getPacketImpl()
                    .metadataText(player, entityID, parse(player), false);
                break;
        }
    }

    @Override
    public void update(@NotNull String newValue) {
        this.value = newValue;
        observerUpdate();
    }
}