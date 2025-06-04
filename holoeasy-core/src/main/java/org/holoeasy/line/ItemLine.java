package org.holoeasy.line;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.util.VersionEnum;
import org.holoeasy.util.VersionUtil;
import org.jetbrains.annotations.NotNull;

public class ItemLine extends LineImpl<ItemStack> {

    private ItemStack value;

    public ItemLine(Hologram hologram, ItemStack value) {
        super(hologram, EntityType.ITEM);
        if (VersionUtil.isCompatible(VersionEnum.V1_8)) {
            throw new IllegalStateException("This version does not support item lines");
        }
        this.value = value;
    }

    @Override
    public @NotNull Type getType() {
        return Type.ITEM_LINE;
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
        hologram.getLib().getPacketImpl()
            .velocity(player, entityID, 0.0, 0.0, 0.0);
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