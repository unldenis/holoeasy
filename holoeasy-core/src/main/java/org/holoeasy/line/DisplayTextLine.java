package org.holoeasy.line;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.holoeasy.hologram.Hologram;
import org.jetbrains.annotations.NotNull;

public class DisplayTextLine extends LineImpl<String> {

    private String value;
    private final int lineWidth;
    private final int backgroundColor;
    private final byte textOpacity;

    public DisplayTextLine(Hologram hologram, String value, int lineWidth, int backgroundColor, byte textOpacity) {
        super(hologram, EntityType.TEXT_DISPLAY);
        this.value = value;
        this.lineWidth = lineWidth;
        this.backgroundColor = backgroundColor;
        this.textOpacity = textOpacity;
    }

    public String parse(Player player) {
        return value;
    }

    @Override
    public @NotNull Type getType() {
        return Type.DISPLAY_TEXT_LINE;
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
        if (!value.isEmpty()) {
            spawn(player);
            this.update(player);
        }
    }

    @Override
    public void hide(Player player) {
        destroy(player);
    }

    @Override
    public void update(Player player) {
        hologram.getLib().getPacketImpl()
            .metadataDisplayText(
                player,
                entityID,
                parse(player),
                lineWidth,
                backgroundColor,
                textOpacity
            );
    }

    @Override
    public void update(@NotNull String newValue) {
        this.value = newValue;
        observerUpdate();
    }
}