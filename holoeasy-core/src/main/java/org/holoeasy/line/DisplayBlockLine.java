package org.holoeasy.line;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.holoeasy.hologram.Hologram;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Experimental
public class DisplayBlockLine extends LineImpl<Material> {

    private Material value;
    private final Type type = Type.DISPLAY_BLOCK_LINE;

    public DisplayBlockLine(Hologram hologram, Material value) {
        super(hologram, EntityType.BLOCK_DISPLAY);
        this.value = value;
    }

    @Override
    public @NotNull Type getType() {
        return type;
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
        hologram.getLib().getPacketImpl()
            .metadataDisplayBlock(player, entityID, value);
    }

    @Override
    public void update(@NotNull Material newValue) {
        this.value = newValue;
        observerUpdate();
    }
}