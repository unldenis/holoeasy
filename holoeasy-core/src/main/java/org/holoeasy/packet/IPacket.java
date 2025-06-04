package org.holoeasy.packet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.holoeasy.HoloEasy;
import org.jetbrains.annotations.ApiStatus;

public interface IPacket {

    void deletePacket(Player player, int entityId);

    void equip(Player player, int entityId, ItemStack helmet);

    void metadataItem(Player player, int entityId, ItemStack item);

    void metadataText(Player player, int entityId, String nameTag, boolean invisible);

    // Overload per compatibilità con il parametro invisibile opzionale di Kotlin
    default void metadataText(Player player, int entityId, String nameTag) {
        metadataText(player, entityId, nameTag, true);
    }

    void rotate(Player player, int entityId, double yaw);

    void spawn(HoloEasy lib, Player player, int entityId, EntityType entityType, Location location);

    void teleport(Player player, int entityId, Location location);

    void velocity(Player player, int entityId, double x, double y, double z);

    @ApiStatus.Experimental
    void metadataDisplayBlock(Player player, int entityId, Material material);

    @ApiStatus.Experimental
    void metadataDisplayText(Player player, int entityId, String text, int lineWidth, int backgroundColor, byte textOpacity);
}