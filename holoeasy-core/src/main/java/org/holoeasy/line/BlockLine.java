package org.holoeasy.line;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.holoeasy.hologram.Hologram;
import org.jetbrains.annotations.NotNull;

public class BlockLine extends LineImpl<ItemStack> {

    private ItemStack value;
    private final Type type = Type.BLOCK_LINE;

    public BlockLine(Hologram hologram, ItemStack value) {
        super(hologram, EntityType.FALLING_BLOCK);
        this.value = value;
    }

    @Override
    public @NotNull Type getType() {
        return type;
    }

    @Override
    public @NotNull ItemStack getValue() {
        return value;
    }

    @Override
    public void setValue(ItemStack value) {
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
            .metadataItem(player, entityID, value);
    }

    @Override
    public void update(@NotNull ItemStack newValue) {
        this.value = newValue;
        observerUpdate();
    }
}